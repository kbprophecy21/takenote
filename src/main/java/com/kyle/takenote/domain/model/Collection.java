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
    private UUID pageId;

    private double posX;
    private double posY;


    //-----------Constructors------------------//
    public Collection(String name ){ // for creating all over collections that is not default.

        this.id = (UUID.randomUUID());
        this.name = name;
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.posX = 40;
        this.posY = 40;
        
    }

    public Collection(UUID id, String name) { // For fixing the default collection
        this.id = id;
        this.name = name;
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.posX = 40;
        this.posY = 40;
    }

    public Collection() {
        // Jackson using this one. Keep it empty or set safe default for the Json API.
    }


    //-----------------Getters & Setters ---------------//

    public UUID getId(){return this.id;};
    public void setId(UUID id){this.id = id;};

    public UUID getPageId(){return this.pageId;}
    public void setPageId(UUID id) {this.pageId = id;}

    public LocalDateTime getUpdatedAt(){return this.updatedAt;};
    public void setUpdatedAt(LocalDateTime upDateTime){this.updatedAt = upDateTime;};

    public LocalDateTime getCreatedAt(){return this.createdAt;};
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;};

    public String getName(){return this.name;};
    public void setName(String name){this.name = name;};

    public double getPosX(){return this.posX;};
    public void setPosX(double posX) {this.posX = posX;}

    public double getPosY(){return this.posY;};
    public void setPosY(double posY){this.posY = posY;}

    public void rename(String name){
        this.name = name;
        touch();
    };



    //---------------Helper Methods----------------//
    private void touch(){

        this.updatedAt = LocalDateTime.now();
    }
}
