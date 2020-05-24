package rs.reviewer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PokeBossSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_POKEBOSS = "pokeboss";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_FIGHT_HEALT = "fightHealt";
    public static final String COLUMN_IMAGE_PATH = "imagePath";
    public static final String COLUMN_NAME = "name";


    //Dajemo ime bazi
    public static final String DATABASE_NAME = "pokeboss.db";
    //i pocetnu verziju baze. Obicno krece od 1
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "create table "
            + TABLE_POKEBOSS + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_LEVEL + " integer, "
            + COLUMN_LATITUDE + " double, "
            + COLUMN_LONGITUDE + " double, "
            + COLUMN_FIGHT_HEALT + " double, "
            + COLUMN_IMAGE_PATH + " text, "
            + COLUMN_NAME + " text "
            + ")";


    //Potrebno je dodati konstruktor zbog pravilne inicijalizacije
    public PokeBossSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Prilikom kreiranja baze potrebno je da pozovemo odgovarajuce metode biblioteke
    //prilikom kreiranja moramo pozvati db.execSQL za svaku tabelu koju imamo
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    //kada zelimo da izmenomo tabele, moramo pozvati drop table za sve tabele koje imamo
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POKEBOSS);
        onCreate(db);
    }


}
