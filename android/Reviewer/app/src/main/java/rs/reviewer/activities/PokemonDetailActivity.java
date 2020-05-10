package rs.reviewer.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.io.IOException;

import model.UsersPokemonsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class PokemonDetailActivity extends AppCompatActivity {

   private Uri id;
   Toolbar toolbar;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.pokemon_detail);
      Bundle extras = getIntent().getExtras();
      id = extras.getParcelable("id");
      fillData(Long.parseLong(id.toString()));

   }

   private void fillData(Long id) {
      Call<ResponseBody> call = BaseService.userService.findUsersPokemonById(id);
      call.enqueue(new Callback<ResponseBody>() {
         @Override
         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            String usersPokemon = null;
            if (response.code() == 200) {
               Log.d("REZ", "Usao petlju");
               try {
                  usersPokemon = response.body().string();
                  UsersPokemonsDTO usersPokemonsDTO = new Gson().fromJson(usersPokemon, UsersPokemonsDTO.class );
                  setUpScreen(usersPokemonsDTO);

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

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            finish();

            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }

   public void setUpScreen(UsersPokemonsDTO usersPokemonsDTO){
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

      toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle(R.string.details);
      toolbar.setNavigationIcon(R.drawable.ic_action_back);
      hp_text.setText(R.string.hp);
      atk_text.setText(R.string.atk);
      defense_text.setText(R.string.defense);
      level_text.setText(R.string.lvl);
      name.setText(usersPokemonsDTO.getPokemon().getName());
      attack.setText(Double.toString(usersPokemonsDTO.getPokemon().getAtk()));
      hp.setText(Double.toString(usersPokemonsDTO.getPokemon().getHp()));
      defense.setText(Double.toString(usersPokemonsDTO.getPokemon().getDefense()));
      level.setText(Double.toString(usersPokemonsDTO.getLevel()));


      Picasso.get()
              .load(usersPokemonsDTO.getPokemon().getImage_path())
              .into(imageView);

   }

}
