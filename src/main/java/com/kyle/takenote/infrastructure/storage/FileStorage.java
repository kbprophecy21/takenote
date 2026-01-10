package com.kyle.takenote.infrastructure.storage;

/**
 * Notes: manage app data folder + attachments.
 */


//import java.io.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class FileStorage {
    
    private final Path baseDir;


    public FileStorage() {
        this.baseDir = Paths.get("data"); // dev-simple local folder
    }


    public Path resolve(String filename) {
        return baseDir.resolve(filename);
    }

    public String readFile(String filename) {
        try {
            Path path = resolve(filename);
            if (!Files.exists(path)) return null; // no data yet!
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read" + filename, e);
        }
    }

     public void writeFile(String filename, String content) {
        try {
            if (!Files.exists(baseDir)) {
                Files.createDirectories(baseDir);
            }
            Path path = resolve(filename);
            Files.writeString(
                    path,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + filename, e);
        }
    }
}
