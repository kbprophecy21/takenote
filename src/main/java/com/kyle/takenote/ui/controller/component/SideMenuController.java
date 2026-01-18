package com.kyle.takenote.ui.controller.component;

import com.kyle.takenote.domain.model.Page;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.domain.service.PageService;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class SideMenuController 
    implements Navigator.SupportsNavigator, Navigator.SupportsServices, Navigator.SupportsPageService {

    private Navigator navigator;
    private CollectionService collectionService;
    private NoteService noteService;
    private PageService pageService;


    @FXML private ListView<Page> pagesListView;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void setServices(CollectionService cs, NoteService ns) {
        this.collectionService = cs;
        this.noteService = ns;
    }

    @Override
    public void setPageService(PageService ps) {
        this.pageService = ps;
        refreshPages();
    }

    // --------- Nav buttons --------- //

    public void handleShowHome() {
        navigator.showHome();
    }

    public void handleShowCollection() {
        navigator.showCollections();
    }

    // --------- Actions (stub for now) --------- //


    @FXML
    private void initialize() {

        pagesListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Page item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        pagesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV == null || navigator == null) return;

            // Phase 1: keep this
            navigator.setActivePageId(newV.getId());
            navigator.showCollections();

            // Phase 2 will become:
            // navigator.showPage(newV.getId());
        });
    }



    @FXML
    private void handleCreatePage() {
        if (pageService == null) return;

        Page created = pageService.createPage("New Page " + (pageService.getAllPages().size() + 1));
        pageService.saveToDisk();

        refreshPages();
        pagesListView.getSelectionModel().select(created);
    }


    public void handleNewNote() {
        // TODO: later we can create note in currently selected collection
        // For now just go to collections or home, your choice:
        navigator.showCollections();
    }

    public void handleNewCollection() {
        // TODO: open create collection flow later
        navigator.showCollections();
    }

    public void handleShowAllNotes() {
        // TODO: add route/view later
        navigator.showCollections();
    }

    public void handleShowTrash() {
        // TODO: add route/view later
        navigator.showHome();
    }

    public void handleShowSettings() {
        // TODO later
        navigator.showHome();
    }

    public void handleShowHelp() {
        // TODO later
        navigator.showHome();
    }




    //----------Helper Methods--------------//

    private void refreshPages() {
        if (pagesListView == null || pageService == null) return;

        pagesListView.getItems().setAll(pageService.getAllPages());

        pagesListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Page item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

}
