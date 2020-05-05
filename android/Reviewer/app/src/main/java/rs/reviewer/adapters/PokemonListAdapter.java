package rs.reviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import model.UsersPokemons;
import rs.reviewer.R;

public class PokemonListAdapter extends BaseAdapter {

    Context mContext;
    List<UsersPokemons> mpokemons;

    public PokemonListAdapter(Context context, List<UsersPokemons> pokemons) {
        mContext = context;
        mpokemons = pokemons;
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
    public long getItemId(int position) {
        return mpokemons.get(position).getId();
    }

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

        TextView name =  view.findViewById(R.id.pokemon_name);
        TextView levelText = view.findViewById(R.id.level_txt);
        TextView level = view.findViewById(R.id.level);

        ImageView image =  view.findViewById(R.id.item_image);


        name.setText( mpokemons.get(position).getPokemon().getName() );
        levelText.setText(R.string.lvl);
        level.setText(String.valueOf(mpokemons.get(position).getLevel()));


        return view;
    }

}
