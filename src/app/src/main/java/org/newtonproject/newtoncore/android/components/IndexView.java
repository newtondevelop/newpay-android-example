package org.newtonproject.newtoncore.android.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.newtonproject.newtoncore.android.utils.DPUtils;


/**
 * IndexView for quick index String which start with 'A','B','C'... and show String.
 *
 * @author weixuefeng@lubangame.com
 * @version 1.0
 * @copyright (c) 2016 Beijing ShenJiangHuDong Technology Co., Ltd. All rights reserved.
 */

public class IndexView extends View {

    private String[] strings = new String[]{"#","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private Paint indexPaint;
    private int indexColor = android.graphics.Color.parseColor("#007aff");

    public IndexView(Context context) {
        super(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(DPUtils.dp2Px(getContext(), 30), (int) (DPUtils.getScreenHeight(getContext()) / 1.2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / (strings.length);
        float xPos = width / 2f;
        indexPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indexPaint.setColor(indexColor);
        indexPaint.setTextSize(DPUtils.dp2Px(getContext(), 10));
        indexPaint.setTextAlign(Paint.Align.CENTER);
        indexPaint.setTypeface(Typeface.DEFAULT_BOLD);
        Paint.FontMetricsInt fm = indexPaint.getFontMetricsInt();
        for (int i = 0; i < strings.length; i++) {
            int center = singleHeight * (i) + singleHeight / 2;
            int baseline = center + (fm.bottom - fm.top) / 2 - fm.bottom;
            canvas.drawText(strings[i], xPos, baseline, indexPaint);
        }
        super.onDraw(canvas);
    }

    public interface ChooseListener {
        void onChoose(int pos, String text);
    }

    private ChooseListener mChooseListener;

    public void setOnChooseListener(ChooseListener chooseListener) {
        this.mChooseListener = chooseListener;
    }

    public void setIndexStrings(String[] indexStrings) {
        //this.strings = indexStrings;
        //requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float y = event.getY();
        int pos = (int) (y / getHeight() * strings.length);
        if (pos < strings.length && pos > -1) {
            String text = strings[pos];
            if (null != mChooseListener) {
                mChooseListener.onChoose(pos, text);
            }
            invalidate();
        }
        return true;
    }
}
