package com.kyle.takenote.ui.controller.component;

import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.navigation.Navigator;

public class SideMenuController implements Navigator.SupportsNavigator, Navigator.SupportsServices {

    private Navigator navigator;
    private CollectionService collectionService;
    private NoteService noteService;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void setServices(CollectionService cs, NoteService ns) {
        this.collectionService = cs;
        this.noteService = ns;
    }

    // --------- Nav buttons --------- //

    public void handleShowHome() {
        navigator.showHome();
    }

    public void handleShowCollection() {
        navigator.showCollections();
    }

    // --------- Actions (stub for now) --------- //

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
}
