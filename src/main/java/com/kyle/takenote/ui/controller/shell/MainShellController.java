package com.kyle.takenote.ui.controller.shell;

//import java.util.logging.Logger;

import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

/**
 * Notes: Owns layout regions + navigation targets.
 */
public class MainShellController  {


    //---------Fields------------//

    /**
     * TODO: Add logger logic behavior later.
     */
    //private static final Logger LOGGER = Logger.getLogger(MainShellController.class.getName());

    private CollectionService collectionService;
    private NoteService noteService;

    private Navigator navigator;

    //---------FXML Fields-----------//
    @FXML
    private StackPane contentArea;

    @FXML
    private com.kyle.takenote.ui.controller.component.ActionBarController actionBarController; 

    

    //--------------------- FXML Methods-----------------//



    @FXML
    private void initialize(){
         // TODO: add logic here. 
    }
 
    @FXML
    private void handleShowHome() {navigator.showHome();}
 
    @FXML
    private void handleShowNote() {navigator.showNotesForCollection(collectionService.getDefaultCollection().getId());}

    @FXML
    private void handleShowCollection() {navigator.showCollections();}


    //-----------------------Methods-----------------------------//

    public void setServices(CollectionService cs, NoteService ns) {
        this.collectionService = cs;
        this.noteService = ns;

        this.navigator = new Navigator(contentArea, actionBarController, cs, ns);
        navigator.showHome(); // for my default screen
    }


    //-------------Helper Methods -------------//

   

    
    
}
