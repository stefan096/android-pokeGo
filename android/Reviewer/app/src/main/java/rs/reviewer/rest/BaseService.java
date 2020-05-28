package rs.reviewer.rest;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseService {

    //EXAMPLE: http://192.168.43.73:8080/rs.ftn.reviewer.rest/rest/proizvodi/
    //kristina 192.168.1.103
    //stefan maglic 192.168.1.6 / novi sad 192.168.1.30
    //nevena 192.168.1.103
    public static final String SERVICE_IP_ADDRESS = "192.168.1.103";
    public static final String SERVICE_PORT = "8000";
    public static final String SERVICE_BASE_URI = "api/";
    public static final String SERVICE_API_PATH = "http://" + SERVICE_IP_ADDRESS + ":" + SERVICE_PORT  + "/" +
            SERVICE_BASE_URI;

    public static final String USER_LOGIN = "user/login";
    public static final String USER_REGISTER = "user/register";
    public static final String USER_FIND_BY_ID = "user/{id}";
    public static final String USER_EDIT = "user/{id}";
    public static final String USER_FOR_POKEMONS = "usersIdPokemons/{id}";
    public static final String USERS_POKEMON = "usersPokemon/{id}";
    public static final String LAST_CAUGHT_POKEMON = "user/{id}/lastPokemonCaught";
    public static final String POKEMON_MAP = "getPokemonsOnMap";
    public static final String COOLDOWN = "cooldown";

    public static final String BOSS = "boss";
    public static final String FIGHT = "fight";



    /*
     * Ovo ce nam sluziti za debug, da vidimo da li zahtevi i odgovoru idu
     * odnosno dolaze i kako izgeldaju.
     * */
    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    /*
     * Prvo je potrebno da definisemo retrofit instancu preko koje ce komunikacija ici
     * */
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    /*
     * Definisemo konkretnu instancu servisa na intnerntu sa kojim
     * vrsimo komunikaciju
     * */
    public static UserService userService = retrofit.create(UserService.class);
}