package com.kyle.takenote.ui.navigation;


/**
 * Notes: Route names/ids (HOME, NOTES, COLLECTIONS, ECT..). 
 * this file holds contants only for escaping from raw data strings.
 * 
 */
public final class Routes {
    private Routes() {}

    // Main content views
    public static final String HOME_VIEW =
            "/com/kyle/takenote/fxml/view/HomeView.fxml";

    public static final String NOTE_BOARD_VIEW =
            "/com/kyle/takenote/fxml/view/NoteBoardView.fxml";

    public static final String COLLECTION_BOARD_VIEW =
            "/com/kyle/takenote/fxml/view/CollectionBoardView.fxml";


            
    // Bottom action bar controls (whatever folder you use)
    public static final String HOME_CONTROLS =
            "/com/kyle/takenote/fxml/component/actioncontentcontrols/HomeControlsView.fxml";

    public static final String NOTE_CONTROLS =
            "/com/kyle/takenote/fxml/component/actioncontentcontrols/NoteControlsView.fxml";

    public static final String COLLECTION_CONTROLS =
            "/com/kyle/takenote/fxml/component/actioncontentcontrols/CollectionControlsView.fxml";
}


