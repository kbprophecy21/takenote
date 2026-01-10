package com.kyle.takenote.infrastructure.persistence.json;


//-----------jackson json imports--------//
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//---------Project imports--------//
import com.kyle.takenote.domain.model.Collection;
import com.kyle.takenote.infrastructure.storage.FileStorage;

//--------Java imports-----------//
import java.util.ArrayList;
import java.util.List;



/**
 * Note: This class is responsible for handling the Json logic for collection.json file. 
 */
public class JsonCollectionRepository {

    //-------Fields---------//
    private static final String FILE = "collections.json";

    private final FileStorage storage;
    private final ObjectMapper mapper;

    //---------Constructor---------//
    public JsonCollectionRepository(FileStorage storage) {
        this.storage = storage;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    //----------------Methods---------------//
    public List<Collection> loadAll() {
        String json = storage.readFile(FILE);
        if (json == null || json.isBlank()) return new ArrayList<>();

        try {
            return mapper.readValue(json, new TypeReference<List<Collection>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse " + FILE, e);
        }
    }

    public void saveAll(List<Collection> collections) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collections);
            storage.writeFile(FILE, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write " + FILE, e);
        }
    }
}

