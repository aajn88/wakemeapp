package com.doers.wakemeapp.common.model.alarms;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * Playlist songs for the alarms
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class Playlist {

    /** Playlist Id **/
    @DatabaseField(generatedId = true)
    private Integer id;

    /** Playlist name **/
    @DatabaseField(canBeNull = false)
    private String name;

    /** Songs' path **/
    @DatabaseField(canBeNull = false, dataType = DataType.SERIALIZABLE)
    private List<String> songsPath;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the songsPath
     */
    public List<String> getSongsPath() {
        return songsPath;
    }

    /**
     * @return songsPath the songsPath to set
     */
    public void setSongsPath(List<String> songsPath) {
        this.songsPath = songsPath;
    }
}
