package com.doers.wakemeapp.common.model.audio;

import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * Playlist songs for the alarms
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class Playlist {

  /** Name of the isDefault column **/
  public static final String IS_DEFAULT_COLUMN = "is_default";

  /** Playlist Id **/
  @DatabaseField(generatedId = true)
  private Integer id;

  /** Playlist name **/
  @DatabaseField(canBeNull = false)
  private String name;

  /** Songs' path **/
  private List<Song> songs;

  /** Is the default playlist? **/
  @DatabaseField(canBeNull = false, columnName = IS_DEFAULT_COLUMN)
  private boolean isDefault;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Song> getSongs() {
    return songs;
  }

  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean aDefault) {
    isDefault = aDefault;
  }

  @Override
  public String toString() {
    return name;
  }
}
