package com.kyle.takenote.ui.navigation;

//-----------Java Imports-----------//
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;


/**
 * Notes: Swaps screen in the shell, fxml files, handles all navigation (My "router").
 */
public class Navigator {

    //----------Fields------------//
    private static final Logger LOGGER = Logger.getLogger(Navigator.class.getName());

    private final StackPane contentArea;
    private final StackPane controlsHost;

    private Object controlsController; // current active controls controller.
    private UUID selectedNoteId;

    private final CollectionService collectionService;
    private final NoteService noteService;

    //--------Contructor----------//
    public Navigator(
            StackPane contentArea,
            StackPane controlsHost,
            CollectionService collectionService,
            NoteService noteService
    ) {
        this.contentArea = contentArea;
        this.controlsHost = controlsHost;
        this.collectionService = collectionService;
        this.noteService = noteService;
    }

    //-----------------Methods----------------//

    public UUID getSelectedNoteId() {return selectedNoteId;}

   public void setSelectedNoteId(UUID id) {
    System.out.println("Navigator.setSelectedNoteId(" + id + ")"); // TEST
    this.selectedNoteId = id;

    if (controlsController instanceof SupportsSelectedNote s) {
        System.out.println("Forwarding selectedNoteId to current controls controller"); // TEST
        s.setSelectedNoteId(id);
    } else {
        System.out.println("Current controls controller does NOT support selected note"); // TEST
    }
}


    // ------------------- Public navigation API Methods ------------------- //

    public void showHome() {
        loadContent(Routes.HOME_VIEW);
        showControls(Routes.HOME_CONTROLS);
    }

    public void showCollections() {
        loadContent(Routes.COLLECTION_BOARD_VIEW);
        showControls(Routes.COLLECTION_CONTROLS);
    }

    public void showNoteEditor(UUID collectionId, UUID noteId) {
        Object controller = loadContent(Routes.NOTE_EDITOR_VIEW);
        showControls(Routes.NOTE_EDITOR_CONTROLS);

        if (controller instanceof  SupportsActiveNote n) {
            n.setActiveNote(noteId);
        }
        if (controller instanceof SupportsActiveCollection c) {
            c.setActiveCollectionId(collectionId);
        }
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
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Object controller = loader.getController();
        injectCommon(controller);

        // push current selection into the newly loaded controls controller
        if (controller instanceof SupportsSelectedNote s) {
            s.setSelectedNoteId(selectedNoteId);
        }

        controlsController = controller;
        controlsHost.getChildren().setAll(root);

    } catch (IOException e) {
        LOGGER.severe("Failed to load controls: " + fxmlPath + " - " + e.getMessage());
    }
}


    private void injectCommon(Object controller) {
        if (controller == null) return;

        if (controller instanceof SupportsServices c) {
            c.setServices(collectionService, noteService);
        }
        if (controller instanceof SupportsNavigator c) {
            c.setNavigator(this);
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

    public interface SupportsActiveNote {
        void setActiveNote(UUID noteId);
    }

    public interface SupportsSelectedNote {
        void setSelectedNoteId(UUID id);
    }
}
