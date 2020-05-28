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

import model.Pokemon;
import model.UsersPokemons;
import retrofit2.http.HEAD;
import rs.reviewer.R;

public class PokemonListAdapter extends BaseAdapter {

    Context mContext;
    List<UsersPokemons> mpokemons;
    boolean mforFight;

    public PokemonListAdapter(Context context, List<UsersPokemons> pokemons, boolean forFight) {
        mContext = context;
        mpokemons = pokemons;
        mforFight = forFight;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isInCooldown(String cooldown){
        Instant instantCooldown = Instant.parse(cooldown);
        Instant currentTime = Instant.now();
        int result = instantCooldown.compareTo(currentTime);
        System.out.println(result >=1 ? "Cooldown time is greater than current time[cooldown is still on]."
                :"Current time is greater than cooldown[cooldown done!].");

        return(result >= 1);//isInCooldown = true

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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isEnabled(int position) {
        if(mforFight){
            String cooldown = mpokemons.get(position).getCooldown();
            if (cooldown != null) {
                if (isInCooldown(cooldown)) {
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        }else {

            return true;
        }
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
        if (isInCooldown(mpokemons.get(position).getCooldown()) && mforFight) {
            RelativeLayout relativeLayout = view.findViewById(R.id.mylayout);
            relativeLayout.setBackgroundColor(Color.parseColor("#C2BFBF"));//kada se skroluje, svi postanu zatamnjeni??

        }



        return view;
    }

}
