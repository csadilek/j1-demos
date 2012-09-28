package org.jboss.errai.demo.jpa.client.local;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jboss.errai.demo.jpa.client.shared.Album;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class Main {

  private List<Album> albums = new ArrayList<Album>();
  
  private AlbumTable albumsWidget = new AlbumTable();
  private Button resetEverythingButton = new Button("Clear local storage");
  private Button newAlbumButton = new Button("New Album...");
  private Button loadDataFromServerButton = new Button("Get data from server");

  @PostConstruct
  public void init() {

    albumsWidget.setDeleteHandler(new RowOperationHandler<Album>() {
      @Override
      public void handle(Album a) {
        albums.remove(a);
        refreshUI();
      }
    });

    albumsWidget.setEditHandler(new RowOperationHandler<Album>() {
      @Override
      public void handle(Album a) {
        AlbumForm af = new AlbumForm(a, null);
        final PopupPanel pp = new PopupPanel(true, true);
        af.setSaveHandler(new RowOperationHandler<Album>() {
          @Override
          public void handle(Album album) {
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
        albums.clear();
        refreshUI();
      }
    });

    loadDataFromServerButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Window.alert("Nothing happens.");
      }
    });

    newAlbumButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        AlbumForm af = new AlbumForm(new Album(), null);
        final PopupPanel pp = new PopupPanel(true, true);
        af.setSaveHandler(new RowOperationHandler<Album>() {
          @Override
          public void handle(Album album) {
            albums.add(album);
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
    albumsWidget.removeAllRows();
    albumsWidget.addAll(albums);
  }

}
