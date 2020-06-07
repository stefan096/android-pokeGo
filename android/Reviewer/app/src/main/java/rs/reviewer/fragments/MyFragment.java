package rs.reviewer.fragments;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import java.io.IOException;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import model.UsersPokemons;
import static android.content.ContentValues.TAG;

import model.UsersPokemonsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import model.User;
import rs.reviewer.R;
import rs.reviewer.activities.LoginActivity;
import rs.reviewer.database.DBContentProvider;
import rs.reviewer.database.ReviewerSQLiteHelper;
import rs.reviewer.utils.UserUtil;
import rs.reviewer.rest.BaseService;

public class MyFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static String USER_KEY = "rs.reviewer.USER_KEY";
    private SimpleCursorAdapter adapter;

    private User user;
    private UsersPokemons pokemon = new UsersPokemons();


    public static MyFragment newInstance() {
		
		MyFragment mpf = new MyFragment();
		
		return mpf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.dashboard_layout, vg, false);

        String userId = UserUtil.getLogInUser(getActivity().getApplicationContext());
        user = new Gson().fromJson(userId, User.class);

        if (user != null) {
            TextView loggedUser = view.findViewById(R.id.name);
            loggedUser.setText(" " + user.getName() + " " + user.getLastName());
        }
		return view;
	}


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(user == null){
            return;
        }
        Call<ResponseBody> call = BaseService.userService.getLatestCaughtPokemon(user.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseJson = null;
                if (response.code() == 200) {
                    Log.d("REZ", "Usao petlju");
                    try {
                        responseJson = response.body().string();
                        pokemon = new Gson().fromJson(responseJson, UsersPokemons.class );
                        setUpPokemon(pokemon);
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

        if(id == R.id.action_log_out){
            //Toast.makeText(getActivity(), R.string.log_out, Toast.LENGTH_SHORT).show();

            UserUtil.setLogInUser(null, getActivity().getApplicationContext());
            Intent login = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(login);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] allColumns = { ReviewerSQLiteHelper.COLUMN_ID,
                ReviewerSQLiteHelper.COLUMN_NAME, ReviewerSQLiteHelper.COLUMN_DESCRIPTION, ReviewerSQLiteHelper.COLUMN_AVATAR };

        return new CursorLoader(getActivity(), DBContentProvider.CONTENT_URI_CINEMA,
                allColumns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    public void setUpPokemon(UsersPokemons usersPokemonsDTO){
        View view = getView();
        TextView editTextName = view.findViewById(R.id.pokemon_name);
        TextView editTextAtk = view.findViewById(R.id.atk);
        TextView editTextDefense = view.findViewById(R.id.defense);
        TextView editTextHp = view.findViewById(R.id.hp);
        ImageView imageView = view.findViewById(R.id.item_image);
        TextView atk_text =  view.findViewById(R.id.atk_text);
        TextView defense_text =  view.findViewById(R.id.defense_text);
        TextView hp_text =  view.findViewById(R.id.hp_text);

        hp_text.setText(R.string.hp);
        atk_text.setText(R.string.atk);
        defense_text.setText(R.string.defense);
        editTextName.setText(usersPokemonsDTO.getPokemon().getName());
        editTextAtk.setText(Double.toString(usersPokemonsDTO.getPokemon().getAtk()));
        editTextHp.setText(Double.toString(usersPokemonsDTO.getPokemon().getHp()));
        editTextDefense.setText(Double.toString(usersPokemonsDTO.getPokemon().getDefense()));

        Picasso.get()
                .load(usersPokemonsDTO.getPokemon().getImage_path())
                .into(imageView);
    }
}