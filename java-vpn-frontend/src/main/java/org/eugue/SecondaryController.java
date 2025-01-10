package org.eugue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.control.TextArea;

public class SecondaryController {

    private Process clientVpnProcess;

    public void StartClient(TextArea outputText) { // Starts client side vpn. Called from Primary controller. Should become events
        outputText.setText("Starting client VPN...");
        try {
            String relativePath = "config/client.ovpn";
            // Resolve the absolute path based on the current working directory
            Path absolutePath = Paths.get(relativePath).toAbsolutePath();
            String command = "openvpn --config " + absolutePath.toString(); 
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);

            clientVpnProcess = processBuilder.start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientVpnProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        // Update the UI with process output
                        javafx.application.Platform.runLater(() -> outputText.appendText(finalLine + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            outputText.appendText("VPN client started successfully.\n");
        } catch (IOException e) {
            outputText.appendText("Error starting VPN in Docker: " + e.getMessage() + "\n");
        }
    }

    public void StopClient(TextArea outputText) { // Stops client side vpn. Called from Primary controller. Should become events
        outputText.setText("Stopping client VPN... ");
        
        if (clientVpnProcess != null) {
            clientVpnProcess.destroy();
            clientVpnProcess = null;
            outputText.appendText("Client VPN stopped.");
        }
    }

    public void CheckClientStatus(TextArea outputText) { // Calls on client side vpn. Called from Primary controller. Should become events
        outputText.setText("Checking VPN Status");
    }

    // @FXML
    // private void switchToPrimary() throws IOException {
    //     App.setRoot("primary");
    // }
}