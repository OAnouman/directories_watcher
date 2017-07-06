package com.graphicodeci.view;

import com.graphicodeci.helpers.WatcherService;
import com.graphicodeci.main.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
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
    @FXML private ListView<String> monitorListview;

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
        startWatcherButton.setDisable(true);
        stopWatcherButton.setDisable(true);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Shows up a directory chooser dialog and save the
     * chosen path
     */
    @FXML
    private void handleChooseDirectory(){

        //Use directoryChooser for let user select the directory he wants
        //to be monitored
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


    /**
     * Starts monitoring of the chosen directory
     */
    @FXML
    private void handleStartWatcher(){

        //Instantiate the background task with chosen path
        wService = new WatcherService(monitoredPath);

        //Bi-Bind controller flag and wService flag to sync
        //watcher state between controller and bg task
        stopWatcherFlag.bindBidirectional(wService.stopTaskFlagProperty());

        //Starting monitoring and set task statue to running
        stopWatcherFlag.set(false);
        wService.start();

        //Disable start monitoring button and enable stop
        stopWatcherButton.setDisable(false);
        startWatcherButton.setDisable(true);

        //Add listener to be notified when wService value
        // property change
        wService.valueProperty().addListener(this::onChanged);
    }

    /**
     * Stop monitoring
     */
    @FXML
    private void handleStopMonitoring(){

        //Set task statue to stopped
        // and stop bg task
        stopWatcherFlag.set(true);
        wService.cancel();

        //Switch buttons states
        stopWatcherButton.setDisable(true);
        startWatcherButton.setDisable(false);

        //Unbind controller and wService flag
        stopWatcherFlag.unbindBidirectional(wService.stopTaskFlagProperty());

        //Unregister listener to free up memory
        wService.valueProperty().removeListener(this::onChanged);
    }

    //Listener for task value update
    private void onChanged(ObservableValue<? extends Path> observable, Path oldValue, Path newValue){

        monitorListview.getItems().add(newValue.toString());
    }
}
