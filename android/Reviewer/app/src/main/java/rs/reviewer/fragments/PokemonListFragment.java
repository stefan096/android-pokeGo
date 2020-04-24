package rs.reviewer.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import model.Pokemon;
import model.User;
import model.UsersPokemons;
import model.UsersPokemonsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.R;
import rs.reviewer.activities.PokemonDetailActivity;
import rs.reviewer.adapters.PokemonListAdapter;
import rs.reviewer.rest.BaseService;
import rs.reviewer.utils.UserUtil;

import static android.content.ContentValues.TAG;

public class PokemonListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private User getUser = new User();
    private List < UsersPokemons > usersPokemons = new ArrayList<>();
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

        Call<ResponseBody> call = BaseService.userService.findByIdForPokemons(id);
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                             String userJson = null;
                             if (response.code() == 200) {
                                 Log.d("REZ", "USaO U FRAGMENT");
                                 try {
                                     userJson = response.body().string();
                                     UsersPokemonsDTO usersPokemonsDTO = new Gson().fromJson(userJson, UsersPokemonsDTO.class );
                                     usersPokemons = usersPokemonsDTO.getPokemons();
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
