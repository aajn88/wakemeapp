package com.doers.wakemeapp.common.model.alarms;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * This is the alarm information and configuration will be stored
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class Alarm {

    /** Alarm ID **/
    @DatabaseField(generatedId = true)
    private Integer id;

    /** Alarm name **/
    @DatabaseField(canBeNull = false)
    private String name;

    /** Scheduled dates **/
    @DatabaseField(dataType = DataType.SERIALIZABLE, canBeNull = false)
    private boolean[] scheduledDates;

    /** Scheduled hour (0-23) **/
    @DatabaseField(canBeNull = false)
    private Integer hour;

    /** Scheduled minute (0-59) **/
    @DatabaseField(canBeNull = false)
    private Integer minute;

    /** Playlist **/
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Playlist playlist;

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
     * @return the scheduledDates
     */
    public boolean[] getScheduledDates() {
        return scheduledDates;
    }

    /**
     * @return scheduledDates the scheduledDates to set
     */
    public void setScheduledDates(boolean[] scheduledDates) {
        this.scheduledDates = scheduledDates;
    }

    /**
     * @return the hour
     */
    public Integer getHour() {
        return hour;
    }

    /**
     * @return hour the hour to set
     */
    public void setHour(Integer hour) {
        this.hour = hour;
    }

    /**
     * @return the minute
     */
    public Integer getMinute() {
        return minute;
    }

    /**
     * @return minute the minute to set
     */
    public void setMinute(Integer minute) {
        this.minute = minute;
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
}
