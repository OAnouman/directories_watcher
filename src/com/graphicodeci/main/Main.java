package com.graphicodeci.main;

import com.graphicodeci.view.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private final String ROOT_LAYOUT_PATH = "../view/RootLayout.fxml/";


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Directories Watcher");

        initRootLayout();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void initRootLayout(){
        //Load RootLayout.fxml
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(ROOT_LAYOUT_PATH));
            //Load and cast to anchor pane
            AnchorPane rootLayout = loader.load();

            //Setting primary stage scene
            primaryStage.setScene(new Scene(rootLayout));

            //Retrieving controller
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
