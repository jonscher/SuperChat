package fr.ecp.sio.superchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import fr.ecp.sio.superchat.Fragments.TweetsFragment;

/**
 * Created by Michaël on 05/12/2014.
 */
public class TweetsActivity extends ActionBarActivity {

    public static int FOLLOW_ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweets_activity);

        if (savedInstanceState == null) {
            Fragment tweetsFragment = new TweetsFragment();
            tweetsFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, tweetsFragment)
                    .commit();
        }
    }

    public void button_follower(View view) {

        FOLLOW_ACTIVITY = 0;
        Intent intent1 = new Intent(this, FollowActivity.class);
        intent1.putExtras(getIntent().getExtras());
        startActivity(intent1);
    }

    public void button_following(View view) {

        FOLLOW_ACTIVITY = 1;
        Intent intent2 = new Intent(this, FollowActivity.class);
        intent2.putExtras(getIntent().getExtras());
        startActivity(intent2);
    }
}
