package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the Google books API and return a {@link List<Book>} object to represent a single book.
     */
    public static List<Book> fetchBookData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Book> extractFeatureFromJson(String json) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            // The compiler warns that (json != "") possibly always evaluates to true
            // There is an edge case where this condition evaluates to false
            if(json != null || json != ""){

                // build up a list of Book objects with the corresponding data.

                JSONObject baseJsonResponse = new JSONObject(json);
                JSONArray bookArray = baseJsonResponse.getJSONArray("items");

                for(int i = 0; i < bookArray.length(); i++){
                    JSONObject currentBook = bookArray.getJSONObject(i);
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    short pageCount = 0;
                    if(volumeInfo.has("pageCount")){
                        pageCount = (short)volumeInfo.getDouble("pageCount");
                    }

                    JSONArray authorArray = volumeInfo.getJSONArray("authors");
                    StringBuilder authorSB = new StringBuilder();
                    // if multiple authors, append a comma plus author name
                    for(int j = 0; j < authorArray.length(); j++){
                        if(j == 0){
                            authorSB.append(authorArray.getString(j));
                        }
                        else if(j > 0){
                            authorSB.append(", ").append(authorArray.getString(j));
                        }
                    }

                    Book book = new Book(title, authorSB.toString(), pageCount);
                    books.add(book);
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Google Books API JSON results", e);
        }

        return books;

    }

}