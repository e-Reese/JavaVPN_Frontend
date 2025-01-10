package org.eugue;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        Button startbButton = new Button("Start VPN");
        startbButton.setPrefSize(130, 50);
        startbButton.setStyle("-fx-background-color: #cfcfc4; -fx-text-fill: #3c1414; -fx-font-size: 16px;");
        Button stopButton = new Button("Stop VPN");
        stopButton.setPrefSize(130, 50);
        stopButton.setStyle("-fx-background-color: #cfcfc4; -fx-text-fill: #3c1414; -fx-font-size: 16px;");
        Button checkStatusButton = new Button("Check VPN Status");
        checkStatusButton.setPrefSize(130, 50);
        checkStatusButton.setStyle("-fx-background-color: #cfcfc4; -fx-text-fill: #3c1414; -fx-font-size: 16px;");
        
        TextArea outputText = new TextArea("See VPN results here...");
        outputText.setEditable(false);
        outputText.setPrefSize(500, 200);
        outputText.setStyle("-fx-control-inner-background: #cfcfc4; -fx-text-fill: #3c1414; -fx-font-size: 14px;");

        
        startbButton.setOnAction(e -> StartVPN(outputText));
        stopButton.setOnAction(e -> StopVPN(outputText));
        checkStatusButton.setOnAction(e -> CheckVPNStatus(outputText));

        HBox hbox = new HBox(23, startbButton, stopButton, checkStatusButton);
        hbox.setAlignment(Pos.CENTER);
        VBox layout = new VBox(30, hbox, outputText);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #004242;");
        scene = new Scene(layout, 690, 400);

        stage.setTitle("VPN Controller");
        stage.setScene(scene);
        stage.show();
    }

    private void StartVPN(TextArea outputText) {
        // Kick off VPN startup
        PrimaryController primaryController = new PrimaryController();
        primaryController.StartServer(outputText); // Client starts after this. Should be refactored into events. 
    }

    private void StopVPN(TextArea outputText) {
        // Call backend api to stop vpn
        PrimaryController primaryController = new PrimaryController();
        primaryController.StopServer(outputText);

    }

    private void CheckVPNStatus(TextArea outputText) {
        // Call backend api to check vpn status
        PrimaryController primaryController = new PrimaryController();
        primaryController.CheckServerStatus(outputText);

        // SecondaryController secondaryController = new SecondaryController();
        // secondaryController.CheckClientStatus(outputText);

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }

}