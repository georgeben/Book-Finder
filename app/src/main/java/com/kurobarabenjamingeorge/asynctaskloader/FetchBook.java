package com.kurobarabenjamingeorge.asynctaskloader;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kurobarabenjamingeorge.asynctaskloader.Utils.NetworkUtils;

/**
 * Created by George Benjamin on 9/10/2018.
 */

public class FetchBook extends AsyncTask<String, Void, String> {

    //Get a reference to the textviews to display the result of te search
    private TextView bookTitle, bookAuthor;
    private ProgressBar pb;

    public FetchBook(TextView bookTitle, TextView bookAuthor, ProgressBar pb){
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.pb = pb;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]) ;
    }

    @Override
    protected void onPostExecute(String s) {
        pb.setVisibility(View.INVISIBLE);
    }
}
