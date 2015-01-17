package fr.ecp.sio.superchat.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

import fr.ecp.sio.superchat.ApiClient;
import fr.ecp.sio.superchat.model.User;

/**
 * Created by jonathan on 04/01/2015.
 */
public class FollowingsLoader extends AsyncTaskLoader<List<User>> {

    private List<User> mResult;
    private String mHandle;

    public FollowingsLoader(Context context, String Handle)  {
        super(context);
        mHandle = Handle;
    }

    @Override
    public List<User> loadInBackground() {
        try{
            return new ApiClient().getUserFollowing(mHandle);
        } catch (Exception e){
            Log.e(FollowingsLoader.class.getName(), "Failed to download Followings", e);
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
        Log.i(FollowingsLoader.class.getName(), "data: " + data);
        Log.i(FollowingsLoader.class.getName(), "ca marche3");
        mResult = data;
        super.deliverResult(data);

    }
}

