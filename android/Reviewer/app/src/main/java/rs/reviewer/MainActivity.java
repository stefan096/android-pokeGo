package rs.reviewer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;

import model.NavItem;
import model.PokeBoss;
import model.PokeBossList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.activities.LoginActivity;
import rs.reviewer.activities.NearbyPokemonActivity;
import rs.reviewer.activities.ProfileActivity;
import rs.reviewer.activities.ReviewerPreferenceActivity;
import rs.reviewer.adapters.DrawerListAdapter;
import rs.reviewer.database.DatabaseHelper;
import rs.reviewer.database.PokeBossSQLiteHelper;
import rs.reviewer.database.PokeNearbySQLiteHelper;
import rs.reviewer.dialogs.FightDialog;
import rs.reviewer.fragments.MapFragment;
import rs.reviewer.fragments.MyFragment;
import rs.reviewer.fragments.PokemonListFragment;
import rs.reviewer.location.LocationTask;
import rs.reviewer.rest.BaseService;
import rs.reviewer.sync.SyncReceiver;
import rs.reviewer.sync.SyncService;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.tools.ReviewerTools;
import rs.reviewer.utils.UserUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private AlertDialog dialog;

    //Sync stuff
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    private SyncReceiver sync;
    public static String SYNC_DATA = "SYNC_DATA";
    public static String LOCATION_DATA = "LOCATION_DATA";
    private String synctime;
    private boolean allowSync;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareMenu(mNavItems);

        mTitle  = getTitle();
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.navList);
        
        // Populate the Navigtion Drawer with options
        mDrawerPane = findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
//                getActionBar().setTitle(mTitle);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setTitle("PokeGO");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        if (savedInstanceState == null) {
            selectItemFromDrawer(0);
        }

        setUpReceiver();
        consultPreferences();
        onNewIntent(getIntent()) ;

    }

    @Override
    protected void onNewIntent (Intent intent) {
        super .onNewIntent(intent) ;
        Bundle extras = intent.getExtras() ;
        if (extras != null ) {
            if (extras.containsKey( "pokemonId" )) {
                FightDialog dlg = new FightDialog(this);
                dlg.prepareDialog((PokeBoss) extras.get("pokeBoss"));
                dlg.show();
                Log.d("NOTIFICATION OPEN", extras.get("pokemonId") + "");
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        PokeBossSQLiteHelper dbHelper = new PokeBossSQLiteHelper(this);
        // Gets the data repository in write mode
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(PokeBossSQLiteHelper.DB_CREATE);


        PokeNearbySQLiteHelper dbHelper1 = new PokeNearbySQLiteHelper(this);
        // Gets the data repository in write mode
        final SQLiteDatabase db1 = dbHelper1.getWritableDatabase();

        db1.execSQL(PokeNearbySQLiteHelper.DB_CREATE);


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

    private void setUpReceiver(){
        sync = new SyncReceiver();

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, SyncService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    }

    private void consultPreferences(){

        /*
        * getDefaultSharedPreferences():
        * koristi podrazumevano ime preference-file-a.
        * Podrzazumevani fajl je setovan na nivou aplikacije tako da sve aktivnosti u istom context-u mogu da mu pristupe jednostavnije
        * getSharedPreferences(name,mode):
        * trazi da se specificira ime preference file-a requires i mod u kome se radi (e.g. private, world_readable, etc.)
        */
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        /*
        * Koristeci parove kljuc-vrednost iz shared preferences mozemo da dobijemo
        * odnosno da zapisemo nekakve vrednosti. Te vrednosti mogu da budu iskljucivo
        * prosti tipovi u Javi.
        * Kao prvi parametar prosledjujemo kljuc, a kao drugi podrazumevanu vrednost,
        * ako nesto pod tim kljucem se ne nalazi u storage-u, da dobijemo podrazumevanu
        * vrednost nazad, i to nam je signal da nista nije sacuvano pod tim kljucem.
        * */
        synctime = sharedPreferences.getString(getString(R.string.pref_sync_list), "1");
        allowSync = sharedPreferences.getBoolean(getString(R.string.pref_sync), false);
        double kilometres = Double.valueOf(sharedPreferences.getString(getString(R.string.pref_map_list), "1"));

        Log.e("SYNC", "citanje " + synctime);
        Log.e("SYNC", "citanje " + allowSync);
        Log.e("SYNC", "citanje " + kilometres);
    }

    /**
     * Svaki put kada uredjaj dobijee novu informaciju o lokaciji ova metoda se poziva
     * i prosledjuje joj se nova informacija o kordinatamad
     * */
    @Override
    public void onLocationChanged(Location location) {
        LocationTask task = new LocationTask(getApplicationContext(), location);
        task.execute();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();

        //Za slucaj da referenca nije postavljena da se izbegne problem sa androidom!
        if (manager == null) {
            setUpReceiver();
        }

        if(allowSync){
            int interval = ReviewerTools.calculateTimeTillNextSync(Integer.parseInt(synctime));
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_DATA);
        IntentFilter locFilter = new IntentFilter();
        filter.addAction(LOCATION_DATA);

        registerReceiver(sync, filter);
    }
    
    private void prepareMenu(ArrayList<NavItem> mNavItems ){
    	mNavItems.add(new NavItem(getString(R.string.home), getString(R.string.home_long), R.drawable.ic_action_group));
        mNavItems.add(new NavItem(getString(R.string.pokemon_list), getString(R.string.pokemon_list_long), R.drawable.ic_action_name));
        mNavItems.add(new NavItem(getString(R.string.nearby_pokemon), getString(R.string.nearby_pokemon_long), R.drawable.ic_action_name));
        mNavItems.add(new NavItem(getString(R.string.map), getString(R.string.map), R.drawable.ic_action_map));
        mNavItems.add(new NavItem(getString(R.string.profile), getString(R.string.profile_long), R.drawable.ic_action_person));
        mNavItems.add(new NavItem(getString(R.string.preferences), getString(R.string.preferences_long), R.drawable.ic_action_settings));

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.activity_itemdetail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                UserUtil.setLogInUser(null, getApplicationContext());
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                finish();
                startActivity(getIntent());

        }

        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	selectItemFromDrawer(position);
        }
    }
    
    
    private void selectItemFromDrawer(int position) {
        if(position == 0){
            FragmentTransition.to(MyFragment.newInstance(), this, false);
        }else if(position == 1){ //list of pokemons
            FragmentTransition.to(PokemonListFragment.newInstance(), this, false);
            //..
        }else if(position == 2){ //pokemon nearby
            Intent pokemon_nearby = new Intent(MainActivity.this, NearbyPokemonActivity.class);
            startActivity(pokemon_nearby);
        }else if(position == 3){ //map
            FragmentTransition.to(MapFragment.newInstance(), this, false);
        }else if(position == 4){ //profile
            Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profile);
        }else if(position == 5){ //preferences
            Intent preference = new Intent(MainActivity.this,ReviewerPreferenceActivity.class);
            startActivity(preference);
        }else{
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).getmTitle());
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPause() {
        if (manager != null) {
            manager.cancel(pendingIntent);
        }

        //osloboditi resurse
        if(sync != null){
            unregisterReceiver(sync);
        }

        super.onPause();

    }

}
