module transport {
    requires javafx.fxml;
    requires javafx.controls;

    opens transport.ui to javafx.fxml;
    opens transport.control to javafx.fxml;
    opens transport.core to javafx.fxml; // nécessaire si vous exposez des classes du modèle à FXML

    exports transport.ui;
    exports transport.control;
    exports transport.core;
}