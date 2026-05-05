module org.example.ecochef {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;

    opens org.example.ecochef to javafx.fxml;
    opens org.example.ecochef.utils to java.xml.bind;
    exports org.example.ecochef;
}