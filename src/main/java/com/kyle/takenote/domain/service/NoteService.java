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

    private CollectionService collectService;
    private ArrayList<Note> listOfNotes;
    private JsonNoteRepository repo;

    private UUID defaultPageId;

    //private final JsonNoteRepository repo;
    
    


    public NoteService(CollectionService collectionService, JsonNoteRepository repository) {
        this.collectService = collectionService;
        this.repo = repository;
        this.listOfNotes = new ArrayList<>();
        
    };



    //-----------------------------Methods---------------------------//

    public void setDefaultPageId(UUID id) {
        this.defaultPageId = id;
    }
    
    public Note createNote(UUID pageId, UUID collectionId, String title, String body){
        UUID finalCollectionId = resolveCollectionId(collectionId);

        String safeTitle = (title == null) ? "" : title; // new way for me to write if statements
        String safeBody = (body == null) ? "" : body;

        Note note = new Note( null, finalCollectionId, safeTitle, safeBody);
        note.setPageId(resolvePageId(pageId));
        addNote(note);
        return note;
    }

    /**
     * Overload Method
     * This one is for the default collection.
     */
    public Note createNote(String title, String body){
        return createNote( null, null, title, body);
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

    public ArrayList<Note> getNotesForPage(UUID pageId){
        ArrayList<Note> pageNotes = new ArrayList<>();
        for (Note note: listOfNotes){
            if (note.getPageId() != null) {
                if (note.getPageId().equals(pageId)){
                    pageNotes.add(note);
                }
            }
        }
        return pageNotes;
    }

    public List<Note> getAllNotes() {
        return new ArrayList<>(listOfNotes);
    }

    public Note getNoteById(UUID id) {
        for (Note n : listOfNotes) {
            if (n.getId().equals(id)){
                return n;
            }
        }
        return null;
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



    public void loadFromDisk() {
        List<Note> loaded = repo.loadAll();

        this.listOfNotes = new ArrayList<>();
        if (loaded != null) this.listOfNotes.addAll(loaded);

        // Leave null collectionId as null.
        // (Optional migration: if default exists already, then attach)
        

        double startX = 40;
        double startY = 40;
        double stepX = 260;
        double stepY = 180;

        int i = 0;
        for (Note note: this.listOfNotes){
            if (note.getPosX() == 0.0 && note.getPosY() == 0.0){
                int col = i % 4;
                int row = i / 4;
                note.setPosX(startX + col * stepX);
                note.setPosY(startY + row * stepY);
                i++;
            }
        }
        
        if (defaultPageId != null) {
            for (Note n : listOfNotes) {
                if (n.getPageId() == null) {
                    n.setPageId(defaultPageId);
                }
            }
        }
    }



    public void saveToDisk(){
        repo.saveAll(listOfNotes);
    }


    public void updateNotePosition(UUID id, double x, double y){
        Note note = getNoteById(id);
        if (note == null) return;

        note.setPosX(x);
        note.setPosY(y);
        note.setUpdatedAt();

    }

    //-----------Helper Methods-------------//

    private UUID resolvePageId(UUID pageId) {
        if (pageId != null) return pageId;
        return defaultPageId; // can be null if I forgets to set it, but set it inside App.java for now 
    }


    


   




    
}
