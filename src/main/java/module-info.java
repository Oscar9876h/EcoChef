module org.example.ecochef {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;

    // Permite que JavaFX abra la aplicación principal
    opens org.example.ecochef to javafx.fxml;

    // Permite que JAXB lea tus clases de configuración en utils
    opens org.example.ecochef.utils to java.xml.bind;

    // LA CLAVE: Permite que JavaFX acceda a tus controladores
    opens org.example.ecochef.controller to javafx.fxml;

    exports org.example.ecochef;
}