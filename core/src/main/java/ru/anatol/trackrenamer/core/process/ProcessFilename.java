package ru.anatol.trackrenamer.core.process;

import com.mpatric.mp3agic.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import ru.anatol.file.OnFile;
import ru.anatol.trackrenamer.core.MyBatis;
import ru.anatol.trackrenamer.core.entities.Info;
import ru.anatol.trackrenamer.core.entities.Track;
import ru.anatol.trackrenamer.core.enums.BitrateTypeEnum;
import ru.anatol.trackrenamer.core.enums.InfoTypeEnum;
import ru.anatol.trackrenamer.core.mapper.InfoMapper;
import ru.anatol.trackrenamer.core.mapper.TrackMapper;

import javax.xml.ws.Holder;
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
import java.util.List;

/**
 * Created by Anatol on 26.04.2017.
 */
public class ProcessFilename implements OnTrack {

    final String delimiter = "-";

    @Override
    public void on(Track track) {
//        System.out.println("------------------");
//        System.out.println(track.getFilename());
        Info info = processFilename(track.getFilename());
        System.out.println(info.getArtist() + " " + delimiter + " " + info.getTitle());
    }

//    @Override
    public Info on(String filename) {
//        System.out.println("------------------");
//        System.out.println(track.getFilename());
        Info info = processFilename(filename);
//        System.out.println(info.getArtist() + " " + delimiter + " " + info.getTitle());
        return info;
    }

    private Info processFilename(String filename) {
        Info info = new Info();
//        info.setType(InfoTypeEnum.FILE_NAME);

        filename = normalize(filename).substring(0, filename.lastIndexOf('.'));
        String[] splitedFilename = filename.split(delimiter);

        if (splitedFilename.length == 1) {
            info.setArtist("Unknown Artist");
            info.setTitle(splitedFilename[0].trim());
            return info;
        }
        if (splitedFilename.length == 2) {
            info.setArtist(splitedFilename[0].trim());
            info.setTitle(splitedFilename[1].trim());
            return info;
        }
        int index = getMiddleIndex(filename, delimiter.length(), splitedFilename);
        List<String> left = new ArrayList<>(splitedFilename.length);
        List<String> right = new ArrayList<>(splitedFilename.length);
        for (int l = 0; l < index; l++) left.add(splitedFilename[l]);
        for (int r = index; r < splitedFilename.length; r++) right.add(splitedFilename[r]);
        info.setArtist(String.join(delimiter, left).trim());
        info.setTitle(String.join(delimiter, right).trim());
        return info;
    }

    private int getMiddleIndex(String filename, int delimiterLength, String[] splitedFilename) {
        int index = 1;
        int minDifferent = filename.length();
        for (int q = 1; q < splitedFilename.length; q++) {
            List<String> left = new ArrayList<>(splitedFilename.length);
            List<String> right = new ArrayList<>(splitedFilename.length);
            int leftLength = (q - 1) * delimiterLength;
            int rightLength = (splitedFilename.length - q - 1) * delimiterLength;
            for (int l = 0; l < q; l++) leftLength += splitedFilename[l].length();
            for (int r = q; r < splitedFilename.length; r++) rightLength += splitedFilename[r].length();
            int different = Math.abs(leftLength - rightLength);
            if (minDifferent > different) {
                minDifferent = different;
                index = q;
            }
        }
        return index;
    }

    private String normalize(String value) {
        return value
                .replace('_', ' ')
                .replace("&amp;", "&")
                .replace(" and ", " & ");
    }
}
