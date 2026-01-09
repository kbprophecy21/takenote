package com.kyle.takenote.ui.controller.component.card;

import java.util.UUID;

import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


/**
 * Notes: Reuseable component controller (?optional).
 * 
 */
public class NoteCardController {
    

    //----FXML Fields----------//
    @FXML private Label titleLabel;
    @FXML private Label bodyLabel;

    //-------Fields--------//
    private Navigator navigator;
    private UUID noteId;
    private UUID collectionId;


    public void setNavigator(Navigator navigator){
        this.navigator = navigator;
    }

    public void setActiveCollectionId(UUID id) {
        this.collectionId = id;
    }


    /**
     * TODO: update function to pass in Note object, instead of this.
     * TODO: Fix null pointer issue, this has terrible logic flow. Redo the whole funciton!!!
     */
    public void setData(UUID noteId, String name, String bodyText){
        if (noteId == null){
            throw new IllegalArgumentException("\"noteId\"cannot be null");
        }
        if (name == null || name.equals("")){
            titleLabel.setText("Untitled");
        }
        if (bodyText == null){
            bodyLabel.setText("");
        }
        if (bodyText.length() > 80){
            bodyLabel.setText(bodyText.substring(0, 80) + "...");
        }
    
        this.noteId = noteId;
        titleLabel.setText(name);
    }
    
    
    /**
     * TODO: Needs to be updated!
     * This will handle the click part to open up the Note editor for the user.
     */
    @FXML
    private void handleNoteCardClick() {
        if (navigator == null) {
            System.out.println("Navigator is null");
            return;
        }
        if (collectionId == null || noteId == null) {
            System.out.println("Missing IDs");
            return;
        }

        navigator.showNoteEditor(collectionId, noteId);
    }

  
    



}
