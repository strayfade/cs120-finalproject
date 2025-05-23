package com.noah;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.lang.Thread;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class MessagingController {

    // Websocket client responsible for messaging
    private WebSocketClient socketClient;

    @FXML
    private VBox messageContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label connectionStatus;

    // List of all messages
    private ArrayList<Message> messages;

    public void addMessage(Message msg) {
        Label messageLabel = new Label(msg.toString());
        messageContainer.getChildren().add(messageLabel);
    }

    public static String getCurrentTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
    
    public void queueMessageViewportRefresh() {

        // Clear viewport
        messageContainer.getChildren().clear();
        try {
            for (int i = 0; i < messages.size(); i++) {
                addMessage(messages.get(i));
            }

            // Sleep waits for messages to be populated before scrolling
            Thread.sleep(100);

            // Scroll to bottom of ScrollPane
            Platform.runLater(() -> {
                scrollPane.layout();
                scrollPane.setVvalue(1.0);
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tryConnectToSocket() {
        try {
            socketClient = new WebSocketClient(new URI("wss://javawebsocket.sorafn.com")) {

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Platform.runLater(() -> {
                        connectionStatus.setStyle("-fx-background-color:rgb(0, 136, 0);");
                        connectionStatus.setText("Connected");
                    });
                }

                @Override
                public void onMessage(String message) {
                    // Makes sure that addMessage runs on the FX application thread
                    Platform.runLater(() -> {
                        Message newMessage = new Message(message.split(": ")[0], getCurrentTime(), message.split(": ")[1]);
                        messages.add(newMessage);
                        queueMessageViewportRefresh();
                    });

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Platform.runLater(() -> {
                        connectionStatus.setStyle("-fx-background-color:rgb(136, 0, 0);");
                        connectionStatus.setText("Not connected");
                    });
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("An error occurred: " + ex.getMessage());
                    Platform.runLater(() -> {
                        connectionStatus.setStyle("-fx-background-color:rgb(136, 0, 0);");
                        connectionStatus.setText("Not connected");
                    });
                }
            };

            socketClient.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField textInput;

    public void initialize() {

        // Tell the textInput to handleSendMessage() when the return key is pressed
        textInput.setOnAction(event -> {
            handleSendMessage();
        });

        tryConnectToSocket();

        messages = new ArrayList<>();
        connectionStatus.setStyle("-fx-background-color:rgb(136, 0, 0);");
        connectionStatus.setText("Not connected");
    }

    public void handleSendMessage() {
        String content = textInput.getText();

        // Make sure there is a message
        if (content.length() > 0) {

            // Clear textbox
            textInput.setText("");

            // Send the message if the socketClient is connected
            if (socketClient.getConnection().isOpen()) {
                socketClient.send(content);
            }
        }
    }
}
