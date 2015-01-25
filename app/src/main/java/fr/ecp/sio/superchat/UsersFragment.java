package fr.ecp.sio.superchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

import fr.ecp.sio.superchat.loaders.UsersLoader;
import fr.ecp.sio.superchat.model.User;
import fr.ecp.sio.superchat.tabHost.TabHostActivity;

/**
 * Created by Michaël on 05/12/2014.
 */
public class UsersFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<User>> {

    private static final int LOADER_USERS = 1000;
    private static final int REQUEST_LOGIN_FOR_POST = 1;
    public static User user;
    private UsersAdapter mListAdapter;
    private boolean mIsMasterDetailsMode;

    private boolean isConnected;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isConnected = AccountManager.isConnected(getActivity());
        return inflater.inflate(R.layout.users_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListAdapter = new UsersAdapter();
        setListAdapter(mListAdapter);
        view.findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsMasterDetailsMode = getActivity().findViewById(R.id.tweets_content) != null;
        if (mIsMasterDetailsMode) {
            getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(LOADER_USERS, null, this);
        Log.i(UsersFragment.class.getName(), "ok1");
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Log.i(UsersFragment.class.getName(), "ok2");
        return new UsersLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> users) {
        mListAdapter.setUsers(users);
        Log.i(UsersFragment.class.getName(), "ok3");
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        user = mListAdapter.getItem(position);
        if (mIsMasterDetailsMode) {
            Fragment tweetsFragment = new TweetsFragment();
            Fragment followFragment = new FollowingFragment();
            Log.i(UsersFragment.class.getName(), "user: " + user);
            tweetsFragment.setArguments(TweetsFragment.newArguments(user));
            followFragment.setArguments(FollowingFragment.newArgument(user));
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.tweets_content, tweetsFragment)
                    .replace(R.id.tweets_follower, followFragment)
                    .commit();
        } else {

            Intent intent = new Intent(getActivity(), TabHostActivity.class);
            intent.putExtras(TweetsFragment.newArguments(user));
            startActivity(intent);
        }
    }

    private void post() {
        if (AccountManager.isConnected(getActivity())) {
            startActivity(new Intent(getActivity(), PostActivity.class));

        } else {
            LoginFragment fragment = new LoginFragment();
            fragment.setTargetFragment(this, REQUEST_LOGIN_FOR_POST);
            fragment.show(getFragmentManager(), "login_dialog");
            Log.i(getActivity().getClass().getName().toString(), "je suis ds cette act1");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN_FOR_POST && resultCode == PostActivity.RESULT_OK) {
            ((MainActivity) getActivity()).ListChanged();

            post();

            //  startActivity(new Intent(getActivity(), PostActivity.class));


            //        getListView().invalidate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AccountManager.isConnected(getActivity()) != isConnected){
            isConnected = AccountManager.isConnected(getActivity());
        }
    }
}