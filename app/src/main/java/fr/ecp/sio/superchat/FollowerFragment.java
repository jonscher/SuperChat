package fr.ecp.sio.superchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import java.util.List;

import fr.ecp.sio.superchat.loaders.FollowersLoader;
import fr.ecp.sio.superchat.model.User;

/**
 * Created by jonathan on 04/01/2015.
 */
public class FollowerFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<User>> {

    private static final int LOADER_FOLLOWINGS = 1080;

    private static final String ARG_USER = "user";

    private User mUser;

    private FollowerAdapter mListAdapter;

    public static Bundle newArgument(User user) {
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
        mListAdapter = new FollowerAdapter();
        getListView().setDividerHeight(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof TabHostActivity) {
            getActivity().setTitle(mUser.getHandle());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(LOADER_FOLLOWINGS, null, this);
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new FollowersLoader(getActivity(), mUser.getHandle());

    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> follow) {
        mListAdapter.setUsers(follow);
        setListAdapter(mListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
    }
}