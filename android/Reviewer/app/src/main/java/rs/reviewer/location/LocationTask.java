package rs.reviewer.location;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Collections;

import model.PokeBoss;
import model.PokeBossList;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.database.DatabaseHelper;
import rs.reviewer.database.PokeBossSQLiteHelper;
import rs.reviewer.database.PokeNearbySQLiteHelper;

public class LocationTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Location location;
    private ArrayList<PokeBoss> pokemons;
    private ArrayList<PokeBoss> nearMe;


    public static String RESULT_CODE = "RESULT_CODE";

    public LocationTask(Context context, Location location)
    {
        this.context = context;
        this.location = location;
        this.nearMe = new ArrayList<>();
    }

    @Override
    protected void onPreExecute()
    {
        //postaviti parametre, pre pokretanja zadatka ako je potrebno
    }


    @Override
    protected Void doInBackground(Void... params) {
        PokeBossSQLiteHelper dbHelper = new PokeBossSQLiteHelper(this.context);
        // Gets the data repository in write mode
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        PokeBossList pokeBossList = DatabaseHelper.readTableData(PokeBossSQLiteHelper.TABLE_POKEBOSS, db);
        db.close();
        this.pokemons = pokeBossList.getPokemonBosses();
        for(PokeBoss boss: this.pokemons) {
            if (isPokemonNearMe(boss)) {
                this.nearMe.add(boss);
            }
        }


        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        int notificationPermit = 10;
        Collections.sort(this.nearMe);
        if (this.nearMe.size() < notificationPermit) {
            notificationPermit = this.nearMe.size();
        }

        PokeNearbySQLiteHelper dbHelper = new PokeNearbySQLiteHelper(context);
        // Gets the data repository in write mode
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        //obrisi sve podatke
        db.execSQL("DELETE FROM " + PokeNearbySQLiteHelper.TABLE_POKENEARBY);

        for (PokeBoss boss : this.nearMe) {
            ContentValues values = new ContentValues();
            values.put(PokeNearbySQLiteHelper.COLUMN_ID, boss.getId());
            values.put(PokeNearbySQLiteHelper.COLUMN_NAME, boss.getPokemon().getName());
            values.put(PokeNearbySQLiteHelper.COLUMN_LATITUDE, boss.getLatitude());
            values.put(PokeNearbySQLiteHelper.COLUMN_LONGITUDE, boss.getLongitude());
            values.put(PokeNearbySQLiteHelper.COLUMN_LEVEL, boss.getLevel());
            values.put(PokeNearbySQLiteHelper.COLUMN_FIGHT_HEALT, boss.getFightHealt());
            values.put(PokeNearbySQLiteHelper.COLUMN_IMAGE_PATH, boss.getPokemon().getImage_path());
            db.replace(PokeNearbySQLiteHelper.TABLE_POKENEARBY, null, values);
        }
        // show max 10 notifications at time
        for(int i = 0; i < notificationPermit; i++) {
            PokeBoss boss = this.nearMe.get(i);
            this.generateSystemNotification(boss);
        }
    }

    private boolean isPokemonNearMe(PokeBoss boss) {
        double distance = getDistance(location.getLatitude(), location.getLongitude(), boss.getLatitude(), boss.getLongitude());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        double kilometres = Double.valueOf(sharedPreferences.getString("pref_map_list", "1"));
        boss.setDistance(distance);
        if (distance <= kilometres) {
            return true;
        }
        return  false;
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double earth_radius = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return earth_radius * c;
    }


    private void generateSystemNotification(PokeBoss boss) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("pokemonName", boss.getName());
        notificationIntent.putExtra("pokemonLevel", boss.getLevel());
        notificationIntent.putExtra("pokemonId", boss.getId());
        notificationIntent.putExtra("pokeBoss", boss);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setAction("PokeGO " + System.currentTimeMillis());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);
        stackBuilder.addNextIntentWithParentStack(notificationIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultIntent = PendingIntent.getActivity(
            this.context, 0, notificationIntent, 0
        );

        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(resultIntent, boss);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "bosses", "Pokemon channel", NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Pokemon boss");
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
            channel.shouldVibrate();
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(boss.getId().intValue(), notificationBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder(PendingIntent pendingIntent, PokeBoss boss) {
        return new NotificationCompat.Builder(context, "bosses")
                .setContentTitle("Pokemon near you!")
                .setContentText("Pokemon " + boss.getName() + " (lvl. " + boss.getLevel() + ") is near you now. Try catching him!")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_action_warning)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Pokemon " + boss.getName() + " (lvl. " + boss.getLevel() + ") is near you now. Try catching him!")
                        .setBigContentTitle("Pokemon near you")
                        .setSummaryText("Catch this pokemon!"))
                .setContentIntent(pendingIntent)
                .setGroup("PokeGO Location Scanner")
                .setContentInfo("Pokemon " + boss.getName() + " (lvl. " + boss.getLevel() + ") is near you now. Try catching him!")
                .setColor(Color.rgb(63, 81, 181))
                .setLights(Color.RED, 1000, 300);

    }
}
