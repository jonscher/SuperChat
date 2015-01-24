package fr.ecp.sio.superchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by jonathan on 03/01/2015.
 */
public class FollowActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_activity);

        if (savedInstanceState == null) {

            Fragment followFragment = new FollowingFragment();
            followFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, followFragment)
                    .commit();

        }
    }
}
