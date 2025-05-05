package com.noah;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    private TextField textInput;
    public void initialize() {

        // Tell the textInput to handleSendMessage() when the return key is pressed
        textInput.setOnAction(event -> {
            handleSendMessage();
        });
    }

    public void handleSendMessage() {
        String content = textInput.getText();
        if (content.length() > 0) {
            textInput.setText("");
            System.out.println("User entered: " + content);
        }
    }
}
