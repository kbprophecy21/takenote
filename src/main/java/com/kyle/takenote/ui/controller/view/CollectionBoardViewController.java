package com.kyle.takenote.ui.controller.view;

//--------Java Imports---------//
import java.io.IOException;
import java.util.UUID;

import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.domain.service.PageService;
import com.kyle.takenote.ui.controller.component.card.CollectionCardController;
import com.kyle.takenote.ui.controller.component.card.NoteCardController;
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
    implements Navigator.SupportsNavigator, Navigator.SupportsServices, Navigator.SupportsActivePage, 
        Navigator.SupportsPageService {
    
    //--------Fields--------//
        /**
         * TODO: add logger for debugging purpose later in near future.
         */
    //private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());
    
    private Navigator navigator;
    private CollectionService collectionService;
    private NoteService noteService;
    private PageService pageService;
    private UUID activePageId;
    private UUID defaultPageId;

    private Node draggedCard;
    private int lastHoverIndex = -1;
    
    //---------FXML Fields----------//
    @FXML
    private Pane collectionBoard;

    

    //---------------------------API'S Services & Navigator -----------------------------------//
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
    public void setPageService(PageService ps) {
        this.pageService = ps;
    }

    @Override
    public void setActivePageId(UUID id) {
        this.activePageId = id;
        refresh();
    }

   
    

    //--------------------------Methods------------------------------//


    private void renderCollectionCard(Collection c, NoteService ns) {
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
            enableCollectionDragMove(cardRoot, id);
            
            collectionBoard.getChildren().add(cardRoot);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load CollectionCard.fxml && NoteCard.fxml", e);
        }
    }

    private void renderNoteCard(Note n) {
       try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/com/kyle/takenote/fxml/component/cards/NoteCard.fxml"
        ));

        Parent cardRoot = loader.load();
        
        NoteCardController noteController  = loader.getController();

        noteController.setNavigator(navigator);
        noteController.setData(n.getId(), n.getTitle(), n.getBody());

        cardRoot.setLayoutX(n.getPosX());
        cardRoot.setLayoutY(n.getPosY());

        UUID id = n.getId();
        enableNoteDragMove(cardRoot, id);

        noteController.setActivePageId(activePageId);
        
        collectionBoard.getChildren().add(cardRoot);
      }
      catch (IOException e){
        throw new RuntimeException("Failed to load NoteCard.fxml", e);
      }
    }

    
    public void setDefaultPageId(UUID id) {this.defaultPageId = id;}

    public UUID getDefaultPageId(){
        return defaultPageId;
    }


    private void enableCollectionDragMove(Node node, UUID collectionId) {

        final double[] offset = new double[2];
        final boolean[] moved = new boolean[] { false };

        node.setOnMousePressed(e -> {
            // mouse offset inside the card
            offset[0] = e.getX();
            offset[1] = e.getY();
            moved[0] = false;
            node.toFront();
            e.consume();
        });

        node.setOnMouseDragged(e -> {
            moved[0] = true;

            // convert mouse point to board coords
            var p = collectionBoard.sceneToLocal(e.getSceneX(), e.getSceneY());

            double newX = p.getX() - offset[0];
            double newY = p.getY() - offset[1];

            node.setLayoutX(newX);
            node.setLayoutY(newY);

            e.consume();
        });

        node.setOnMouseReleased(e -> {
            // only save if it was actually dragged (prevents click from saving)
            if (moved[0]) {
                collectionService.updateCollectionPosition(
                    collectionId,
                    node.getLayoutX(),
                    node.getLayoutY()
                );
                collectionService.saveToDisk();
                
            }
            e.consume();
        });
    }

    private void enableNoteDragMove(Node node, UUID noteId) {

        final double[] offset = new double[2];
        final boolean[] moved = new boolean[] { false };

        node.setOnMousePressed(e -> {
            // mouse offset inside the card
            offset[0] = e.getX();
            offset[1] = e.getY();
            moved[0] = false;
            node.toFront();
            e.consume();
        });

        node.setOnMouseDragged(e -> {
            moved[0] = true;

            // convert mouse point to board coords
            var p = collectionBoard.sceneToLocal(e.getSceneX(), e.getSceneY());

            double newX = p.getX() - offset[0];
            double newY = p.getY() - offset[1];

            node.setLayoutX(newX);
            node.setLayoutY(newY);

            e.consume();
        });

        node.setOnMouseReleased(e -> {
            // only save if it was actually dragged (prevents click from saving)
            if (moved[0]) {
                noteService.updateNotePosition(
                    noteId,
                    node.getLayoutX(),
                    node.getLayoutY()
                );
                noteService.saveToDisk();
            }
            e.consume();
        });
    }


   
    
    /**
     * Note: use this for later use in other type features. 
     */
    private void handleDragReorder(Node node){

        node.setOnDragDetected(e -> {
            draggedCard = node;
            lastHoverIndex = -1;

            var db = node.startDragAndDrop(TransferMode.MOVE);

            var content = new ClipboardContent();
            content.putString("collection-card"); // marker only part kyle;
            db.setContent(content);

            var img = node.snapshot(null, null);
            db.setDragView(img);
            db.setDragView(img, img.getWidth() / 2, img.getHeight() / 2);

            node.setOpacity(0.4);
            node.getStyleClass().add("dragging"); // TODO: add .dragging style class in my .css file in collectionboard.css 

            e.consume();
        });

        node.setOnDragOver(e -> {
            if (draggedCard == null) return;
            if (draggedCard == node) return;
            if (!e.getDragboard().hasString()) return;

            e.acceptTransferModes(TransferMode.MOVE);

            // live make room for the other collection cards.
            int hoverIndex = collectionBoard.getChildren().indexOf(node);
            if (hoverIndex != lastHoverIndex) {
                collectionBoard.getChildren().remove(draggedCard);
                collectionBoard.getChildren().add(hoverIndex, draggedCard);
                lastHoverIndex = hoverIndex;
            }

            

            e.consume();
        });

            node.setOnDragDropped(e -> {
            e.setDropCompleted(draggedCard != null);
            e.consume();
        });

        node.setOnDragDone(e -> {
            draggedCard = null;
            lastHoverIndex = -1;

            node.setOpacity(1.0);
            node.getStyleClass().remove("dragging"); // TODO: add .dragging style class in my .css file in collectionboard.css 
            
            e.consume();
        });
    }


    //----------Helper Methods----------//

    private void refresh() {
        System.out.println("REFRESH CALLED!!"); // TEST
        System.out.println("\nCOLLECTION BOARD: " + collectionBoard); // TEST
        System.out.println("\nCOLLECTION SERVICES: " + collectionService); // TEST
        System.out.println("\nPAGE SERVICES: " + pageService); // TEST
        System.out.println("\nNOTE SERVICES: " + noteService); // TEST
        System.out.println("\nNAVIGATOR: " + navigator); // TEST
        if (collectionBoard == null || collectionService == null || pageService == null || noteService == null || navigator == null) return;

        collectionBoard.getChildren().clear();

        UUID pageId = navigator.getActivePageId();

        System.out.println("\nACTIVE PAGE ID: " + pageId); // TEST
        if (pageId == null) pageId = pageService.getDefaultPageId();

        for (Collection c : collectionService.getCollectionsForPage(pageId)) {
            renderCollectionCard(c, noteService);
        }

        for (Note n: noteService.getNotesForPage(pageId)) {
            if (n.getCollectionId() == null) {
               renderNoteCard(n); 
            }
            
        }

    }


}
