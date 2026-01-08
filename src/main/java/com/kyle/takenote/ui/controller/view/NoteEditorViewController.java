package com.kyle.takenote.ui.controller.view;


import java.awt.Button;
import java.awt.TextArea;
import java.awt.TextField;
import java.util.UUID;

import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;

public class NoteEditorViewController 
    implements Navigator.SupportsNavigator, Navigator.SupportsServices, Navigator.SupportsActiveCollection, Navigator.SupportsActiveNote {
    


    Navigator navigator;

    CollectionService collectionService;
    NoteService noteService;

    UUID activeCollectionId;
    UUID activeNoteId;

    private Note activeNote; // keep reference once loaded in. 


    //----------FXML Fields-------------//
    @FXML TextField titleField;
    @FXML TextArea bodyArea;
    @FXML Button saveBtn;
    @FXML Button backBtn;
    

    //----------FXML Methods--------------//

    @FXML
    private void initialize(){
        //saveBtn.setOnAction(e -> onSave());
        //backBtn.setOnAction(e -> onBack());
    }

    //------------Methods----------------//

    @Override
    pubilc void setServices(CollectionService cs, NoteService ns) {
        this.collectionService = cs;
        this.noteService = ns;
        tryLoad();
    }

    @Override
    public void setActiveCollectionId(UUID id) {
        this.activeCollectionId = id;
        tryLoad();
    }

    @Override
    public void setActiveNote(UUID noteId) {
        this.activeNoteId = noteId;
        tryLoad();
    }

    //--------------Helper Methods-----------------//
    private void tryLoad() {
        if (collectionService == null || noteService == null) throw new NullPointerException("\"collectionService\" or \"noteService\" cannot be null.");
        if (activeCollectionId == null || activeNoteId == null) throw new NullPointerException("\"activeCollectionId\" or \"activeNoteId\" cannot be null.");
        Collection c = collectionService.getCollectionById(activeCollectionId);
        if (c == null) throw new NullPointerException("\" Collection c = collectionService.getCollectionById(activeCollectionId);\" returned null. This cannot be null");
        
        activeNote = c.getNotes().stream().filter(n -> n.getId().equals(activeNoteId)).findFirst().orElse(null);

        if (activeNote == null) throw new NullPointerException("\"activeNote\" cannot be null.");

        // Fill UI
        titleField.setText(activeNote.getTitle());
        bodyArea.setText(activeNote.getBody());

    }


    /**
     * TODO: Need to finish these
     */
     private void onSave() {
        if (activeNote == null) return;

        activeNote.setTitle(titleField.getText());
        activeNote.setBody(bodyArea.getText());

        // If your NoteService has an update method, call it.
        // If not, Option A simplest is: you're editing the same Note object in the collection list,
        // so it's already "saved" in memory.
        // Later you'll persist to JSON/file/db.

        // Example if you add it:
        // noteService.updateNote(activeCollectionId, activeNote);

        // Optional: navigate back after save
        onBack();
    }

    private void onBack() {
        if (navigator == null) return;

        // Go back to the note board for the active collection
        // (use whatever method you already have)
        navigator.showNotesForCollection(activeCollectionId);
    }


}
