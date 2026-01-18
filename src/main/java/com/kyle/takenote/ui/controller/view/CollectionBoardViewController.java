package com.kyle.takenote.ui.controller.view;

//--------Java Imports---------//
import java.io.IOException;
import java.util.UUID;

import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.controller.component.card.CollectionCardController;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * Notes: this class controls the Collection Board View. 
 */

public class CollectionBoardViewController 
    implements Navigator.SupportsNavigator, Navigator.SupportsServices, Navigator.SupportsActivePage {
    
    //--------Fields--------//
        /**
         * TODO: add logger for debugging purpose later in near future.
         */
    //private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());
    
    private Navigator navigator;
    private CollectionService collectionService;
    private NoteService noteService;
    private UUID activePageId;
    private UUID defaultPageId;

    private Node draggedCard;
    private int lastHoverIndex = -1;
    
    //---------FXML Fields----------//
    @FXML
    private Pane collectionBoard;

    

    //------------Methods---------------//
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

    @Override
    public void setActivePageId(UUID id) {
        this.activePageId = id;
        refresh();
    }

   
    

    private void addCollectionCard(Collection c, NoteService ns) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/kyle/takenote/fxml/component/cards/CollectionCard.fxml"
            ));

            Parent cardRoot = loader.load();
            CollectionCardController cardController = loader.getController();

            cardController.setNavigator(navigator);
            cardController.setData(c.getId(), c.getName(), ns.getNotesForCollection(c.getId()).size());

            cardRoot.setLayoutX(c.getPosX());
            cardRoot.setLayoutY(c.getPosY());

            UUID id = c.getId();
            enableDragMove(cardRoot, id);
            
            collectionBoard.getChildren().add(cardRoot);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load CollectionCard.fxml", e);
        }
    }

    
    public void setDefaultPageId(UUID id) {this.defaultPageId = id;}

    public UUID getDefaultPageId(){
        return defaultPageId;
    }

    /**
     * Note: use this for later use in other type features. 
     */
    private void handleDragReorder(Node card){

        card.setOnDragDetected(e -> {
            draggedCard = card;
            lastHoverIndex = -1;

            var db = card.startDragAndDrop(TransferMode.MOVE);

            var content = new ClipboardContent();
            content.putString("collection-card"); // marker only part kyle;
            db.setContent(content);

            var img = card.snapshot(null, null);
            db.setDragView(img);
            db.setDragView(img, img.getWidth() / 2, img.getHeight() / 2);

            card.setOpacity(0.4);
            card.getStyleClass().add("dragging"); // TODO: add .dragging style class in my .css file in collectionboard.css 

            e.consume();
        });

        card.setOnDragOver(e -> {
            if (draggedCard == null) return;
            if (draggedCard == card) return;
            if (!e.getDragboard().hasString()) return;

            e.acceptTransferModes(TransferMode.MOVE);

            // live make room for the other collection cards.
            int hoverIndex = collectionBoard.getChildren().indexOf(card);
            if (hoverIndex != lastHoverIndex) {
                collectionBoard.getChildren().remove(draggedCard);
                collectionBoard.getChildren().add(hoverIndex, draggedCard);
                lastHoverIndex = hoverIndex;
            }

            

            e.consume();
        });

            card.setOnDragDropped(e -> {
            e.setDropCompleted(draggedCard != null);
            e.consume();
        });

        card.setOnDragDone(e -> {
            draggedCard = null;
            lastHoverIndex = -1;

            card.setOpacity(1.0);
            card.getStyleClass().remove("dragging"); // TODO: add .dragging style class in my .css file in collectionboard.css 
            
            e.consume();
        });
    }

    private void enableDragMove(Node card, UUID collectionId) {

        final double[] offset = new double[2];
        final boolean[] moved = new boolean[] { false };

        card.setOnMousePressed(e -> {
            // mouse offset inside the card
            offset[0] = e.getX();
            offset[1] = e.getY();
            moved[0] = false;
            card.toFront();
            e.consume();
        });

        card.setOnMouseDragged(e -> {
            moved[0] = true;

            // convert mouse point to board coords
            var p = collectionBoard.sceneToLocal(e.getSceneX(), e.getSceneY());

            double newX = p.getX() - offset[0];
            double newY = p.getY() - offset[1];

            card.setLayoutX(newX);
            card.setLayoutY(newY);

            e.consume();
        });

        card.setOnMouseReleased(e -> {
            // only save if it was actually dragged (prevents click from saving)
            if (moved[0]) {
                collectionService.updateCollectionPosition(
                    collectionId,
                    card.getLayoutX(),
                    card.getLayoutY()
                );
                collectionService.saveToDisk();
            }
            e.consume();
        });
    }


   



    //----------Helper Methods----------//

    private void refresh() {
        if (collectionBoard == null || collectionService == null || noteService == null || navigator == null) return;

        collectionBoard.getChildren().clear();

        UUID pageId = navigator.getActivePageId();
        if (pageId == null) pageId = collectionService.getDefaultPageId();

        for (Collection c : collectionService.getCollectionsForPage(pageId)) {
            addCollectionCard(c, noteService);
        }
    }


}
