package com.kyle.takenote.ui.controller.shell;

//import java.util.logging.Logger;

import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.domain.service.PageService;
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
    private PageService pageService;

    private Navigator navigator;

    //---------FXML Fields-----------//
    
    @FXML private StackPane sideMenuArea;

    @FXML private StackPane contentArea;

    @FXML private StackPane controlHost;

   

    

    //--------------------- FXML Methods-----------------//



    @FXML
    private void initialize(){
         // TODO: add logic here
        
    }
 
    @FXML
    private void handleShowHome() {navigator.showHome();}

    @FXML
    private void handleShowCollection() {navigator.showCollections();}


    //-----------------------Methods-----------------------------//

    public void setServices(CollectionService cs, NoteService ns, PageService ps) {
        this.collectionService = cs;
        this.noteService = ns;
        this.pageService = ps;

        this.navigator = new Navigator(contentArea, controlHost, sideMenuArea, cs, ns, ps);
        navigator.showSideMenu();
        navigator.showHome(); // for my default screen
    }


    //-------------Helper Methods -------------//

   

    
    
}
