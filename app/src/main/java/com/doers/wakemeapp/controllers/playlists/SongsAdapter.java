package com.doers.wakemeapp.controllers.playlists;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.doers.wakemeapp.R;
import com.doers.wakemeapp.common.model.audio.Song;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the songs adapter
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class SongsAdapter extends RecyclerView.Adapter {

  /** Context **/
  private final Context mContext;

  /** Layout inflater **/
  private final LayoutInflater mInflater;

  /** Listed songs **/
  private List<Song> mSongs = new ArrayList<>();

  /** The alarm {@link MediaPlayer} **/
  private MediaPlayer mSongMediaPlayer;

  /**
   * Songs adapter constructor
   *
   * @param context App context
   */
  public SongsAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }

  /**
   * Songs adapter constructor given the songs list
   *
   * @param context App context
   */
  public SongsAdapter(Context context, List<Song> songs) {
    mContext = context;
    mSongs = songs;
    mInflater = LayoutInflater.from(context);
  }

  /**
   * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent an
   * item.
   * <p/>
   * This new ViewHolder should be constructed with a new View that can represent the items of the
   * given type. You can either create a new View manually or inflate it from an XML layout file.
   * <p/>
   * The new ViewHolder will be used to display items of the adapter using {@link
   * #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display different
   * items in the data set, it is a good idea to cache references to sub views of the View to
   * avoid unnecessary {@link View#findViewById(int)} calls.
   *
   * @param parent The ViewGroup into which the new View will be added after it is bound to an
   * adapter
   * position.
   * @param viewType The view type of the new View.
   * @return A new ViewHolder that holds a View of the given view type.
   * @see #getItemViewType(int)
   * @see #onBindViewHolder(ViewHolder, int)
   */
  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new SongHolder(mInflater.inflate(R.layout.list_item_single_selectable, parent, false));
  }

  /**
   * Called by RecyclerView to display the data at the specified position. This method should
   * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
   * position.
   * <p/>
   * Note that unlike {@link ListView}, RecyclerView will not call this method again if the
   * position of the item changes in the data set unless the item itself is invalidated or the new
   * position cannot be determined. For this reason, you should only use the <code>position</code>
   * parameter while acquiring the related data item inside this method and should not keep a copy
   * of it. If you need the position of an item later on (e.g. in a click listener), use {@link
   * ViewHolder#getAdapterPosition()} which will have the updated adapter position.
   * <p/>
   * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can handle
   * effcient partial bind.
   *
   * @param holder The ViewHolder which should be updated to represent the contents of the item at
   * the
   * given position in the data set.
   * @param position The position of the item within the adapter's data set.
   */
  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    SongHolder vh = (SongHolder) holder;
    final Song song = mSongs.get(position);

    vh.mContainer.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        stopAudio();
        mSongMediaPlayer = MediaPlayer.create(mContext, Uri.parse(song.getPath()));
        if (mSongMediaPlayer != null) {
          mSongMediaPlayer.start();
        }
      }
    });
    vh.mNameTv.setText(song.getName());
  }

  /**
   * This method should be invoked in {@link Activity#onStop()}
   */
  public void onStop() {
    stopAudio();
  }

  /**
   * This method stops the audio if it is playing
   */
  private void stopAudio() {
    if (mSongMediaPlayer != null && mSongMediaPlayer.isPlaying()) {
      mSongMediaPlayer.stop();
    }
  }

  /**
   * Returns the total number of items in the data set hold by the adapter.
   *
   * @return The total number of items in this adapter.
   */
  @Override public int getItemCount() {
    return mSongs.size();
  }

  /**
   * This method adds a song to the list
   *
   * @param song Song to be added
   */
  public void addSong(Song song) {
    mSongs.add(song);
    notifyItemInserted(mSongs.size() - 1);
  }

  /**
   * This method returns the current songs list
   *
   * @return Songs list
   */
  public List<Song> getSongs() {
    return mSongs;
  }

  /**
   * Song's View Holder
   */
  private class SongHolder extends ViewHolder {

    /** Main container **/
    View mContainer;

    /** Name TextView **/
    TextView mNameTv;

    /** Checkbox for selection **/
    CheckBox mSelectionCb;

    /**
     * Constructor for Song's View Holder
     *
     * @param itemView Item of the View Holder
     */
    public SongHolder(View itemView) {
      super(itemView);

      mContainer = itemView;
      mNameTv = (TextView) itemView.findViewById(R.id.text_tv);
      mSelectionCb = (CheckBox) itemView.findViewById(R.id.selectable_cb);

      mSelectionCb.setVisibility(View.GONE);
    }
  }
}
