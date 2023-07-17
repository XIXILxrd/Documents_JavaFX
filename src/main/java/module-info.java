module com.example.documents {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.documents to javafx.fxml;
    exports com.example.documents;
    exports com.example.documents.controller;
    opens com.example.documents.controller to javafx.fxml;
}