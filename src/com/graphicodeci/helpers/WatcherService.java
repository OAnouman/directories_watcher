package com.graphicodeci.helpers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by MARTIAL ANOUMAN on 7/5/2017.
 *
 * This class extends Service class.
 * It's used to create and run a background task
 * for handling events popped up by FileSystems watcher
 *
 * @author Martial Anouman
 */
public class WatcherService extends Service<Path> {

    private WatchService watcher ;
    private Path monitoredPath;
    private final BooleanProperty stopTaskFlag;

    /**
     * Construstor with oarameter
     * @param monitoredPath : Directory absolute path to be monitored
     */
    public WatcherService(Path monitoredPath) {
        this.monitoredPath = monitoredPath;

        //Task stopped on start up
        this.stopTaskFlag = new SimpleBooleanProperty(true);
    }

    /**
     * Instanciate a watcher and register for event
     * @return
     */
    private WatchKey initWatcher(){
        try {
            watcher = FileSystems.getDefault().newWatchService();

            //Setting a watchKey used to get notified about
            // events related to the monitored path
            WatchKey key = monitoredPath.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY
            );

            return key;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Task createTask() {

        return new Task<Path>(){
            @Override
            protected Path call() throws Exception {

                System.out.println("Starting monitoring...");

                WatchKey key = initWatcher();
                if (key == null) {
                    updateMessage("Failed");
                    return null;
                }

                for(;;){

                    //Wait for key to be signaled (notified about an event)
                    //If key null end loop
                    try {
                        key = watcher.take();
                    } catch (InterruptedException e) {
                        break;
                    }

                    //Fetching array of events
                    for(WatchEvent<?> event : key.pollEvents()){

                        //Retrieving and testing event kind
                        WatchEvent.Kind<?> kind = event.kind();
                        WatchEvent<Path> ev = (WatchEvent<Path>)event;

                        if(kind == StandardWatchEventKinds.ENTRY_CREATE){
                            updateValue(ev.context());
                        }
                        if(kind == StandardWatchEventKinds.ENTRY_MODIFY){
                            updateValue(ev.context());
                        }
                        if(kind == StandardWatchEventKinds.ENTRY_DELETE){
                            updateValue(ev.context());
                        }

                        //Loop end if ....
                        if(stopTaskFlag.get()) {
                            System.out.println("Stopping monitoring...");
                            break;
                        }
                    }
                    key.reset();
                }

                return null;
            }
        };
    }

    public boolean isStopTaskFlag() {
        return stopTaskFlag.get();
    }

    public BooleanProperty stopTaskFlagProperty() {
        return stopTaskFlag;
    }
}
