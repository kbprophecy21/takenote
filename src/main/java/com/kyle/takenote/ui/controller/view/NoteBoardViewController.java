package com.kyle.takenote.ui.controller.view;


import javafx.scene.layout.FlowPane;

public class NoteBoardViewController {

    /**
     * TODO: add logger for debugging purpose later in near future.
     */
    //private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());
    
    FlowPane noteBoard;

    



    //----------Helper Methods----------//

    private void refresh() {
        noteBoard.getChildren().clear();

    }
}
