module com.tuanyvan.xkcdjavacomicviewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.desktop;
    requires commons.validator;


    opens com.tuanyvan.xkcdjavacomicviewer to javafx.fxml;
    exports com.tuanyvan.xkcdjavacomicviewer;
}