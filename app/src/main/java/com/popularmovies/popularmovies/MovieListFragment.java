package com.popularmovies.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class MovieListFragment extends Fragment {

    private static final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private ArrayAdapter<Movie> mMovieAdapter;
    private String sort;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sort = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_movie_popular));
        new FetchMovieListTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movielist, container, false);

        // Set the adapter
        mMovieAdapter = new MovieListAdapter(getActivity(), new ArrayList<Movie>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_movie);

        listView.setAdapter(mMovieAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie item = mMovieAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(getString(R.string.entity), item);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class FetchMovieListTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPostExecute(Movie[] result) {
            if(result != null) {
                mMovieAdapter.clear();
                for(Movie movie : result) {
                    mMovieAdapter.add(movie);
                }
            }
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieList = null;
            Movie movieListStr[] = null;

            try {
                final String MOVIE_LIST_BASE_URL = "http://api.themoviedb.org/3/movie";
                final String KEY_ID = "api_key";
                Uri builder = Uri.parse(MOVIE_LIST_BASE_URL).buildUpon()
                        .appendEncodedPath(sort)
                        // TODO: 03/08/2016 put de key in build.gradle
                        .appendQueryParameter(KEY_ID, "03f5de62cb5bef08ed662678d4004fd2")
                        .build();

                URL url = new URL(builder.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0) {
                    return null;
                }

                movieList = buffer.toString();
                movieListStr = getMovieListFromJson(movieList);
            } catch (IOException ioe) {
                Log.d(LOG_TAG, "IOException");
            } catch (JSONException jse) {
                Log.d(LOG_TAG, "JSONException");
            }

            return movieListStr;
        }

        private Movie[] getMovieListFromJson(String movieListStr) throws JSONException {
            final String MOVIE_RESULTS = "results";
            final String MOVIE_POSTER = "poster_path";
            final String MOVIE_ORIGINAL_TITTLE = "original_title";
            final String MOVIE_SYNOPSIS = "overview";
            final String MOVIE_USER_RATE = "vote_average";
            final String MOVIE_RELEASE_DATE = "release_date";

            JSONObject movieListJson = new JSONObject(movieListStr);
            JSONArray movieListArray = movieListJson.getJSONArray(MOVIE_RESULTS);

            Movie[] result = new Movie [movieListArray.length()];

            for(int i = 0; i < movieListArray.length(); i++) {

                JSONObject movieString = movieListArray.getJSONObject(i);

                String poster = movieString.getString(MOVIE_POSTER);
                String originalTittle = movieString.getString(MOVIE_ORIGINAL_TITTLE);
                String synopsis = movieString.getString(MOVIE_SYNOPSIS);
                String userRate = movieString.getString(MOVIE_USER_RATE);
                String releaseDate = movieString.getString(MOVIE_RELEASE_DATE);

                 result[i] = new Movie(poster, originalTittle, synopsis, userRate, releaseDate);
            }
            return result;
        }
    }
}
