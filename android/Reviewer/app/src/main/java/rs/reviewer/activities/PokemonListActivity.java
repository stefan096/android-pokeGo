package rs.reviewer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Pokemon;
import model.UsersPokemons;
import model.UsersPokemonsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;
import rs.reviewer.utils.UserUtil;

public class PokemonListActivity extends Activity {
    private List<Pokemon> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_list);

        TextView atk = findViewById(R.id.atk_text);
        TextView defense = findViewById(R.id.defense_text);
        TextView hp = findViewById(R.id.hp_text);

        atk.setText(R.string.atk);
        defense.setText(R.string.defense);
        hp.setText(R.string.hp);

        String userId = UserUtil.getLogInUser(getApplicationContext());


        Call<ResponseBody> call = BaseService.usersPokemonsService.findByUser(Long.parseLong(userId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("REZ", "Message SUCC");

                try{



                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}
