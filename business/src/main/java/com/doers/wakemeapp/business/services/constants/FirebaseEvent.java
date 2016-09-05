package com.doers.wakemeapp.business.services.constants;

/**
 * Expected Firebase events. These are the possible events for WakeMeApp to be sent to Firebase
 * Analytics
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public enum FirebaseEvent {

  /** This event occurs when an alarm was created using the informative text **/
  CREATE_ALARM_CLICKING_TEXT,

  /** This event occurs when an alarm was created using the FAB button **/
  CREATE_ALARM_CLICKING_FAB,

  /** This event occurs when the user clicks on create playlist **/
  CREATE_PLAYLIST,

  /** This event occurs when the user creates a playlist **/
  PLAYLIST_CREATED,

  /** This event occurs when the user clicks on a playlist to edit it **/
  UPDATE_PLAYLIST,

  /** This event occurs when the user updates an existing playlist **/
  PLAYLIST_UPDATED,

  /** This event occurs when the user clicks the WakeMeApp toolbar icon **/
  TOOLBAR_WAKE_ME_APP_ICON_CLICKED

}
