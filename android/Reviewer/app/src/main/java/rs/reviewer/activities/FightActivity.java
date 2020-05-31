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
import com.squareup.picasso.Picasso;

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
import static android.os.SystemClock.sleep;

public class FightActivity extends AppCompatActivity {

    private Uri id;
    private Uri bossId;
    private Uri fightId;
    private int attackCounter;
    private UsersPokemonsDTO usersPokemonsDTO;
    private PokeBoss pokeBoss;
    private Pokemon pokemonBoss, pokemonUser;
    private FightDTO fightDTO;
    private double healthBoss, healthUser;
    private String attackTurn;
    private int counterForTurn;
    private boolean firstTime;
    private Uri atkCounter;
    private Uri pokeListSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.two_fighters);
        Bundle extras = getIntent().getExtras();

        id = extras.getParcelable("id"); //chosen pokemon id
        bossId = extras.getParcelable("bossId");
        fightId = extras.getParcelable("fightId");
        pokeListSize = extras.getParcelable("pokeListSize");
        atkCounter = extras.getParcelable("attackCounter");
        attackCounter = Integer.valueOf(atkCounter.toString());

        getBoss(Long.parseLong(bossId.toString()), false);
        getPokemon(Long.parseLong(id.toString()), false);

    }


    private void getBoss(Long bossId, boolean firstTime) {
        final boolean tempFirstTime = firstTime;

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
                        setUpScreen1(pokeBoss, tempFirstTime);

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
    private void getPokemon(Long id, boolean firstTime) {
        final boolean tempFirstTime = firstTime;

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
                        setUpScreen2(usersPokemonsDTO, tempFirstTime);
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

    public void setUpScreen1(PokeBoss pokemon1, boolean firstTime){
        TextView name1 = findViewById(R.id.pokemon_name);
        TextView hp1 = findViewById(R.id.hp);
        TextView hp_text1 = findViewById(R.id.hp_text);
        ImageView imageView1 = findViewById(R.id.item_image);
        Pokemon pokemon = pokemon1.getPokemon();

        name1.setText(pokemon.getName());
        hp_text1.setText(R.string.hp);
        if(firstTime){
            hp1.setText(Double.toString(pokemon1.getPokemon().getHp()));
        }
        else{
            hp1.setText(Double.toString(pokemon1.getFightHealt()));
        }
        Picasso.get()
                .load(pokemon.getImage_path())
                .into(imageView1);


    }

    public void setUpScreen2(UsersPokemonsDTO pokemon2, boolean firstTime){
        TextView name2 = findViewById(R.id.pokemon_name2);
        TextView hp2 = findViewById(R.id.hp2);
        TextView hp_text2 = findViewById(R.id.hp_text2);
        ImageView imageView2 = findViewById(R.id.item_image2);

        name2.setText(pokemon2.getPokemon().getName());
        hp_text2.setText(R.string.hp);

        if(firstTime){
            hp2.setText(Double.toString(pokemon2.getPokemon().getHp()));
        }
        else{
            hp2.setText(Double.toString(pokemon2.getFightHealt()));
        }

        Picasso.get()
                .load(pokemon2.getPokemon().getImage_path())
                .into(imageView2);

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

    public void updateMove(String attacks){
        TextView onTheMove = findViewById(R.id.onTheMove);

        if (attacks.equals("BOSS_ATTACKS")) {
            onTheMove.setText(R.string.boss_atks);
        } else {
            onTheMove.setText(R.string.use_atks);
        }
    }

    public void updateCounterForTurn(int counter){
        TextView counterForTurn = findViewById(R.id.counterForTurn);
        counterForTurn.setText("[" + counter +  "]");
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

                        attackTurn = fightDTO.getFightStateMove();
                        counterForTurn = fightDTO.getCounterForTurn();
                        healthBoss = fightDTO.getBoss().getFightHealt();
                        healthUser = fightDTO.getPokemonOnMove().getFightHealt();

                        while (healthBoss > 0 && healthUser > 0) {
                            updateHealth(healthBoss,healthUser);
                            updateMove(attackTurn);
                            updateCounterForTurn(counterForTurn);

                            sleep(2000);
                            callMethod();

                            break;

                        }

                        if ( healthUser > 0 && healthBoss <=0 ) {

                            Intent intent = new Intent(getApplicationContext(), CaughtPokemonActivity.class);
                            setCooldownPokemon(Long.parseLong(id.toString()));

                            intent.putExtra("bossId", bossId);
                            intent.putExtra("id", id);

                            startActivity(intent);

                        }else if (healthBoss > 0 && healthUser <= 0) {
                            Intent intent = new Intent(getApplicationContext(), LostFightActivity.class);
                            setCooldownPokemon(Long.parseLong(id.toString()));

                            intent.putExtra("bossId", bossId);
                            intent.putExtra("id", id);
                            intent.putExtra("fightId", fightId);
                            intent.putExtra("pokeListSize", pokeListSize);
                            intent.putExtra("attackCounter", Uri.parse(Integer.toString(attackCounter)));

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

    public void setCooldownPokemon(Long id){
        Call<ResponseBody> call2 = BaseService.userService.cooldown(id);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                String fight = null;
                if (response.code() == 200) {

                    Log.d("cooldown", "Usao petlju");


                } else {

                    Log.d("cooldown", "error: " + response.code());

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: fail");
            }

        });
    }

}
