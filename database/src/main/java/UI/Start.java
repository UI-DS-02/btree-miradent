package UI;

import DataBaseManager.DBM;
import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application
{
    @Override
    public void start(Stage primaryStage){
        new CreateTable(new DBM()).start(new Stage());
    }
}
