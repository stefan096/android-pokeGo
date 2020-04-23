package rs.reviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import model.Pokemon;
import model.User;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.fragments.MyFragment;
import rs.reviewer.fragments.PokemonListFragment;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.utils.UserUtil;

public class PokemonDetailActivity extends AppCompatActivity {

   private Pokemon pokemon;
   Toolbar toolbar;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.pokemon_detail);

      TextView editTextName = findViewById(R.id.pokemon_name);
      TextView editTextAtk = findViewById(R.id.atk);
      TextView editTextDefense = findViewById(R.id.defense);
      TextView editTextCp = findViewById(R.id.hp);
      ImageView imageView = findViewById(R.id.item_image);

      editTextName.setText("Pikachu");
      editTextAtk.setText("3231");
      editTextCp.setText("3231");
      editTextDefense.setText("3231");
      toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle("Pokemon details");

   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            finish();

            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }
}
