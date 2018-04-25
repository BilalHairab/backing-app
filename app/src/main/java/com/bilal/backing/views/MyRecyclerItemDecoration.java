package com.bilal.backing.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyRecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private int verticalSpaceHeight;
    private int horizontalSpaceHeight;

    public MyRecyclerItemDecoration(int verticalSpaceHeight, int horizontalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.horizontalSpaceHeight = horizontalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = horizontalSpaceHeight;
        outRect.right = horizontalSpaceHeight;
        outRect.top = verticalSpaceHeight;
        outRect.bottom = verticalSpaceHeight;

    }
}
