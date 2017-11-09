package ru.anatol.trackrenamer.core.process;

import com.mpatric.mp3agic.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import ru.anatol.file.OnFile;
import ru.anatol.trackrenamer.core.MyBatis;
import ru.anatol.trackrenamer.core.entities.Info;
import ru.anatol.trackrenamer.core.entities.Track;
import ru.anatol.trackrenamer.core.entities.TrackInfo;
import ru.anatol.trackrenamer.core.enums.BitrateTypeEnum;
import ru.anatol.trackrenamer.core.enums.InfoTypeEnum;
import ru.anatol.trackrenamer.core.mapper.InfoMapper;
import ru.anatol.trackrenamer.core.mapper.TrackInfoMapper;
import ru.anatol.trackrenamer.core.mapper.TrackMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anatol on 26.04.2017.
 */
public class ProcessFile implements OnFile {

    private final SqlSessionFactory sessionFactory = MyBatis.getSqlSessionFactory();

    @Override
    public void on(File file) {
        try {
            System.out.println(file.getAbsolutePath());
            String md5Hex = md5Hex(file);

            try (SqlSession session = sessionFactory.openSession()) {
                TrackMapper trackMapper = session.getMapper(TrackMapper.class);
                if (trackMapper.existsMD5(md5Hex)) {
                    System.out.println("------- already exists");
                    return;
                }
                InfoMapper infoMapper = session.getMapper(InfoMapper.class);

                Track track = convert(file);
                track.setMd5(md5Hex);
                trackMapper.addTrack(track);

                TrackInfoMapper trackInfoMapper = session.getMapper(TrackInfoMapper.class);
                for (TrackInfo trackInfo : track.getTrackInfos()) {
                    if (trackInfo.getInfo().getId() == null) {
                        Info equalsInfo = infoMapper.getEqualsInfo(trackInfo.getInfo());
                        if (equalsInfo != null) {
                            trackInfo.setInfo(equalsInfo);
                        } else {
                            infoMapper.addInfo(trackInfo.getInfo());
                        }
                    }
                    trackInfoMapper.addTrackInfo(trackInfo);
                };

                session.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Track convert(File file) throws InvalidDataException, IOException, UnsupportedTagException, NoSuchAlgorithmException {
        Mp3File mp3file = new Mp3File(file.getAbsolutePath());
//        String md5Hex = md5Hex(file);

        Track track = new Track();
//        track.setMd5(md5Hex);
        track.setSize(file.length());
        track.setFilename(file.getName());
        track.setLength(mp3file.getLengthInSeconds());
        track.setBitrate(mp3file.getBitrate());
        track.setBitrateType(mp3file.isVbr() ? BitrateTypeEnum.VBR : BitrateTypeEnum.CBR);
        track.setTrackInfos(new ArrayList<>(3));
        if (true) {
            TrackInfo fileName = new TrackInfo();
            fileName.setType(InfoTypeEnum.FILE_NAME);
            fileName.setTrack(track);
            fileName.setInfo(fromFileName(file.getName()));
            track.getTrackInfos().add(fileName);
        }
        if (mp3file.hasId3v1Tag()) {
            TrackInfo id3v1 = new TrackInfo();
            id3v1.setType(InfoTypeEnum.ID3V1);
            id3v1.setTrack(track);
            id3v1.setInfo(fromID3v1(mp3file.getId3v1Tag()));
            track.getTrackInfos().add(id3v1);
        }
        if (mp3file.hasId3v2Tag()) {
            TrackInfo id3v2 = new TrackInfo();
            id3v2.setType(InfoTypeEnum.ID3V2);
            id3v2.setTrack(track);
            id3v2.setInfo(fromID3v2(mp3file.getId3v2Tag()));
            track.getTrackInfos().add(id3v2);
        }
        return track;
    }


    private String md5Hex(File file) throws IOException, NoSuchAlgorithmException {
        try (FileInputStream is = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(is, md);
            byte[] buffer = new byte[1024];
            while (dis.read(buffer) != -1) ;
            BigInteger bigInt = new BigInteger(1, md.digest());
            String md5Hex = bigInt.toString(16);
            while (md5Hex.length() < 32) md5Hex = "0" + md5Hex;
            return md5Hex;
        }
    }

    private Info fromFileName(String filename) {
        ProcessFilename processFilename = new ProcessFilename();
        return processFilename.on(filename);
    }

    private Info fromID3v1(ID3v1 id3v1) {
        Info info = new Info();
        if (id3v1.getArtist() != null && !id3v1.getArtist().isEmpty()) {
            info.setArtist(id3v1.getArtist());
        }
        if (id3v1.getTitle() != null && !id3v1.getTitle().isEmpty()) {
            info.setTitle(id3v1.getTitle());
        }
        if (id3v1.getComment() != null && !id3v1.getComment().isEmpty()) {
            info.setComment(id3v1.getComment());
        }
        return info;
    }

    private Info fromID3v2(ID3v2 id3v2) {
        Info info = new Info();
        if (id3v2.getArtist() != null && !id3v2.getArtist().isEmpty()) {
            info.setArtist(id3v2.getArtist());
        }
        if (id3v2.getTitle() != null && !id3v2.getTitle().isEmpty()) {
            info.setTitle(id3v2.getTitle());
        }
        if (id3v2.getComment() != null && !id3v2.getComment().isEmpty()) {
            info.setComment(id3v2.getComment());
        }
        return info;
    }

    private String bitrateToString(Mp3File mp3file) {
        return mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? BitrateTypeEnum.VBR : BitrateTypeEnum.CBR);
    }

    private String time(Mp3File mp3file) {
        Date date = new Date(mp3file.getLengthInSeconds() * 1000);
        return new SimpleDateFormat("mm.ss").format(date);
    }
}
