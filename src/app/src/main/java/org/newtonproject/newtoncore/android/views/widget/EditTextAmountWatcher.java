package org.newtonproject.newtoncore.android.views.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class EditTextAmountWatcher implements TextWatcher {

    private static final String TAG = EditTextAmountWatcher.class.getSimpleName();
    private final EditText edittext;
    private TextView textView;
    private boolean limit = false;
    private String mSuffix;

    public EditTextAmountWatcher(EditText editText) {
        this.edittext = editText;
    }

    public void linkTextView(TextView textView, String suffix) {
        this.textView = textView;
        mSuffix = suffix;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int position = edittext.getSelectionStart();//get position of cursor
        int nbCommaBefore;
        int nbCommaAfter;
        String finalStr;
        String str = s.toString();
        nbCommaBefore = str.length() - str.replace(",", "").length();//count nb comma before the String has been formatted
        edittext.removeTextChangedListener(this);
        finalStr = formatAmount(str);
        nbCommaAfter = finalStr.length() - finalStr.replace(",", "").length();//count nb comma after the String has been formatted
        edittext.setText(finalStr);
        //algorithm to set the position of the cursor
        if (position == str.length()) {
            edittext.setSelection(finalStr.length());
        } else if (position == 0) {
            edittext.setSelection(0);
        } else if (nbCommaBefore < nbCommaAfter) {
            edittext.setSelection(position + 1);
        } else if (nbCommaAfter < nbCommaBefore) {
            edittext.setSelection(position - 1);
        } else {
            if(position > str.length()) {
                edittext.setSelection(str.length());
            } else {
                edittext.setSelection(position);
            }
        }
        edittext.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        judgeNumber(edittext, s);
    }

    private void judgeNumber(EditText editText, Editable edt) {
        String temp = edt.toString();
        int posDot = temp.indexOf(".");
        int index = editText.getSelectionStart();
        if (index > 0 && temp.length() - posDot - 1 > 18) {
            edt.delete(index-1, index);
            limit = true;
        } else {
            limit = false;
        }
        if(textView != null) {
            textView.setText(String.format("%s %s", edittext.getText(), mSuffix));
        }
    }

    private String formatAmount(String str) {
        String finalStr = str;
        String formattedStr;
        boolean containsDot = false;
        if (str.contains(".")) {
            containsDot = true;
            formattedStr = str.split("\\.")[0];
        } else {
            formattedStr = str;
        }
        if (!str.isEmpty()) {
            formattedStr = formattedStr.replace(",", "");
            formattedStr = formattedStr.replaceAll("(\\d)(?=(\\d{3})+$)", "$1,"); //format the str with a regex
            if (containsDot) {
                if (str.split("\\.").length != 1) {
                    finalStr = formattedStr + "." + str.split("\\.")[1].replace(",", "");//concatenate 2 part if there is a dot and replace comma if a dot has been added
                } else {
                    finalStr = formattedStr + ".";
                }
            } else {
                finalStr = formattedStr;
            }
        }
        return finalStr;
    }
}
