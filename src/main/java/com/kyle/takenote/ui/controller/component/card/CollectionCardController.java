package com.kyle.takenote.ui.controller.component.card;

import java.util.UUID;

import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;


/**
 * Notes: This class is responsible for the controller of my collection cards.
 */
public class CollectionCardController implements Navigator.SupportsNavigator, Navigator.SupportsSelectedCollection {
    
    //------------Fields----------//
    private Navigator navigator;
    private UUID collectionId;

    //----------FXML Fields--------------//
    @FXML private Label nameLabel;
    @FXML private Label metaLabel;
    @FXML private StackPane root;


    //------------------Methods--------------------------//
    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void setSelectedCollectionId(UUID id) {
        this.collectionId = id;
    }


    public void setData(UUID collectionId, String name, int noteCount) {
        this.collectionId = collectionId;
        nameLabel.setText(name);
        metaLabel.setText(noteCount + " notes");
    }


    public void setSelected(boolean selected) {
        if (selected) {
            if (!root.getStyleClass().contains("selected")) root.getStyleClass().add("selected");
        } else {
            root.getStyleClass().remove("selected");
        }
    }

    //---------------FXML Methods------------------//

    @FXML
    private void initialize() {
        root.getStyleClass().add("collection-card");

        root.setOnMouseEntered(e -> {
            if (!root.getStyleClass().contains("hovered")) {
                root.getStyleClass().add("hovered");
            }
        });

        root.setOnMouseExited(e -> root.getStyleClass().remove("hovered"));
    }


    @FXML
    private void handleCollectionCardClick(MouseEvent event) {
        if (navigator != null && collectionId != null) {

            if (event.getClickCount() == 1){
               navigator.setSelectedCollectionId(collectionId);
            }
            if (event.getClickCount() == 2) {
                navigator.showNotesForCollection(collectionId);
            }
            
        }
    }
}
