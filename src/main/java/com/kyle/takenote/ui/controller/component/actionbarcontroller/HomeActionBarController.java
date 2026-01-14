package com.kyle.takenote.ui.controller.component.actionbarcontroller;

//----------Java Imports---------//
import java.io.IOException;
import java.util.UUID;

import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;


/**
 * Notes: this class is Home controller for bottom part of the application. 
 * 
 */
public class HomeActionBarController 
    implements Navigator.SupportsNavigator, Navigator.SupportsServices, Navigator.SupportsActiveCollection {
    

    //---------Fields--------//
    private CollectionService collectionService;
    private NoteService noteService;
    private Navigator navigator;

    private UUID activeCollectionId;
    

    /**
     * TODO: add logger for debugging purpose later in near future.
     */
    //private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());
    
    //----FXML Fields----//
    @FXML
    private StackPane controlHost;

    
    //---------Methods----------//
    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void setServices(CollectionService cs, NoteService ns) {
        this.collectionService = cs;
        this.noteService = ns;
    }

    @Override
    public void setActiveCollectionId(UUID id) {
        this.activeCollectionId = id;
    }

    

    private void requireInjected() {
        if (navigator == null || collectionService == null || noteService == null) {
            throw new IllegalStateException("ActionBarController not injected (services/navigator).");
        }
    }




    //------------------FXML Methods-----------------//


   @FXML
    private void handleNewNote() {

        requireInjected();

        UUID targetCollectionId =
                (activeCollectionId != null)
                        ? activeCollectionId
                        : collectionService.getOrCreateDefaultCollection().getId();

        Note created = noteService.createNote(targetCollectionId, "", "");

        collectionService.saveToDisk();
        noteService.saveToDisk();

        navigator.showNoteEditor(targetCollectionId, created.getId());
    }

 
    

    @FXML
    private void handleNewCollection(){
        //TODO: Add logic here for creating new Collection.
    }


    

   

    //----------------Helper Methods-------------------//

    public void showControls(String fxmlPath) {
        try {
            var url = getClass().getResource(fxmlPath);
            if (url == null) throw new IllegalArgumentException("FXML not found: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            
            Object controlsController = loader.getController();
            if (controlsController instanceof Navigator.SupportsServices s) {
                s.setServices(collectionService, noteService);
            }
            if (controlsController instanceof Navigator.SupportsNavigator n) {
                n.setNavigator(navigator);
            }

            controlHost.getChildren().setAll(root);

        } catch (IOException e) {
                throw new RuntimeException("Failed to load controls: " + fxmlPath, e);
            }


    }

   
}
