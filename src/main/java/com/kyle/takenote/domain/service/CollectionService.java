package com.kyle.takenote.domain.service;

//-------Java Imports-------//
import java.util.ArrayList;
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
    public CollectionService() {

        this.listOfCollections = new ArrayList<>();
        ensureDefaultCollectionExists();

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
        return this.defaultCollection;
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
                c.setName(newName);
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

        this.listOfCollections = new ArrayList<>(loaded);
        if (loaded != null) {
            this.listOfCollections.addAll(loaded);
        }
        ensureDefaultCollectionExists();
    }

    public void saveToDisk(){
        repo.saveAll(listOfCollections);
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
