package com.kyle.takenote.ui.controller.view;


import java.util.logging.Logger;

import javafx.scene.layout.FlowPane;

public class NoteBoardViewController {

    private static final Logger LOGGER = Logger.getLogger(CollectionBoardViewController.class.getName());

    FlowPane noteBoard;

    



    //----------Helper Methods----------//

    private void refresh() {
        noteBoard.getChildren().clear();

    }
}
