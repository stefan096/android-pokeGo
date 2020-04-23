package rs.reviewer.fragments;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Pokemon;
import model.User;
import model.UsersPokemons;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.activities.DetailActivity;
import rs.reviewer.activities.LoginActivity;
import rs.reviewer.activities.PokemonDetailActivity;
import rs.reviewer.adapters.PokemonListAdapter;
import rs.reviewer.database.DBContentProvider;
import rs.reviewer.database.ReviewerSQLiteHelper;
import rs.reviewer.rest.BaseService;
import rs.reviewer.rest.UsersPokemonsService;
import rs.reviewer.utils.UserUtil;

public class PokemonListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private User getUser;
    private List < UsersPokemons > usersPokemons;
    private Pokemon pokemon = new Pokemon();


    public static PokemonListFragment newInstance() {

        PokemonListFragment pf = new PokemonListFragment();

        return pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.map_layout, vg, false);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), PokemonDetailActivity.class);
        //Uri todoUri = Uri.parse(DBContentProvider.CONTENT_URI_CINEMA + "/" + id);
        // intent.putExtra("id", todoUri);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Toast.makeText(getActivity(), "onActivityCreated()", Toast.LENGTH_SHORT).show();
        String userId = UserUtil.getLogInUser(getActivity().getApplicationContext());
        final User user = new Gson().fromJson(userId, User.class);
        Long id = user.getId();
        pokemon.setAtk(55);
        pokemon.setHp(431);
        pokemon.setName("Pikachu");
        pokemon.setDefense(322);
        final UsersPokemons usersPokemons1 = new UsersPokemons(pokemon);

        Call<ResponseBody> call = BaseService.userService.findById(id);
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                             String userJson = null;
                             try {
                                 userJson = response.body().string();
                                 getUser = new Gson().fromJson(userJson, User.class);
                                 usersPokemons = getUser.getPokemons();
                                 if (usersPokemons.isEmpty()){
                                     UsersPokemons up = new UsersPokemons();
                                     up.setPokemon(pokemon);
                                     usersPokemons.add(up);
                                 }
                                 PokemonListAdapter adapter = new PokemonListAdapter(getActivity(), usersPokemons);
                                 setListAdapter(adapter);
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseBody> call, Throwable t) {

                         }
                     });


        //= user.getPokemons();
       /* Pokemon pokemon = new Pokemon();
        pokemon.setAtk(55);
        pokemon.setHp(431);
        pokemon.setName("Pikachu");
        pokemon.setDefense(322);
        Pokemon pokemon1 = new Pokemon();
        pokemon1.setAtk(55);
        pokemon1.setHp(431);
        pokemon1.setName("Bulbasaur");
        pokemon1.setDefense(322);
        UsersPokemons usersPokemons1 = new UsersPokemons(pokemon);
        UsersPokemons usersPokemons2 = new UsersPokemons(pokemon1);*/

       /* usersPokemons.add(usersPokemons1);
        usersPokemons.add(usersPokemons2)*/;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.activity_itemdetail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            Toast.makeText(getActivity(), "Refresh App", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_new) {
            Toast.makeText(getActivity(), "Create Text", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String userId = UserUtil.getLogInUser(getActivity().getApplicationContext());
        User user = new Gson().fromJson(userId, User.class);

        List<UsersPokemons> usersPokemons = user.getPokemons();


        return new CursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
