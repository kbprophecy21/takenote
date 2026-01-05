package com.kyle.takenote.domain.service;


import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.domain.model.Note;

/**
 * Notes: Buisness logic (create, search, save).
 * 
 * Perform CRUD operations list of notes for a collection object.
 * 
 * NoteService should not "own" collections, but it can ask CollectionServices.java for them.
 * 
 */
public class NoteService {

    CollectionService collectservice;
    
    


    public NoteService(CollectionService collectionService) {
        this.collectservice = collectionService;
        
    };



    //-----------------------------Methods---------------------------//

    public Note createDraft(){
        return new Note("", "");
    }


    public void saveDraftNote(Note note){
       Collection collection = collectservice.getDefaultCollection();
       note.setTitle("untitled");
       collection.addNote(note);
    };


   




    
}
