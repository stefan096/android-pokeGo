package rs.reviewer.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import model.FightDTO;
import model.PokeBoss;
import model.PokeBossList;
import model.User;
import model.UsersPokemons;
import model.UsersPokemonsDTO;
import model.UsersPokemonsDTOList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.dialogs.FightDialog;
import rs.reviewer.R;
import rs.reviewer.adapters.PokemonBossListAdapter;
import rs.reviewer.database.DatabaseHelper;
import rs.reviewer.database.PokeBossSQLiteHelper;
import rs.reviewer.database.PokeNearbySQLiteHelper;
import rs.reviewer.rest.BaseService;
import rs.reviewer.utils.UserUtil;
import android.widget.Toolbar;

import static android.content.ContentValues.TAG;

public class NearbyPokemonActivity extends ListActivity {

    private ArrayList<PokeBoss> pokemon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_nearby);
        setupScreen();
    }

    public void setupScreen() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setTitle(R.string.nearby_pokemon);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        PokeNearbySQLiteHelper dbHelper = new PokeNearbySQLiteHelper(getApplicationContext());
        // Gets the data repository in write mode
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        PokeBossList pokeBossList = DatabaseHelper.readTableData(PokeNearbySQLiteHelper.TABLE_POKENEARBY, db);
        db.close();

        pokemon = pokeBossList.getPokemonBosses();
        if (pokemon.size() == 0) {
            return;
        }

        PokemonBossListAdapter adapter = new PokemonBossListAdapter(this, pokemon, false);
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        PokeBoss boss = pokemon.get(position);

        FightDialog dlg = new FightDialog(this);
        dlg.prepareDialog(boss);
        dlg.show();
    }
}
