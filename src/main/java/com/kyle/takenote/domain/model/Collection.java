package com.kyle.takenote.domain.model;


//------Java Imports-----------//
import java.time.LocalDateTime;
import java.util.UUID;



/**
 * Notes: This class will hold a group of notes( it will act like a folder/notebook).
 */
public class Collection {

    //------Fields-------//
    private UUID id;
    private String name;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;


    //-----------Constructors------------------//
    public Collection(String name ){ // for creating all over collections that is not default.

        this.id = (UUID.randomUUID());
        this.name = name;
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        
    }

    public Collection(UUID id, String name) { // For fixing the default collection
        this.id = id;
        this.name = name;
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public Collection() {
        // Jackson using this one. Keep it empty or set safe default for the Json API.
    }


    //-----------------Getters & Setters ---------------//

    public UUID getId(){return this.id;};
    public void setID(UUID id){this.id = id;}; 

    public LocalDateTime getUpdatedAt(){return this.updatedAt;};
    public void setUpdatedAt(LocalDateTime upDateTime){this.updatedAt = upDateTime;};

    public LocalDateTime getCreatedAt(){return this.createdAt;};
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;};

    public String getName(){return this.name;};
    public void setName(String name){this.name = name;};
    
    public void rename(String name){
        this.name = name;
        touch();
    };



    //---------------Helper Methods----------------//
    private void touch(){

        this.updatedAt = LocalDateTime.now();
    }
}
