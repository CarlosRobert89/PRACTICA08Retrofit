package carlos.robert.a08retrofit.conexiones;

import java.util.ArrayList;

import carlos.robert.a08retrofit.modelos.Album;
import carlos.robert.a08retrofit.modelos.Photo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiConexiones {
    //Consultar todos
    @GET("/albums")
    Call<ArrayList<Album>> getAlbums();

    //Consultar todos con cierto parámetro
    @GET("/photos?")
    Call<ArrayList<Photo>> getPhotosAlbum(@Query("albumId") int almubId);

    //Consultar todos de cierto path
    @GET("/albums/{albumId}/photos")
    Call<ArrayList<Photo>> getPhotosAlbumPath(@Path("albumId") int albumId);

    //Insertar album con POST
    @POST("/albums")
    Call<Album> postAlbum(@Body Album a);

    //Insertar album con Formulario
    @FormUrlEncoded
    @POST("/albums")
    Call<Album> postAlbumForm(@Field("userId") int idUser, @Field("title") String titulo);

    @DELETE("albums/{id}")
    Call<Album> deleteAlbum(@Path("id") int idAlbum);
}
