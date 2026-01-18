package com.kyle.takenote.domain.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.kyle.takenote.domain.model.Page;
import com.kyle.takenote.infrastructure.persistence.json.JsonPageRepository;

public class PageService {

    private final JsonPageRepository repo;
    private ArrayList<Page> pages = new ArrayList<>();

    // Stable default page id (like your default collection id pattern)
    private static final UUID DEFAULT_PAGE_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000010");

    public PageService(JsonPageRepository repo) {
        this.repo = repo;
    }

    public UUID getDefaultPageId() { return DEFAULT_PAGE_ID; }

    public List<Page> getAllPages() {
        return new ArrayList<>(pages);
    }

    public Page getPageById(UUID id) {
        for (Page p : pages) if (id.equals(p.getId())) return p;
        return null;
    }

    public Page createPage(String name) {
        Page p = new Page(name);
        pages.add(p);
        return p;
    }

    public void loadFromDisk() {
        List<Page> loaded = repo.loadAll();

        pages = new ArrayList<>();
        if (loaded != null) pages.addAll(loaded);

        pages.removeIf(p -> p.getId() == null);

        LinkedHashMap<UUID, Page> unique = new LinkedHashMap<>();
        for (Page p : pages) unique.putIfAbsent(p.getId(), p);
        pages = new ArrayList<>(unique.values());

        ensureDefaultPageExists();
    }

    public void saveToDisk() {
        repo.saveAll(pages);
    }

    public Page getOrCreateDefaultPage() {
        for (Page p : pages) {
            if (DEFAULT_PAGE_ID.equals(p.getId())) return p;
        }
        Page created = new Page("Default Page");
        created.setId(DEFAULT_PAGE_ID);
        pages.add(0, created);
        return created;
    }

    private void ensureDefaultPageExists() {
        getOrCreateDefaultPage();
    }
}
