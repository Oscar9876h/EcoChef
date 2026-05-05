module org.example.ecochef {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ecochef to javafx.fxml;
    exports org.example.ecochef;
}