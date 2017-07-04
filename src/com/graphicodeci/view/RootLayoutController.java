package com.graphicodeci.view;

import com.graphicodeci.main.Main;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.text.html.ListView;
import java.awt.*;
import java.io.File;

/**
 * Root layout controller. Initialize and set all fields
 * @author Martial Anouman
 */
public class RootLayoutController {


    @FXML private TextField pirectoryPath;

    @FXML private Button chooseDirectort;
    @FXML private Button startWatcher;
    @FXML private Button stopWatcher;

    @FXML private ListView monitor;

    private Main mainApp;




    /**
     * Default constructor. Needed for FXMLLoader
     */
    public RootLayoutController(){}

    /**
     * Called after contructor when all fields have been initializad.
     */
    @FXML
    private void initialize(){

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleChooseDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory to be monitored...");
        directoryChooser.setInitialDirectory(new File("C:\\"));
        File chosenDirectory = directoryChooser.showDialog(mainApp.getPrimaryStage());
    }


}
