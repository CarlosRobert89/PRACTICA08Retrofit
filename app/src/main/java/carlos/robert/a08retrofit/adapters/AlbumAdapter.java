package carlos.robert.a08retrofit.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import carlos.robert.a08retrofit.PhotosActivity;
import carlos.robert.a08retrofit.R;
import carlos.robert.a08retrofit.conexiones.ApiConexiones;
import carlos.robert.a08retrofit.conexiones.RetrofitObject;
import carlos.robert.a08retrofit.modelos.Album;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumVH> {
    private List<Album> objects;
    private int resource;
    private Context context;

    public AlbumAdapter(List<Album> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View albumView = LayoutInflater.from(context).inflate(resource, null);
        albumView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new AlbumVH(albumView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumVH holder, int position) {
        Album a = objects.get(position);

        holder.lbTitulo.setText(a.getTitulo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ALBUM_ID", a.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete(a).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private AlertDialog confirmDelete(Album a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("¿Seguro que quiere borrar?");
        builder.setCancelable(false);

        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Retrofit retrofit = RetrofitObject.getConexion();
                ApiConexiones api = retrofit.create(ApiConexiones.class);

                Call<Album> deleteAlbum = api.deleteAlbum(a.getId());

                deleteAlbum.enqueue(new Callback<Album>() {
                    @Override
                    public void onResponse(Call<Album> call, Response<Album> response) {
                        if (response.code() == HttpsURLConnection.HTTP_OK) {
                            int posicion = objects.indexOf(a);
                            notifyItemRemoved(posicion);
                        }
                    }

                    @Override
                    public void onFailure(Call<Album> call, Throwable t) {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return builder.create();
    }

    public class AlbumVH extends RecyclerView.ViewHolder {
        TextView lbTitulo;
        ImageButton btnEliminar;

        public AlbumVH(@NonNull View itemView) {
            super(itemView);
            lbTitulo = itemView.findViewById(R.id.lbTituloAlbumViewHolder);
            btnEliminar = itemView.findViewById(R.id.btnEliminarAlbumCard);
        }
    }
}
