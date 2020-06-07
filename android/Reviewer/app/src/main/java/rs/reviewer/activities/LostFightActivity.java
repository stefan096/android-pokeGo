package rs.reviewer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import model.PokeBoss;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;

import static android.content.ContentValues.TAG;

public class LostFightActivity extends AppCompatActivity {

    private Uri id;
    private Uri bossId;
    private Uri fightId;
    private Uri attackCounter;
    private Uri pokeListSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_boss);
        Bundle extras = getIntent().getExtras();

        id = extras.getParcelable("id");
        bossId = extras.getParcelable("bossId");
        fightId = extras.getParcelable("fightId");
        attackCounter = extras.getParcelable("attackCounter");
        pokeListSize = extras.getParcelable("pokeListSize");

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
        TextView text = findViewById(R.id.text);


        text.setText(R.string.lost);
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

    public void setUpChooseFButton(){
        Button choose_fighter = findViewById(R.id.btn_choose_fighter);
        int atkCounter = Integer.parseInt(attackCounter.toString());
        int pokeLSize = Integer.parseInt(pokeListSize.toString());

        Log.d("Attack counter", atkCounter + "");
        if(atkCounter <= 2 && pokeLSize > 1) {
            choose_fighter.setText(R.string.lost_btn);
            choose_fighter.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent chooseFighter = new Intent(v.getContext(), ChooseFighterActivity.class);
                    chooseFighter.putExtra("bossId", bossId);
                    chooseFighter.putExtra("fightId", fightId);
                    chooseFighter.putExtra("attackCounter", attackCounter);
                    startActivity(chooseFighter);
                }
            });
        }else{
            choose_fighter.setEnabled(false);
            choose_fighter.setText(R.string.over);

        }

    }

    public void setUpCloseButton(){
        ImageButton x_button = findViewById(R.id.btn_close);
        x_button.setImageResource(R.drawable.ic_action_close);
        x_button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                Intent main = new Intent(v.getContext(), MainActivity.class);
                startActivity(main);
                finish();

            }
        });
    }


}
