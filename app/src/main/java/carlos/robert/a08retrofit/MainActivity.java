package carlos.robert.a08retrofit;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import carlos.robert.a08retrofit.adapters.AlbumAdapter;
import carlos.robert.a08retrofit.conexiones.ApiConexiones;
import carlos.robert.a08retrofit.conexiones.RetrofitObject;
import carlos.robert.a08retrofit.databinding.ActivityMainBinding;
import carlos.robert.a08retrofit.modelos.Album;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Album> listaAlbums;
    private AlbumAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaAlbums = new ArrayList<>();
        adapter = new AlbumAdapter(listaAlbums, R.layout.album_view_holder, this);
        layoutManager = new LinearLayoutManager(this);

        //pasarlo al recyclerView
        binding.contenedor.contentMainAlbums.setAdapter(adapter);
        binding.contenedor.contentMainAlbums.setLayoutManager(layoutManager);

        doGetAlbums();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarAlbum().show();
            }
        });
    }

    private AlertDialog insertarAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CREAR ALBUM");
        builder.setCancelable(false);

        View albumViewModel = LayoutInflater.from(this).inflate(R.layout.album_view_model, null);
        EditText txtUsuario = albumViewModel.findViewById(R.id.txtUsuarioAlbumViewModel);
        EditText txtTitulo = albumViewModel.findViewById(R.id.txtTituloAlbumViewModel);
        builder.setView(albumViewModel);

        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("INSERTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(txtUsuario.getText().toString().isEmpty()
                || txtTitulo.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }else{
                    Album a = new Album();
                    a.setUserId(Integer.parseInt(txtUsuario.getText().toString()));
                    a.setTitulo(txtTitulo.getText().toString());
                  //  doPostAlbum(a);
                    doPostAlbumForm(Integer.parseInt(txtUsuario.getText().toString()),
                            txtTitulo.getText().toString());
                }
            }
        });
        return builder.create();
    }

    private void doPostAlbumForm(int userId, String title) {
        Retrofit retrofit = RetrofitObject.getConexion();
        ApiConexiones api = retrofit.create(ApiConexiones.class);

        Call<Album> postAlbum = api.postAlbumForm(userId,title);

        postAlbum.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.code() == HttpsURLConnection.HTTP_CREATED){
                    listaAlbums.add(0, new Album(
                            userId, response.body().getId(),title));
                    adapter.notifyItemInserted(0);
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {

            }
        });
    }

    private void doPostAlbum(Album a) {
        Retrofit retrofit = RetrofitObject.getConexion();
        ApiConexiones api = retrofit.create(ApiConexiones.class);

        Call<Album> postAlbum = api.postAlbum(a);

        postAlbum.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.code() == HttpsURLConnection.HTTP_CREATED){
                    listaAlbums.add(0, response.body());
                    adapter.notifyItemInserted(0);
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {

            }
        });
    }

    private void doGetAlbums() {
        //conectamos con la api
        Retrofit retrofit = RetrofitObject.getConexion();
        //la conexión utilizara la interfaz ApiConexiones
        ApiConexiones api = retrofit.create(ApiConexiones.class);
        //traemos la info (albums) de la API
        Call<ArrayList<Album>> getAlbums = api.getAlbums();

        //hacemos la llamada a la API
        getAlbums.enqueue(new Callback<ArrayList<Album>>() {
            @Override
            public void onResponse(Call<ArrayList<Album>> call, Response<ArrayList<Album>> response) {
                //si hay respuesta
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    ArrayList<Album> temp = response.body();
                    listaAlbums.addAll(temp);
                    adapter.notifyItemRangeInserted(0, listaAlbums.size());
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Album>> call, Throwable t) {
                //si no hay respuesta
                Toast.makeText(MainActivity.this, "NO HAY INTERNET", Toast.LENGTH_SHORT).show();
                Log.e("FAILURE", t.getLocalizedMessage());
            }
        });
    }
}