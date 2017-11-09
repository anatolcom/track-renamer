package ru.anatol.trackrenamer.app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.ibatis.session.SqlSession;
import ru.anatol.file.ExtensionFilter;
import ru.anatol.file.FileFinder;
import ru.anatol.trackrenamer.core.MyBatis;
import ru.anatol.trackrenamer.core.entities.Info;
import ru.anatol.trackrenamer.core.entities.Track;
import ru.anatol.trackrenamer.core.mapper.TrackMapper;
import ru.anatol.trackrenamer.core.process.ProcessFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Anatol on 19.06.2017.
 */
public class TrackRenamer extends Application {
    public static ObservableList<Track> observableTracks = FXCollections.observableArrayList();
    public static ObservableList<Info> observableInfos = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws Exception {
//        URL fxml = getClass().getResource("MainForm.fxml");
//        Parent root = FXMLLoader.load(fxml);
//        Scene scene = new Scene(root);

        Scene scene = new Scene(createPane(), 550, 550);

        stage.setTitle("Track renamer");
        stage.setScene(scene);
        stage.show();

    }

    private void onScanDir(ActionEvent event) {
        observableTracks.clear();
        try (SqlSession session = MyBatis.openSession()) {
            String path = "C:/Users/Anatol/Desktop/BackUp/_music/Ambient";
            ExtensionFilter extFilter = new ExtensionFilter(new HashSet(Arrays.asList("mp3")));
            new FileFinder(path, new ProcessFile()).filter(extFilter).find();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onShowTracks(ActionEvent event) {
        observableTracks.clear();
        try (SqlSession session = MyBatis.openSession()) {
            TrackMapper trackMapper = session.getMapper(TrackMapper.class);
            List<Track> tracks = trackMapper.getTracks();
            tracks.forEach(track -> observableTracks.add(track));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onShowInfos(MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
    }

//    private void onShowInfos(ActionEvent event) {
//        Object source = event.getSource();
//        observableInfos.clear();
//        try {
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    private Parent createPane() {
        GridPane root = new GridPane();
        root.setPadding(new Insets(25, 25, 25, 25));

        Text title = new Text("File finder");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        root.add(title, 0, 0, 2, 1);

//        Label pathLabel = new Label("Input path:");
//        root.add(pathLabel, 0, 1);
//
//        String path = "C:/Users/Anatol/Desktop/BackUp/_music/Ambient";
//
//        TextField pathFiled = new TextField(path);
//        pathFiled.setId("pathId");
//        root.add(pathFiled, 1, 1);

        Button btnScan = new Button();
        btnScan.setText("Scan");
        btnScan.setOnAction(this::onScanDir);
        root.add(btnScan, 0, 2);

        Button btnFind = new Button();
        btnFind.setText("Find");
        btnFind.setOnAction(this::onShowTracks);
        root.add(btnFind, 1, 2);

        ListView<String> listTracks = new ListView<>();
        listTracks.setMinWidth(500);
//        listTracks.setId("listId");
        observableTracks.addListener((ListChangeListener) obj -> listTracks.setItems(obj.getList()));
        listTracks.setOnMouseClicked(this::onShowInfos);
        root.add(listTracks, 0, 3, 2, 1);

        ListView<String> listInfos = new ListView<>();
        listInfos.setMinWidth(500);
//        listInfos.setId("listId");
        observableInfos.addListener((ListChangeListener) obj -> listInfos.setItems(obj.getList()));
        root.add(listInfos, 0, 4, 2, 1);

        return root;
    }


}
