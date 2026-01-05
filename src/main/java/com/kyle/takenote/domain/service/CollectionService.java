package com.kyle.takenote.domain.service;

import java.util.ArrayList;
import java.util.UUID;

import com.kyle.takenote.domain.model.Collection;


/**
 * Notes: this class owns collections and their notes.
 * It will use CRUD operations on the Collection.java data.
 * hold list of Collection objects.
 */
public class CollectionService {

    ArrayList<Collection> listOfCollections;
    Collection defaultCollection;



    public CollectionService() {

        this.listOfCollections = new ArrayList<>();
        this.defaultCollection = new Collection("default");
        listOfCollections.add(this.defaultCollection);
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


}
