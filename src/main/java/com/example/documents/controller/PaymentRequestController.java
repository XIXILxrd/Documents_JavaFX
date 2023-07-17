package com.example.documents.controller;

import com.example.documents.MainApplication;
import com.example.documents.model.Currency;
import com.example.documents.model.PaymentRequest;
import com.example.documents.persistence.IPersistenceHandler;
import com.example.documents.persistence.PersistenceHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class PaymentRequestController implements Initializable {

    IPersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

    @FXML
    private TextField amountText;

    @FXML
    private TextField commissionText;

    @FXML
    private TextField counterpartText;

    @FXML
    private ComboBox<Currency> currencyCBox;

    @FXML
    private TextField currencyRateText;

    @FXML
    private DatePicker dataText;

    @FXML
    private TextField numberText;

    @FXML
    private TextField userText;

    @FXML
    private Label warningLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Currency> currencies = FXCollections.observableArrayList(Currency.values());

        currencyCBox.setItems(currencies);
    }

    public void cancelButtonOnAction() {
        MainApplication.setMainScene();
    }

    public void acceptButtonOnAction() {
        if (isAllFieldsFilled()) {
            warningLabel.setVisible(false);

            try {
                PaymentRequest paymentRequest = new PaymentRequest(
                        null,
                        numberText.getText(),
                        Date.valueOf(dataText.getValue()),
                        userText.getText(),
                        counterpartText.getText(),
                        new BigDecimal(amountText.getText()),
                        currencyCBox.getValue().toString(),
                        new BigDecimal(currencyRateText.getText()),
                        new BigDecimal(commissionText.getText())
                );

                persistenceHandler.createPaymentRequest(paymentRequest);

                MainApplication.setMainScene();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            warningLabel.setVisible(true);
        }
    }

    private boolean isAllFieldsFilled() {
        return !(numberText.getText().isBlank() ||
                dataText.getValue().toString().isBlank() ||
                userText.getText().isBlank() ||
                counterpartText.getText().isBlank() ||
                amountText.getText().isBlank() ||
                currencyCBox.getValue().toString().isBlank() ||
                currencyRateText.getText().isBlank() ||
                commissionText.getText().isBlank());
    }
}
