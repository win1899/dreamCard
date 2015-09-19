package com.dreamcard.app.view.interfaces;

import com.dreamcard.app.entity.ErrorMessageInfo;

/**
 * Created by Moayed on 6/21/2014.
 */
public interface IServiceListener {
    public void onServiceSuccess(Object b,int processType);
    public void onServiceFailed(ErrorMessageInfo info);
}
