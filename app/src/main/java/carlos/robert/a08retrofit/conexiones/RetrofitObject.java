package carlos.robert.a08retrofit.conexiones;

import carlos.robert.a08retrofit.configuracion.Constantes;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitObject {
    public static Retrofit getConexion(){
        return new Retrofit.Builder()
                .baseUrl(Constantes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //utilizo la librería para la converión
                .build();
    }
}
