package fr.ecp.sio.superchat.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import fr.ecp.sio.superchat.AccountManager;
import fr.ecp.sio.superchat.ApiClient;
import fr.ecp.sio.superchat.model.User;

/**
 * Created by Michaël on 05/12/2014.
 */
public class UsersLoader extends AsyncTaskLoader<List<User>> {

    private List<User> mResult;

    public UsersLoader(Context context) {
        super(context);
    }

    @Override
    public List<User> loadInBackground() {
        try {
            if (AccountManager.isConnected(getContext())) {
                return new ApiClient().getUsersConnected(AccountManager.getUserToken(getContext()));

            } else {
                return new ApiClient().getUsers();
            }
        } catch (IOException e) {
            Log.e(UsersLoader.class.getName(), "Failed to download users", e);
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mResult != null) {
            deliverResult(mResult);
        }
        if (takeContentChanged() || mResult == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    public void deliverResult(List<User> data) {
        mResult = data;
        super.deliverResult(data);
    }

}