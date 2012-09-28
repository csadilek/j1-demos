package org.jboss.errai.demo.jpa.server;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.errai.demo.jpa.client.shared.Album;
import org.jboss.errai.demo.jpa.client.shared.Artist;
import org.jboss.errai.demo.jpa.client.shared.Format;
import org.jboss.errai.demo.jpa.client.shared.Genre;

public class DataStore {

  private static List<Album> albums = Collections.synchronizedList(new ArrayList<Album>());
  
  static {
    Genre rock = new Genre("Rock");
    Genre soul = new Genre("Soul");
    Genre rnb = new Genre("R&B");

    Artist beatles = new Artist();
    beatles.setName("The Beatles");
    beatles.addGenre(rock);

    Artist samNDave = new Artist();
    samNDave.setName("Sam & Dave");
    samNDave.addGenre(rock);
    samNDave.addGenre(soul);
    samNDave.addGenre(rnb);

    Album album = new Album();
    album.setArtist(beatles);
    album.setFormat(Format.LP);
    album.setName("Let It Be");
    album.setReleaseDate(new Date(11012400000L));
    albums.add(album);

    album = new Album();
    album.setArtist(beatles);
    album.setFormat(Format.LP);
    album.setName("Abbey Road");
    album.setReleaseDate(new Date(-8366400000L));
    albums.add(album);

    album = new Album();
    album.setArtist(beatles);
    album.setFormat(Format.LP);
    album.setName("Yellow Submarine");
    album.setReleaseDate(new Date(-30481200000L));
    albums.add(album);

    album = new Album();
    album.setArtist(beatles);
    album.setFormat(Format.LP);
    album.setName("The Beatles");
    album.setReleaseDate(new Date(-34974000000L));
    albums.add(album);

    album = new Album();
    album.setArtist(beatles);
    album.setFormat(Format.LP);
    album.setName("Magical Mystery Tour");
    album.setReleaseDate(new Date(-66164400000L));
    albums.add(album);

    album = new Album();
    album.setArtist(beatles);
    album.setFormat(Format.LP);
    album.setName("Sgt. Pepper's Lonely Hearts Club Band");
    album.setReleaseDate(new Date(-81633600000L));
    albums.add(album);

    album = new Album();
    album.setArtist(beatles);
    album.setFormat(Format.LP);
    album.setName("Revolver");
    album.setReleaseDate(new Date(-107553600000L));
    albums.add(album);

    album = new Album();
    album.setArtist(beatles);
    album.setFormat(Format.LP);
    album.setName("Rubber Soul");
    album.setReleaseDate(new Date(-128718000000L));
    albums.add(album);

    album = new Album();
    album.setArtist(samNDave);
    album.setFormat(Format.LP);
    album.setName("Hold On, I'm Comin'");
    album.setReleaseDate(new Date(-121114800000L));
    albums.add(album);

    album = new Album();
    album.setArtist(samNDave);
    album.setFormat(Format.LP);
    album.setName("Double Dynamite");
    album.setReleaseDate(new Date(-97354800000L));
    albums.add(album);

    album = new Album();
    album.setArtist(samNDave);
    album.setFormat(Format.LP);
    album.setName("Soul Men");
    album.setReleaseDate(new Date(-71092800000L));
    albums.add(album);
  }
  
  public static List<Album> getAlbums() {
    return albums;
  }
  
  public static void createAlbum(Album album) {
    album.setId(null);
    albums.add(album);
  }
}
