package rs.reviewer.fragments;

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
import java.util.ArrayList;
import java.util.List;

import model.User;
import model.UsersPokemons;
import model.UsersPokemonsDTOList;
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
    private List < UsersPokemons > usersPokemons = new ArrayList<>();
    private UsersPokemons pokemon = new UsersPokemons();
    private String userId;
    private User user;


    public static PokemonListFragment newInstance() {

        PokemonListFragment pf = new PokemonListFragment();

        return pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(false);
        View view = inflater.inflate(R.layout.list_layout, vg, false);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), PokemonDetailActivity.class);
        Uri todoUri = Uri.parse(Long.toString(id));
        intent.putExtra("id", todoUri);
        startActivity(intent);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId = UserUtil.getLogInUser(getActivity().getApplicationContext());
        user = new Gson().fromJson(userId, User.class);
        Call<ResponseBody> call = BaseService.userService.findByIdForPokemons(user.getId());
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                             String userJson = null;
                             if (response.code() == 200) {
                                 Log.d("REZ", "Usao petlju");
                                 try {
                                     userJson = response.body().string();
                                     UsersPokemonsDTOList usersPokemonsDTO = new Gson().fromJson(userJson, UsersPokemonsDTOList.class );
                                     usersPokemons = usersPokemonsDTO.getPokemons();
                                     PokemonListAdapter adapter = new PokemonListAdapter(getActivity(), usersPokemons, false);
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
