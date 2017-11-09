package ru.anatol.trackrenamer.app;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Anatol on 23.06.2017.
 */
public class TrackRenamerController implements Initializable {

    @FXML
    private ListView<String> listId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TrackRenamer.observableTracks.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change obj) {
                listId.setItems(obj.getList());
            }
        });

//        TrackRenamer.observableTracks.add("1");
//        TrackRenamer.observableTracks.add("2");
//        TrackRenamer.observableTracks.add("3");
    }


}
