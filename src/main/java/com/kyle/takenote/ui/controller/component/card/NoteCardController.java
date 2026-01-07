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


    public void setNavigator(Navigator navigator){
        this.navigator = navigator;
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
    
    
    /*
     * TODO: fix this later. Need a route inside Navigator.java.
     * This will handle the click part to open up the Note editor for the user.
    /* 
    @FXML
    private void handleNoteClick(MouseEvent e) {
        if (navigator != null && noteId != null) {
            navigator.showNotesForCollection(noteId);
        }
    }
    */



}
