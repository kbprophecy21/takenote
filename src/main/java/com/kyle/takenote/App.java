package com.kyle.takenote;

//-------Java Imports----------//
import java.io.IOException;

import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.infrastructure.persistence.json.JsonNoteRepository;
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
     
        loadMainShell(stage);
        
    }




    public static void main(String[] args) {
        launch(args);
    }




//---------------Helper Methods--------------------//

    private void loadMainShell(Stage stage) throws IOException{

        FileStorage storage = new FileStorage();
        JsonNoteRepository noteRepo = new JsonNoteRepository(storage);

        CollectionService cs = new CollectionService();
        NoteService ns = new NoteService(cs, noteRepo);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kyle/takenote/fxml/shell/MainShell.fxml"));
        Parent root = loader.load();
        MainShellController controller = loader.getController();
        controller.setServices(cs, ns);

        ns.loadFromDisk();
        


        Scene scene = new Scene(root, 1400, 800);

        stage.setTitle("TakeNote");
        stage.setScene(scene);
        stage.show();
    }




  



}