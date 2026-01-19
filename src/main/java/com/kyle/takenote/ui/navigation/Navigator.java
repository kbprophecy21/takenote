package com.kyle.takenote.ui.navigation;

//-----------Java Imports-----------//
import java.util.UUID;
import java.util.logging.Logger;

import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.domain.service.PageService;

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
    private final StackPane sideMenuArea;

    private Object controlsController; // current active controls controller.
    private Object contentController;
    private UUID selectedNoteId;
    private UUID selectedCollectionId;
    private UUID activePageId;

    private final CollectionService collectionService;
    private final NoteService noteService;
    private final PageService pageService;

    //--------Contructor----------//
    public Navigator(
            StackPane contentArea,
            StackPane controlsHost,
            StackPane sideMenuArea,
            CollectionService collectionService,
            NoteService noteService,
            PageService pageService
    ) {
        this.contentArea = contentArea;
        this.controlsHost = controlsHost;
        this.sideMenuArea = sideMenuArea;
        this.collectionService = collectionService;
        this.noteService = noteService;
        this.pageService = pageService;
    }

    //-----------------Methods----------------//

    public UUID getSelectedNoteId() {return selectedNoteId;}

    public UUID getSelectedCollectionId() {return selectedCollectionId;}

    public UUID getActivePageId(){return this.activePageId;}

    public void setActivePageId(UUID id) {
        this.activePageId = id;

        if (contentController instanceof SupportsActivePage p) {
            p.setActivePageId(id);
        }
    }


    public void setSelectedNoteId(UUID id) {
        this.selectedNoteId = id;

        if (controlsController instanceof SupportsSelectedNote s) {
            
            s.setSelectedNoteId(id);
        } 
    }

    public void setSelectedCollectionId(UUID id) {
        this.selectedCollectionId = id;

        if (controlsController instanceof  SupportsSelectedCollection s) {
            s.setSelectedCollectionId(id);
        }
    }

    // ------------------- Public navigation API Methods ------------------- //

    public void showHome() {
        loadContent(Routes.HOME_VIEW);
        showControls(Routes.HOME_CONTROLS);
    }

    public void showCollections() {
    if (activePageId == null) {
        activePageId = pageService.getDefaultPageId(); // or however you do it
    }

    Object controller = loadContent(Routes.COLLECTION_BOARD_VIEW);
    showControls(Routes.COLLECTION_CONTROLS);

    if (controller instanceof SupportsActivePage p) {
        p.setActivePageId(activePageId);
    }
}



    public void showSideMenu() {
        loadSideMenu(Routes.SIDE_MENU_VIEW);
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

        if (controlsController instanceof  SupportsActiveCollection c) {
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

            contentController = controller;
            contentArea.getChildren().setAll(root);
            return controller;

        } catch (Exception e) {
            LOGGER.severe("Failed to load content: " + fxmlPath);
            e.printStackTrace(); // <-- THIS is what you need right now
            return null;
        }
    }


    private Object loadSideMenu(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            injectCommon(controller);

            sideMenuArea.getChildren().setAll(root);
            return controller;

        } catch (Exception e) {
            LOGGER.severe("Failed to load side menu: " + fxmlPath);
            e.printStackTrace();
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

        } catch (Exception e) {
            LOGGER.severe("Failed to load controls: " + fxmlPath);
            e.printStackTrace();
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
        if (controller instanceof SupportsPageService p) {
            p.setPageService(pageService);
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

    public interface SupportsSelectedCollection {
        void setSelectedCollectionId(UUID id);
    }

    public interface SupportsPageService {
        void setPageService(com.kyle.takenote.domain.service.PageService ps);
    }

    public interface SupportsActivePage {
        void setActivePageId(UUID id);
    }

}