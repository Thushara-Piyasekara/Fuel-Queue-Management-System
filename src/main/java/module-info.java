module com.example.classes_version_gui_v1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.classes_version_gui_v1 to javafx.fxml;
    exports com.example.classes_version_gui_v1;
}