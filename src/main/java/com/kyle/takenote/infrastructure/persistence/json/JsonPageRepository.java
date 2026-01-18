package com.kyle.takenote.infrastructure.persistence.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kyle.takenote.domain.model.Page;
import com.kyle.takenote.infrastructure.storage.FileStorage;

import java.util.ArrayList;
import java.util.List;

public class JsonPageRepository {

    private static final String FILE = "pages.json";

    private final FileStorage storage;
    private final ObjectMapper mapper;

    public JsonPageRepository(FileStorage storage) {
        this.storage = storage;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public List<Page> loadAll() {
        String json = storage.readFile(FILE);
        if (json == null || json.isBlank()) return new ArrayList<>();

        try {
            return mapper.readValue(json, new TypeReference<List<Page>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse " + FILE, e);
        }
    }

    public void saveAll(List<Page> pages) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pages);
            storage.writeFile(FILE, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write " + FILE, e);
        }
    }
}
