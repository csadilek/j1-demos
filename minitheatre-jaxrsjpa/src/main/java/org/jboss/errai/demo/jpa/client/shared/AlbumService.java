package org.jboss.errai.demo.jpa.client.shared;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/albums")
public interface AlbumService {

  @GET
  @Produces("application/json")
  public List<Album> listAlbums();

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  public void createAlbum(Album album);

}
