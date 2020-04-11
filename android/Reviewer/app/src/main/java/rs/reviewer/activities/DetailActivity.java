package rs.reviewer.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import model.Cinema;
import rs.reviewer.R;
import rs.reviewer.database.ReviewerSQLiteHelper;

public class DetailActivity extends AppCompatActivity {
    private Uri todoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();

        todoUri = extras.getParcelable("id");
        fillData(todoUri);
    }

    private void fillData(Uri todoUri) {
        String[] allColumns = { ReviewerSQLiteHelper.COLUMN_ID,
                ReviewerSQLiteHelper.COLUMN_NAME, ReviewerSQLiteHelper.COLUMN_DESCRIPTION, ReviewerSQLiteHelper.COLUMN_AVATAR };

        Cursor cursor = getContentResolver().query(todoUri, allColumns, null, null,
                null);

        cursor.moveToFirst();
        Cinema cinema = createCinema(cursor);

        TextView tvName = (TextView)findViewById(R.id.tvName);
        TextView tvDescr = (TextView)findViewById(R.id.tvDescr);

        tvName.setText(cinema.getName());
        tvDescr.setText(cinema.getDescription());

        cursor.close();
    }

    public static Cinema createCinema(Cursor cursor) {
        Cinema cinema = new Cinema();
        cinema.setId(cursor.getLong(0));
        cinema.setName(cursor.getString(1));
        cinema.setDescription(cursor.getString(2));
        cinema.setAvatar(cursor.getInt(3));
        return cinema;
    }

}
