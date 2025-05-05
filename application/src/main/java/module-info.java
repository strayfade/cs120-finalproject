module com.noah {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.noah to javafx.fxml;
    exports com.noah;
}
