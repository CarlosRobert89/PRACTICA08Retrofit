package carlos.robert.a08retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import carlos.robert.a08retrofit.adapters.PhotoAdapter;
import carlos.robert.a08retrofit.conexiones.ApiConexiones;
import carlos.robert.a08retrofit.conexiones.RetrofitObject;
import carlos.robert.a08retrofit.modelos.Photo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotosActivity extends AppCompatActivity {
    private PhotoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Photo> listaPhotos;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        recycler = findViewById(R.id.content_photos);
        listaPhotos = new ArrayList<>();
        adapter = new PhotoAdapter(this, listaPhotos, R.layout.photo_view_holder);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        //recibirá la info del id del album
        Intent intent = getIntent();
        if(intent.getExtras().getInt("ALBUM_ID")!= 0){
            doGetPhotos(intent.getExtras().getInt("ALBUM_ID"));

        }else{
            Toast.makeText(this, "ALBUM INCORRECTO", Toast.LENGTH_SHORT).show();
        }
    }

    private void doGetPhotos(int albumId) {
        Retrofit retrofit = RetrofitObject.getConexion();
        ApiConexiones api = retrofit.create(ApiConexiones.class);

       // Call<ArrayList<Photo>> getPhotos = api.getPhotosAlbum(albumId);
        Call<ArrayList<Photo>> getPhotos = api.getPhotosAlbumPath(albumId);

        getPhotos.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                if(response.code() == HttpURLConnection.HTTP_OK){
                    listaPhotos.addAll(response.body());
                    adapter.notifyItemRangeInserted(0, listaPhotos.size());
                }else{
                    Toast.makeText(PhotosActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                Toast.makeText(PhotosActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                Log.e("FAILURE", t.getLocalizedMessage());
            }
        });
    }
}