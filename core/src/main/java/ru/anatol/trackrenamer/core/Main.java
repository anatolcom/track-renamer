/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.trackrenamer.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import ru.anatol.file.ExtensionFilter;
import ru.anatol.file.FileFinder;
import ru.anatol.trackrenamer.core.entities.Track;
import ru.anatol.trackrenamer.core.mapper.TrackMapper;

/**
 * @author Anatol
 */
public class Main {

    public static void main(String[] args) {
//        String mode = "scan_dir";
        String mode = "test";
        if ("scan_dir".equals(mode)) {
            String path = "C:/Users/Anatol/Desktop/BackUp/_music/Ambient";
            ExtensionFilter extFilter = new ExtensionFilter(new HashSet(Arrays.asList("mp3")));
            new FileFinder(path, new ProcessTrack()).filter(extFilter).find();
        }
        if ("test".equals(mode)) {
            try (SqlSession session = MyBatis.openSession()) {
                TrackMapper trackMapper = session.getMapper(TrackMapper.class);
                List<Track> tracks = trackMapper.getTracks();
                for (Track track : tracks) {
                    track.print();
                }
            }
        }

    }
}
