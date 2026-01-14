package com.kyle.takenote.domain.service;

  
//--------Java Imports-------//
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.infrastructure.persistence.json.JsonNoteRepository;


/** 
 * Notes: Business logic (create, search, save).
 * 
 * Perform CRUD operations list of notes.
 */
public class NoteService {

    CollectionService collectService;
    ArrayList<Note> listOfNotes;
    JsonNoteRepository repo;

    //private final JsonNoteRepository repo;
    
    


    public NoteService(CollectionService collectionService, JsonNoteRepository repository) {
        this.collectService = collectionService;
        this.repo = repository;
        this.listOfNotes = new ArrayList<>();
        
    };



    //-----------------------------Methods---------------------------//


    
    public Note createNote(UUID collectionId, String title, String body){
        UUID finalCollectionId = resolveCollectionId(collectionId);

        String safeTitle = (title == null) ? "" : title; // new way for me to write if statements
        String safeBody = (body == null) ? "" : body;

        Note note = new Note(finalCollectionId, safeTitle, safeBody);
        addNote(note);
        return note;
    }

    /**
     * Overload Method
     * This one is for the default collection.
     */
    public Note createNote(String title, String body){
        return createNote(null, title, body);
    }
    
    private void addNote(Note note){ 
        // Notes: Kyle remember:
        // when add null checks, make sure to use IllegalArgumentException()
        // or Logger helps for debugging.
        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null");
        }
        listOfNotes.add(note);
    }


    public ArrayList<Note> getNotesForCollection(UUID collectionId){
        ArrayList<Note> collectionNotes = new ArrayList<>();
        for (Note note : listOfNotes) {
            if (note.getCollectionId() != null){

                if (note.getCollectionId().equals(collectionId)){
                    collectionNotes.add(note);
                }
            }
          
        }
        return collectionNotes;
    }

    public List<Note> getAllNotes() {
        return new ArrayList<>(listOfNotes);
    }


    public boolean deleteNote(UUID id){
        
        for (int i = 0; i < listOfNotes.size(); i++) {
            Note note = listOfNotes.get(i);
            if (note.getId().equals(id)) {
                listOfNotes.remove(i);
                return true;
            }
        }
        return false;
    }
   


    public void saveDraftNote(Note note){
       addNote(note);
    };


    private UUID resolveCollectionId(UUID collectionId) {
        return collectionId;
    }


    public void ensureNoteHasCollection(Note note) {
        if (note == null) return;

        if (note.getCollectionId() == null) {
            UUID defId = collectService.getOrCreateDefaultCollection().getId();
            note.setCollectionId(defId);
        }
    }



    public void loadFromDisk() {
        List<Note> loaded = repo.loadAll();

        this.listOfNotes = new ArrayList<>();
        if (loaded != null) this.listOfNotes.addAll(loaded);

        // Leave null collectionId as null.
        // (Optional migration: if default exists already, then attach)
        UUID defId = null;
        var def = collectService.getDefaultCollectionIfExists();
        if (def != null) defId = def.getId();

        if (defId != null) {
            for (Note n : listOfNotes) {
                if (n.getCollectionId() == null) n.setCollectionId(defId);
            }
        }
    }



    public void saveToDisk(){
        repo.saveAll(listOfNotes);
    }

    


   




    
}
