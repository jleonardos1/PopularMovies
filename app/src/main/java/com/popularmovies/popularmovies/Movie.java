package com.popularmovies.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by campo on 28/07/2016.
 */
public class Movie implements Parcelable {

    public String poster;
    public String originalTitle;
    public String sinopsis;
    public String userRate;
    public String releaseDate;

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie (String poster, String originalTitle, String sinopsis,
                  String userRate, String releaseDate) {
        this.poster = poster;
        this.originalTitle = originalTitle;
        this.sinopsis = sinopsis;
        this.userRate = userRate;
        this.releaseDate = releaseDate;
    }

    public Movie(Parcel in) {
        this.poster = in.readString();
        this.originalTitle = in.readString();
        this.sinopsis = in.readString();
        this.userRate = in.readString();
        this.releaseDate = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster);
        dest.writeString(this.originalTitle);
        dest.writeString(this.sinopsis);
        dest.writeString(this.userRate);
        dest.writeString(this.releaseDate);
    }
}
