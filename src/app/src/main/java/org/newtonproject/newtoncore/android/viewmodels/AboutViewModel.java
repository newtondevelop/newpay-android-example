package org.newtonproject.newtoncore.android.viewmodels;

import org.newtonproject.newtoncore.android.data.repository.PreferenceRepositoryType;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AboutViewModel extends BaseViewModel {

    AboutViewModel(PreferenceRepositoryType preferenceRepositoryType) {
    }

    @Override
    public void onCleared() {
        disposableSelf(disposable);
        super.onCleared();
    }
}
