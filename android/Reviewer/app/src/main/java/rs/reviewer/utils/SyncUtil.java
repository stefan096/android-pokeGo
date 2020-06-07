package rs.reviewer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import model.PokeBoss;
import model.PokeBossList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.database.DatabaseHelper;
import rs.reviewer.database.PokeBossSQLiteHelper;
import rs.reviewer.rest.BaseService;

import static android.content.ContentValues.TAG;

public class SyncUtil {

    public void readFromDbPokemons(Context context){
        PokeBossSQLiteHelper dbHelper = new PokeBossSQLiteHelper(context);
        // Gets the data repository in write mode
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        //db.execSQL("DROP TABLE IF EXISTS " + PokeBossSQLiteHelper.TABLE_POKEBOSS);
        db.execSQL(PokeBossSQLiteHelper.DB_CREATE);

        DatabaseHelper.printTableData(PokeBossSQLiteHelper.TABLE_POKEBOSS, db);
        PokeBossList pokeBossList = DatabaseHelper.readTableData(PokeBossSQLiteHelper.TABLE_POKEBOSS, db);
        Log.d("STEFAN","count: " + pokeBossList.getPokemonBosses().size());

        //treba da se odradi punjenje baze samo ako je prazna
        if(pokeBossList.getPokemonBosses().size() != 0){
            return;
        }


        Call<ResponseBody> call = BaseService.userService.getPokemonMap(/*currentLocation.getLatitude(),
                currentLocation.getLongitude()*/ 0, 0);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseJson = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao petlju");
                    try {
                        responseJson = response.body().string();
                        PokeBossList bosses = new Gson().fromJson(responseJson, PokeBossList.class);
                        List<PokeBoss> pokemons = bosses.getPokemonBosses();

                        for(PokeBoss boss: pokemons) {

                            // Create a new map of values, where column names are the keys
                            ContentValues values = new ContentValues();
                            values.put(PokeBossSQLiteHelper.COLUMN_ID, boss.getId());
                            values.put(PokeBossSQLiteHelper.COLUMN_NAME, boss.getPokemon().getName());
                            values.put(PokeBossSQLiteHelper.COLUMN_LATITUDE, boss.getLatitude());
                            values.put(PokeBossSQLiteHelper.COLUMN_LONGITUDE, boss.getLongitude());
                            values.put(PokeBossSQLiteHelper.COLUMN_LEVEL, boss.getLevel());
                            values.put(PokeBossSQLiteHelper.COLUMN_FIGHT_HEALT, boss.getFightHealt());
                            values.put(PokeBossSQLiteHelper.COLUMN_IMAGE_PATH, boss.getPokemon().getImage_path());

                            long id = db.replace(PokeBossSQLiteHelper.TABLE_POKEBOSS, null, values);

                            Log.d("STEFAN","replacing: ");
                        }

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
}
