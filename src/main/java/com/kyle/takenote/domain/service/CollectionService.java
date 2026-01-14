package com.kyle.takenote.domain.service;

//-------Java Imports-------//
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

    
    private static final UUID DEFAULT_COLLECTION_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");



    //----------Constuctor-------------//
    public CollectionService(JsonCollectionRepository repository) {

        this.repo = repository;
        this.listOfCollections = new ArrayList<>();
        
    }



    //-----------Getters & Setters---------//

    public UUID getDefaultCollectionId(){return DEFAULT_COLLECTION_ID;}



    //---------------Methods--------------//

    public ArrayList<Collection> getAllCollections() {
        return new ArrayList<>(listOfCollections);
    }


    public Collection createCollection(String name) {
        Collection newCollection = new Collection(name);
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
            this.defaultCollection = existing;
            return existing;
        }

        Collection created = new Collection(DEFAULT_COLLECTION_ID, "default");
        this.defaultCollection = created;
        listOfCollections.add(0, created);
        return created;
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

}
