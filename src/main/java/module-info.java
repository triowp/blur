module com.example.foto_blur {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.foto_blur to javafx.fxml;
    exports com.example.foto_blur;
}