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
import java.util.Collections;
import java.util.InputMismatchException;


public class EditTable extends Application {

    private final DBM controller;
    private final Table table;
    private Stage stage;

    public EditTable(Table table, DBM controller) {
        this.table = table;
        this.controller = controller;
    }


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        BorderPane root = new BorderPane();
        root.setCenter(fields());
        Scene scene = new Scene(root, 600,300);
        stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        stage.setResizable(false);
        stage.setTitle("Edit Table");
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

//        insert
        Label labelInsert = new Label("Insert : ");
        TextField insert = new TextField();
        insert.setPrefWidth(420);
        Button insertButton = new Button("Insert");
        HBox insertHBox = new HBox(labelInsert, insert, insertButton);
        insertHBox.setSpacing(5);
        insertHBox.setPadding(new Insets(5));
        insertButton.setOnMouseClicked(mouseEvent -> {
            try {
                String[] values = insert.getText().split(", ");
                for (int i = 0; i < table.getColumnType().size(); i++) {
                    switch (table.getColumnType().get(i)) {
                        case "int" -> Integer.parseInt(values[i]);
                        case "boolean" -> {
                            Boolean b = Boolean.parseBoolean(values[i]);
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
                }
                ArrayList<String> valArray = new ArrayList<>();
                Collections.addAll(valArray, values);
                controller.insertRow(table.getName(), valArray);
                alert(Alert.AlertType.CONFIRMATION, "inserted successfully");
            }
             catch (Exception e) {
                 alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        vBox.getChildren().add(insertHBox);

//        update
        Label updateCommand = new Label("columnName, oldValue, newValue ||  index, columnName, newValue");
        Label labelUpdate = new Label("Update :");
        TextField update = new TextField("");
        update.setPrefWidth(420);
        Button updateButton = new Button(" Update");
        HBox updateHBox= new HBox(labelUpdate, update, updateButton);
        updateHBox.setSpacing(5);
        updateButton.setPadding(new Insets(5));
        updateButton.setOnMouseClicked(mouseEvent -> {
            try {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    String[] values = update.getText().split(", ");
                    int index = -1;
                    for (int i = 0; i < table.getColumnCounts(); i++) {
                        if (table.getColumnNames().get(i).equals(values[0])) {
                            index = i;
                        }
                    }
                    for (int i = 1; i < 3; i++) {
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
                    }
                    controller.updateRow(table.getName(), values[0], values[1], values[2]);
                    alert(Alert.AlertType.CONFIRMATION, "updated successfully");
                } else {
                    String[] values = update.getText().split(", ");
                    int indexRow = Integer.parseInt(values[0]);
                    int index = -1;
                    for (int i = 0; i < table.getColumnCounts(); i++) {
                        if (table.getColumnNames().get(i).equals(values[1])) {
                            index = i;
                        }
                    }
                    switch (table.getColumnType().get(index)) {
                        case "int" -> Integer.parseInt(values[2]);
                        case "boolean" -> {
                            Boolean t = Boolean.parseBoolean(values[2]);
                        }
                        case "byte" -> Byte.parseByte(values[2]);
                        case "short" -> Short.parseShort(values[2]);
                        case "long" -> Long.parseLong(values[2]);
                        case "float" -> Float.parseFloat(values[2]);
                        case "double" -> Double.parseDouble(values[2]);
                        case "char" -> {
                            if (values[2].length() != 1)
                                throw new InputMismatchException("is not Char!");
                        }
                        case "String" -> {
                        }
                    }
                    controller.updateByIndex(table.getName(), indexRow, values[1], values[2]);
                    alert(Alert.AlertType.CONFIRMATION, "updated successfully");
                }
            }
            catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        vBox.getChildren().addAll(updateCommand, updateHBox);

//        delete
        Label labelDelete = new Label("Delete : ");
        TextField delete = new TextField("");
        delete.setPrefWidth(421);
        Button deleteButton = new Button(" Delete ");
        HBox deleteHBox = new HBox(labelDelete, delete, deleteButton);
        deleteHBox.setSpacing(5);
        deleteButton.setPadding(new Insets(5));
        deleteButton.setOnMouseClicked(mouseEvent -> {
            try {
                int index = Integer.parseInt(delete.getText());
                if (index >= table.getRows().size())
                    throw new RuntimeException("InValid index");
                controller.deletion(table.getName(), index);
                alert(Alert.AlertType.CONFIRMATION, "deleted");
            } catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        vBox.getChildren().add(deleteHBox);

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
        Button selectTable = new Button("   Selection Table   ");
        selectTable.setOnMouseClicked(mouseEvent -> {
            try {
                new SelectionTable(controller, table).start(new Stage());
                stage.close();
            }
            catch (Exception e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
        HBox box = new HBox(show, createTable, selectTable);
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
