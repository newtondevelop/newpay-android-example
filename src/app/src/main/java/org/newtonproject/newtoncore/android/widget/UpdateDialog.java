package org.newtonproject.newtoncore.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class UpdateDialog extends Dialog {
    private static final String TAG = "Update Dialog";
    boolean isForceUpgrade;
    Context activity;
    View.OnClickListener leftOnClickListener, rightOnClickListener;
    String leftBtnText;
    String rightBtnText;
    String titleText;
    String updateReasonText;
    View progressbarPart;
    TextView title, updateReasonTextView, leftBtn, rightBtn;
    LinearLayout splitter1;
    private int mTitleBackgroundColor = Integer.MAX_VALUE;
    private int mTitleColor = Integer.MAX_VALUE;
    private int mRightTextColor = Integer.MAX_VALUE;

    public boolean isForceUpgrade() {
        return isForceUpgrade;
    }

    public void setIsForceUpgrade(boolean isForceUpgrade) {
        this.isForceUpgrade = isForceUpgrade;
        if (isForceUpgrade) {
            if(null != splitter1) {
                splitter1.setVisibility(View.GONE);
            }
            if(null != leftBtn) {
                leftBtn.setVisibility(View.GONE);
            }
        }
    }

    public void setUpdateReasonText(String reason) {
        this.updateReasonText = reason;
    }

    public void setLeftOnClickListener(View.OnClickListener leftOnClickListener) {
        this.leftOnClickListener = leftOnClickListener;
    }

    public void setRightOnClickListener(View.OnClickListener rightOnClickListener) {
        this.rightOnClickListener = rightOnClickListener;
    }

    private UpdateDialog(Context instance) {
        super(instance, R.style.dialog_style1);
        this.activity = instance;
    }

    private void setLeftBtnText(String leftBtnText) {
        this.leftBtnText = leftBtnText;
    }

    private void setRightBtnText(String rightBtnText) {
        this.rightBtnText = rightBtnText;
    }

    private void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    private void setTitleBackgroundColor(int titleBackgroundColor) {
        mTitleBackgroundColor = titleBackgroundColor;
    }

    private void setTitleColor(int titleColor) {
        mTitleColor = titleColor;
    }

    private void setRightTextColor(int rightTextColor) {
        mRightTextColor = rightTextColor;
    }

    public void updateProgress(int progress) {
        try {
            ((ProgressBar) progressbarPart.findViewById(R.id.progressbar)).setProgress(progress);
        } catch (Exception e) {
            Log.e(TAG, "Progress error");
            e.printStackTrace();
        }
    }

    public void showInstallView() {
        try {
            rightBtn.setVisibility(View.VISIBLE);
            splitter1.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.VISIBLE);
            leftBtn.setText(activity.getString(R.string.cancel));
            rightBtn.setText(activity.getString(R.string.install));
            progressbarPart.setVisibility(View.GONE);
            ((ProgressBar) progressbarPart.findViewById(R.id.progressbar)).setProgress(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressView() {
        try {
            rightBtn.setVisibility(View.GONE);
            splitter1.setVisibility(View.GONE);
            leftBtn.setVisibility(View.VISIBLE);
            leftBtn.setEnabled(false);
            leftBtn.setText(activity.getString(R.string.updating));
            progressbarPart.setVisibility(View.VISIBLE);
            ((ProgressBar) progressbarPart.findViewById(R.id.progressbar)).setProgress(0);
        } catch (Exception e) {
            Log.e(TAG, "Progress Error");
            e.printStackTrace();
        }
    }

    public boolean isUnforcibleInitialState(){
        return rightBtn.getVisibility() == View.VISIBLE && rightBtn.getText().toString().equals(activity.getString(R.string.update));
    }

    public void showRetryView() {
        try {
            rightBtn.setVisibility(View.VISIBLE);
            splitter1.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.VISIBLE);
            leftBtn.setEnabled(true);
            leftBtn.setText(activity.getString(R.string.cancel));
            rightBtn.setText(activity.getString(R.string.retry));
            progressbarPart.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "Retry error");
            e.printStackTrace();
        }
    }

    public void showRetryViewIsForce() {
        try {
            rightBtn.setVisibility(View.VISIBLE);
            splitter1.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.GONE);
            leftBtn.setText(activity.getString(R.string.cancel));
            rightBtn.setText(activity.getString(R.string.update));
            progressbarPart.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "showRetryViewIsForce error");
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.global_dialog_update);
        this.setCancelable(false);

        progressbarPart = findViewById(R.id.progressbar_part);

        title = (TextView)findViewById(R.id.title);
        leftBtn = (TextView)findViewById(R.id.left_btn);
        rightBtn = (TextView)findViewById(R.id.right_btn);
        updateReasonTextView = (TextView)findViewById(R.id.update_reason_text);
        splitter1 = (LinearLayout)findViewById(R.id.splitter1);

        if (isForceUpgrade) {
            splitter1.setVisibility(View.GONE);
            leftBtn.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(titleText)) {
            title.setText("");
        } else {
            title.setText(titleText);
        }
        if (TextUtils.isEmpty(updateReasonText)) {
            updateReasonTextView.setText("");
        } else {
            updateReasonTextView.setText(updateReasonText);
        }
        if (TextUtils.isEmpty(leftBtnText)) {
            leftBtn.setText("");
        } else {
            leftBtn.setText(leftBtnText);
        }
        if (TextUtils.isEmpty(rightBtnText)) {
            rightBtn.setText("");
        } else {
            rightBtn.setText(rightBtnText);
        }
        if(mTitleBackgroundColor != Integer.MAX_VALUE) {
            title.setBackgroundColor(mTitleBackgroundColor);
        }
        if(mTitleColor != Integer.MAX_VALUE) {
            title.setTextColor(mTitleColor);
        }
        if(mRightTextColor != Integer.MAX_VALUE) {
            rightBtn.setTextColor(mRightTextColor);
        }
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftOnClickListener != null) {
                    leftOnClickListener.onClick(v);
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightOnClickListener != null) {
                    rightOnClickListener.onClick(v);
                }
            }
        });
    }

    public static class Builder {

        Context context;
        View.OnClickListener leftOnClickListener, rightOnClickListener;

        public Builder(Context context){
            this.context = context;
        }

        boolean is_force_upgrade;
        String left_btn_text;
        String right_btn_text;
        String title_text;
        String update_reason_text;
        private int titleBackgroundColor = Integer.MAX_VALUE;
        private int titleColor = Integer.MAX_VALUE;

        private int rightTextColor = Integer.MAX_VALUE;

        public Builder setIsForceUpgrade(boolean is_force_upgrade) {
            this.is_force_upgrade = is_force_upgrade;
            return this;
        }

        public Builder setUpdateReasonText(String update_reason_text) {
            this.update_reason_text = update_reason_text;
            return this;
        }

        public String getLeftBtnText() {
            return left_btn_text;
        }

        public Builder setLeftBtnText(String left_btn_text) {
            this.left_btn_text = left_btn_text;
            return this;
        }

        public String getRightBtnText() {
            return right_btn_text;
        }

        public Builder setRightBtnText(String right_btn_text) {
            this.right_btn_text = right_btn_text;
            return this;
        }

        public String getTitleText() {
            return title_text;
        }

        public Builder setTitleText(String title_text) {
            this.title_text = title_text;
            return this;
        }

        public Builder setTitleBackgroundColor(int titleBackgroundColor) {
            this.titleBackgroundColor = titleBackgroundColor;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setRightTextColor(int rightTextColor) {
            this.rightTextColor = rightTextColor;
            return this;
        }

        public View.OnClickListener getLeftOnClickListener() {
            return leftOnClickListener;
        }

        public Builder setLeftOnClickListener(View.OnClickListener leftOnClickListener) {
            this.leftOnClickListener = leftOnClickListener;
            return this;
        }

        public View.OnClickListener getRightOnClickListener() {
            return rightOnClickListener;
        }

        public Builder setRightOnClickListener(View.OnClickListener rightOnClickListener) {
            this.rightOnClickListener = rightOnClickListener;
            return this;
        }

        public UpdateDialog build(){
            UpdateDialog notifyDialog = new UpdateDialog(context);
            notifyDialog.setLeftBtnText(getLeftBtnText());
            notifyDialog.setRightBtnText(getRightBtnText());
            notifyDialog.setTitleText(getTitleText());
            notifyDialog.setLeftOnClickListener(leftOnClickListener);
            notifyDialog.setRightOnClickListener(rightOnClickListener);
            notifyDialog.setUpdateReasonText(update_reason_text);
            notifyDialog.setIsForceUpgrade(is_force_upgrade);
            if(titleBackgroundColor != Integer.MAX_VALUE) {
                notifyDialog.setTitleBackgroundColor(titleBackgroundColor);
            }
            if(titleColor != Integer.MAX_VALUE) {
                notifyDialog.setTitleColor(titleColor);
            }
            if(rightTextColor != Integer.MAX_VALUE) {
                notifyDialog.setRightTextColor(rightTextColor);
            }
            return notifyDialog;
        }

    }
}
