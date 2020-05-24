package rs.reviewer.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Cinema;
import model.PokeBoss;
import model.PokeBossList;
import model.Pokemon;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.R;
import rs.reviewer.database.DBContentProvider;
import rs.reviewer.database.DatabaseHelper;
import rs.reviewer.database.PokeBossSQLiteHelper;
import rs.reviewer.database.ReviewerSQLiteHelper;
import rs.reviewer.dialogs.LocationDialog;
import rs.reviewer.dialogs.FightDialog;
import rs.reviewer.rest.BaseService;

import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final List<Target> targets = new ArrayList<>();

    private LocationManager locationManager;
    private String provider;
    private SupportMapFragment mMapFragment;
    private AlertDialog dialog;
    private Marker home;
    private GoogleMap map;
    private Location currentLocation;
    private ArrayList<PokeBoss> pokemons;

    public static MapFragment newInstance() {

        MapFragment mpf = new MapFragment();

        return mpf;
    }

    public void getPokemons() {

        Log.d("STEFAN","countSS: " + "USAO");

        if (currentLocation == null) {
            Log.d("REZ", "NEMA LOKACIJE");
            return;
        }

        PokeBossSQLiteHelper dbHelper = new PokeBossSQLiteHelper(getContext());
        // Gets the data repository in write mode
        final SQLiteDatabase db = dbHelper.getWritableDatabase();


        /*Call<ResponseBody> call = BaseService.userService.getPokemonMap(currentLocation.getLatitude(), currentLocation.getLongitude());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseJson = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao petlju");
                    try {
                        responseJson = response.body().string();
                        PokeBossList bosses = new Gson().fromJson(responseJson, PokeBossList.class);
                        pokemons = bosses.getPokemonBosses();

                        for(PokeBoss boss: pokemons) {

                            // Create a new map of values, where column names are the keys
                            ContentValues values = new ContentValues();
                            values.put(PokeBossSQLiteHelper.COLUMN_ID, boss.getId());
                            values.put(PokeBossSQLiteHelper.COLUMN_NAME, boss.getPokemon().getName());
                            values.put(PokeBossSQLiteHelper.COLUMN_LATITUDE, boss.getLongitude());
                            values.put(PokeBossSQLiteHelper.COLUMN_LONGITUDE, boss.getLatitude());
                            values.put(PokeBossSQLiteHelper.COLUMN_LEVEL, boss.getLevel());
                            values.put(PokeBossSQLiteHelper.COLUMN_FIGHT_HEALT, boss.getFightHealt());
                            values.put(PokeBossSQLiteHelper.COLUMN_IMAGE_PATH, boss.getPokemon().getImage_path());

                            long id = db.replace(PokeBossSQLiteHelper.TABLE_POKEBOSS, null, values);

                            addPokemonToMap(boss);
                        }

                        DatabaseHelper.printTableData(PokeBossSQLiteHelper.TABLE_POKEBOSS, db);
                        PokeBossList pokeBossList = DatabaseHelper.readTableData(PokeBossSQLiteHelper.TABLE_POKEBOSS, db);
                        Log.d("STEFAN","count: " + pokeBossList.getPokemonBosses().size());

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
        });*/

        //DatabaseHelper.printTableData(PokeBossSQLiteHelper.TABLE_POKEBOSS, db);
        PokeBossList pokeBossList = DatabaseHelper.readTableData(PokeBossSQLiteHelper.TABLE_POKEBOSS, db);
        Log.d("STEFAN","countSS: " + pokeBossList.getPokemonBosses().size());

        pokemons = pokeBossList.getPokemonBosses();

        for(PokeBoss boss: pokemons) {
            addPokemonToMap(boss);
        }
    }

    /**
     * Prilikom kreidanja fragmenta preuzimamo sistemski servis za rad sa lokacijama
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


//        Log.d("STEFAN","" + "usao");
//
//        ReviewerSQLiteHelper dbHelper = new ReviewerSQLiteHelper(getContext());
//
//        // Gets the data repository in write mode
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//        //values.put(ReviewerSQLiteHelper.COLUMN_ID, 21);
//        values.put(ReviewerSQLiteHelper.COLUMN_NAME, "nazivSS");
//        values.put(ReviewerSQLiteHelper.COLUMN_DESCRIPTION, "opisSS");
//
//        long id = db.replace(ReviewerSQLiteHelper.TABLE_CINEMA, null, values);
//        System.out.println(id);
//        Log.d("STEFAN","" + id);

        //DatabaseHelper.printTableDataCinema(ReviewerSQLiteHelper.TABLE_CINEMA, db);
        //List<Cinema> cinemas = DatabaseHelper.readTableDataCinema(ReviewerSQLiteHelper.TABLE_CINEMA, db);
        //Log.d("STEFAN","count" + cinemas.size());

    }

    /**
     * Kada zelmo da dobijamo informacije o lokaciji potrebno je da specificiramo
     * po kom kriterijumu zelimo da dobijamo informacije GSP, MOBILNO(WIFI, MObilni internet), GPS+MOBILNO
     * **/
    private void createMapFragmentAndInflate() {
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, true);

        mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();

        mMapFragment.getMapAsync(this);
    }

    private void showLocatonDialog() {
        if (dialog == null) {
            dialog = new LocationDialog(getActivity()).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        createMapFragmentAndInflate();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps && !wifi) {
            showLocatonDialog();
        } else {
            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    locationManager.requestLocationUpdates(provider, 0, 0, this);
                }else if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    //Request location updates:
                    locationManager.requestLocationUpdates(provider, 0, 0, this);
                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.map_layout, vg, false);

        return view;
    }

    /**
     * Svaki put kada uredjaj dobijee novu informaciju o lokaciji ova metoda se poziva
     * i prosledjuje joj se nova informacija o kordinatamad
     * */
    @Override
    public void onLocationChanged(Location location) {

        if (
            map != null &&
            currentLocation != null &&
            currentLocation.getLatitude() != location.getLatitude() &&
            currentLocation.getLongitude() != location.getLongitude()
        ) {
            addMarker(location);
        }
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

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Allow user location")
                        .setMessage("To continue working we need your locations....Allow now?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 0, 0, this);
                    }

                } else if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 0, 0, this);
                    }

                }
                return;
            }

        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (!marker.equals(home))
        {
            for (PokeBoss boss : pokemons) {
                if (
                    marker.getPosition().latitude == boss.getLatitude() &&
                    marker.getPosition().longitude == boss.getLongitude()
                ) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    FightDialog dlg = new FightDialog(getActivity());
                    dlg.prepareDialog(boss);
                    dlg.show();
                    return true;
                }
            }
            //handle click here
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        googleMap.setOnMarkerClickListener(this);
        Location location = null;

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                location = locationManager.getLastKnownLocation(provider);
            }else if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                //Request location updates:
                location = locationManager.getLastKnownLocation(provider);
            }
        }

        if (location != null) {
            Log.d("Location", "lat " + location.getLatitude() + " LNG " + location.getLongitude());
            addMarker(location);
            getPokemons();
        }
    }

    private void addPokemonToMap(PokeBoss pokemon) {
        final Marker marker = map.addMarker(new MarkerOptions()
                .title(pokemon.getPokemon().getName())
                .position(new LatLng(pokemon.getLatitude(), pokemon.getLongitude())));
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get()
                .load(pokemon.getPokemon().getImage_path())
                .into(target);
        targets.add(target);
    }

    private void addMarker(Location location) {
        currentLocation = location;
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        boolean shouldAnimate = home == null;
        if (home != null) {
            home.remove();
        }

        Log.d("Location", "lat " + location.getLatitude() + " Lon " + location.getLongitude());

        home = map.addMarker(new MarkerOptions()
                .title("YOUR_POSITON")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(loc));
        home.setFlat(true);

        if (shouldAnimate) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(loc).zoom(14).build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    /**
     *
     * Rad sa lokacja izuzetno trosi bateriju.Obavezno osloboditi kada vise ne koristmo
     * */
    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }
}