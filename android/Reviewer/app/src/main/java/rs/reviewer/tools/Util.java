package rs.reviewer.tools;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.Cinema;
import rs.reviewer.database.DBContentProvider;
import rs.reviewer.database.ReviewerSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by skapl on 15-Apr-16.
 */
public class Util {
    public static void initDB(Activity activity) {
        ReviewerSQLiteHelper dbHelper = new ReviewerSQLiteHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        {
            ContentValues entry = new ContentValues();
            entry.put(ReviewerSQLiteHelper.COLUMN_NAME, "Arena");
            entry.put(ReviewerSQLiteHelper.COLUMN_DESCRIPTION, "Cineplexx 3D");
            entry.put(ReviewerSQLiteHelper.COLUMN_AVATAR, -1);

            activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_CINEMA, entry);

            entry = new ContentValues();
            entry.put(ReviewerSQLiteHelper.COLUMN_NAME, "Cinestar");
            entry.put(ReviewerSQLiteHelper.COLUMN_DESCRIPTION, "Najnoviji 5D");
            entry.put(ReviewerSQLiteHelper.COLUMN_AVATAR, -1);

            activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_CINEMA, entry);
        }

        db.close();
    }

}
