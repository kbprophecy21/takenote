package com.kyle.takenote.infrastructure.persistence.json;

/**
 * Notes: local JSON implementation for NoteRepository.
 */

//--------jackson json imports-------------//
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//-------Project imports----------//
import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.infrastructure.storage.FileStorage;

//------java imports--------//
import java.util.ArrayList;
import java.util.List;


/**
 * Note: This class is responsible for handling the Json logic for notes.json file. 
 */
public class JsonNoteRepository {


    //-------------Fields--------------//
    private static final String FILE = "notes.json";

    private final FileStorage storage;
    private final ObjectMapper mapper;


    //--------------Constructor---------//
    public JsonNoteRepository(FileStorage storage) {
        this.storage = storage;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule()); // safe even if you don't use time yet
    }


    //---------------------Methods---------------------//
    public List<Note> loadAll() {
        String json = storage.readFile(FILE);
        
        if (json == null || json.isBlank()) return new ArrayList<>();

        try {
            return mapper.readValue(json, new TypeReference<List<Note>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse " + FILE, e);
        }
    }

    public void saveAll(List<Note> notes) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(notes);
            storage.writeFile(FILE, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write " + FILE, e);
        }
    }
}

