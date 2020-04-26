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
        TextView atk =  view.findViewById(R.id.atk);
        TextView defence =  view.findViewById(R.id.defense);
        TextView hp =  view.findViewById(R.id.hp);
        TextView atk_text =  view.findViewById(R.id.atk_text);
        TextView defense_text =  view.findViewById(R.id.defense_text);
        TextView hp_text =  view.findViewById(R.id.hp_text);
        ImageView image =  view.findViewById(R.id.item_image);


        name.setText( mpokemons.get(position).getPokemon().getName() );
        hp.setText(Double.toString(mpokemons.get(position).getPokemon().getHp()));
        atk.setText(Double.toString(mpokemons.get(position).getPokemon().getAtk()));
        defence.setText(Double.toString(mpokemons.get(position).getPokemon().getDefense()));
        hp_text.setText(R.string.hp);
        atk_text.setText(R.string.atk);
        defense_text.setText(R.string.defense);



        return view;
    }

}
