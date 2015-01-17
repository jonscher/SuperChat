package fr.ecp.sio.superchat.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import fr.ecp.sio.superchat.ApiClient;
import fr.ecp.sio.superchat.model.User;

/**
 * Created by jonathan on 03/01/2015.
 */
public class FollowersLoader extends AsyncTaskLoader<List<User>>{

    private List<User> mResult;
    private String mHandle;

    public FollowersLoader(Context context, String Handle) {
        super(context);
        mHandle = Handle;
    }

    @Override
    public List<User> loadInBackground() {
        try{
            return new ApiClient().getUserFollowers(mHandle);
        } catch (Exception e){
            Log.e(FollowersLoader.class.getName(),"Failed to download Follower", e);
            return null;
        }
      }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mResult != null){
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
        Log.i(FollowersLoader.class.getName(), "data: " + data);
        Log.i(FollowersLoader.class.getName(), "ca marche2");
        mResult = data;
        super.deliverResult(data);

    }
}

