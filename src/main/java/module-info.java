module com.tuanyvan.xkcdjavacomicviewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.tuanyvan.xkcdjavacomicviewer to javafx.fxml;
    exports com.tuanyvan.xkcdjavacomicviewer;
}