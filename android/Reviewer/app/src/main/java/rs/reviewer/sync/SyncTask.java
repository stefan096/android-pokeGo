package rs.reviewer.sync;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import model.GenerateGeoDataDTO;
import model.Geopoint;
import model.PokeBoss;
import model.PokeBossList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.database.DatabaseHelper;
import rs.reviewer.database.PokeBossSQLiteHelper;
import rs.reviewer.location.LocationTask;
import rs.reviewer.rest.BaseService;
import rs.reviewer.tools.ReviewerTools;

import static android.content.ContentValues.TAG;

/**
 * Created by milossimic on 4/6/16.
 */
public class SyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;


    public static String RESULT_CODE = "RESULT_CODE";

    public SyncTask(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        //postaviti parametre, pre pokretanja zadatka ako je potrebno
    }


    @Override
    protected Void doInBackground(Void... params) {

        //simulacija posla koji se obavlja u pozadini i traje duze vreme
        //Toast.makeText(this.context, R.string.wrong_credentials, Toast.LENGTH_LONG).show();

        Log.d("SYNC","sync happened: " );
        try {

            double kilometres = 100;
            Log.e("STEFAN km","kilometres: " + kilometres);


            PokeBossSQLiteHelper dbHelper = new PokeBossSQLiteHelper(context);
            // Gets the data repository in write mode
            final SQLiteDatabase db = dbHelper.getWritableDatabase();

            //obrisi sve podatke
            db.execSQL("DELETE FROM " + PokeBossSQLiteHelper.TABLE_POKEBOSS);

            PokeBossList pokeBossList = DatabaseHelper.readTableData(PokeBossSQLiteHelper.TABLE_POKEBOSS, db);
            Log.d("STEFAN BRISANJE","BRISANJE count: " + pokeBossList.getPokemonBosses().size());

            final GenerateGeoDataDTO generated = this.populateCoordinate(kilometres);
            Call<ResponseBody> call = BaseService.userService.getPokemonMapSpecific(generated);
            final Context context = this.context;
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

                                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                                Criteria criteria = new Criteria();
                                String provider = locationManager.getBestProvider(criteria, true);
                                Location location = locationManager.getLastKnownLocation(provider);
                                LocationTask task = new LocationTask(context, location);
                                task.execute();

                                Log.d("STEFAN","replacing: SYNC");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SecurityException e){
                            e.printStackTrace();
                        }
                    }else{
                        Log.d("pokes","error: "+response.code());

                    }
                    db.close();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailure: NISTAA");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void result) {

        Intent ints = new Intent(MainActivity.SYNC_DATA);
        int status = ReviewerTools.getConnectivityStatus(context);
        ints.putExtra(RESULT_CODE, status);
        context.sendBroadcast(ints);
    }

    private GenerateGeoDataDTO populateCoordinate(double kilometres){
        GenerateGeoDataDTO generatedDTO = new GenerateGeoDataDTO();
        generatedDTO.setRadius(kilometres*1000);
        generatedDTO.setNumberOfData(100);

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(provider);
            Log.e("LOCATION", "lng: " + location.getLongitude());
            Log.e("LOCATION", "lat: " + location.getLatitude());
        }
        catch (SecurityException e){

        }

        Geopoint geopoint = new Geopoint();
        if(location == null){
            geopoint.setLatitude(45.3622);
            geopoint.setLongitude(19.5317);
        }
        else{
            geopoint.setLatitude(location.getLatitude());
            geopoint.setLongitude(location.getLongitude());
        }
        generatedDTO.setGeopoint(geopoint);

        return generatedDTO;
    }
}
