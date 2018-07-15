package com.example.vivek.musiccolors;



public class SongInfo {
    public String song,artist,uri;


    public SongInfo(String song, String artist, String uri) {
        this.song = song;
        this.artist = artist;
        this.uri = uri;

    }

    public String getSong() {
        return song;
    }

    public String getArtist() {
        return artist;
    }

    public String getUri() {
        return uri;
    }

}
