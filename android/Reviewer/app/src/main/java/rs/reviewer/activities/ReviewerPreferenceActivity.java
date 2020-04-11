package rs.reviewer.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceFragmentCompat;
import rs.reviewer.R;
import rs.reviewer.tools.FragmentTransition;

public class ReviewerPreferenceActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		FragmentTransition.to(PrefsFragment.newInstance(), this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/*
	* Ekran za podesenja u novojim verzijama Android-a se preporucjue da bude fragment
	* Sto znaci da moramo da napraivmo aktivnost, na koju cemo 'zalepiti' fragment'
	* Kako je to specifican fragment, za njega ne ravimo layout, kao za ostale frafgmente/aktivnosti
	* nego koristimo posebnu specifikaciuju koja opisuje koja to podesenja imamo
	* NOTE: pogeldati res/xml/preferences.xml file za vise detalja.
	* */
	public static class PrefsFragment extends PreferenceFragmentCompat {

		private static PrefsFragment newInstance() {
			Bundle args = new Bundle();

			PrefsFragment fragment = new PrefsFragment();
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			addPreferencesFromResource(R.xml.preferences);
		}
	}
	
}
