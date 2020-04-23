package org.newtonproject.newtoncore.android.widget;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.data.service.FingerprintUiHelper;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/11/14--8:05 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class FingerprintAuthenticationDialogFragment extends DialogFragment {

    private FingerprintUiHelper fingerprintUiHelper;
    private String TAG = "FingerDialogFragment";
    private FingerprintUiHelper.Callback fingerprintUiHelperCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(fingerprintUiHelper != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fingerprintUiHelper.startListening(null);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(fingerprintUiHelper != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fingerprintUiHelper.stopListening();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.use_fingerprint_to_authenticate_title);
        View view = inflater.inflate(R.layout.dialog_fingerprint_container, container, false);
        fingerprintUiHelper = new FingerprintUiHelper.FingerprintUiHelperBuilder(FingerprintManagerCompat.from(getContext()))
                .build(view.findViewById(R.id.fingerImageView), view.findViewById(R.id.fingerTextView), fingerprintUiHelperCallback);
        return view;
    }

    public void setFingerprintUiHelperCallback(FingerprintUiHelper.Callback callback) {
        this.fingerprintUiHelperCallback = callback;
    }
}
