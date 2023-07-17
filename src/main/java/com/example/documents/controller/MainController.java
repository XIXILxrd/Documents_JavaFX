package com.example.documents.controller;

import com.example.documents.MainApplication;
import com.example.documents.model.Payment;
import com.example.documents.model.PaymentRequest;
import com.example.documents.model.Waybill;
import com.example.documents.persistence.IPersistenceHandler;
import com.example.documents.persistence.PersistenceHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final int WAYBILL = 1;
    private final int PAYMENT = 2;
    private final int PAYMENT_REQUEST = 3;

    private IPersistenceHandler persistenceHandler = PersistenceHandler.getInstance();
    private ObservableList<Object> listOfDocuments;
    private ObservableList<Object> selectedItems;

    @FXML
    private Button exitButton;

    @FXML
    private ListView<Object> listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listOfDocuments = FXCollections.observableArrayList();
        selectedItems = FXCollections.observableArrayList();

        updateUI();

        listView.setCellFactory(CheckBoxListCell.forListView(selectedItem -> {
            BooleanProperty observable = new SimpleBooleanProperty();

            observable.addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    selectedItems.add(selectedItem);
                } else {
                    selectedItems.remove(selectedItem);
                }
            });

            return observable;
        }));
    }

    public void waybillButtonOnAction() {
        FXMLLoader view = new FXMLLoader(MainApplication.class.getResource("waybill_view.fxml"));

        try {
            Scene waybillScene = new Scene(view.load());
            MainApplication.setStage(waybillScene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void paymentButtonOnAction() {
        FXMLLoader view = new FXMLLoader(MainApplication.class.getResource("payment_view.fxml"));

        try {
            Scene paymentScene = new Scene(view.load());
            MainApplication.setStage(paymentScene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void paymentRequestButtonOnAction() {
        FXMLLoader view = new FXMLLoader(MainApplication.class.getResource("paymentRequest_view.fxml"));

        try {
            Scene paymentRequestScene = new Scene(view.load());
            MainApplication.setStage(paymentRequestScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitButtonOnAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void deleteButtonOnAction() {
        for (Object document : selectedItems) {
            persistenceHandler.deleteDocument(document);
            listOfDocuments.remove(document);
        }

        selectedItems.clear();

        listView.refresh();
    }

    public void viewButtonOnAction() {
        String title;
        String content;

        try {
            for (Object document : selectedItems) {
                if (document instanceof Waybill) {
                    title = "Накладная";
                    content = "Номер: " + ((Waybill) document).getNumber() + "\n" +
                            "Дата: " + ((Waybill) document).getDate() + "\n" +
                            "Пользователь: " + ((Waybill) document).getUser() + "\n" +
                            "Сумма: " + ((Waybill) document).getAmount() + "\n" +
                            "Валюта: " + ((Waybill) document).getCurrency() + "\n" +
                            "Курс Валюты: " + ((Waybill) document).getCurrencyRate() + "\n" +
                            "Товар: " + ((Waybill) document).getItem() + "\n" +
                            "Количество: " + ((Waybill) document).getQuantity() + "\n";
                } else if (document instanceof PaymentRequest) {
                    title = "Заявка на оплату";
                    content = "Номер: " + ((PaymentRequest) document).getNumber() + "\n" +
                            "Дата: " + ((PaymentRequest) document).getDate() + "\n" +
                            "Пользователь: " + ((PaymentRequest) document).getUser() + "\n" +
                            "Контрагент: " + ((PaymentRequest) document).getCounterpart() + "\n" +
                            "Сумма: " + ((PaymentRequest) document).getAmount() + "\n" +
                            "Валюта: " + ((PaymentRequest) document).getCurrency() + "\n" +
                            "Курс Валюты: " + ((PaymentRequest) document).getCurrencyRate() + "\n" +
                            "Комиссия: " + ((PaymentRequest) document).getCommission() + "\n";
                } else if (document instanceof Payment) {
                    title = "Платёжка";
                    content = "Номер: " + ((Payment) document).getNumber() + "\n" +
                            "Дата: " + ((Payment) document).getDate() + "\n" +
                            "Пользователь: " + ((Payment) document).getUser() + "\n" +
                            "Сумма: " + ((Payment) document).getAmount() + "\n" +
                            "Сотрудник: " + ((Payment) document).getEmployee() + "\n";

                } else {
                    throw new IllegalArgumentException();
                }

                FXMLLoader view = new FXMLLoader(MainApplication.class.getResource("edit_view.fxml"));
                Scene editScene = new Scene(view.load());
                Stage stage = new Stage();

                TextArea text = (TextArea) editScene.getRoot().getChildrenUnmodifiable().get(1);
                text.setText(content);

                stage.setScene(editScene);
                stage.setTitle(title);

                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadButtonOnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберете текстовый файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));

        Stage stage = new Stage();

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            return;
        }

        try {
            Path filePath = Paths.get(selectedFile.getAbsolutePath());
            List<String> fileContent = Files.readAllLines(filePath);

            switch (fileCheck(filePath)) {
                case WAYBILL -> {
                    Waybill waybill = new Waybill(
                            null,
                            fileContent.get(0).substring("Номер: ".length()),
                            Date.valueOf(fileContent.get(1).substring("Дата: ".length())),
                            fileContent.get(2).substring("Пользователь: ".length()),
                            new BigDecimal(fileContent.get(3).substring("Сумма: ".length())),
                            fileContent.get(4).substring("Валюта: ".length()),
                            new BigDecimal(fileContent.get(5).substring("Курс Валюты: ".length())),
                            fileContent.get(6).substring("Товар: ".length()),
                            Double.parseDouble(fileContent.get(7).substring("Количество: ".length()))
                    );

                    persistenceHandler.createWaybill(waybill);
                    listOfDocuments.add(waybill);
                }
                case PAYMENT -> {
                    Payment payment = new Payment(
                            null,
                            fileContent.get(0).substring("Номер: ".length()),
                            Date.valueOf(fileContent.get(1).substring("Дата: ".length())),
                            fileContent.get(2).substring("Пользователь: ".length()),
                            new BigDecimal(fileContent.get(3).substring("Сумма: ".length())),
                            fileContent.get(4).substring("Сотрудник: ".length())
                    );

                    persistenceHandler.createPayment(payment);
                    listOfDocuments.add(payment);
                }
                case PAYMENT_REQUEST -> {
                    PaymentRequest paymentRequest = new PaymentRequest(
                            null,
                            fileContent.get(0).substring("Номер: ".length()),
                            Date.valueOf(fileContent.get(1).substring("Дата: ".length())),
                            fileContent.get(2).substring("Пользователь: ".length()),
                            fileContent.get(3).substring("Контрагент: ".length()),
                            new BigDecimal(fileContent.get(4).substring("Сумма: ".length())),
                            fileContent.get(5).substring("Валюта: ".length()),
                            new BigDecimal(fileContent.get(6).substring("Курс Валюты: ".length())),
                            new BigDecimal(fileContent.get(7).substring("Комиссия: ".length()))
                    );

                    persistenceHandler.createPaymentRequest(paymentRequest);
                    listOfDocuments.add(paymentRequest);
                }
                default -> throw new IllegalArgumentException("Файл не содержит необходимые строки для заполнения");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void saveButtonOnAction() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберете папку");

        Stage stage = new Stage();

        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            return;
        }

        try {
            Path directoryPath = Paths.get(selectedDirectory.getAbsolutePath());

            System.out.println(directoryPath);

            for (Object object : selectedItems) {
                if (object instanceof Waybill) {
                    waybillToFile(directoryPath, (Waybill) object);
                } else if (object instanceof Payment) {
                    paymentToFile(directoryPath, (Payment) object);
                } else if (object instanceof PaymentRequest) {
                    paymentRequestToFile(directoryPath, (PaymentRequest) object);
                } else {
                    throw new IllegalArgumentException();
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void waybillToFile(Path path, Waybill waybill) {
        try (FileWriter writer = new FileWriter(path.toString() + "\\" + waybill.getNumber() + "_" + waybill.getDate() + ".txt")) {
            String content = "Номер: " + waybill.getNumber() + "\n" +
                    "Дата: " + waybill.getDate() + "\n" +
                    "Пользователь: " + waybill.getUser() + "\n" +
                    "Сумма: " + waybill.getAmount() + "\n" +
                    "Валюта: " + waybill.getCurrency() + "\n" +
                    "Курс Валюты: " + waybill.getCurrencyRate() + "\n" +
                    "Товар: " + waybill.getItem() + "\n" +
                    "Количество: " + waybill.getQuantity() + "\n";

            writer.write(content);

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paymentToFile(Path path, Payment payment) {
        try (FileWriter writer = new FileWriter(path.toString() + "\\" + payment.getNumber() + "_" + payment.getDate() + ".txt")) {
            String content = "Номер: " + payment.getNumber() + "\n" +
                    "Дата: " + payment.getDate() + "\n" +
                    "Пользователь: " + payment.getUser() + "\n" +
                    "Сумма: " + payment.getAmount() + "\n" +
                    "Сотрудник: " + payment.getEmployee() + "\n";

            writer.write(content);

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paymentRequestToFile(Path path, PaymentRequest paymentRequest) {
        try (FileWriter writer = new FileWriter(path.toString() + "\\" + paymentRequest.getNumber() + "_" + paymentRequest.getDate() + ".txt")) {
            String content = "Номер: " + paymentRequest.getNumber() + "\n" +
                    "Дата: " + paymentRequest.getDate() + "\n" +
                    "Пользователь: " + paymentRequest.getUser() + "\n" +
                    "Контрагент: " + paymentRequest.getCounterpart() + "\n" +
                    "Сумма: " + paymentRequest.getAmount() + "\n" +
                    "Валюта: " + paymentRequest.getCurrency() + "\n" +
                    "Курс Валюты: " + paymentRequest.getCurrencyRate() + "\n" +
                    "Комиссия: " + paymentRequest.getCommission() + "\n";

            writer.write(content);

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int fileCheck(Path filePath) {
        List<String> waybillCheckList = List.of("Номер:", "Дата:", "Пользователь:", "Сумма:", "Валюта:", "Курс Валюты:", "Товар:", "Количество:");
        List<String> paymentCheckList = List.of("Номер:", "Дата:", "Пользователь:", "Сумма:", "Сотрудник:");
        List<String> paymentRequestCheckList = List.of("Номер:", "Дата:", "Пользователь:", "Контрагент:", "Сумма:", "Валюта:", "Курс Валюты:", "Комиссия:");

        int classInFile = -1;

        try {
            List<String> fileContent = Files.readAllLines(filePath);
            int size = fileContent.size();
            List<String> checkList;

            if (size == paymentCheckList.size() && fileContent.get(4).startsWith(paymentCheckList.get(4))) {
                checkList = paymentCheckList;
                classInFile = PAYMENT;
            } else if (size == waybillCheckList.size() && fileContent.get(7).startsWith(waybillCheckList.get(7))) {
                checkList = waybillCheckList;
                classInFile = WAYBILL;
            } else if (size == paymentRequestCheckList.size() && fileContent.get(7).startsWith(paymentRequestCheckList.get(7))) {
                checkList = paymentRequestCheckList;
                classInFile = PAYMENT_REQUEST;
            } else {
                throw new IllegalArgumentException("Файл не содержит необходимые строки для заполнения");
            }

            for (int i = 0; i < size; i++) {
                if (!fileContent.get(i).startsWith(checkList.get(i))) {
                    throw new IllegalArgumentException("Файл не содержит необходимые строки для заполнения");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return classInFile;
    }

    public void updateUI() {
        listOfDocuments.clear();

        listOfDocuments.addAll(persistenceHandler.getDocuments());

        listView.setItems(listOfDocuments);
    }
}


