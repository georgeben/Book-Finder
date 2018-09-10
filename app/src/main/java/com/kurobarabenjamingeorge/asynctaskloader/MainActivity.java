package com.kurobarabenjamingeorge.asynctaskloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
        String searchQuery = bookSearchQuery.getText().toString();

        new FetchBook(bookTitle, bookAuthor, pb).execute(searchQuery);
    }
}
