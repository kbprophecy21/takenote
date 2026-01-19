package com.kyle.takenote.domain.model;

//------Java Imports-----------//
import java.time.LocalDateTime;
import java.util.UUID;



/**
 * Notes: Page model class.
 */
public class Page {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Page() {}

    public Page(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void rename(String newName) {
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }
}
