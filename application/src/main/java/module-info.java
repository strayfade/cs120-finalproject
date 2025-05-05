module com.noah {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.java_websocket;

    opens com.noah to javafx.fxml;
    exports com.noah;
}
