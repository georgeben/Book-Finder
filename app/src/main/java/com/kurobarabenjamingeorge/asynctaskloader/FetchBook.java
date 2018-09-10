package com.kurobarabenjamingeorge.asynctaskloader;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kurobarabenjamingeorge.asynctaskloader.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        super.onPreExecute();
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pb.setVisibility(View.INVISIBLE);

        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject currentBook = jsonArray.getJSONObject(i);
                String title = null;
                String author = null;

                JSONObject volumesInfo = currentBook.getJSONObject("volumeInfo");
                try{
                    title = volumesInfo.getString("title");
                    author = volumesInfo.getString("authors");
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(title != null && author != null){
                    bookTitle.setText(title);
                    bookAuthor.setText(author);

                    return;
                }
            }

            bookTitle.setText("No results found");
            bookAuthor.setText("");

        } catch (Exception e) {
            bookTitle.setText("No results found");
            bookAuthor.setText("");
            e.printStackTrace();
        }
    }
}
