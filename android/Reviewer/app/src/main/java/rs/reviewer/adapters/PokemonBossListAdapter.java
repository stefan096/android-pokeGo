package rs.reviewer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.util.List;
import com.squareup.picasso.Picasso;

import model.PokeBoss;
import model.Pokemon;
import model.UsersPokemons;
import retrofit2.http.HEAD;
import rs.reviewer.R;

public class PokemonBossListAdapter extends BaseAdapter {

    Context mContext;
    List<PokeBoss> mpokemons;
    boolean mforFight;

    public PokemonBossListAdapter(Context context, List<PokeBoss> pokemons, boolean forFight) {
        mContext = context;
        mpokemons = pokemons;
        mforFight = forFight;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getCount() {
        return mpokemons.size();
    }

    @Override
    public Object getItem(int position) {
        return mpokemons.get(position);
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public long getItemId(int position) {
        return mpokemons.get(position).getId();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pokemon_list, null);

        }
        else {
            view = convertView;
        }
        Pokemon pokemon = mpokemons.get(position).getPokemon();
        TextView name =  view.findViewById(R.id.pokemon_name);
        TextView levelText = view.findViewById(R.id.level_txt);
        TextView level = view.findViewById(R.id.level);
        ImageView image =  view.findViewById(R.id.item_image);
        name.setText( pokemon.getName() );
        levelText.setText(R.string.lvl);
        level.setText(String.valueOf(mpokemons.get(position).getLevel()));
        Picasso.get()
                .load(pokemon.getImage_path())
                .resize(300, 300)
                .centerCrop()
                .into(image);
        return view;
    }

}
