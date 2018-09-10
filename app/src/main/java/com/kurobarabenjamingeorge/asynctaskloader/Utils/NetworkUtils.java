package com.kurobarabenjamingeorge.asynctaskloader.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by George Benjamin on 9/10/2018.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URI = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";

    public static String getBookInfo(String queryString){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try{
            Uri builtUri = Uri.parse(BASE_URI).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "5")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL requestUrl = new URL(builtUri.toString());

            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");

            }

            if(buffer.length() == 0){
                return  null;
            }

            bookJSONString = buffer.toString();

            Log.i("Books result", bookJSONString);

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            if(connection != null){
                connection.disconnect();
            }

            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        return bookJSONString;
    }
}
