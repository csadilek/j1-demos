package org.jboss.errai.demo.jpa.client.local;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.demo.jpa.client.shared.Album;
import org.jboss.errai.demo.jpa.client.shared.AlbumService;
import org.jboss.errai.demo.jpa.client.shared.Artist;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.jpa.client.local.ErraiEntityManager;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class Main {

  @Inject
  private EntityManager em;

  @Inject
  private Caller<AlbumService> albumService;

  private AlbumTable albumsWidget = new AlbumTable();
  private Button resetEverythingButton = new Button("Clear local storage");
  private Button newAlbumButton = new Button("New Album...");
  private Button loadDataFromServerButton = new Button("Get data from server");

  @PostConstruct
  public void init() {

    albumsWidget.setDeleteHandler(new RowOperationHandler<Album>() {
      @Override
      public void handle(Album a) {
        em.remove(a);
        em.flush();
        refreshUI();
      }
    });

    albumsWidget.setEditHandler(new RowOperationHandler<Album>() {
      @Override
      public void handle(Album a) {
        AlbumForm af = new AlbumForm(a, em);
        final PopupPanel pp = new PopupPanel(true, true);
        af.setSaveHandler(new RowOperationHandler<Album>() {
          @Override
          public void handle(Album album) {
            em.flush();
            refreshUI();
            pp.hide();
          }
        });
        pp.setWidget(af);
        pp.setGlassEnabled(true);
        pp.show();
        af.grabFocus();
      }
    });

    resetEverythingButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        ((ErraiEntityManager) em).removeAll();
        refreshUI();
      }
    });

    loadDataFromServerButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        albumService.call(new RemoteCallback<List<Album>>() {
          @Override
          public void callback(List<Album> albums) {
            for (Album album : albums) {
              TypedQuery<Album> q = em.createNamedQuery("albumByName", Album.class);
              q.setParameter("name", album.getName());
              if (q.getResultList().isEmpty()) {
                em.persist(album);
              }
            }
            em.flush();
            refreshUI();
          }
        }).listAlbums();
      }
    });

    newAlbumButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        AlbumForm af = new AlbumForm(new Album(), em);
        final PopupPanel pp = new PopupPanel(true, true);
        af.setSaveHandler(new RowOperationHandler<Album>() {
          @Override
          public void handle(Album album) {
            em.persist(album);
            em.flush();
            refreshUI();
            pp.hide();
          }
        });
        pp.setWidget(af);
        pp.setGlassEnabled(true);
        pp.show();
        af.grabFocus();
      }
    });

    refreshUI();

    RootPanel.get().add(resetEverythingButton);
    RootPanel.get().add(loadDataFromServerButton);
    RootPanel.get().add(albumsWidget);
    RootPanel.get().add(newAlbumButton);
  }

  private void refreshUI() {
    TypedQuery<Album> albums = em.createNamedQuery("allAlbums", Album.class);
    albumsWidget.removeAllRows();
    albumsWidget.addAll(albums.getResultList());
  }

}
