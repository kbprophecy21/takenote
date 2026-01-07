package com.kyle.takenote.domain.service;


import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.domain.model.Note;

/**
 * Notes: Business logic (create, search, save).
 * 
 * Perform CRUD operations list of notes for a collection object.
 * 
 * NoteService should not "own" collections, but it can ask CollectionServices.java for them.
 * 
 * This class should never hold data only manage them.
 * 
 */
public class NoteService {

    CollectionService collectservice;
    
    


    public NoteService(CollectionService collectionService) {
        this.collectservice = collectionService;
        
    };



    //-----------------------------Methods---------------------------//


    public void addNote(Collection collection, Note note){ 
        // Notes: Kyle remember:
        // when add null checks, make sure to use IllegalArgumentException()
        // or Logger helps for debugging.
        if (collection == null){
            throw new IllegalArgumentException("Collection cannot be null");
        }
        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null");
        }
        if (note.getTitle() == null || note.getTitle().isEmpty()){
            note.setTitle("untitled");
        }
        collection.addNote(note);
    }


    public Note createDraft(){
        return new Note("", "");
    }


    public void saveDraftNote(Collection collection, Note note){
       addNote(collection, note);
    };


   




    
}
