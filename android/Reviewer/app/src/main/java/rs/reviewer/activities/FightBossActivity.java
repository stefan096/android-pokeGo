package rs.reviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import model.FightDTO;
import model.PokeBoss;
import model.User;
import model.UsersPokemonsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;
import rs.reviewer.utils.UserUtil;

import static android.content.ContentValues.TAG;

public class FightBossActivity extends AppCompatActivity {

    private Uri bossId;
    private Uri fightId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_boss);
        Bundle extras = getIntent().getExtras();
        bossId = extras.getParcelable("bossId");
        fillData(Long.parseLong(bossId.toString()));
        setUpCloseButton();
        setUpChooseFButton();
    }

    private void fillData(Long id) {
        Call<ResponseBody> call = BaseService.userService.getBossById(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String boss = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao u petlju(boss)");
                    try {
                        boss = response.body().string();
                        PokeBoss pokeBoss = new Gson().fromJson(boss, PokeBoss.class );
                        setUpScreen(pokeBoss);

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

    public void setUpCloseButton(){
        ImageButton imageButton = findViewById(R.id.btn_close);
        imageButton.setImageResource(R.drawable.ic_action_close);
        imageButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    public void setUpChooseFButton( ){
        Button choose_fighter = findViewById(R.id.btn_choose_fighter);
        choose_fighter.setText(R.string.btn_choose_f);

        choose_fighter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PokeBoss pokeBoss = new PokeBoss();
                pokeBoss.setId(Long.parseLong(bossId.toString()));
                String userId = UserUtil.getLogInUser(getApplicationContext());
                User user = new Gson().fromJson(userId, User.class);
                FightDTO fightDTO = new FightDTO();
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
                                FightDTO fightDTO = new Gson().fromJson(fight, FightDTO.class);
                                fightId = Uri.parse(Long.toString(fightDTO.getId()));
                                Intent chooseFighter = new Intent(getApplicationContext(), ChooseFighterActivity.class);
                                chooseFighter.putExtra("bossId", bossId);
                                chooseFighter.putExtra("fightId", fightId);
                                startActivity(chooseFighter);

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
        });

    }


    public void setUpScreen(PokeBoss pokeBoss){
        TextView name = findViewById(R.id.pokemon_name);
        TextView attack = findViewById(R.id.atk);
        TextView defense = findViewById(R.id.defense);
        TextView hp = findViewById(R.id.hp);
        ImageView imageView = findViewById(R.id.item_image);
        TextView atk_text =  findViewById(R.id.atk_text);
        TextView defense_text =  findViewById(R.id.defense_text);
        TextView hp_text =  findViewById(R.id.hp_text);
        TextView level_text = findViewById(R.id.level_text);
        TextView level = findViewById(R.id.level);

        hp_text.setText(R.string.hp);
        atk_text.setText(R.string.atk);
        defense_text.setText(R.string.defense);
        level_text.setText(R.string.lvl);
        name.setText(pokeBoss.getPokemon().getName());
        attack.setText(Double.toString(pokeBoss.getPokemon().getAtk()));
        hp.setText(Double.toString(pokeBoss.getPokemon().getHp()));
        defense.setText(Double.toString(pokeBoss.getPokemon().getDefense()));
        level.setText(Double.toString(pokeBoss.getLevel()));

        Picasso.get()
                .load(pokeBoss.getPokemon().getImage_path())
                .into(imageView);

    }








}
