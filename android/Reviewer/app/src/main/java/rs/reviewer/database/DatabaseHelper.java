package rs.reviewer.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Cinema;
import model.PokeBoss;
import model.PokeBossList;
import model.Pokemon;

public class DatabaseHelper {

    public static void printTableDataCinema(String table_name, SQLiteDatabase db){

        Cursor cur = db.rawQuery("SELECT * FROM " + table_name,
                null);

        if(cur.getCount() != 0 && cur.moveToFirst()){

            int idInt  = cur.getColumnIndex(ReviewerSQLiteHelper.COLUMN_ID);
            int nameInt = cur.getColumnIndex(ReviewerSQLiteHelper.COLUMN_NAME);
            int descriptionInt = cur.getColumnIndex(ReviewerSQLiteHelper.COLUMN_DESCRIPTION);

            do{
                String row_values = "";

//                for(int i = 0 ; i < cur.getColumnCount(); i++){
//                    row_values = row_values + " || " + cur.getString(i);
//                }

                long id  = cur.getLong(idInt);
                String name = cur.getString(nameInt);
                String description = cur.getString(descriptionInt);
                row_values = "id: " + id + " name: " + name + " description: " + description;

                Log.d("LOG_TAG_HERE", row_values);

            }while (cur.moveToNext());
        }
    }

    public static List<Cinema> readTableDataCinema(String table_name, SQLiteDatabase db){

        List<Cinema> cinemas = new ArrayList<>();

        Cursor cur = db.rawQuery("SELECT * FROM " + table_name,
                null);

        if(cur.getCount() != 0 && cur.moveToFirst()){

            int idInt  = cur.getColumnIndex(ReviewerSQLiteHelper.COLUMN_ID);
            int nameInt = cur.getColumnIndex(ReviewerSQLiteHelper.COLUMN_NAME);
            int descriptionInt = cur.getColumnIndex(ReviewerSQLiteHelper.COLUMN_DESCRIPTION);

            do{

                long id  = cur.getLong(idInt);
                String name = cur.getString(nameInt);
                String description = cur.getString(descriptionInt);
                Cinema cinema = new Cinema(id, name, description);

                cinemas.add(cinema);


            }while (cur.moveToNext());
        }

        return cinemas;
    }

    public static void printTableData(String table_name, SQLiteDatabase db){

        Cursor cur = db.rawQuery("SELECT * FROM " + table_name,
                null);

        if(cur.getCount() != 0 && cur.moveToFirst()){

            int idInt  = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_ID);
            //pokemon
            int levelInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_LEVEL);
            int latitudeInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_LATITUDE);
            int longitudeInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_LONGITUDE);
            int fightHealtInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_FIGHT_HEALT);
            int imagePathInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_IMAGE_PATH);
            int nameInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_NAME);

            do{
                String row_values = "";
                long id  = cur.getLong(idInt);
                //pokemon
                int level = cur.getInt(levelInt);
                double latitude = cur.getDouble(latitudeInt);
                double longitude = cur.getDouble(longitudeInt);
                double fightHealt = cur.getDouble(fightHealtInt);
                String imagePath = cur.getString(imagePathInt);
                String name = cur.getString(nameInt);


                row_values = "id: " + id + " name: " + name + " level: " + level +
                        " latitude: " + latitude + " longitude: " + longitude +
                        " fightHealt: " + fightHealt + " imagePath: " + imagePath;

                Log.d("LOG_TAG_HERE", row_values);

            }while (cur.moveToNext());
        }
    }
    public static void deleteNearbyPokemon(int bossId, SQLiteDatabase db) {
        db.delete(PokeNearbySQLiteHelper.TABLE_POKENEARBY,  PokeNearbySQLiteHelper.COLUMN_ID + " = " + bossId, null);
    }

    public static PokeBossList readTableData(String table_name, SQLiteDatabase db){

        PokeBossList pokeBossList = new PokeBossList();
        pokeBossList.setPokemonBosses(new ArrayList<PokeBoss>());
        ArrayList<PokeBoss> pokemons = new ArrayList<>();

        Cursor cur = db.rawQuery("SELECT * FROM " + table_name,
                null);

        if(cur.getCount() != 0 && cur.moveToFirst()){

            int idInt  = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_ID);
            //pokemon
            int levelInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_LEVEL);
            int latitudeInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_LATITUDE);
            int longitudeInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_LONGITUDE);
            int fightHealtInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_FIGHT_HEALT);
            int imagePathInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_IMAGE_PATH);
            int nameInt = cur.getColumnIndex(PokeBossSQLiteHelper.COLUMN_NAME);

            do{

                long id  = cur.getLong(idInt);
                //pokemon
                int level = cur.getInt(levelInt);
                double latitude = cur.getDouble(latitudeInt);
                double longitude = cur.getDouble(longitudeInt);
                double fightHealt = cur.getDouble(fightHealtInt);
                String imagePath = cur.getString(imagePathInt);
                String name = cur.getString(nameInt);

                Pokemon pokemon = new Pokemon();
                pokemon.setName(name);
                pokemon.setImage_path(imagePath);

                PokeBoss pokeBoss = new PokeBoss(id, pokemon, level, latitude, longitude,
                        fightHealt, imagePath, name);

                pokemons.add(pokeBoss);

            }while (cur.moveToNext());
        }

        pokeBossList.setPokemonBosses(pokemons);
        return pokeBossList;
    }
}
