package com.kyle.takenote.ui.controller.view;

import java.io.IOException;

import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.controller.component.card.CollectionCardController;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;

/**
 * Notes: this class controls the Collection Board View. 
 */

public class CollectionBoardViewController implements Navigator.SupportsNavigator, Navigator.SupportsServices {
    

        /**
         * TODO: add logger for debugging purpose later in near future.
         */
    //private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());
    

    
    @FXML
    private FlowPane collectionBoard;

    private Navigator navigator;
    private CollectionService collectionService;
    private NoteService noteService;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
        refresh();
    }

    @Override
    public void setServices(CollectionService cs, NoteService ns) {
        this.collectionService = cs;
        this.noteService = ns;
        refresh();
    }

   

    private void addCollectionCard(Collection c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/kyle/takenote/fxml/component/cards/CollectionCard.fxml"
            ));

            Parent cardRoot = loader.load();
            CollectionCardController cardController = loader.getController();

            cardController.setNavigator(navigator);
            cardController.setData(c.getId(), c.getName(), c.getNotes().size());

           collectionBoard.getChildren().add(cardRoot);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load CollectionCard.fxml", e);
        }
    }

   



    //----------Helper Methods----------//

     private void refresh() {
        if (collectionBoard == null || collectionService == null || navigator == null) return;

        collectionBoard.getChildren().clear();

        // For now: show only default collection card
        Collection def = collectionService.getDefaultCollection();

        addCollectionCard(def);
    }
}
