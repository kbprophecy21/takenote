package com.kyle.takenote;

//-------Java Imports----------//
import java.io.IOException;
import java.util.UUID;

import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.domain.service.PageService;
import com.kyle.takenote.infrastructure.persistence.json.JsonCollectionRepository;
import com.kyle.takenote.infrastructure.persistence.json.JsonNoteRepository;
import com.kyle.takenote.infrastructure.persistence.json.JsonPageRepository;
import com.kyle.takenote.infrastructure.storage.FileStorage;
import com.kyle.takenote.ui.controller.shell.MainShellController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Notes: Main application area. 
 */
public class App extends Application { // entry point of a javaFx application


    @Override
    public void start(Stage stage) throws IOException { //Stage class is the window of the application. JavaFx will always need Stage, Scene, PaneLayout.
     
        Services services = buildServices();
        Parent root = loadMainShell(services);

        Scene scene = new Scene(root, 1400, 800);
        stage.setTitle("TakeNotes");
        stage.setScene(scene);
        stage.show();
        
    }




    public static void main(String[] args) {
        launch(args);
    }




//---------------Helper Methods--------------------//

    private Parent loadMainShell(Services services) throws IOException{

            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/kyle/takenote/fxml/shell/MainShell.fxml")
        );

        Parent root = loader.load();
        MainShellController controller = loader.getController();

        controller.setServices(
            services.collectionService,
            services.noteService,
            services.pageService
        );

        return root;
    }


    private static class Services {
        final PageService pageService;
        final CollectionService collectionService;
        final NoteService noteService;

        public Services(PageService pageService, CollectionService collectionService, NoteService noteService) {
            this.pageService = pageService;
            this.collectionService = collectionService;
            this.noteService = noteService;
        }  
    }

    private Services buildServices() {

        FileStorage storage = new FileStorage();

        JsonPageRepository pageRepo = new JsonPageRepository(storage);
        JsonCollectionRepository collectionRepo = new JsonCollectionRepository(storage);
        JsonNoteRepository noteRepo = new JsonNoteRepository(storage);

        PageService ps = new PageService(pageRepo);
        CollectionService cs = new CollectionService(collectionRepo);
        NoteService ns = new NoteService(cs, noteRepo);

        // Load pages first so we can get defaultPageId
        ps.loadFromDisk();
        ps.getOrCreateDefaultPage();
        ps.saveToDisk();

        UUID defaultPageId = ps.getDefaultPageId();

        // Tell services what the default page is for migration + new items
        cs.setDefaultPageId(defaultPageId);      
        ns.setDefaultPageId(defaultPageId);

        // Now load collections/notes migration can now fill pageId
        cs.loadFromDisk();
        cs.saveToDisk();

        ns.loadFromDisk();
        ns.saveToDisk();

        return new Services(ps, cs, ns);
    }

}