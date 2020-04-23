package org.newtonproject.newtoncore.android.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.data.entity.common.ErrorEnvelope;
import org.newtonproject.newtoncore.android.data.entity.common.ServiceException;

import io.reactivex.disposables.Disposable;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class BaseViewModel extends ViewModel {

	protected final MutableLiveData<ErrorEnvelope> error = new MutableLiveData<>();
	protected final MutableLiveData<Boolean> progress = new MutableLiveData<>();
	protected MutableLiveData<Throwable> onCommonError = new MutableLiveData<>();
	protected Disposable disposable;

	@Override
	public void onCleared() {
		cancel();
	}

	public void disposableSelf(Disposable disposable) {
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}

	protected void cancel() {
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}

	public LiveData<Throwable> onCommonError() { return onCommonError; }

	public LiveData<ErrorEnvelope> error() {
		return error;
	}

	public LiveData<Boolean> progress() {
		return progress;
	}

	protected void onError(Throwable throwable) {
		progress.postValue(false);
		if (throwable instanceof ServiceException) {
			error.postValue(((ServiceException) throwable).error);
		} else {
			error.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null, throwable));
			// TODO: Add dialog with offer send error log to developers: notify about error.
			Log.d("SESSION", "Err", throwable);
		}
	}
}
