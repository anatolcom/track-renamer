package ru.anatol.trackrenamer.core.process;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import ru.anatol.trackrenamer.core.MyBatis;
import ru.anatol.trackrenamer.core.entities.*;
import ru.anatol.trackrenamer.core.mapper.SongMapper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Anatol on 03.05.2017.
 */
public class ProcessArtist implements OnTrack {

    private SqlSessionFactory sessionFactory = MyBatis.getSqlSessionFactory();

    @Override
    public void on(Track track) {
        try (SqlSession session = sessionFactory.openSession()) {
            SongMapper songMapper = session.getMapper(SongMapper.class);

            List<Info> infos = track.getTrackInfos()
                    .stream()
                    .map(TrackInfo::getInfo)
                    .collect(Collectors.toList());

            List<ArtistVariant> foundVariants = new LinkedList<>();
            List<ArtistVariant> newVariants = new LinkedList<>();
            for (Info info : infos) {
                if (info == null || info.getArtist() == null || hasVariant(newVariants, info.getArtist())) continue;
                ArtistVariant artistVariant = songMapper.findArtistVariant(info.getArtist());
                if (artistVariant == null) {
                    newVariants.add(new ArtistVariant(info.getArtist()));
                } else {
                    foundVariants.add(artistVariant);
                }
            }

            final Artist artist;
            if (foundVariants.isEmpty()) {
                artist = new Artist();
            } else {
                artist = foundVariants.get(0).getArtist();
                for (ArtistVariant artistVariant : foundVariants) {
                    if (!artist.equals(artistVariant.getArtist())) throw new Exception("more one artists found");
                }
            }

            if (artist.getId() == null) songMapper.addArtist(artist);
            for (ArtistVariant artistVariant : newVariants) {
                artistVariant.setArtist(artist);
                songMapper.addArtistVariant(artistVariant);
            }
            session.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean hasVariant(List<ArtistVariant> artistVariants, String value) {
        for (ArtistVariant artistVariant : artistVariants) {
            if (artistVariant.getValue().equals(value)) return true;
        }
        return false;
    }
}
