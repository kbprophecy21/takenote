package com.kyle.takenote.ui.navigation;

/**
 * Notes: Swaps screen in the shell, fxml files, handles all navigation (My "router").
 */
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.controller.component.ActionBarController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class Navigator {

    private static final Logger LOGGER = Logger.getLogger(Navigator.class.getName());

    private final StackPane contentArea;
    private final ActionBarController actionBarController;

    private final CollectionService collectionService;
    private final NoteService noteService;

    public Navigator(
            StackPane contentArea,
            ActionBarController actionBarController,
            CollectionService collectionService,
            NoteService noteService
    ) {
        this.contentArea = contentArea;
        this.actionBarController = actionBarController;
        this.collectionService = collectionService;
        this.noteService = noteService;
    }

    // ------------------- Public navigation API ------------------- //

    public void showHome() {
        loadContent(Routes.HOME_VIEW);
        showControls(Routes.HOME_CONTROLS);
    }

    public void showCollections() {
        loadContent(Routes.COLLECTION_BOARD_VIEW);
        showControls(Routes.COLLECTION_CONTROLS);
    }

    public void showNotesForCollection(UUID collectionId) {
        Object controller = loadContent(Routes.NOTE_BOARD_VIEW);
        showControls(Routes.NOTE_CONTROLS);

        // If NoteBoard controller supports receiving the collectionId, pass it
        if (controller instanceof SupportsActiveCollection c) {
            c.setActiveCollectionId(collectionId);
        }
    }

    // ------------------- FXML loading helpers ------------------- //

    private Object loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            injectCommon(controller);

            contentArea.getChildren().setAll(root);
            return controller;

        } catch (IOException e) {
            LOGGER.severe("Failed to load content: " + fxmlPath + " - " + e.getMessage());
            return null;
        }
    }

    private void showControls(String fxmlPath) {
        if (actionBarController == null) {
            LOGGER.severe("ActionBarController is null. Check fx:include wiring.");
            return;
        }
        actionBarController.showControls(fxmlPath);
    }

    private void injectCommon(Object controller) {
        if (controller == null) return;

        if (controller instanceof SupportsNavigator c) {
            c.setNavigator(this);
        }
        if (controller instanceof SupportsServices c) {
            c.setServices(collectionService, noteService);
        }
    }

    // ------------------- Tiny “contracts” controllers can implement ------------------- //

    public interface SupportsNavigator {
        void setNavigator(Navigator navigator);
    }

    public interface SupportsServices {
        void setServices(CollectionService cs, NoteService ns);
    }

    public interface SupportsActiveCollection {
        void setActiveCollectionId(UUID id);
    }
}
