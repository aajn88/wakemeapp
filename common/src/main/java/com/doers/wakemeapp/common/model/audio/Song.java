package com.doers.wakemeapp.common.model.audio;

import com.j256.ormlite.field.DatabaseField;

/**
 * This is the song model where all information related to the song is stored
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class Song {

    /** Playlist ID Column **/
    public static final String PLAYLIST_ID = "playlist_id";

    /** Song ID **/
    @DatabaseField(generatedId = true)
    private Integer id;

    /** Playlist owner ID **/
    @DatabaseField(columnName = PLAYLIST_ID, foreign = true, foreignAutoRefresh = true)
    private Playlist playlist;

    /** Song name **/
    @DatabaseField(canBeNull = false)
    private String name;

    /** Song's Path **/
    @DatabaseField(canBeNull = false)
    private String path;

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
     * @return the playlist
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * @return playlist the playlist to set
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
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
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
