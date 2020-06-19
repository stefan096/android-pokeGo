package rs.reviewer.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
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

public class CaughtPokemonActivity extends AppCompatActivity {

    private Uri id;
    private Uri bossId;
    private ImageButton x_button;
    private Button choose_fighter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_boss);
        Bundle extras = getIntent().getExtras();
        id = extras.getParcelable("id");
        bossId = extras.getParcelable("bossId");
        fillData(Long.parseLong(bossId.toString()));
        setUpCloseButton(x_button);
        setUpChooseFButton(choose_fighter);

        //posalji backu id bossa
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("NEVENA","NEVENA: "+ "USAO");
        Log.e("NEVENA","NEVENA: "+ Long.parseLong(bossId.toString()));

        Call<ResponseBody> call = BaseService.userService.updateBoss(Long.parseLong(bossId.toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String boss = null;
                if (response.code() == 200) {
                    Log.d("ASD", "Usao u petlju(boss)");
                    //to do nekako azurirati mapu

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


        text.setText(R.string.won_long);
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

    public void setUpChooseFButton(Button choose_fighter){
        choose_fighter = findViewById(R.id.btn_choose_fighter);
        choose_fighter.setText(R.string.won_btn);
        choose_fighter.setBackgroundColor(Color.TRANSPARENT);
        choose_fighter.setTextColor(Color.rgb(0,128,0));


    }

    public void setUpCloseButton(ImageButton imageButton){
        imageButton = findViewById(R.id.btn_close);
        imageButton.setImageResource(R.drawable.ic_action_close);
        imageButton.setOnClickListener( new View.OnClickListener() {

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
