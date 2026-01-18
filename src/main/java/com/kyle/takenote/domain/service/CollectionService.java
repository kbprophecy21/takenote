package com.kyle.takenote.domain.service;

//-------Java Imports-------//
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.infrastructure.persistence.json.JsonCollectionRepository;



/**
 * Notes: this class owns collections and their notes.
 * It will use CRUD operations on the Collection.java data.
 * hold list of Collection objects.
 */
public class CollectionService {

    //--------Fields----------//
    private ArrayList<Collection> listOfCollections;
    private Collection defaultCollection;
    private JsonCollectionRepository repo;
    private UUID defaultPageId;

    
    private static final UUID DEFAULT_COLLECTION_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");



    //----------Constuctor-------------//
    public CollectionService(JsonCollectionRepository repository) {

        this.repo = repository;
        this.listOfCollections = new ArrayList<>();
        
    }



    //-----------Getters & Setters---------//

    public UUID getDefaultCollectionId(){return DEFAULT_COLLECTION_ID;}
    
    public void setDefaultPageId(UUID id) {
        this.defaultPageId = id;
    }
    
    public UUID getDefaultPageId() {
        return defaultPageId;
    }


    //---------------Methods--------------//

    public ArrayList<Collection> getAllCollections() {
        return new ArrayList<>(listOfCollections);
    }


    public Collection createCollection(String name) {
        Collection newCollection = new Collection(name);
        listOfCollections.add(newCollection);
        return newCollection;
    }

    //Overload Method
    public Collection createCollection(UUID pageId, String name) {
        Collection newCollection = new Collection(name);
        newCollection.setPageId(resolvePageId(pageId));
        listOfCollections.add(newCollection);
        return newCollection;
    }

    public Collection getDefaultCollection() {
        return getDefaultCollectionIfExists(); 
    }


    public Collection getCollectionById(UUID id){
        for (Collection c: listOfCollections) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public boolean renameCollection(UUID id, String newName){
        for (Collection c: listOfCollections){
            if (c.getId().equals(id)){
                c.rename(newName);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCollection(UUID id){

        for (int i = 0; i < listOfCollections.size(); i++) {
            Collection current = listOfCollections.get(i);
            
            if (!id.equals(defaultCollection.getId())) {
                if (current.getId().equals(id)){
                    listOfCollections.remove(i);
                    return true;
                }
            } 
        }
        return false;
    }

    public void loadFromDisk(){

        List<Collection> loaded = repo.loadAll();

        this.listOfCollections = new ArrayList<>();
        if (loaded != null) this.listOfCollections.addAll(loaded);

        
        this.listOfCollections.removeIf(c -> c.getId() == null);

        
        LinkedHashMap<UUID, Collection> unique = new LinkedHashMap<>();
        for (Collection c : this.listOfCollections) unique.putIfAbsent(c.getId(), c);
        this.listOfCollections = new ArrayList<>(unique.values());

        
        this.defaultCollection = getDefaultCollectionIfExists();


        double startX = 40;
        double startY = 40;
        double stepX = 260; // card width + padding
        double stepY = 180;

        int i = 0;
        for (Collection c : this.listOfCollections) {
            // If old JSON had no positions, they'll be 0.0
            if (c.getPosX() == 0.0 && c.getPosY() == 0.0) {
                int col = i % 4;
                int row = i / 4;
                c.setPosX(startX + col * stepX);
                c.setPosY(startY + row * stepY);
                i++;
            }
        }
        if (defaultPageId != null) {
            for (Collection c : listOfCollections) {
                if (c.getPageId() == null) {
                    c.setPageId(defaultPageId);
                }
            }
    }


    }


    public void saveToDisk(){
        repo.saveAll(listOfCollections);
    }

    public Collection getDefaultCollectionIfExists() {
        for (Collection c : listOfCollections) {
            if (DEFAULT_COLLECTION_ID.equals(c.getId())) return c;
        }
        return null;
    }

   public Collection getOrCreateDefaultCollection() {
        Collection existing = getDefaultCollectionIfExists();
        if (existing != null) {
            // patch pageId if missing
            if (existing.getPageId() == null) {
                existing.setPageId(resolvePageId(null));
            }
            this.defaultCollection = existing;
            return existing;
        }

        Collection created = new Collection(DEFAULT_COLLECTION_ID, "default");
        created.setPageId(resolvePageId(null));

        this.defaultCollection = created;
        listOfCollections.add(0, created);
        return created;
    }



    public void updateCollectionPosition(UUID id, double x, double y) {
        Collection c = getCollectionById(id);
        if (c == null) return;

        c.setPosX(x);
        c.setPosY(y);
        c.setUpdatedAt(LocalDateTime.now());
    }

    public List<Collection> getCollectionsForPage(UUID pageId) {
        List<Collection> out = new ArrayList<>();
        if (pageId == null) return out;

        for (Collection c : listOfCollections) {
            if (pageId.equals(c.getPageId())) out.add(c);
        }
        return out;
    }





    //-----------Helper Methods------------//
    
    private void ensureDefaultCollectionExists(){
        for (Collection c : this.listOfCollections) {
            if (getDefaultCollectionId().equals(c.getId())){
                defaultCollection = c;
                return;
            }
        }
        this.defaultCollection = new Collection(getDefaultCollectionId(), "default");
        listOfCollections.add(0, this.defaultCollection);
    }


    private UUID resolvePageId(UUID pageId) {
        if (pageId != null) return pageId;
        return defaultPageId; // we'll set this from App.java
    }


}
