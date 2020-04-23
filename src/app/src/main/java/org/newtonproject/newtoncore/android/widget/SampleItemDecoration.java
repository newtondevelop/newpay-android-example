package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.newtonproject.newtoncore.android.utils.DPUtils;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/11/19--9:30 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class SampleItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;

    public SampleItemDecoration(Context context) {
        mPaint = new Paint();
        mPaint.setTextSize(DPUtils.dp2Px(context, 10));
        mPaint.setColor(Color.parseColor("#f5f5f5"));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int x = parent.getPaddingLeft() + DPUtils.dp2Px(parent.getContext(), 10);
        final int width = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize - 1; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            final int y = child.getBottom() + layoutParams.bottomMargin;
            final int height = y + 5;
            if (mPaint != null) {
                c.drawRect(x, y, width, height, mPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.drawLine(parent.getX(), parent.getY(), parent.getRight(), parent.getY(), mPaint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 1);
    }
}
