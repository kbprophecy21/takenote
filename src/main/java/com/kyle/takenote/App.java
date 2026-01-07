package com.kyle.takenote;

import java.io.IOException;

import com.kyle.takenote.domain.model.Note;
import com.kyle.takenote.domain.service.CollectionService;
import com.kyle.takenote.domain.service.NoteService;
import com.kyle.takenote.ui.controller.shell.MainShellController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
        CollectionService cs = new CollectionService();
        NoteService ns = new NoteService(cs);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kyle/takenote/fxml/shell/MainShell.fxml"));
        Parent root = loader.load();
        MainShellController controller = loader.getController();
        controller.setServices(cs, ns);

        seedNote(cs, ns);


        Scene scene = new Scene(root, 1400, 800);

        stage.setTitle("TakeNote");
        stage.setScene(scene);
        stage.show();
    }




  //-----------------Temporary methods(testing purposes)---------------//
  /**
   * TODO: Notes: Use this for giving the app working data for GUI.
   */
    private void seedNote(CollectionService cs, NoteService ns){
        ns.addNote(cs.getDefaultCollection(), new Note("Grocery list", "apple, bread, fruit, tomatoes"));
        ns.addNote(cs.getDefaultCollection(), new Note("List number 2", "some random stuff. working on the railroad"));
        ns.addNote(cs.getDefaultCollection(), new Note("List number 3", "apple, bread, fruit, tomatoes"));
        ns.addNote(cs.getDefaultCollection(), new Note("", "water, blue, apple Jelly"));
        // ....seeding Note testing data(temporary)---
        
        System.out.println("Seed notes in default: " + cs.getDefaultCollection().getNotes().size());
       
    };



}