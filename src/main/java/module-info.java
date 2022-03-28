module com.tuanyvan.xkcdjavacomicviewer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tuanyvan.xkcdjavacomicviewer to javafx.fxml;
    exports com.tuanyvan.xkcdjavacomicviewer;
}