package com.example.musicplayer_ui.model;

import android.net.Uri;

public class Songs {

    // Media store member variables
    long id;
    Uri uri;
    String name;
    int duration;
    long albumId;
    Uri albumArtUri;

    // constructor
    public Songs(long id, Uri uri, String name, int duration, long albumId, Uri albumArtUri) {
        this.id = id;
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.albumId = albumId;
        this.albumArtUri = albumArtUri;
    }

    // getters to get the values


    public long getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public long getAlbumId() {
        return albumId;
    }

    public Uri getAlbumArtUri() {
        return albumArtUri;
    }
}
