package rs.reviewer.rest;

import model.FightDTO;
import model.GenerateGeoDataDTO;
import model.LoginDTO;
import model.PokeBoss;
import model.User;
import model.UsersPokemons;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Query;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by skapl on 09-May-17.
 */
/*
 * Klasa koja mapira putanju servisa
 * opisuje koji metod koristimo ali i sta ocekujemo kao rezultat
 * */
public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST(BaseService.USER_LOGIN)
    Call<ResponseBody> login(@Body LoginDTO loginDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST(BaseService.USER_REGISTER)
    Call<ResponseBody> register(@Body User user);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(BaseService.USER_FIND_BY_ID)
    Call<ResponseBody> findById(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(BaseService.USER_EDIT)
    Call<ResponseBody> editUser(@Path("id") Long id, @Body User user);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(BaseService.USER_FOR_POKEMONS)
    Call<ResponseBody> findByIdForPokemons(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(BaseService.USERS_POKEMON)
    Call<ResponseBody> findUsersPokemonById(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(BaseService.LAST_CAUGHT_POKEMON)
    Call<ResponseBody> getLatestCaughtPokemon(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(BaseService.POKEMON_MAP)
    Call<ResponseBody> getPokemonMap(@Query("lat") double lat, @Query("lng") double lng);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(BaseService.POKEMON_MAP_SPECIFIC)
    Call<ResponseBody> getPokemonMapSpecific(@Body GenerateGeoDataDTO generateGeoDataDTO);

    @Headers({
        "User-Agent: Mobile-Android",
                "Content-Type:application/json"
    })
    @GET(BaseService.BOSS)
    Call<ResponseBody> getBossById(@Query("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @PUT(BaseService.FIGHT)
    Call<ResponseBody> fight(@Body FightDTO fightDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(BaseService.COOLDOWN)
    Call<ResponseBody> cooldown(@Query("id") Long id);
}