package rs.reviewer.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import model.FightDTO;
import model.PokeBoss;
import model.User;
import model.UsersPokemons;
import model.UsersPokemonsDTO;
import model.UsersPokemonsDTOList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.R;
import rs.reviewer.adapters.PokemonListAdapter;
import rs.reviewer.rest.BaseService;
import rs.reviewer.utils.UserUtil;

import static android.content.ContentValues.TAG;

public class ChooseFighterActivity extends ListActivity {

    private String userId;
    private User user;
    private List<UsersPokemons> usersPokemons;
    private Uri bossId;
    private Uri fightId;
    private Uri chosenPokemonId ;
    private Uri attackCounterUri;
    private Long bossIdLong;
    private FightDTO fightDTO;
    private UsersPokemonsDTO usersPokemonsDTO;
    private double health1 = 1.0,health2 = 1.0;
    private int pokeListSize;
    private int attackCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_choose_fighter);
        userId = UserUtil.getLogInUser(getApplicationContext());
        user = new Gson().fromJson(userId, User.class);
        Call<ResponseBody> call = BaseService.userService.findByIdForPokemons(user.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String userJson = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao petlju");
                    try {
                        userJson = response.body().string();
                        UsersPokemonsDTOList usersPokemonsDTO = new Gson().fromJson(userJson, UsersPokemonsDTOList.class );
                        usersPokemons = usersPokemonsDTO.getPokemons();
                        PokemonListAdapter adapter = new PokemonListAdapter(getApplicationContext(), usersPokemons, true);
                        setListAdapter(adapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("pokes","error: "+response.code());

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: NISTAA");
            }
        });

    }




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        fightDTO = new FightDTO();
        chosenPokemonId = Uri.parse(Long.toString(id));
        Bundle extras = getIntent().getExtras();
        fightId = extras.getParcelable("fightId");
        bossId = extras.getParcelable("bossId");
        Uri attackCounter2 = extras.getParcelable("attackCounter");
        if(attackCounter2 != null ){
            attackCounter = Integer.parseInt(attackCounter2.toString());
        }
        final PokeBoss pokeBoss = new PokeBoss();
        pokeBoss.setId(Long.parseLong(bossId.toString()));
        String userId = UserUtil.getLogInUser(getApplicationContext());
        final User user = new Gson().fromJson(userId, User.class);
        final UsersPokemonsDTO usersPokemons = new UsersPokemonsDTO(Long.parseLong(chosenPokemonId.toString()));
        fightDTO.setPokemonOnMove(usersPokemons);
        fightDTO.setId(Long.parseLong(fightId.toString()));
        fightDTO.setUser(user);
        fightDTO.setBoss(pokeBoss);
        Call<ResponseBody> call2 = BaseService.userService.fight(fightDTO);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                String fight = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao petlju");
                    try {
                        fight = response.body().string();
                        FightDTO fightDTO2 = new Gson().fromJson(fight, FightDTO.class);
                        attackCounterUri = Uri.parse(String.valueOf(attackCounter));
                        Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                        intent.putExtra("id", chosenPokemonId);
                        intent.putExtra("bossId", bossId);
                        intent.putExtra("fightId", fightId);
                        intent.putExtra("pokeListSize", Uri.parse(String.valueOf(pokeListSize)));

                        startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("pokes", "error: " + response.code());

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: fail");
            }
        });







    }





}
