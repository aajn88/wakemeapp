package com.doers.wakemeapp.common.model.audio;

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
    private List<Song> songs;

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
     * @return the songs
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * @return songs the songs to set
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
