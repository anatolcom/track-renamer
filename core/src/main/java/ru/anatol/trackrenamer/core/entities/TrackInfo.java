package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;
import ru.anatol.Printable;
import ru.anatol.trackrenamer.core.enums.InfoTypeEnum;

/**
 * @author Anatol
 */
@Alias("track_info")
public class TrackInfo implements Printable {
    private Long id;
    private InfoTypeEnum type;
    private Track track;
    private Info info;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InfoTypeEnum getType() {
        return type;
    }

    public void setType(InfoTypeEnum type) {
        this.type = type;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return type + ": " + track.getMd5() + " - " + info.getId();
    }

    @Override
    public void print() {
        System.out.printf("%5d %10s %32s %5d\n", id, type, track.getMd5(), info.getId());
    }
}
