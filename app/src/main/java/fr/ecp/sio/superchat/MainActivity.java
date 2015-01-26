package fr.ecp.sio.superchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import fr.ecp.sio.superchat.Fragments.FollowingFragment;
import fr.ecp.sio.superchat.Fragments.LoginFragment;
import fr.ecp.sio.superchat.Fragments.TweetsFragment;
import fr.ecp.sio.superchat.Fragments.UsersFragment;

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
                            ListChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
        return true;
    }
    //Méthode qui permet de rafraichir le fragment.

    public void ListChanged() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof UsersFragment)
                ((UsersFragment) fragment).reloadList();
            else {
                ((UsersFragment) fragment).reloadList();
            }
        }
    }

    //Je reload le fragment lorsque je reviens dans MainActivity.

    @Override
    public void onResume() {
        super.onResume();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof UsersFragment)
                ((UsersFragment) fragment).reloadList();
        }
    }
}