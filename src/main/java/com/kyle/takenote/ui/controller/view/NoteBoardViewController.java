package com.kyle.takenote.ui.controller.view;

//------Java imports------//
import java.io.IOException;
import java.util.UUID;

import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.controller.component.card.NoteCardController;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;




/**
 * Note: This class is responsible for handle the controller logic of my NoteBoardView.fxml. 
 */
public class NoteBoardViewController 
    implements Navigator.SupportsNavigator, Navigator.SupportsServices, Navigator.SupportsActiveCollection {


    //------Fields---------//
    /**
     * TODO: add logger for debugging purpose later in near future.
     */
    //private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());
   

    
    private UUID activeCollectionId;
    private UUID selectedNoteId;

    private CollectionService collectionService;
    private NoteService noteService;
    private Navigator navigator;


    //---FXML Fields-------//
   @FXML private FlowPane noteBoard;
   @FXML private Label collectionNameLabel;

    

   //----------------------Methods----------------//

   /**
    * TODO: have refresh() safeGuard null checks for navigator, services, activeCollectionId.
    */
   
    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
        refresh();
    }

    @Override
    public void setServices(CollectionService cs, NoteService ns){
        this.collectionService = cs;
        this.noteService = ns;
    }

   @Override
   public void setActiveCollectionId(UUID id) {
        this.activeCollectionId = id;
        requireInjected();
        
        if (collectionNameLabel != null) {
            collectionNameLabel.setText(collectionService.getCollectionById(activeCollectionId).getName());
        }
        
        renderNotesForActiveCollection();
    };

    private void requireInjected() {
        if (navigator == null || collectionService == null || noteService == null) {
            throw new IllegalStateException("NoteBoardViewController not injected (services/navigator).");
        }
    }


    private void renderNotesForActiveCollection() {
        if (noteBoard == null) throw new NullPointerException("\"noteBoard\" cannot be null (FXML injection didn't happen).");
        if (collectionService == null) throw new NullPointerException("\"collectionService\" cannot be null (Navigator did't inject services).");
        if (activeCollectionId == null) throw new NullPointerException("\"activeCollectionId\" cannot be null (No collection selected yet).");
        
        Collection activeCollection = collectionService.getCollectionById(activeCollectionId);
        if (activeCollection == null) throw new NullPointerException("\"activeCollection\" cannot be null.");
        noteBoard.getChildren().clear();
        for (Note note : noteService.getNotesForCollection(activeCollectionId)) {
            addNoteCard(note);
        }
    }
    
    /**
     * TODO: update function so navigator will handle loader.
     */
    private void addNoteCard(Note n){
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/com/kyle/takenote/fxml/component/cards/NoteCard.fxml"
        ));

        Parent cardRoot = loader.load();
        NoteCardController noteController = loader.getController();

        noteController.setNavigator(navigator);
        noteController.setData(n.getId(), n.getTitle(), n.getBody());

        noteController.setActiveCollectionId(activeCollectionId);
        
        noteBoard.getChildren().add(cardRoot);


      }
      catch (IOException e){
        throw new RuntimeException("Failed to load NoteCard.fxml", e);
      }
    }


    //---------------FXML Methods----------------------//

    @FXML
    private void handleRenameCollection() {
        requireInjected();

        var collection = collectionService.getCollectionById(activeCollectionId);
        if (collection == null) return;

        TextInputDialog dialog = new TextInputDialog(collection.getName());
        dialog.setTitle("Rename Collection");
        dialog.setHeaderText(null);
        dialog.setContentText("New Name: ");

        dialog.showAndWait().ifPresent(input -> {
            String newName = input.trim();
            if (newName.isBlank()) return;

            collectionService.renameCollection(activeCollectionId, newName);
            collectionService.saveToDisk();

            collectionNameLabel.setText(newName);
        });
    }



   


    //----------Helper Methods----------//

    private void refresh() {

        if (noteBoard != null) {
            noteBoard.getChildren().clear();
        }
    }
}
