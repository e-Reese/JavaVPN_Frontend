/* 
 * Should refactor to create single PingServer method to handle standard http requests to the server. 
 * - Create event systems to controll UI and wait for server responses
 */

package org.eugue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.scene.control.TextArea;


public class PrimaryController {  

    public static SecondaryController secondaryController; // just a reference to the secondary controller so values/processes persist

    public void StartServer(TextArea outputText) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://13.59.29.144:8080/vpn/start"))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
        
        outputText.setText("Starting Client...");

        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        futureResponse.thenAccept(response -> {
            Platform.runLater(() -> {
                if (response.statusCode() == 200) {
                    System.out.println("Status code: " + response.statusCode());
                    System.out.println(response.body());
                    outputText.setText("Server Started:\n" + response.body());
                    // Starting Client
                    secondaryController = new SecondaryController();
                    secondaryController.StartClient(outputText);
                } else {
                    System.out.println("Status code: " + response.statusCode());
                    System.out.println("Error: " + response.body());
                    outputText.setText("Error starting serverside VPN: " + response.body());
                }
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> outputText.setText("Error: " + e.getMessage()));
            System.out.println("Error: " + e.getMessage());
            return null;
        });
    }


    public void StopServer(TextArea outputText) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://13.59.29.144:8080/vpn/stop"))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        futureResponse.thenAccept(response -> {
            Platform.runLater(() -> {
                if (response.statusCode() == 200) {
                    System.out.println(response.body());
                    System.out.println(response.statusCode());
                    outputText.setText("Server Stopped");
                    if (secondaryController != null) {
                        secondaryController.StopClient(outputText);
                    }
                    
                } else {
                    System.out.println(response.statusCode());
                    outputText.setText("Error stopping server side VPN: " + response.body());
                }
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> outputText.setText("Error: " + e.getMessage()));
            System.out.println("Error: " + e.getMessage());
            return null;
        });
    }


    public void CheckServerStatus(TextArea outputText) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://13.59.29.144:8080/vpn/status"))
            .build();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code: " + response.statusCode());
            System.out.println("Response Body: " + response.toString());
            outputText.setText("VPN Status: " + response.toString());
            
        } catch (IOException | InterruptedException e) {
            System.out.println("Response Body: " + e.getMessage());
        }
    }

    // @FXML
    // private void switchToSecondary() throws IOException {
    //     App.setRoot("secondary");
    // }
}
