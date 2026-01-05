package com.kyle.takenote.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;



/**
 * Notes: This class will hold a group of notes( it will act like a folder/notebook).
 */
public class Collection {

    final UUID id;
    String name;
    ArrayList<Note> noteList;
    LocalDateTime updatedAt;
    LocalDateTime createdAt;


    public Collection(String name ){

        this.id = (UUID.randomUUID());
        this.name = name;
        this.noteList = new ArrayList<>();
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        
    }


    //-----------------Getters & Setters ---------------//

    public UUID getId(){return this.id;};

    public LocalDateTime getUpDatedAt(){return this.updatedAt;};
    public LocalDateTime getCreatedAt(){return this.createdAt;};

    public String getName(){return this.name;};
    public void setName(String name){
        this.name = name;
        touch();
    };

    public ArrayList<Note> getNotes(){
        return new ArrayList<>(this.noteList);
    };


    public void addNote(Note note){
        this.noteList.add(note);
        touch();
    }




    //---------------Helper Methods----------------//
    private void touch(){

        this.updatedAt = LocalDateTime.now();
    }
}
