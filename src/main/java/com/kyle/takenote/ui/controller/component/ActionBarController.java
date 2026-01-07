package com.kyle.takenote.ui.controller.component;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class ActionBarController {
    

    //---------Fields--------//

    /**
     * TODO: add logger for debugging purpose later in near future.
     */
    //private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());
    
    //----FXML Fields----//
    @FXML
    private StackPane controlHost;






    //------------------FXML Methods-----------------//


    @FXML
    private void handleNewNote(){
     //TODO: Add logic here for creating new note.
    }

    @FXML
    private void handleNewCollection(){
        //TODO: Add logic here for creating new Collection.
    }

   



    

   


    //----------------Helper Methods-------------------//

    public void showControls(String fxmlPath) {
        try {

            var url = getClass().getResource(fxmlPath);

            if (url == null) {
                throw new IllegalArgumentException("FXML not found: " + fxmlPath);
            }
            
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            controlHost.getChildren().setAll(root);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load controls: " + fxmlPath, e);
        }
    }

   
}
