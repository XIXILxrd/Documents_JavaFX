package com.example.documents.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EditController {

    public TextArea textArea;
    @FXML
    private Button acceptButton;

    public void acceptButtonOnAction() {
        Stage stage = (Stage) acceptButton.getScene().getWindow();
        stage.close();
    }
}