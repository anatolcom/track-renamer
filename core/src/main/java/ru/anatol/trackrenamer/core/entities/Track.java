/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;
import ru.anatol.Printable;
import ru.anatol.trackrenamer.core.enums.BitrateTypeEnum;

import java.util.List;

/**
 * @author Anatol
 */
@Alias("track")
public class Track implements Printable {

    //    private Long id;
    private String md5;
    private String filename;
    private Long size;
    private Long length;
    private Integer bitrate;
    //    @ @Param("bitrate_type")
//    @MapKey("bitrate_type")
    private BitrateTypeEnum bitrateType;
    private List<TrackInfo> trackInfos;
//    private Info fromName;
//    private Info id3v1;
//    private Info id3v2;
//    private Song song;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public BitrateTypeEnum getBitrateType() {
        return bitrateType;
    }

    public void setBitrateType(BitrateTypeEnum bitrateType) {
        this.bitrateType = bitrateType;
    }

//    public Info getFromName() {
//        return fromName;
//    }
//
//    public void setFromName(Info fromName) {
//        this.fromName = fromName;
//    }
//
//    public Info getId3v1() {
//        return id3v1;
//    }
//
//    public void setId3v1(Info id3v1) {
//        this.id3v1 = id3v1;
//    }
//
//    public Info getId3v2() {
//        return id3v2;
//    }
//
//    public void setId3v2(Info id3v2) {
//        this.id3v2 = id3v2;
//    }

    public List<TrackInfo> getTrackInfos() {
        return trackInfos;
    }

    public void setTrackInfos(List<TrackInfo> trackInfos) {
        this.trackInfos = trackInfos;
    }

    @Override
    public void print() {
        System.out.printf("%32s %100s %10d %5d %3d %3s\n", md5, filename, size, length, bitrate, bitrateType);
//        if (fromName != null) fromName.print();
//        if (id3v1 != null) id3v1.print();
//        if (id3v2 != null) id3v2.print();
    }

    @Override
    public String toString() {
        return filename + " " + bitrateType + " " + bitrate;
    }
}
