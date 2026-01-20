package com.kyle.takenote.ui.controller.component.card;

//-------Java Imports------//
import java.util.UUID;

import com.kyle.takenote.ui.navigation.Navigator;
import com.kyle.takenote.ui.util.SvgImageLoader;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


/**
 * Notes: Reuseable component controller (?optional).
 * 
 */
public class NoteCardController 
    implements Navigator.SupportsNavigator, Navigator.SupportsActiveCollection {
    

    //----FXML Fields----------//
    @FXML private Label titleLabel;
    @FXML private Label bodyLabel;
    @FXML private Label updatedDateLabel; // TODO: add this to the note card. for the setData();

    //-------Fields--------//
    private Navigator navigator;
    private UUID noteId;
    private UUID collectionId;

    @FXML private ImageView bgImage;


    //---------------Methods--------------------//
    @Override
    public void setNavigator(Navigator navigator){
        this.navigator = navigator;
    }

    @Override
    public void setActiveCollectionId(UUID id) {
        this.collectionId = id;
    }


    /**
     * TODO: update function to pass in Note object, instead of this.
     * TODO: Fix null pointer issue, this has terrible logic flow. Redo the whole funciton!!!
     */
    public void setData(UUID noteId, String name, String bodyText) {

        if (noteId == null) {
            throw new IllegalArgumentException("\"noteId\" cannot be null");
        }
        this.noteId = noteId;

        // Title
        String safeTitle = (name == null || name.isBlank()) ? "Untitled" : name.trim();
        titleLabel.setText(safeTitle);

        // Body preview
        String safeBody = (bodyText == null) ? "" : bodyText.trim();
        if (safeBody.length() > 80) {
            safeBody = safeBody.substring(0, 80) + "...";
        }
        bodyLabel.setText(safeBody);
    }

    

    //----------------FXML Methods----------------------//
    
    /**
     * TODO: Needs to be updated!
     * This will handle the click part to open up the Note editor for the user.
     */


    @FXML
    public void initialize() {
        var img = SvgImageLoader.loadSvgAsImage(
                "/com/kyle/takenote/images/svg/bluestickynoteimage.svg", 
                220, 
                140
        );

        //TEMP: might change these later to fix the file image inside another software.
        /**Note: How to tune it: change the viewport rectangle:
         * Increase x,y → shifts crop right/down
         * Increase width,height → zooms out
         * Decrease width,height → zooms in
         */
        bgImage.setImage(img);

        bgImage.setViewport(new Rectangle2D(
            120, 60,
            360, 260
        ));

        bgImage.setFitWidth(220);
        bgImage.setFitHeight(140);
        bgImage.setPreserveRatio(false);

    }


    @FXML
    private void handleNoteCardClick(MouseEvent event) {


        if (navigator == null) {
            System.out.println("Navigator is null");
            return;
        }
        if (noteId == null) {
            System.out.println("Missing IDs");
            return;
        }

        if (event.getClickCount() == 1) {
            
            navigator.setSelectedNoteId(noteId);
        }
        if (event.getClickCount() == 2) {
            
            navigator.showNoteEditor(collectionId, noteId);
        }
        
        
    }

  
    



}
