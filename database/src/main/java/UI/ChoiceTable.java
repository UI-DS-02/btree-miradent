package UI;

import DataBaseManager.DBM;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Table;

public class ChoiceTable extends Application {
    public ChoiceTable(DBM controller) {
        this.controller = controller;
    }
    private final DBM controller;
    private Stage stage;
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        BorderPane root = new BorderPane();
        root.setCenter(fields());
        Scene scene = new Scene(root, 600,300);
        stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        stage.setResizable(false);
        stage.getIcons().add(new Image("icon.png"));
        stage.show();
    }
    private HBox fields() {
        Label label = new Label("Name Table : ");
        TextField field = new TextField("");
        field.setPrefWidth(420);
        Button button = new Button(" Select ");
        HBox hBox = new HBox(label, field, button);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(5));
        button.setOnMouseClicked(mouseEvent -> {
            try {
                Table table = controller.getTables().get(field.getText());
                new EditTable(table, controller).start(new Stage());
                stage.close();
            } catch (Exception e) {
                alert(e.getMessage());
            }
        });
        return hBox;
    }
    private void alert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("my-alert-style");
        dialogPane.setMinHeight(250);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icon.png"));
        alert.getDialogPane().getStylesheets().add("style.css");
        alert.showAndWait();
    }
}
