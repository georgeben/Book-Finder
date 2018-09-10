package com.kurobarabenjamingeorge.asynctaskloader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private EditText bookSearchQuery;
    private TextView bookTitle, bookAuthor;
    private ProgressBar pb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            new FetchBook(bookTitle, bookAuthor, pb).execute(searchQuery);
        }else if(searchQuery.length() <= 0){
            Toast.makeText(this, "Please enter a term to search", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        
    }
}
