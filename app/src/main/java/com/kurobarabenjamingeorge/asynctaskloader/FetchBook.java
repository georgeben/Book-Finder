package com.kurobarabenjamingeorge.asynctaskloader;

import android.os.AsyncTask;
import android.widget.TextView;

/**
 * Created by George Benjamin on 9/10/2018.
 */

public class FetchBook extends AsyncTask<String, Void, String> {

    //Get a reference to the textviews to display the result of te search
    private TextView bookTitle, bookAuthor;

    public FetchBook(TextView bookTitle, TextView bookAuthor){
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
    }
    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
