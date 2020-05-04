package rs.reviewer.activities;

import android.app.Activity;
import android.os.Bundle;

public class FightBossActivity extends Activity {

    private Long bossId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        bossId = extras.getParcelable("bossID");
    }

}
