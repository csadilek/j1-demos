package org.jboss.errai.demo.jpa.server;

import java.util.List;

import org.jboss.errai.demo.jpa.client.shared.Album;
import org.jboss.errai.demo.jpa.client.shared.AlbumService;

public class AlbumServiceImpl implements AlbumService {

  @Override
  public List<Album> listAlbums() {
    return DataStore.getAlbums();
  }

  @Override
  public void createAlbum(Album album) {
    DataStore.createAlbum(album);
  }

}
