/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.trackrenamer.core;

import java.lang.reflect.Array;
import java.util.*;

import org.apache.ibatis.session.SqlSession;
import ru.anatol.file.ExtensionFilter;
import ru.anatol.file.FileFinder;
import ru.anatol.trackrenamer.core.entities.*;
import ru.anatol.trackrenamer.core.mapper.SongMapper;
import ru.anatol.trackrenamer.core.mapper.TrackMapper;
import ru.anatol.trackrenamer.core.process.ProcessArtist;
import ru.anatol.trackrenamer.core.process.ProcessFile;
import ru.anatol.trackrenamer.core.process.ProcessFilename;

/**
 * @author Anatol
 */
public class Main {

//    private static List<Artist> artistTable = new LinkedList<>(); //???
//    private static List<ArtistVariant> artistVariantTable = new LinkedList<>(); //???

//    private static ArtistVariant findArtistVariant(String artist) {
//        for (ArtistVariant av : artistVariantTable) {
//            if (av.getValue().equals(artist)) return av;
//        }
//        return null;
//    }

//    private static Artist getArtist(List<ArtistVariant> foundVariants, List<ArtistVariant> newVariants) throws Exception {
//        final Artist artist;
//        if (foundVariants.isEmpty()) {
//            artist = new Artist();
//            artistTable.add(artist);
//        } else {
//            artist = foundVariants.get(0).getArtist();
//            for (ArtistVariant artistVariant : foundVariants) {
//                if (artist.equals(artistVariant.getArtist())) {
//                    continue;
//                }
//                throw new Exception("more one artists found");
//            }
//            if (artist == null) throw new Exception("artist not found");
//        }
//        for (ArtistVariant artistVariant : newVariants) {
//            artistVariant.setArtist(artist);
//            artistVariantTable.add(artistVariant);
//        }
//        return artist;
//
//    }

    public static void main(String[] args) {
//        String mode = "scan_dir";
        String mode = "filename";
//        String mode = "test";
        if ("scan_dir".equals(mode)) {
            String path = "C:/Users/Anatol/Desktop/BackUp/_music/Ambient";
            ExtensionFilter extFilter = new ExtensionFilter(new HashSet(Arrays.asList("mp3")));
            new FileFinder(path, new ProcessFile()).filter(extFilter).find();
        }
        if ("filename".equals(mode)) {
            try (SqlSession session = MyBatis.openSession()) {
                TrackMapper trackMapper = session.getMapper(TrackMapper.class);
                List<Track> tracks = trackMapper.getTracks();
                ProcessFilename processFilename = new ProcessFilename();
                tracks.forEach(processFilename::on);
//                for (Track track : tracks) processFilename.on(track);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if ("test".equals(mode)) {
            try (SqlSession session = MyBatis.openSession()) {
                TrackMapper trackMapper = session.getMapper(TrackMapper.class);
                List<Track> tracks = trackMapper.getTracks();

                List<Song> songs = new LinkedList<>();

                for (Track track : tracks) {
                    track.print();
                    new ProcessArtist().on(track);

//                    Song song = null;//new Song(new Artist(), new Title());
//                    songs.add(song);
//                    System.out.println("artist: " + artist.getValue());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
