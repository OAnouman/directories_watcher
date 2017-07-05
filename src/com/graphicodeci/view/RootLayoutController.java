package com.graphicodeci.view;

import com.graphicodeci.helpers.WatcherService;
import com.graphicodeci.main.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

/**
 * Root layout controller. Initialize and set all fields
 * @author Martial Anouman
 */
public class RootLayoutController {


    @FXML private TextField directoryPathField;

    @FXML private Button chooseDirectoryButton;
    @FXML private Button startWatcherButton;
    @FXML private Button stopWatcherButton;
    @FXML private ListView monitorListview;

    private Path monitoredPath;
    private BooleanProperty stopWatcherFlag = new SimpleBooleanProperty(true);
    WatcherService wService ;

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

        //set default button
        chooseDirectoryButton.setDefaultButton(true);
        //Disable both button on start up
        startWatcherButton.setDisable(false);
        stopWatcherButton.setDisable(false);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleChooseDirectory(){
        //Use directoriyChooser for let user select the directory he wants
        // to monitor
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory to be monitored...");
        directoryChooser.setInitialDirectory(new File("C:\\"));
        File chosenDirectory = directoryChooser.showDialog(mainApp.getPrimaryStage());


        //Check that the path is valid and if it exists
        if(chosenDirectory.isDirectory() && chosenDirectory.exists()){
            //Put the full path in the textField
            directoryPathField.setText(chosenDirectory.getAbsolutePath());
        }

        //Enable buttons after directory has been chosen
        startWatcherButton.setDisable(false);
        stopWatcherButton.setDisable(true);

        //Getting the path
        monitoredPath = Paths.get(chosenDirectory.getAbsolutePath());
    }


    @FXML
    private void handleStartWatcher(){

        wService = new WatcherService(monitoredPath);
        stopWatcherFlag.bindBidirectional(wService.stopTaskFlagProperty());

        //Starting monitoring
        stopWatcherFlag.set(false);
        wService.start();

        //Disable start monitoring button and enable stop
        stopWatcherButton.setDisable(false);
        startWatcherButton.setDisable(true);

    }

    @FXML
    private void handleStopMonitorig(){
        wService.cancel();
        stopWatcherFlag.set(true);

        stopWatcherButton.setDisable(true);
        startWatcherButton.setDisable(false);
    }
}
