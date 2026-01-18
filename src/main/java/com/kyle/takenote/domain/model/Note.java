package com.kyle.takenote.domain.model;

//------Java Imports---------//
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Notes: this class is the note model for define the data for the Note itself. 
 * Not Note logic or business logic goes in here.
 */
public class Note {

    //----------Fields------------//
    private UUID id; 
    private String title = ""; // optional to the user but can add at creation or later. Edit at anytime.
    private String body; // Contains text data, (note text, and other forms of information for the user)
    private UUID collectionId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    private UUID pageId;

    private double posX;
    private double posY;


    //-----------------Constuctors-------------//
    public Note (UUID collectId, String title, String body) {

        this.id = (UUID.randomUUID());
        this.collectionId = collectId;
        this.title = title;
        this.body = body;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.posX = 40; // TODO: play with these numbers later to see how they effect the notes.
        this.posY = 40; // TODO: same for this one too.
    };

    /**
     * Required for jackson
     */
    public Note() {

    }


    //------------------getter & setter--------------//

    public UUID getId(){return this.id;}
    public void setId(UUID id){this.id = id;}
    

    public UUID getCollectionId(){return this.collectionId;}
    public void setCollectionId(UUID id) { this.collectionId = id; }

    public UUID getPageId(){return this.pageId;}
    public void setPageId(UUID id) {this.pageId = id;}

    public double getPosX(){return this.posX;}
    public void setPosX(double xNumber) {this.posX = xNumber;}

    public double getPosY(){return this.posY;}
    public void setPosY(double yNumber) {this.posY = yNumber;}


    public void rename(String title){
        this.title = title;
        touch();
    };

    public String getTitle(){return this.title;};
    public void setTitle(String name){this.title = name;};

    public String getBody(){return this.body;}
    public void setBody(String body) {this.body = body;};
    
    public void updateBody(String body){
        this.body = body;
        touch();
    };

    public LocalDateTime getUpdatedAt(){return this.updatedAt;};
    public void setUpdatedAt(){this.updatedAt = LocalDateTime.now();}
    
    public LocalDateTime getCreatedAt(){return this.createdAt;};
    public void setCreatedAt(){this.createdAt = LocalDateTime.now();}

    //---------------------helper methods------------------------------//

    

    private void touch() {
        this.updatedAt = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = this.updatedAt;
    }

}
