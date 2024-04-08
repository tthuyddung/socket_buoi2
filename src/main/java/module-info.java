module com.example.dung {
    requires javafx.controls;
    requires javafx.fxml;


    opens bai1 to javafx.fxml;
    exports bai1;
}