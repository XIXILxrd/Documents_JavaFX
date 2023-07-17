package com.example.documents.controller;

import com.example.documents.MainApplication;
import com.example.documents.model.Payment;
import com.example.documents.persistence.IPersistenceHandler;
import com.example.documents.persistence.PersistenceHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
    IPersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

    @FXML
    private TextField amountText;

    @FXML
    private DatePicker dataText;

    @FXML
    private TextField employeeText;

    @FXML
    private TextField numberText;

    @FXML
    private TextField userText;

    @FXML
    private Label warningLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void acceptButtonOnAction() {
        if (isAllFieldsFilled()) {
            warningLabel.setVisible(false);

            try {
                Payment payment = new Payment(
                        null,
                        numberText.getText(),
                        Date.valueOf(dataText.getValue()),
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
                dataText.getValue().toString().isBlank());
    }
}