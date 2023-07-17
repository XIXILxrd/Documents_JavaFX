package com.example.documents.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EditController {

    @FXML
    private Button acceptButton;

    public void acceptButtonOnAction() {
        Stage stage = (Stage) acceptButton.getScene().getWindow();
        stage.close();
    }
}