package com.kyle.takenote.ui.controller.component.actionbarcontroller;

//----------Java Imports---------//
import java.io.IOException;
import java.util.UUID;

import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;


/**
 * Notes: this class is controller for bottom part of the application. 
 * 
 */
public class CollectionActionBarController 
    implements Navigator.SupportsNavigator, Navigator.SupportsServices, Navigator.SupportsActiveCollection, Navigator.SupportsSelectedCollection{
    

    //---------Fields--------//
    private CollectionService collectionService;
    private NoteService noteService;
    private Navigator navigator;
    private UUID activeCollectionId;
    private UUID selectedCollectionId;

    

    /**
     * TODO: add logger for debugging purpose later in near future.
     */
    //private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());
    
    //----FXML Fields----//
    @FXML private StackPane controlHost;
    @FXML private Button deleteCollectionBtn;
    
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

    @Override
    public void setSelectedCollectionId(UUID id) {
        this.selectedCollectionId = id;

         // Enable button when collection folder is selected.
         deleteCollectionBtn.setDisable(id == null);
    }

   

    private void requireInjected() {
        if (navigator == null || collectionService == null || noteService == null) {
            throw new IllegalStateException("ActionBarController not injected (services/navigator).");
        }
    }




    //------------------FXML Methods-----------------//


    @FXML
    private void initialize() {
        deleteCollectionBtn.setDisable(true);
    }


    @FXML
    private void handleNewNote() {

        requireInjected();

        UUID targetCollectionId =
                (activeCollectionId != null)
                        ? activeCollectionId
                        : collectionService.getOrCreateDefaultCollection().getId();

        Note created = noteService.createNote(null, targetCollectionId, "", "");

        collectionService.saveToDisk();
        noteService.saveToDisk();

        navigator.showNoteEditor(targetCollectionId, created.getId());
    }


 
    

    @FXML
    private void handleNewCollection() {
        requireInjected();

        // Use the page currently selected in the app
        UUID pageId = navigator.getActivePageId();

        // Fallback to default page if nothing selected yet
        if (pageId == null) {
            pageId = collectionService.getDefaultPageId();
        }

        Collection created = collectionService.createCollection(pageId, "Untitled Name");
        collectionService.saveToDisk();

        // refresh board
        navigator.showCollections();
    }



    @FXML
    private void handleDeleteCollection() {
        requireInjected();

        if (selectedCollectionId == null) return;

        // Don’t allow deleting default (if it exists)
        if (selectedCollectionId.equals(collectionService.getDefaultCollectionId())) {
            return;
        }

        // Delete notes that belong to this collection (important!)
        for (var note : new java.util.ArrayList<>(noteService.getNotesForCollection(selectedCollectionId))) {
            noteService.deleteNote(note.getId());
        }

        // Delete the collection itself
        if (collectionService.deleteCollection(selectedCollectionId)) {
            collectionService.saveToDisk();
            noteService.saveToDisk();
        }

        // Clear selection + refresh board
        selectedCollectionId = null;
        navigator.setSelectedCollectionId(null);

        navigator.showCollections();
        selectedCollectionId = null;
        deleteCollectionBtn.setDisable(true);
        navigator.setSelectedCollectionId(null);
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
