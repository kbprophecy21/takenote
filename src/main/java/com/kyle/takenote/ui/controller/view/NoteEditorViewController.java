package com.kyle.takenote.ui.controller.view;

//-----Java Imports----//
import java.util.UUID;

import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.navigation.Navigator;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;


/**
 * Note: This class is my Editor view controller. 
 */
public class NoteEditorViewController 
    implements Navigator.SupportsNavigator, Navigator.SupportsServices, Navigator.SupportsActiveCollection, Navigator.SupportsActiveNote {
    


    //-----------Fields--------------//
    Navigator navigator;

    CollectionService collectionService;
    NoteService noteService;

    UUID activeCollectionId;
    UUID activeNoteId;

    private Note activeNote; // keep reference once loaded in. 


    //----------FXML Fields-------------//
    @FXML private TextField titleField;
    @FXML private TextArea noteTextArea;
    @FXML private Canvas inkCanvas;
    @FXML private StackPane editorStack;



    @FXML private Button saveBtn;
    @FXML private Button backBtn;
    
    

    //----------FXML Methods--------------//

    @FXML
    private void initialize(){
        saveBtn.setOnAction(e -> onSave());
        backBtn.setOnAction(e -> onBack());
        inkCanvas.widthProperty().bind(editorStack.widthProperty());
        inkCanvas.heightProperty().bind(editorStack.heightProperty());
    }

    //--------------Methods----------------//

    @Override
    public void setServices(CollectionService cs, NoteService ns) {
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
    
    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
        tryLoad();
    }

    private void requireInjected() {
        if (collectionService == null || noteService == null) {
            throw new IllegalStateException("NoteEditorViewController not injected (services/navigator).");
        }
    }

    //--------------Helper Methods-----------------//
    private void tryLoad() {
        requireInjected();

        if (activeNoteId == null) return;

        // If we have a collection, load from that collection list
        if (activeCollectionId != null) {
            activeNote = noteService.getNotesForCollection(activeCollectionId)
                .stream().filter(n -> n.getId().equals(activeNoteId)).findFirst().orElse(null);
        } else {
            // No collection yet: search all notes for this noteId
            activeNote = noteService.getAllNotes()
                .stream().filter(n -> n.getId().equals(activeNoteId)).findFirst().orElse(null);
        }

        if (activeNote == null) return;

        titleField.setText(activeNote.getTitle());
        noteTextArea.setText(activeNote.getBody());


        if (activeNote == null) return;

            // Fill UI
            titleField.setText(activeNote.getTitle());
            noteTextArea.setText(activeNote.getBody());

        }


    /**
     * TODO: Need to finish these
     */
    @FXML
    private void onSave() {
        
        if (activeNote == null) return;

        String uiTitle = titleField.getText();
        String uiBody = noteTextArea.getText();

        if (uiTitle == null || uiTitle.isBlank()) {
            activeNote.rename("Untitled");
        } else {
            activeNote.rename(uiTitle);
        }

        activeNote.updateBody(uiBody == null ? "" : uiBody);

        
        noteService.ensureNoteHasCollection(activeNote);

        
        collectionService.saveToDisk();
        noteService.saveToDisk();

        
        this.activeCollectionId = activeNote.getCollectionId();

        onBack();
    }


    @FXML
    private void onBack() {
        if (navigator == null) return;

        // Go back to the note board for the active collection
        // (use whatever method you already have)
        navigator.showNotesForCollection(activeCollectionId);
    }


}
