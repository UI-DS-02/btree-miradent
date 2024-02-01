package UI;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class ShowTable extends Application {
    public ShowTable(String[][] data) {
        this.data = data;
    }
    private final String[][] data;
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setCenter(tableView());
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }
    private TableView<String[]> tableView() {
        TableView<String[]> tableView = new TableView<>();
        for (int i = 1; i < data.length; i++) {
            tableView.getItems().add(data[i]);
        }

        for (int i = 0; i < data[0].length; i++) {
            TableColumn<String[], String> column = new TableColumn<>(data[0][i]);
            final int columnIndex = i ;
            column.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()[columnIndex])));
            tableView.getColumns().add(column);
        }
        return tableView;
    }
}
