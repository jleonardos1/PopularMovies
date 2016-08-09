package com.popularmovies.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Locale;

public class DetailActivity extends ActionBarActivity {

    private static final String THUMBNAIL_PATH = "http://image.tmdb.org/t/p/w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    public static class DetailFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            Movie movie = null;

            if(intent != null && intent.hasExtra("movie")) {
                movie = intent.getParcelableExtra("movie");
            }
            TextView textView1 = (TextView) rootView.findViewById(R.id.movie_tittle);
            textView1.setText(movie.originalTitle);

            TextView textView2 = (TextView) rootView.findViewById(R.id.user_rating);
            textView2.setText("User Reting: " + movie.userRate);

            DateFormat df = DateFormat.getDateInstance();


            TextView textView3 = (TextView) rootView.findViewById(R.id.release_date);
            textView3.setText("Release Date: " + movie.releaseDate);

            TextView textView4 = (TextView) rootView.findViewById(R.id.sinopsis);
            textView4.setText(movie.sinopsis);

            Uri builder = Uri.parse(THUMBNAIL_PATH).buildUpon()
                    .appendEncodedPath(movie.poster)
                    .build();

            ImageView iconView = (ImageView) rootView.findViewById(R.id.movie_thumbnail);

            Picasso.with(getContext()).load(builder.toString()).into(iconView);

            return rootView;
        }
    }
}
