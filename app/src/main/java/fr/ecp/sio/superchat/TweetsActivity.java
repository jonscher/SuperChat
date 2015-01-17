package fr.ecp.sio.superchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by Michaël on 05/12/2014.
 */
public class TweetsActivity extends ActionBarActivity {

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

      /*  Fragment followingFragment = new FollowingFragment();
        followingFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, followingFragment)
                .commit();*/

            Intent intent1 = new Intent(this, FollowerActivity.class);
            intent1.putExtras(getIntent().getExtras());
            startActivity(intent1);
    }

     public void button_following(View view) {
      /*  Fragment followingFragment = new FollowingFragment();
        followingFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, followingFragment)
                .commit();*/

        Intent intent2 = new Intent(this, FollowingActivity.class);
        intent2.putExtras(getIntent().getExtras());
        startActivity(intent2);
    }
}
