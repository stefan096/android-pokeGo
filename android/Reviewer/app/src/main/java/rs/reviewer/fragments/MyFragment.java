package rs.reviewer.fragments;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import rs.reviewer.R;
import rs.reviewer.activities.DetailActivity;
import rs.reviewer.database.DBContentProvider;
import rs.reviewer.database.ReviewerSQLiteHelper;

public class MyFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static String USER_KEY = "rs.reviewer.USER_KEY";
    private SimpleCursorAdapter adapter;

	public static MyFragment newInstance() {
		
		MyFragment mpf = new MyFragment();
		
		return mpf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.map_layout, vg, false);

		return view;
	}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

//        Cinema cinema = (Cinema)l.getAdapter().getItem(position);


        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Uri todoUri = Uri.parse(DBContentProvider.CONTENT_URI_CINEMA + "/" + id);
        intent.putExtra("id", todoUri);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Toast.makeText(getActivity(), "onActivityCreated()", Toast.LENGTH_SHORT).show();

        //Dodaje se adapter
        getLoaderManager().initLoader(0, null, this);
        String[] from = new String[] { ReviewerSQLiteHelper.COLUMN_NAME, ReviewerSQLiteHelper.COLUMN_DESCRIPTION };
        int[] to = new int[] {R.id.name, R.id.description};
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.cinema_list, null, from,
                to, 0);
        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.activity_itemdetail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            Toast.makeText(getActivity(), "Refresh App", Toast.LENGTH_SHORT).show();
        }
//        if(id == R.id.action_new){
//            Toast.makeText(getActivity(), "Create Text", Toast.LENGTH_SHORT).show();
//        }
        if(id == R.id.action_log_out){
            Toast.makeText(getActivity(), R.string.log_out, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] allColumns = { ReviewerSQLiteHelper.COLUMN_ID,
                ReviewerSQLiteHelper.COLUMN_NAME, ReviewerSQLiteHelper.COLUMN_DESCRIPTION, ReviewerSQLiteHelper.COLUMN_AVATAR };

        return new CursorLoader(getActivity(), DBContentProvider.CONTENT_URI_CINEMA,
                allColumns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}