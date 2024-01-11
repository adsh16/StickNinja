module com.example.stickheroapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.StickHeroApplication to javafx.fxml;
    exports com.example.StickHeroApplication;
}