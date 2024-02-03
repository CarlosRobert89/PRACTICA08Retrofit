package carlos.robert.a08retrofit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import carlos.robert.a08retrofit.R;
import carlos.robert.a08retrofit.modelos.Photo;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoVH> {
    private Context context;
    private List<Photo> objects;
    private int resources;

    public PhotoAdapter(Context context, List<Photo> objects, int resources) {
        this.context = context;
        this.objects = objects;
        this.resources = resources;
    }

    @NonNull
    @Override
    public PhotoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoVH(LayoutInflater.from(context).inflate(resources,null));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoVH holder, int position) {
        Photo p = objects.get(position);

        holder.lbTitulo.setText(p.getTitle());
        //importamos libreria picasso (com.squareup.picasso)
        Picasso.get()
                .load(p.getUrl()) //cargar el texto
                .placeholder(R.drawable.ic_launcher_foreground)//mientras carga saldrá esta imagen
                .error(R.drawable.ic_launcher_background)//si hay un error pondrá esta
                .into(holder.imgPhoto);//lo guardará dentro de imgPhoto
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class PhotoVH extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView lbTitulo;
        public PhotoVH(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.imgImagenPhotoCard);
            lbTitulo = itemView.findViewById(R.id.lbTituloPhotoCard);
        }
    }
}
