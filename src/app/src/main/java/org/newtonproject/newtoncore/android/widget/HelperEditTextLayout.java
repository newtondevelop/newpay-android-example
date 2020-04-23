package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.utils.Validators;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/12/29--11:09 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class HelperEditTextLayout extends ConstraintLayout {

    private String TAG = "HelperEditTextLayout";

    private TextView titleTextView;
    public EditText editText;
    private TextView errorTextView;

    private String mTitle;

    private String mHint;

    private int mInputType = EditorInfo.TYPE_CLASS_TEXT; // 128
    private int mMaxLength = Integer.MAX_VALUE;
    private int numberDecimal = 0x00002002;
    private int mMinLines = 1;
    private boolean mRootClickable = false;
    private boolean mEnable = true;
    private int mDividerColor = -1;
    private View divideView;
    private KeyListener mKeyListener;
    private boolean isErrorViewAlwaysShow = false;
    protected View view;

    public HelperEditTextLayout(Context context) {
        this(context, null);
    }

    public HelperEditTextLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HelperEditTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.helperEditLayout);
        if (null != attrArray) {
            mTitle = attrArray.getString(R.styleable.helperEditLayout_title);
            mHint = attrArray.getString(R.styleable.helperEditLayout_hint);
            mInputType = attrArray.getInt(R.styleable.helperEditLayout_inputType, EditorInfo.TYPE_CLASS_TEXT);
            mMaxLength = attrArray.getInteger(R.styleable.helperEditLayout_maxLength, Integer.MAX_VALUE);
            mMinLines = attrArray.getInteger(R.styleable.helperEditLayout_minLines, 1);
            mRootClickable = attrArray.getBoolean(R.styleable.helperEditLayout_rootClickable, false);
            mEnable = attrArray.getBoolean(R.styleable.helperEditLayout_enable, true);
            mDividerColor = attrArray.getColor(R.styleable.helperEditLayout_editBaseLineColor, getResources().getColor(R.color.divide));
            attrArray.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view = LayoutInflater.from(getContext()).inflate(getLayoutRes(), this, false);
        titleTextView = view.findViewById(R.id.titleTextView);
        editText = view.findViewById(R.id.edittext);
        editText.setMinLines(mMinLines);
        divideView = view.findViewById(R.id.divideView);
        divideView.setBackgroundColor(mDividerColor);
        mKeyListener = editText.getKeyListener();
        titleTextView.setText(mTitle);
        editText.setHint(mHint);
        setEditEnabled(mEnable);
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(mMaxLength)});
        errorTextView = view.findViewById(R.id.errorTextView);
        errorTextView.setVisibility(GONE);
        addView(view);
        editText.setInputType(mInputType);
        setHintSize();
        initFocus();
    }

    public int getLayoutRes(){
        return R.layout.layout_helper_edittext;
    }

    private void initFocus() {
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Log.e(TAG, "OnFocus");
                    //editText.setBackgroundColor(Color.BLACK);
                }else{
                    Log.e(TAG, "NO Focues");
                    if(!isErrorViewAlwaysShow){
                        errorTextView.setVisibility(GONE);
                        errorTextView.setText(null);
                    }
                }
            }
        });
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void setText(@StringRes int text) {
        editText.setText(text);
    }

    public void setTitleText(String text) {
        titleTextView.setText(text);
    }

    public void setTitleText(@StringRes int text) {
        titleTextView.setText(text);
    }

    public void setEditEnabled(boolean enabled) {
        if(!enabled) {
            editText.setKeyListener(null);
        }else {
            editText.setKeyListener(mKeyListener);
        }
    }

    public String getEditText() {
        if(mInputType == numberDecimal) {
            return editText.getText().toString().trim().replace(",", "");
        } else {
            return editText.getText().toString().trim();
        }
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(getEditText());
    }

    public boolean checkEmpty(int errorMessageId) {
        boolean empty = isEmpty();
        if(empty) {
            setError(errorMessageId);
        }
        return empty;
    }

    public void setError(String errorMessage) {
        errorTextView.setVisibility(VISIBLE);
        errorTextView.setText(errorMessage);
        Log.e(TAG, "Show Error" + errorMessage);
    }

    public void setError(@StringRes int errorMessageId) {
        errorTextView.setVisibility(VISIBLE);
        errorTextView.setText(errorMessageId);
    }

    public void setErrorColor(@ColorInt int color){
        errorTextView.setTextColor(color);
    }

    public void setErrorViewAlwaysShow(boolean flag){
        isErrorViewAlwaysShow = flag;
    }

    public void hideError() {
        errorTextView.setVisibility(GONE);
    }

    public boolean checkPassword(int password_error) {
        String editText = getEditText();
        boolean passed = Validators.checkPassword(editText);
        if(!passed) {
            setError(password_error);
        }
        return passed;
    }

    public boolean checkEquals(String string, int password_not_equal) {
        String editText = getEditText();
        if(TextUtils.equals(string, editText)) {
            return true;
        }
        setError(password_not_equal);
        return false;
    }

    public void addTextChangedListener(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    public EditText getEditTextView() {
        return editText;
    }

    public void setHintSize() {
        CharSequence hint = editText.getHint();
        if(hint == null) return;
        SpannableString spannableString = new SpannableString(hint);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(12, true);
        spannableString.setSpan(absoluteSizeSpan,0 , spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannableString(spannableString));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mRootClickable) {
            return true;
        }else{
            return super.onInterceptTouchEvent(ev);
        }
    }
}
