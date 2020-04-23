package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.newtonproject.newtoncore.android.R;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TagsLayout extends ViewGroup {

    private static final String TAG = "TagsLayout";
    private int childHorizontalSpace;

    private int childVerticalSpace;

    public TagsLayout(Context context) {
        super(context);
    }

    public TagsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.TagsLayout);
        if (null != attrArray) {
            childHorizontalSpace = attrArray.getDimensionPixelSize(R.styleable.TagsLayout_tagHorizontalSpace, 0);
            childVerticalSpace = attrArray.getDimensionPixelSize(R.styleable.TagsLayout_tagVerticalSpace, 0);
            attrArray.recycle();
        }
    }

    public TagsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Measure Child's space and height.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // get TagsLayout's size.
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // get TagsLayout's mode
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // record width and height when TagsLayout's mode is wrap_content.
        int width = 0;
        int height = 0;

        // record per row's width and height.
        int lineWidth = 0;
        int lineHeight = 0;

        // record child's count
        int childCount = getChildCount();

        // record padding info.
        int left = getPaddingLeft();
        int top = getPaddingTop();

        // iteration per child
        for(int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }
            // measure child's size and mode
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            // get child's width and height
            int childWidth = childView.getMeasuredWidth() + childHorizontalSpace;
            int childHeight = childView.getMeasuredHeight() + childVerticalSpace;
            // if width > matchparent, and tagslayout should make a new line.
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                lineWidth = childWidth;
                width = Math.max(lineWidth, childWidth);
                height += lineHeight;
                lineHeight = childHeight;
                // new row's first view
                childView.setTag(new ChildViewLocation(left, top + height, childWidth + left - childHorizontalSpace, height + childView.getMeasuredHeight() + top));
            } else {
                childView.setTag(new ChildViewLocation(lineWidth + left, top + height, lineWidth + childWidth - childHorizontalSpace + left, height + childView.getMeasuredHeight() + top));
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

        }
        width = Math.max(width, lineWidth) + getPaddingRight() + getPaddingLeft();
        height += lineHeight;
        sizeHeight += getPaddingTop() + getPaddingBottom();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if(childView.getVisibility() == GONE) {
                continue;
            }
            ChildViewLocation location = (ChildViewLocation) childView.getTag();
            childView.layout(location.left, location.top, location.right, location.bottom);
        }
    }

    /**
     * Record Child View's location
     */
    private class ChildViewLocation {

        public int left;
        public int right;
        public int top;
        public int bottom;

        public ChildViewLocation(int left, int top, int right, int bottom) {
            this.left = left;
            this.bottom = bottom;
            this.top = top;
            this.right = right;
        }
    }
}
