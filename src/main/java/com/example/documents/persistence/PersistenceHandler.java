package com.example.documents.persistence;

import com.example.documents.model.Payment;
import com.example.documents.model.PaymentRequest;
import com.example.documents.model.Waybill;
import org.postgresql.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceHandler implements IPersistenceHandler {
    private static PersistenceHandler instance;
    public String url = "localhost";
    private final int port = 5432;
    private final String databaseName = "documents";
    private final String username = "postgres";
    private final String password = "1234";
    private Connection connection;

    private PersistenceHandler() {
        initializePostgresqlDatabase();
    }

    public static PersistenceHandler getInstance() {
        if (instance == null) {
            instance = new PersistenceHandler();
        }

        return instance;
    }

    private void initializePostgresqlDatabase() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://" + url + ":" + port + "/" + databaseName, username, password);
            System.out.println("Database connected");
        } catch (SQLException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (connection == null) {
                System.exit(-1);
            }
        }
    }

    @Override
    public void createWaybill(Waybill waybill) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO waybill (number, date, \"user\", amount, currency, currencyRate, item, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
            );

            insertStatement.setString(1, waybill.getNumber());
            insertStatement.setDate(2, waybill.getDate());
            insertStatement.setString(3, waybill.getUser());
            insertStatement.setBigDecimal(4, waybill.getAmount());
            insertStatement.setString(5, waybill.getCurrency());
            insertStatement.setBigDecimal(6, waybill.getCurrencyRate());
            insertStatement.setString(7, waybill.getItem());
            insertStatement.setDouble(8, waybill.getQuantity());

            insertStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void createPayment(Payment payment) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO payment (number, date, \"user\", amount, employee) VALUES (?, ?, ?, ?, ?);"
            );

            insertStatement.setString(1, payment.getNumber());
            insertStatement.setDate(2, payment.getDate());
            insertStatement.setString(3, payment.getUser());
            insertStatement.setBigDecimal(4, payment.getAmount());
            insertStatement.setString(5, payment.getEmployee());

            insertStatement.execute();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void createPaymentRequest(PaymentRequest paymentRequest) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO payment_request (number, date, \"user\", counterpart, amount, currency, currencyRate, commission) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            insertStatement.setString(1, paymentRequest.getNumber());
            insertStatement.setDate(2, paymentRequest.getDate());
            insertStatement.setString(3, paymentRequest.getUser());
            insertStatement.setString(4, paymentRequest.getCounterpart());
            insertStatement.setBigDecimal(5, paymentRequest.getAmount());
            insertStatement.setString(6, paymentRequest.getCurrency());
            insertStatement.setBigDecimal(7, paymentRequest.getCurrencyRate());
            insertStatement.setBigDecimal(8, paymentRequest.getCommission());

            insertStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();

        }
    }

    @Override
    public List<Object> getDocuments() {
        List<Waybill> waybills = getWaybills();
        List<Payment> payments = getPayments();
        List<PaymentRequest> paymentRequests = getPaymentRequests();

        if (waybills == null || paymentRequests == null || payments == null) {
            return null;
        }

        List<Object> allDocuments = new ArrayList<>();

        allDocuments.addAll(waybills);
        allDocuments.addAll(payments);
        allDocuments.addAll(paymentRequests);

        return allDocuments;
    }

    @Override
    public void deleteDocument(Object document) {
        String tableName;
        Long documentId;

        if (document instanceof Waybill) {
            tableName = "waybill";
            documentId = ((Waybill) document).getId();
        } else if (document instanceof Payment) {
            tableName = "payment";
            documentId = ((Payment) document).getId();
        } else if (document instanceof PaymentRequest) {
            tableName = "payment_request";
            documentId = ((PaymentRequest) document).getId();
        } else {
            throw new IllegalArgumentException("Object must be class a Waybill/Payment/PaymentRequest");
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + tableName + " WHERE id=" + documentId);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private List<Waybill> getWaybills() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM waybill");
            ResultSet sqlReturnValues = statement.executeQuery();
            List<Waybill> returnValues = new ArrayList<>();

            while (sqlReturnValues.next()) {
                returnValues.add(new Waybill(
                        sqlReturnValues.getLong(1),
                        sqlReturnValues.getString(2),
                        sqlReturnValues.getDate(3),
                        sqlReturnValues.getString(4),
                        sqlReturnValues.getBigDecimal(5),
                        sqlReturnValues.getString(6),
                        sqlReturnValues.getBigDecimal(7),
                        sqlReturnValues.getString(8),
                        sqlReturnValues.getDouble(9)
                ));
            }

            return returnValues;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    private List<PaymentRequest> getPaymentRequests() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM payment_request");
            ResultSet sqlReturnValues = statement.executeQuery();
            List<PaymentRequest> returnValues = new ArrayList<>();

            while (sqlReturnValues.next()) {
                returnValues.add(new PaymentRequest(
                        sqlReturnValues.getLong(1),
                        sqlReturnValues.getString(2),
                        sqlReturnValues.getDate(3),
                        sqlReturnValues.getString(4),
                        sqlReturnValues.getString(5),
                        sqlReturnValues.getBigDecimal(6),
                        sqlReturnValues.getString(7),
                        sqlReturnValues.getBigDecimal(8),
                        sqlReturnValues.getBigDecimal(9)
                ));
            }

            return returnValues;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    private List<Payment> getPayments() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM payment");
            ResultSet sqlReturnValues = statement.executeQuery();
            List<Payment> returnValues = new ArrayList<>();

            while (sqlReturnValues.next()) {
                returnValues.add(new Payment(
                        sqlReturnValues.getLong(1),
                        sqlReturnValues.getString(2),
                        sqlReturnValues.getDate(3),
                        sqlReturnValues.getString(4),
                        sqlReturnValues.getBigDecimal(5),
                        sqlReturnValues.getString(6)
                ));
            }

            return returnValues;

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return null;
    }
}

