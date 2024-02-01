package UI;

import DataBaseManager.DBM;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Table;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class SelectionTable extends Application {
    public SelectionTable(DBM controller, Table table) {
        this.controller = controller;
        this.table = table;
    }

    private final Table table;
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
        stage.setTitle("Selection Table");
        stage.getIcons().add(new Image("icon.png"));
        stage.show();
    }
    private VBox fields() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,10,10,2));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.TOP_LEFT);
//        table info
        StringBuilder nameType = new StringBuilder("{Name : Type}, ");
        for (int i = 0; i < table.getColumnNames().size() * 2; i++) {
            if (i%2 == 0) {
                nameType.append(table.getColumnNames().get(i/2)).append(" : ");
            } else {
                nameType.append(table.getColumnType().get(i/2)).append(",  ");
            }
        }
        Label tableName = new Label("Tabel Name : " + table.getName());
        Label tableCol = new Label(nameType.toString());
        vBox.getChildren().addAll(tableName, tableCol);
        vBox.setBackground(new Background(new BackgroundFill(Color.rgb(229, 178, 153), CornerRadii.EMPTY, Insets.EMPTY)));

//        select
        Label selectV = new Label("       index || columnName, newValue");
        Label labelSelect = new Label("Select :");
        TextField select = new TextField();
        select.setPrefWidth(200);
        Button selectButton = new Button("Select");
        HBox selectHBox = new HBox(labelSelect, select, selectButton);
        selectHBox.setSpacing(5);
        selectButton.setPadding(new Insets(5));
        selectButton.setOnMouseClicked(mouseEvent -> {
            try {
                if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    String[] values = select.getText().split(", ");
                    int index = -1;
                    for (int i = 0; i < table.getColumnCounts(); i++) {
                        if (table.getColumnNames().get(i).equals(values[0])) {
                            index = i;
                        }
                    }
                    int i = 1;
                    switch (table.getColumnType().get(index)) {
                        case "int" -> Integer.parseInt(values[i]);
                        case "boolean" -> {
                            Boolean t = Boolean.parseBoolean(values[i]);
                        }
                        case "byte" -> Byte.parseByte(values[i]);
                        case "short" -> Short.parseShort(values[i]);
                        case "long" -> Long.parseLong(values[i]);
                        case "float" -> Float.parseFloat(values[i]);
                        case "double" -> Double.parseDouble(values[i]);
                        case "char" -> {
                            if (values[i].length() != 1)
                                throw new  InputMismatchException("is not Char!");
                        }
                        case "String" -> {}
                    }
                    alert(Alert.AlertType.CONFIRMATION, controller.selectRow(table.getName(), values[0], values[1]));
                } else {
                    int index = Integer.parseInt(select.getText());
                    alert(Alert.AlertType.CONFIRMATION, controller.selectRow(table.getName(), index));
                }
            }
            catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        TextField rangField = new TextField();
        rangField.setPrefWidth(100);
        Button rangeButton = new Button("Show in Range");
        rangeButton.setOnMouseClicked(mouseEvent -> {
            try {
                String[] values = rangField.getText().split(", ");
                new ShowTable(controller.rangeOfRows(table.getName(), Integer.parseInt(values[0]), Integer.parseInt(values[0]))).start(new Stage());
            } catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        selectHBox.getChildren().addAll(rangField, rangeButton);
        vBox.getChildren().addAll(selectV, selectHBox);

//        block
        Label selectBlock = new Label("       index, columnName");
        Label labelBlock = new Label("Select Block:");
        TextField block = new TextField();
        select.setPrefWidth(200);
        Button blockButton = new Button("Select");
        HBox blockHBox = new HBox(labelBlock, block, blockButton);
        blockHBox.setSpacing(5);
        blockHBox.setPadding(new Insets(5));
        blockButton.setOnMouseClicked(mouseEvent -> {
            try {
                String[] values = block.getText().split(", ");
                alert(Alert.AlertType.CONFIRMATION, controller.selectBlock(table.getName(), Integer.parseInt(values[0]), values[1]));
            }
            catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        vBox.getChildren().addAll(selectBlock, blockHBox);
        Button show = new Button("  Show Table  ");
        show.setOnMouseClicked(mouseEvent -> {
            try {
                new ShowTable(controller.showCompleteTable(table.getName())).start(new Stage());
            }
            catch (Exception e) {
                e.printStackTrace();
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        Button createTable = new Button("   Crate Table   ");
        createTable.setOnMouseClicked(mouseEvent -> {
            try {
                new CreateTable(controller).start(new Stage());
                stage.close();
            }
            catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        Button editTable = new Button("   Edit Table   ");
        editTable.setOnMouseClicked(mouseEvent -> {
            try {
                new EditTable(table, controller).start(new Stage());
                stage.close();
            }
            catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        Button choice = new Button("   Choice Table   ");
        choice.setOnMouseClicked(mouseEvent -> {
            try {
                new ChoiceTable(controller).start(new Stage());
                stage.close();
            }
            catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        HBox box = new HBox(show, createTable, editTable, choice);
        box.setSpacing(5);
        box.setPadding(new Insets(5));
        vBox.getChildren().add(box);
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
}
