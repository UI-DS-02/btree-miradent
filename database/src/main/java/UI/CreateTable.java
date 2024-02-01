package UI;

import DataBaseManager.DBM;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;


public class CreateTable extends Application {
    public CreateTable(DBM controller) {
        this.controller = controller;
    }

    private final DBM controller;
    private static final HashSet<String> validType;
    private Stage stage;
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        BorderPane root = new BorderPane();
        root.setCenter(createTable());
        Scene scene = new Scene(root, 600,600);
        stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        stage.setTitle("Create Table");
        stage.getIcons().add(new Image("icon.png"));
        stage.setResizable(false);
        stage.show();
    }
    private VBox createTable() {
        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.rgb(229, 178, 153), CornerRadii.EMPTY, Insets.EMPTY)));

        vBox.setPadding(new Insets(10,10,10,2));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.TOP_LEFT);

        Label tableName = new Label("Table Name : ");
        TextField name = new TextField();
        Label numberOfColumn = new Label("Number Of Column : ");
        TextField number = new TextField();
        number.setPrefWidth(130);
        Button ok = new Button("OK");
        HBox nameHbox = new HBox(tableName, name);
        HBox numberHbox = new HBox(numberOfColumn, number, ok);
        nameHbox.setSpacing(5);
        numberHbox.setSpacing(5);
        nameHbox.setPadding(new Insets(5));
        numberHbox.setPadding(new Insets(5));
        HBox hBoxLabel = new HBox(new Label("                              Name Of Column"),
                new Label("                             Type"));
        hBoxLabel.setSpacing(5);
        hBoxLabel.setPadding(new Insets(5));
        vBox.getChildren().addAll(nameHbox, numberHbox);
        HashMap<Label, Map.Entry<TextField, TextField>> columns = new HashMap<>();
        LinkedList<Label> keys = new LinkedList<>();
        ok.setOnMouseClicked(mouseEvent -> {
            if (vBox.getChildren().size() == 2) {
                try {
                    int n = Integer.parseInt(number.getText());
                    if (n < 6) {
                        String nameText = name.getText();
                    if (!name.getText().equals("") && !controller.getTables().containsKey(name.getText())) {
                            controller.createTable(nameText);
                            vBox.getChildren().add(hBoxLabel);
                            for (int i = 1; i <= n; i++) {
                                Label label = new Label("Column " + i);
                                keys.addLast(label);
                                TextField textField = new TextField();
                                TextField textField1 = new TextField();
                                columns.put(label, Map.entry(textField, textField1));
                                HBox hBox = new HBox(label, textField, textField1);
                                hBox.setSpacing(5);
                                hBox.setPadding(new Insets(5));
                                vBox.getChildren().add(hBox);
                            }
                            Button buttonOk = new Button("OK");
                            HBox hBoxButton = new HBox(buttonOk);
                            hBoxButton.setSpacing(5);
                            hBoxButton.setAlignment(Pos.CENTER_RIGHT);
                            hBoxButton.setPadding(new Insets(5));
                            vBox.getChildren().add(hBoxButton);
                            buttonOk.setOnMouseClicked(mouseEvent1 -> {
                                try {
                                    for (Label key : keys) {
                                        if (!validType.contains(columns.get(key).getValue().getText()))
                                            throw new RuntimeException("InValid type in " + key.getText());
                                    }
                                    HashSet<String> namesCol = new HashSet<>();
                                    for (Label key : keys) {
                                        namesCol.add(columns.get(key).getKey().getText());
                                    }
                                    if (namesCol.size() != n)
                                        throw new RuntimeException("duplicated name !");
                                    for (Label key : keys) {
                                        controller.addColumn(columns.get(key).getKey().getText(), nameText, columns.get(key).getValue().getText());
                                    }
                                    alert(Alert.AlertType.CONFIRMATION, "Added successfully");
                                    new EditTable(controller.getTables().get(name.getText()), controller).start(new Stage());
                                    stage.close();

                                } catch (Exception e) {
                                    alert(Alert.AlertType.ERROR, e.getMessage());
                                }
                            });

                        } else {
                            throw new RuntimeException("duplicated name!");
                        }
                    } else {
                        throw new RuntimeException("must be lower or equal 6 !");
                    }
                } catch (Exception e) {
                    alert(Alert.AlertType.ERROR, e.getMessage());
                }
            }
        });
        return vBox;
    }
    private void alert(Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("my-alert-style");
        dialogPane.setMinHeight(250);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icon.png"));
        alert.getDialogPane().getStylesheets().add("style.css");
        alert.showAndWait();
    }
    static {
        String[] type = "boolean byte short int long float double char String".split(" ");
        validType = new HashSet<>();
        Collections.addAll(validType, type);
    }
}
