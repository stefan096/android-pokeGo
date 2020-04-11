package rs.reviewer.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.Nullable;

/**
 * Created by skapl on 13-Apr-16.
 */
public class DBContentProvider extends ContentProvider {
    private ReviewerSQLiteHelper database;

    private static final int CINEMA = 10;
    private static final int CINEMA_ID = 20;

    private static final String AUTHORITY = "rs.reviewer";

    private static final String CINEMA_PATH = "cinema";

    public static final Uri CONTENT_URI_CINEMA = Uri.parse("content://" + AUTHORITY + "/" + CINEMA_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, CINEMA_PATH, CINEMA);
        sURIMatcher.addURI(AUTHORITY, CINEMA_PATH + "/#", CINEMA_ID);
    }

    @Override
    public boolean onCreate() {
        database = new ReviewerSQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exist
        //checkColumns(projection);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case CINEMA_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere(ReviewerSQLiteHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                //$FALL-THROUGH$
            case CINEMA:
                // Set the table
                queryBuilder.setTables(ReviewerSQLiteHelper.TABLE_CINEMA);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri retVal = null;
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case CINEMA:
                id = sqlDB.insert(ReviewerSQLiteHelper.TABLE_CINEMA, null, values);
                retVal = Uri.parse(CINEMA_PATH + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsDeleted = 0;
        switch (uriType) {
            case CINEMA:
                rowsDeleted = sqlDB.delete(ReviewerSQLiteHelper.TABLE_CINEMA,
                        selection,
                        selectionArgs);
                break;
            case CINEMA_ID:
                String idCinema = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ReviewerSQLiteHelper.TABLE_CINEMA,
                            ReviewerSQLiteHelper.COLUMN_ID + "=" + idCinema,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(ReviewerSQLiteHelper.TABLE_CINEMA,
                            ReviewerSQLiteHelper.COLUMN_ID + "=" + idCinema
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsUpdated = 0;
        switch (uriType) {
            case CINEMA:
                rowsUpdated = sqlDB.update(ReviewerSQLiteHelper.TABLE_CINEMA,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CINEMA_ID:
                String idCinema = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ReviewerSQLiteHelper.TABLE_CINEMA,
                            values,
                            ReviewerSQLiteHelper.COLUMN_ID + "=" + idCinema,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ReviewerSQLiteHelper.TABLE_CINEMA,
                            values,
                            ReviewerSQLiteHelper.COLUMN_ID + "=" + idCinema
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
