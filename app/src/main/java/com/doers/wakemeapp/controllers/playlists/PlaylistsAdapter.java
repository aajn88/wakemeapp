package com.doers.wakemeapp.controllers.playlists;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IFirebaseAnalyticsService;
import com.doers.wakemeapp.business.services.constants.FirebaseEvent;
import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.custom_views.common.Snackbar;

import java.util.List;

/**
 * This is the playlists adapter
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class PlaylistsAdapter extends RecyclerView.Adapter {

  /** Playlists list **/
  private final List<Playlist> mPlaylists;

  /** Context **/
  private final Context mContext;

  /** Layout inflater **/
  private final LayoutInflater mInflater;

  /** Firebase analytics services **/
  private final IFirebaseAnalyticsService mFirebaseAnalyticsService;

  /**
   * Adapter's constructor
   *
   * @param context
   *         App context
   * @param mPlaylists
   *         Playlists list
   */
  public PlaylistsAdapter(Context context, List<Playlist> mPlaylists,
                          IFirebaseAnalyticsService firebaseAnalyticsService) {
    this.mFirebaseAnalyticsService = firebaseAnalyticsService;
    this.mPlaylists = mPlaylists;
    mContext = context;
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
   * @param parent
   *         The ViewGroup into which the new View will be added after it is bound to an adapter
   *         position.
   * @param viewType
   *         The view type of the new View.
   *
   * @return A new ViewHolder that holds a View of the given view type.
   *
   * @see #getItemViewType(int)
   * @see #onBindViewHolder(ViewHolder, int)
   */
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new PlaylistHolder(
            mInflater.inflate(R.layout.list_item_single_front_number, parent, false));
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
   * @param holder
   *         The ViewHolder which should be updated to represent the contents of the item at the
   *         given position in the data set.
   * @param position
   *         The position of the item within the adapter's data set.
   */
  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    PlaylistHolder vh = (PlaylistHolder) holder;
    final Playlist playlist = mPlaylists.get(position);

    vh.mNameTv.setText(playlist.getName());
    vh.mSongsNumberTv.setText(Integer.toString(playlist.getSongs().size()));
    vh.mContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (playlist.isDefault()) {
          Snackbar.make(view, R.string.default_playlist_cannot_edit, Snackbar.LENGTH_SHORT).show();
          return;
        }
        AddPlaylistActivity.startActivity((Activity) mContext, playlist.getId(),
                AddPlaylistActivity.EDIT_PLAYLIST_REQUEST_CODE);
        mFirebaseAnalyticsService.logEvent(FirebaseEvent.UPDATE_PLAYLIST);
      }
    });
  }

  /**
   * Returns the total number of items in the data set hold by the adapter.
   *
   * @return The total number of items in this adapter.
   */
  @Override
  public int getItemCount() {
    return mPlaylists.size();
  }

  /**
   * This method returns the playlist at a given position
   *
   * @param position
   *         Position to be consulted
   */
  public Playlist getItem(int position) {
    return mPlaylists.get(position);
  }

  /**
   * This method removes a playlist from the adapter
   *
   * @param position
   *         Position to be removed
   */
  public void remove(int position) {
    mPlaylists.remove(position);
    notifyItemRemoved(position);
  }

  /**
   * Playlist's View Holder
   */
  private class PlaylistHolder extends ViewHolder {

    /** Container **/
    View mContainer;

    /** Name TextView **/
    TextView mNameTv;

    /** Checkbox for selection **/
    TextView mSongsNumberTv;

    /**
     * Constructor for Playlist's View Holder
     *
     * @param itemView
     *         Item of the View Holder
     */
    public PlaylistHolder(View itemView) {
      super(itemView);

      mContainer = itemView;
      mNameTv = (TextView) itemView.findViewById(R.id.text_tv);
      mSongsNumberTv = (TextView) itemView.findViewById(R.id.number_tv);
    }
  }
}
