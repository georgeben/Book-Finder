package com.kurobarabenjamingeorge.asynctaskloader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>{

    private EditText mSearchEditText;
    private TextView bookTitle, bookAuthor;
    private ProgressBar pb ;

    private static final int mBookLoaderID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks if a loader already exists, if it does, it re-associates the loader to the activity
        if(getSupportLoaderManager().getLoader(mBookLoaderID) != null){
            getSupportLoaderManager().initLoader(mBookLoaderID, null, this);
        }

        //Getting a reference to the UI Elements
        mSearchEditText = (EditText) findViewById(R.id.bookSearchQuery);
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthor = (TextView) findViewById(R.id.bookAuthor);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        //Initially, the progress bar is invisible
        pb.setVisibility(View.INVISIBLE);
    }

    public void searchBook(View view) {
        //Called when the Search button is pressed
        String searchQuery = mSearchEditText.getText().toString().trim();

        //Hide the keyboard
        InputMethodManager inMgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inMgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //Checks if the phone is connected to a network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() && searchQuery.length() != 0){
            Bundle queryBundle = new Bundle();
            queryBundle.putString("searchQuery", searchQuery);
            //Starts a new loader and with the new data inputed by the user
            getSupportLoaderManager().restartLoader(0, queryBundle, this);

            //Makes the progress bar visible
            pb.setVisibility(View.VISIBLE);
        }else if(searchQuery.length() <= 0){
            //No search term entered
            Toast.makeText(this, "Please enter a term to search", Toast.LENGTH_SHORT).show();
        }else{
            //No internet connection
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        //Creates a new book loader
        return new BookLoader(this, args.getString("searchQuery"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        //Update the UI with data fetched

        //Hide the progress bar
        pb.setVisibility(View.INVISIBLE);

        //Parse the JSON received
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
