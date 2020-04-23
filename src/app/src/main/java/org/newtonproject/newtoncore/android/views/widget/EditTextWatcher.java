package org.newtonproject.newtoncore.android.views.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class EditTextWatcher implements TextWatcher {

    private static final String TAG = EditTextWatcher.class.getSimpleName();
    private final EditText edittext;
    private WatcherType mWatcherType;

    public EditTextWatcher(EditText editText, WatcherType watcherType) {
        this.edittext = editText;
        mWatcherType = watcherType;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        judgeNumber(edittext, s);
    }

    private void judgeNumber(EditText editText, Editable edt) {
        switch (mWatcherType) {
            case NO_SPACE:
                noSpaceWatcher(editText, edt);
                break;
            case LOWER_CASE:
                break;
            case UPPER_CASE:
                break;
        }
    }

    private void noSpaceWatcher(EditText editText, Editable editable) {
        String temp = editable.toString();
        int posDot = temp.indexOf(" "); // space position
        int index = editText.getSelectionStart(); // index.
        if (posDot >= 0) {
            editable.delete(index-1, index);
        }
    }


    public enum WatcherType {
        NO_SPACE, UPPER_CASE, LOWER_CASE;
    }
}
