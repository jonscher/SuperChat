package fr.ecp.sio.superchat.tabHost;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import fr.ecp.sio.superchat.AccountManager;
import fr.ecp.sio.superchat.FollowerFragment;
import fr.ecp.sio.superchat.FollowingFragment;
import fr.ecp.sio.superchat.LoginFragment;
import fr.ecp.sio.superchat.R;
import fr.ecp.sio.superchat.SettingsActivity;
import fr.ecp.sio.superchat.TweetsFragment;
import fr.ecp.sio.superchat.UsersFragment;

public class TabHostActivity extends ActionBarActivity implements OnTabChangeListener, OnPageChangeListener {

    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    private static final int REQUEST_LOGIN_FOR_POST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host_activity);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Tab Initialization
        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(TabHostActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (AccountManager.isConnected(TabHostActivity.this)) {
            getMenuInflater().inflate(R.menu.menu_disconnect, menu);

        } else {
            getMenuInflater().inflate(R.menu.menu_connect, menu);

        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (AccountManager.isConnected(TabHostActivity.this)) {
            getMenuInflater().inflate(R.menu.menu_disconnect, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_connect, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.menu_connection) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            LoginFragment fragment = new LoginFragment();
            fragment.show(fm, "fragment_edit_name");
        }

        if (id == R.id.menu_disconnect) {
            new AlertDialog.Builder(TabHostActivity.this)
                    .setTitle(R.string.logout)
                    .setMessage(R.string.logout_confirm)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AccountManager.logout(TabHostActivity.this);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }


    // Method to add a TabHost
    private static void AddTab(TabHostActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        // TODO Put here your Fragments
        Fragment tweetsFragment = new TweetsFragment();
        tweetsFragment.setArguments(getIntent().getExtras());
        fList.add(tweetsFragment);
        Fragment followingFragment = new FollowingFragment();
        followingFragment.setArguments(getIntent().getExtras());
        fList.add(followingFragment);
        Fragment followerFragment = new FollowerFragment();
        followerFragment.setArguments(getIntent().getExtras());
        fList.add(followerFragment);

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        TabHostActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tweets").setIndicator("Tweets"));
        TabHostActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Following").setIndicator("Following"));
        TabHostActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Follower").setIndicator("Follower"));

        mTabHost.setOnTabChangedListener(this);
    }
    public void lanchActivity(){
        Fragment followFragment = new UsersFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.test, followFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN_FOR_POST) {


            Log.i(this.getClass().getName().toString(), "je suis ds TABHOST");
            //        getListView().invalidate();
        }
    }
    public void ListChanged() {
        Fragment followfragments = new FollowingFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, followfragments)
                .commit();
    }
}