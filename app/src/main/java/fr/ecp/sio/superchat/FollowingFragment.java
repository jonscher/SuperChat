package fr.ecp.sio.superchat;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import java.util.List;

import fr.ecp.sio.superchat.loaders.FollowingsLoader;
import fr.ecp.sio.superchat.model.User;

/**
 * Created by jonathan on 04/01/2015.
 */
public class FollowingFragment  extends ListFragment implements LoaderManager.LoaderCallbacks<List<User>> {

    private static final int LOADER_FOLLOWINGS = 1080;

    private static final String ARG_USER = "user";

    private User mUser;

    private UsersAdapter mListAdapter;

    public static Bundle newArgument (User user) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getArguments().getParcelable(ARG_USER);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListAdapter = new UsersAdapter(getActivity());
        getListView().setDividerHeight(0);
    }


    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(LOADER_FOLLOWINGS, null, this);
    }


    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new FollowingsLoader(getActivity(), mUser.getHandle());
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> followings) {
        mListAdapter.setUsers(followings);
        setListAdapter(mListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {

    }
}
