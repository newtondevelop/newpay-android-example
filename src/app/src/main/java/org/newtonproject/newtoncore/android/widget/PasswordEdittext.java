package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.util.AttributeSet;

import org.newtonproject.newtoncore.android.R;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class PasswordEdittext extends AppCompatEditText {

    private static final String TAG = "PasswordEdittext";
    private Context mContext;

    /**
     * start circle x axis.
     */
    private float startX;
    private float startY;

    private float cX;

    /**
     * circle radius
     */
    private int radius = 10;

    /**
     * view's height, and width;
     */
    private int height;
    private int width;

    /**
     * current input length
     */
    private int textLength = 0;
    private int bottomLineLength;

    /**
     * max input count
     */
    private int totalCount = 6;

    private int circleColor = Color.BLACK;

    private int bottomLineColor = Color.GRAY;

    private int borderColor = Color.parseColor("#cccccc");

    private Paint borderPaint;

    private int divideLineStartX;

    private int divideLineWidth = 2;

    private int devideLineColor = Color.GRAY;
    private int focusedColor = Color.GRAY;
    private RectF rectF = new RectF();
    private RectF focusedRectF = new RectF();
    private int viewType = typeWeChat;
    private final static int typeWeChat = 0;
    private final static int typeBottomLine = 1;

    private int rectAngle = 8;

    private Paint divideLinePaint;

    private Paint circlePaint;

    private Paint bottomLinePaint;

    private int currentPosition = 0;

    private OnPasswordListener mListener;

    public interface OnPasswordListener {
        void onComplete(String password);
    }

    public PasswordEdittext(Context context) {
        super(context);

    }

    public PasswordEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PasswordEdittext);
        viewType = typedArray.getInt(R.styleable.PasswordEdittext_borderType, typeWeChat);
        typedArray.recycle();
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setCursorVisible(false);
        this.setFilters(new InputFilter[] {new InputFilter.LengthFilter(totalCount)});
    }

    public PasswordEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * init paint
     */
    private void initPaint() {
        circlePaint = getPaint(5, Paint.Style.FILL, circleColor);
        bottomLinePaint = getPaint(5, Paint.Style.FILL, bottomLineColor);
        borderPaint = getPaint(3, Paint.Style.STROKE, borderColor);
        divideLinePaint = getPaint(divideLineWidth, Paint.Style.FILL, borderColor);
    }

    private Paint getPaint(int strokeWidth, Paint.Style style, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        divideLineStartX = width / totalCount;
        startX = width / totalCount / 2;
        startY = height / 2;
        bottomLineLength = width / (totalCount + 2);
        rectF.set(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (viewType) {
            case typeWeChat:
                drawWechatBorder(canvas);
                break;
            case typeBottomLine:
                drawBottomBorder(canvas);
                //drawItemFocused(canvas);
                break;
            default:
                break;
        }
        drawCircle(canvas);
    }

    private void drawWechatBorder(Canvas canvas) {
        canvas.drawRoundRect(rectF, rectAngle, rectAngle, borderPaint);
        for(int i = 0; i < totalCount - 1; i++) {
            canvas.drawLine((i + 1) * divideLineStartX, 0, (i + 1) * divideLineStartX, height, divideLinePaint);
        }
    }

    private void drawItemFocused(Canvas canvas) {
        if(currentPosition > totalCount - 1) {
            return;
        }
        focusedRectF.set(currentPosition * divideLineStartX, 0, (currentPosition + 1) * divideLineStartX,
                height);
        canvas.drawRoundRect(focusedRectF, rectAngle, rectAngle, getPaint(3, Paint.Style.STROKE, focusedColor));
    }

    private void drawBottomBorder(Canvas canvas) {
        for(int i = 0; i < totalCount; i++) {
            cX = startX + i * 2 * startX;
            canvas.drawLine(cX - bottomLineLength/2, height, cX + bottomLineLength/2, height, bottomLinePaint);
        }
    }

    private void drawCircle(Canvas canvas) {
        for(int i = 0; i < textLength; i++) {
            canvas.drawCircle(startX + i * 2 * startX, startY, radius, circlePaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.currentPosition = start + lengthAfter;
        textLength = text.toString().length();
        if(textLength == totalCount) {
            if(mListener != null) {
                mListener.onComplete(text.toString().trim());
            }
        }
        invalidate();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if(selStart == selEnd) {
            setSelection(getText().length());
        }
    }

    public String getPasswordString() {
        return getText().toString().trim();
    }

    public void setOnPasswordListener(OnPasswordListener onPasswordListener) {
        this.mListener = onPasswordListener;
    }

    public void clean() {
        setText("");
    }
}
