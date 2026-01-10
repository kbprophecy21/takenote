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
import javafx.scene.layout.FlowPane;





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
   @FXML FlowPane noteBoard;

    

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
        renderNotesForActiveCollection();
    };


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

        System.out.println("NoteBoard navigator = " + navigator); // TEST

      }
      catch (IOException e){
        throw new RuntimeException("Failed to load NoteCard.fxml", e);
      }
    }



   


    //----------Helper Methods----------//

    private void refresh() {
        noteBoard.getChildren().clear();
    

    }
}
