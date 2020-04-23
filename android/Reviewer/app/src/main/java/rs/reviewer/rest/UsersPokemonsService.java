package rs.reviewer.rest;

import model.LoginDTO;
import model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
public interface UsersPokemonsService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(BaseService.USERSPOKEMONSLIST)
    Call<ResponseBody> findByUser(@Path("id") Long id);



}