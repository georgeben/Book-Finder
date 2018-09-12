package com.kurobarabenjamingeorge.asynctaskloader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.kurobarabenjamingeorge.asynctaskloader.Utils.NetworkUtils;

/**
 * Created by George Benjamin on 9/10/2018.
 */

public class BookLoader extends AsyncTaskLoader<String> {
    private String mQueryString;
    public BookLoader(Context context, String queryString) {
        super(context);
        this.mQueryString = queryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(mQueryString);
    }
}
