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

}