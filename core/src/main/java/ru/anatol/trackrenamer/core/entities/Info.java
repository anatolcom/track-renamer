package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;
import ru.anatol.Printable;
import ru.anatol.trackrenamer.core.enums.InfoTypeEnum;

/**
 * @author Anatol
 */
@Alias("info")
public class Info implements Printable {
    private Long id;
//    private InfoTypeEnum type;
    private String artist;
    private String title;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
//
//    public InfoTypeEnum getType() {
//        return type;
//    }
//
//    public void setType(InfoTypeEnum type) {
//        this.type = type;
//    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return artist + " - " + title;
    }

    @Override
    public void print() {
        System.out.printf("%5d %10s %25s %20s\n", id, artist, title, comment);
    }
}
