package fr.ecp.sio.superchat.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import fr.ecp.sio.superchat.AccountManager;
import fr.ecp.sio.superchat.ApiClient;
import fr.ecp.sio.superchat.Fragments.FollowingFragment;
import fr.ecp.sio.superchat.Fragments.TweetsFragment;
import fr.ecp.sio.superchat.Fragments.UsersFragment;
import fr.ecp.sio.superchat.MainActivity;
import fr.ecp.sio.superchat.PostActivity;
import fr.ecp.sio.superchat.R;
import fr.ecp.sio.superchat.TabHostActivity;
import fr.ecp.sio.superchat.model.User;

/**
 * Created by Michaël on 05/12/2014.
 */
public class UsersAdapter extends BaseAdapter {

    private static List<User> mUsers;

    public List<User> getUsers() {
        return mUsers;
    }

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
        TabHostActivity.setmUsers(mUsers);
    }

    @Override
    public int getCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    @Override
    public User getItem(int position) {
        if (mUsers== null){
            return TabHostActivity.getmUsers().get(position);
        }else
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (getItem(position).getId() == null) {
            return 0;
        } else {
            return getItem(position).getId().hashCode();
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        }
        final User user = getItem(position);
        TextView handleView = (TextView) convertView.findViewById(R.id.handle);
        handleView.setText(user.getHandle());
        TextView statusView = (TextView) convertView.findViewById(R.id.status);
        switch (user.getStatus()) {
            case "online":
                statusView.setText(R.string.online);
                break;
            case "offline":
                statusView.setText(R.string.offline);
                break;
            default:
                statusView.setText("");
        }
        ImageView profilePictureView = (ImageView) convertView.findViewById(R.id.profile_picture);
        Picasso.with(convertView.getContext()).load(user.getProfilePicture()).into(profilePictureView);
        final ImageButton button_delete = (ImageButton) convertView.findViewById(R.id.rem_following);
        final ImageButton button_add = (ImageButton) convertView.findViewById(R.id.add_following);

// J'afiche les boutons dans certaines conditions
        if ((AccountManager.isConnected(parent.getContext()) && !user.getHandle().equals(AccountManager.getUserHandle(parent.getContext())) && parent.getContext().getClass().equals(MainActivity.class)) || ((AccountManager.isConnected(parent.getContext()) && parent.getContext().getClass().equals(TabHostActivity.class) && !user.getHandle().equals(AccountManager.getUserHandle(parent.getContext())) &&
                TweetsFragment.mUser.getHandle().equals(AccountManager.getUserHandle(parent.getContext()))))) {
            if (user.isFollowing()) {
                button_delete.setVisibility(View.VISIBLE);
                button_add.setVisibility(View.GONE);
            } else {
                button_delete.setVisibility(View.GONE);
                button_add.setVisibility(View.VISIBLE);
            }
        } else {
            if (parent.getContext().getClass().equals(TabHostActivity.class)) {
                button_add.setVisibility(View.GONE);
                button_delete.setVisibility(View.VISIBLE);
            }
            button_add.setVisibility(View.GONE);
            button_delete.setVisibility(View.GONE);
        }

// Au moment du clic, des actions sont réalisées: Connection à l'API, Message de confirmation ou d'erreur,
// actualisation de la liste si je suis dans TabHostActivity.

        button_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AsyncTask<String, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(String... arg) {
                        try {
                            String handle = AccountManager.getUserHandle(parent.getContext());
                            String token = AccountManager.getUserToken(parent.getContext());
                            String content = user.getHandle();
                            new ApiClient().postAddFollowing(handle, token, content);
                            return 1;
                        } catch (IOException e) {
                            Log.e(PostActivity.class.getName(), "Post failed", e);
                            return 0;
                        }
                    }

                    @Override
                    protected void onPostExecute(Integer success) {
                        if (success == 1) {
                            Toast.makeText(parent.getContext(), "Vous suivez " + user.getHandle(), Toast.LENGTH_SHORT).show();
                            button_add.setVisibility(View.GONE);
                            button_delete.setVisibility(View.VISIBLE);
                            user.setFollowing(true);
                            if (parent.getContext() instanceof MainActivity && UsersFragment.mIsMasterDetailsMode) {
                                ((MainActivity) parent.getContext()).ListChanged();
                            }
                        } else {
                            Toast.makeText(parent.getContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });


        button_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AsyncTask<String, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(String... arg) {
                        try {
                            String handle = AccountManager.getUserHandle(parent.getContext());
                            String token = AccountManager.getUserToken(parent.getContext());
                            String content = user.getHandle();
                            new ApiClient().postDeleteFollowing(handle, token, content);
                            return 1;
                        } catch (IOException e) {
                            Log.e(PostActivity.class.getName(), "Post failed", e);
                            return 0;
                        }
                    }

                    @Override
                    protected void onPostExecute(Integer success) {
                        if (success == 1) {
                            Toast.makeText(parent.getContext(), "Vous ne suivez plus " + user.getHandle(), Toast.LENGTH_SHORT).show();
                            user.setFollowing(false);
                            if (parent.getContext() instanceof TabHostActivity) {
                                ((TabHostActivity) parent.getContext()).ListChanged();
                            } else if (parent.getContext() instanceof MainActivity && UsersFragment.mIsMasterDetailsMode) {
                                ((MainActivity) parent.getContext()).ListChanged();
                            } else {
                                button_delete.setVisibility(View.GONE);
                                button_add.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(parent.getContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });
        return convertView;
    }
}

