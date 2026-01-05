package com.kyle.takenote.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;



public class Note {

    final UUID id; 
    String title = ""; // optional to the user but can add at creation or later. Edit at anytime.
    String body; // Contains text data, (note text, and other forms of information for the user)
    LocalDateTime updatedAt;
    LocalDateTime createdAt;


    public Note (String title, String body) {

        this.id = (UUID.randomUUID());
        this.title = title;
        this.body = body;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    };


    //------------------getter & setter--------------//

    public UUID getId(){return this.id;}

    public void setTitle(String title){
        this.title = title;
        touch();
    };
    public String getTitle(){return this.title;};

    public String getBody(){return this.body;}
    public void setBody(String body){
        this.body = body;
        touch();
    };

    public LocalDateTime getUpdatedAt(){return this.updatedAt;};
    
    public LocalDateTime getCreatedAt(){return this.createdAt;};

    //---------------------helper methods------------------------------//

    public String getPreview(){
        int len = this.body.length();
        if (this.body == null){
            return "";
        }
        else if (len > 100) {
            return this.body.substring(0, 100) + "...";
        }
        else {
            return "";
        }
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

}
