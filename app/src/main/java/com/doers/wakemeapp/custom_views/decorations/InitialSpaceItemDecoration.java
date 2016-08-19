package com.doers.wakemeapp.custom_views.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Vertical space decoration for recycler views
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class InitialSpaceItemDecoration extends RecyclerView.ItemDecoration {

  private final int mInitialSpaceHeight;

  /**
   * Constructor for vertical space
   *
   * @param initialSpaceHeight
   *         Vertical space height
   */
  public InitialSpaceItemDecoration(int initialSpaceHeight) {
    this.mInitialSpaceHeight = initialSpaceHeight;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                             RecyclerView.State state) {
    if (parent.getChildAdapterPosition(view) == 0) {
      outRect.top = mInitialSpaceHeight;
    }
  }
}