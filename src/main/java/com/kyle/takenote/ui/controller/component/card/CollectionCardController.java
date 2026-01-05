package com.kyle.takenote.ui.controller.component.card;

import java.util.UUID;

import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CollectionCardController {
    
    @FXML private Label nameLabel;
    @FXML private Label metaLabel;


    private Navigator navigator;
    private UUID collectionId;

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }


    public void setData(UUID collectionId, String name, int noteCount) {
        this.collectionId = collectionId;
        nameLabel.setText(name);
        metaLabel.setText(noteCount + " notes");
    }

    @FXML
    private void handleClick(MouseEvent e) {
        if (navigator != null && collectionId != null) {
            navigator.showNotesForCollection(collectionId);
        }
    }
}
