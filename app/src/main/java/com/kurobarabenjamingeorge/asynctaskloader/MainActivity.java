package com.kurobarabenjamingeorge.asynctaskloader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>{

    private EditText bookSearchQuery;
    private TextView bookTitle, bookAuthor;
    private ProgressBar pb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportLoaderManager().getLoader(0) != null){
            getSupportLoaderManager().initLoader(0, null, this);
        }

        bookSearchQuery = (EditText) findViewById(R.id.bookSearchQuery);
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthor = (TextView) findViewById(R.id.bookAuthor);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
    }

    public void searchBook(View view) {
        String searchQuery = bookSearchQuery.getText().toString().trim();

        //Hide the keyboard
        InputMethodManager inMgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inMgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() && searchQuery.length() != 0){
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", searchQuery);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            pb.setVisibility(View.VISIBLE);
            //new FetchBook(bookTitle, bookAuthor, pb).execute(searchQuery);
        }else if(searchQuery.length() <= 0){
            Toast.makeText(this, "Please enter a term to search", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, args.getString("queryString"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        pb.setVisibility(View.INVISIBLE);

        try{
            JSONObject jsonObject = new JSONObject(data);
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

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
