module org.eugue {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires javafx.graphics;

    opens org.eugue to javafx.fxml; 
    exports org.eugue;
}
