package com.popularmovies.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by campos on 28/07/2016.
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieListAdapter.class.getSimpleName();
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

    public MovieListAdapter(Activity context, List<Movie> movieList) {
        super(context, 0, movieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        Uri uri = null;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        Uri builder = Uri.parse(IMAGE_PATH).buildUpon()
                .appendEncodedPath(movie.poster)
                .build();

        ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_icon);

        Picasso.with(getContext()).load(builder.toString()).into(iconView);

        return convertView;
    }
 }
