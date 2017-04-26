package ru.anatol.trackrenamer.core;

import com.mpatric.mp3agic.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.apache.ibatis.session.SqlSession;
import ru.anatol.file.OnFile;
import ru.anatol.trackrenamer.core.entities.Info;
import ru.anatol.trackrenamer.core.entities.Track;
import ru.anatol.trackrenamer.core.enums.BitrateTypeEnum;
import ru.anatol.trackrenamer.core.enums.InfoTypeEnum;
import ru.anatol.trackrenamer.core.mapper.InfoMapper;
import ru.anatol.trackrenamer.core.mapper.TrackMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Anatol on 26.04.2017.
 */
public class ProcessTrack implements OnFile {

    @Override
    public void on(File file) {

        try {

            System.out.println(file.getAbsolutePath());
            String md5Hex = md5Hex(file);
//            System.out.println(md5Hex + " " + file.getName());

            SqlSession session = MyBatis.openSession();
            TrackMapper trackMapper = session.getMapper(TrackMapper.class);
            if (trackMapper.existsMD5(md5Hex)) {
                System.out.println("------- already exists");
                return;
            }
            InfoMapper infoMapper = session.getMapper(InfoMapper.class);

            try {
                Track track = convert(file);
                track.setMd5(md5Hex);
                if (track.getFromName() != null) infoMapper.addInfo(track.getFromName());
                if (track.getId3v1() != null) infoMapper.addInfo(track.getId3v1());
                if (track.getId3v2() != null) infoMapper.addInfo(track.getId3v2());
                trackMapper.addTrack(track);
                session.commit();
            } catch (Exception ex) {
                session.rollback();
                ex.printStackTrace();
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
        track.setFromName(fromFileName(file.getName()));
        if (mp3file.hasId3v1Tag()) track.setId3v1(fromID3v1(mp3file.getId3v1Tag()));
        if (mp3file.hasId3v2Tag()) track.setId3v2(fromID3v2(mp3file.getId3v2Tag()));
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
        Info info = new Info();
        info.setType(InfoTypeEnum.FILE_NAME);

//        filename.lastIndexOf("/");
//        info.setArtist(id3v1.getArtist());
//        info.setTitle(id3v1.getTitle());
//        info.setComment(id3v1.getComment());
        return info;
    }

    private Info fromID3v1(ID3v1 id3v1) {
        Info info = new Info();
        info.setType(InfoTypeEnum.ID3V1);
        info.setArtist(id3v1.getArtist());
        info.setTitle(id3v1.getTitle());
        info.setComment(id3v1.getComment());
        return info;
    }

    private Info fromID3v2(ID3v2 id3v2) {
        Info info = new Info();
        info.setType(InfoTypeEnum.ID3V2);
        info.setArtist(id3v2.getArtist());
        info.setTitle(id3v2.getTitle());
        info.setComment(id3v2.getComment());
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
