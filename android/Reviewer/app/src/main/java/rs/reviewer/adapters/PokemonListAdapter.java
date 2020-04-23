package rs.reviewer.adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.NavItem;
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
        return 0;
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

        TextView name = (TextView) view.findViewById(R.id.pokemon_name);
        TextView atk = (TextView) view.findViewById(R.id.atk);
        TextView defence = (TextView) view.findViewById(R.id.defense);
        TextView hp = (TextView) view.findViewById(R.id.hp);
        ImageView image = (ImageView) view.findViewById(R.id.item_image);

        name.setText( mpokemons.get(position).getPokemon().getName() );
        atk.setText(Double.toString(mpokemons.get(position).getPokemon().getAtk()));
        defence.setText(Double.toString(mpokemons.get(position).getPokemon().getDefense()));
        hp.setText(Double.toString(mpokemons.get(position).getPokemon().getHp()));

        return view;
    }
}
