package com.example.documents.controller;

import com.example.documents.MainApplication;
import com.example.documents.model.Currency;
import com.example.documents.model.Waybill;
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

public class WaybillController implements Initializable {
    IPersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

    @FXML
    private TextField amountText;

    @FXML
    private TextField currencyRateText;

    @FXML
    private ComboBox<Currency> currencyCBox;

    @FXML
    private DatePicker dataText;

    @FXML
    private TextField itemText;

    @FXML
    private TextField numberText;

    @FXML
    private TextField quantityText;

    @FXML
    private TextField userText;

    @FXML
    private Label warningLabel;

    public void acceptButtonOnAction() {
        if (isAllFieldsFilled()) {
            warningLabel.setVisible(false);

            try {
                Waybill waybill = new Waybill(
                        null,
                        numberText.getText(),
                        Date.valueOf(dataText.getValue()),
                        userText.getText(),
                        new BigDecimal(amountText.getText()),
                        currencyCBox.getValue().toString(),
                        new BigDecimal(currencyRateText.getText()),
                        itemText.getText(),
                        Double.parseDouble(quantityText.getText())
                );

                persistenceHandler.createWaybill(waybill);

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
                currencyRateText.getText().isBlank() ||
                numberText.getText().isBlank() ||
                userText.getText().isBlank() ||
                dataText.getValue().toString().isBlank() ||
                itemText.getText().isBlank() ||
                quantityText.getText().isBlank());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Currency> currencies = FXCollections.observableArrayList(Currency.values());

        currencyCBox.setItems(currencies);
    }
}
