package com.example.documents.controller;

import com.example.documents.MainApplication;
import com.example.documents.model.Payment;
import com.example.documents.persistence.IPersistenceHandler;
import com.example.documents.persistence.PersistenceHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.sql.Date;

public class PaymentController {
    IPersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

    @FXML
    private Button cancelButton;

    @FXML
    private Button acceptButton;

    @FXML
    private TextField amountText;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField employeeText;

    @FXML
    private TextField numberText;

    @FXML
    private TextField userText;

    @FXML
    private Label warningLabel;

    public void acceptButtonOnAction() {
        if (isAllFieldsFilled()) {
            warningLabel.setVisible(false);

            try {
                Payment payment = new Payment(
                        null,
                        numberText.getText(),
                        Date.valueOf(datePicker.getValue()),
                        userText.getText(),
                        new BigDecimal(amountText.getText()),
                        employeeText.getText()
                );

                persistenceHandler.createPayment(payment);

                MainApplication.setMainScene();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            warningLabel.setVisible(true);
        }
    }

    public void cancelButtonOnAction() {
        MainApplication.setMainScene();
    }

    private boolean isAllFieldsFilled() {
        return !(amountText.getText().isBlank() ||
                employeeText.getText().isBlank() ||
                numberText.getText().isBlank() ||
                userText.getText().isBlank() ||
                datePicker.getValue().toString().isBlank());
    }
}