package rs.reviewer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import model.FightDTO;
import model.PokeBoss;
import model.Pokemon;
import model.User;
import model.UsersPokemons;
import model.UsersPokemonsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;
import rs.reviewer.utils.UserUtil;

import static android.content.ContentValues.TAG;

public class FightActivity extends AppCompatActivity {

    private Uri id;
    private Uri bossId;
    private Uri fightId;
    private UsersPokemonsDTO usersPokemonsDTO;
    private PokeBoss pokeBoss;
    private Pokemon pokemonBoss, pokemonUser;
    private Long userId;
    private Button button;
    private FightDTO fightDTO;
    private double healthBoss, healthUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_fighters);
        Bundle extras = getIntent().getExtras();
        id = extras.getParcelable("id");
        bossId = extras.getParcelable("bossId");
        fightId = extras.getParcelable("fightId");
        getBoss(Long.parseLong(bossId.toString()));
        getPokemon(Long.parseLong(id.toString()));
    }


    private void getBoss(Long bossId) {

        Call<ResponseBody> call = BaseService.userService.getBossById(bossId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String boss = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao petlju");
                    try {
                        boss = response.body().string();
                        pokeBoss = new Gson().fromJson(boss, PokeBoss.class );
                        pokemonBoss = pokeBoss.getPokemon();
                        setUpScreen1(pokemonBoss);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("pokes","error: "+response.code());

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: fail");
            }
        });

    }
    private void getPokemon(Long id) {

        Call<ResponseBody> call = BaseService.userService.findUsersPokemonById(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String usersPokemon = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao petlju");
                    try {
                        usersPokemon = response.body().string();
                        usersPokemonsDTO = new Gson().fromJson(usersPokemon, UsersPokemonsDTO.class);
                        pokemonUser = usersPokemonsDTO.getPokemon();
                        setUpScreen2(pokemonUser);
                        startFight(usersPokemonsDTO);
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

    public void setUpScreen1(Pokemon pokemon1){
        TextView name1 = findViewById(R.id.pokemon_name);
        TextView hp1 = findViewById(R.id.hp);
        TextView hp_text1 = findViewById(R.id.hp_text);
        ImageView imageView1 = findViewById(R.id.item_image);

        name1.setText(pokemon1.getName());
        hp_text1.setText(R.string.hp);
        hp1.setText(Double.toString(pokemon1.getHp()));


    }

    public void setUpScreen2(Pokemon pokemon2){
        TextView name2 = findViewById(R.id.pokemon_name2);
        TextView hp2 = findViewById(R.id.hp2);
        TextView hp_text2 = findViewById(R.id.hp_text2);
        ImageView imageView2 = findViewById(R.id.item_image2);
        name2.setText(pokemon2.getName());
        hp_text2.setText(R.string.hp);
        hp2.setText(Double.toString(pokemon2.getHp()));

    }

    public void startFight(UsersPokemonsDTO usersPokemonsDTO){
        Long fightID = Long.parseLong(fightId.toString());
        String userId = UserUtil.getLogInUser(getApplicationContext());
        PokeBoss pokeBoss = new PokeBoss();
        pokeBoss.setId(Long.parseLong(bossId.toString()));
        fightDTO = new FightDTO();
        fightDTO.setId(fightID);
        fightDTO.setBoss(pokeBoss);
        String usrid = UserUtil.getLogInUser(getApplicationContext());
        User user = new Gson().fromJson(usrid, User.class);
        fightDTO.setUser(user);
        fightDTO.setPokemonOnMove(usersPokemonsDTO);

        callMethod();

    }

    public void updateHealth(double bossH, double userH){
        TextView hpB = findViewById(R.id.hp);
        TextView hpU = findViewById(R.id.hp2);
        hpB.setText(Double.toString(bossH));
        hpU.setText(Double.toString(userH));

    }


    public void callMethod() {
        Call<ResponseBody> call2 = BaseService.userService.fight(fightDTO);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                String fight = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao petlju");

                    try {
                        fight = response.body().string();
                        FightDTO fightDTO = new Gson().fromJson(fight, FightDTO.class);
                        healthBoss = fightDTO.getBoss().getFightHealt();
                        healthUser = fightDTO.getPokemonOnMove().getFightHealt();
                        while (healthBoss > 0 && healthUser > 0) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    updateHealth(healthBoss,healthUser);
                                }
                            }, 3000);   //2 seconds
                            callMethod();
                            break;
                        }

                        if ( healthUser > 0 ) {

                            Intent intent = new Intent(getApplicationContext(), CaughtPokemonActivity.class);
                            intent.putExtra("bossId", bossId);
                            intent.putExtra("id", id);
                            startActivity(intent);


                        }else{

                            Intent intent = new Intent(getApplicationContext(), LostFightActivity.class);
                            intent.putExtra("bossId", bossId);
                            intent.putExtra("id", id);
                            startActivity(intent);



                        }



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
