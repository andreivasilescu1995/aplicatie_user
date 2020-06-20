package com.aplicatie.user.views;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewDecorator extends RecyclerView.ItemDecoration {
    private final int top;
    private final int bottom;
    private final int left;
    private final int right;

    public RecyclerViewDecorator(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = top;
        outRect.bottom = bottom;
        outRect.left = left;
        outRect.right = right;
    }
}