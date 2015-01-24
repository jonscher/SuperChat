package fr.ecp.sio.superchat;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

//pour se connecter à Jerome ds l'appli: user:Jerome et mdp:test
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            UsersFragment usersFragment = new UsersFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, usersFragment)
                    .commit();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (AccountManager.isConnected(MainActivity.this)) {
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        }
        if (id == R.id.menu_connexion) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            LoginFragment editNameDialog = new LoginFragment();
            editNameDialog.show(fm, "fragment_edit_name");
        }
        if (id == R.id.menu_disconnect) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.logout)
                    .setMessage(R.string.logout_confirm)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AccountManager.logout(MainActivity.this);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
        return true;
    }
    public void ListChanged() {
            Fragment usersfragments = new UsersFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, usersfragments)
                    .commit();
    }
}
