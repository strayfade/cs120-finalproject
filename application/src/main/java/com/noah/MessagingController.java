package com.noah;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class MessagingController {

    private WebSocketClient socketClient;

    public void tryConnectToSocket() {
        try {
            socketClient = new WebSocketClient(new URI("wss://javawebsocket.sorafn.com")) {

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to WebSocket Server!");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Received message: " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Connection closed. Reason: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("An error occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };

            // Connect to the WebSocket server
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
    }

    public void handleSendMessage() {
        String content = textInput.getText();
        if (content.length() > 0) {
            textInput.setText("");
            System.out.println("User entered: " + content);
            if (socketClient.getConnection().isOpen()) {
                socketClient.send(content);
            }
        }
    }
}
