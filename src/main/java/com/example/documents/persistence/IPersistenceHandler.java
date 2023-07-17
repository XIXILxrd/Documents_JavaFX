package com.example.documents.persistence;

import com.example.documents.model.Payment;
import com.example.documents.model.PaymentRequest;
import com.example.documents.model.Waybill;

import java.util.List;

public interface IPersistenceHandler {
    void createWaybill(Waybill waybill);
    void createPayment(Payment payment);
    void createPaymentRequest(PaymentRequest paymentRequest);
    List<Object> getDocuments();
    void deleteDocument(Object document);
}
