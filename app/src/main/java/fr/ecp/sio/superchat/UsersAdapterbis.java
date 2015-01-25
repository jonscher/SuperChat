package fr.ecp.sio.superchat;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
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

import fr.ecp.sio.superchat.model.User;
import fr.ecp.sio.superchat.tabHost.TabHostActivity;

/**
 * Created by Michaël on 05/12/2014.
 */
public class UsersAdapterbis extends BaseAdapter implements DialogInterface.OnShowListener {
    private List<User> mUsers;

    public List<User> getUsers() {
        return mUsers;
    }

    public void setUsers(List<User> users) {
        mUsers = users;

        //   notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mUsers != null ? mUsers.size() : 0;
    }


    @Override
    public User getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        //  if (convertView == null) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        //       }
        final User user = getItem(position);
        final TextView handleView = (TextView) convertView.findViewById(R.id.handle);
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

        final ImageButton button_add = (ImageButton) convertView.findViewById(R.id.add_following);
        if (user.isFollowing() == true || AccountManager.isConnected(parent.getContext())==false){
            button_add.setVisibility(View.GONE);
        }
        button_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (AccountManager.isConnected(parent.getContext())) {
                    new AsyncTask<String, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(String... arg) {
                            try {
                                String handle = AccountManager.getUserHandle(parent.getContext());
                                String token = AccountManager.getUserToken(parent.getContext());
                                String content = user.getHandle();
                                if (handle.compareTo(content) != 0) {
                                    new ApiClient().postAddFollowing(handle, token, content);
                                    return 1;
                                } else {
                                    return 2;
                                }
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

                            } else if (success == 2) {
                                Toast toast = Toast.makeText(parent.getContext(), "Vous ne pouvez pas vous suivre. Choisissez un autre utilisateur à suivre", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                Toast.makeText(parent.getContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();

                } else {
                    new AlertDialog.Builder(parent.getContext())
                            .setMessage("Veuillez vous connecter")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });

        final ImageButton button_delete = (ImageButton) convertView.findViewById(R.id.rem_following);

        // if (AccountManager.isConnected(parent.getContext()) == false){
        if (user.isFollowing()== false){
            button_delete.setVisibility(View.GONE);
        }
        button_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (AccountManager.isConnected(parent.getContext())) {
                    final AsyncTask<String, Void, Integer> execute = new AsyncTask<String, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(String... arg) {
                            try {
                                String handle = AccountManager.getUserHandle(parent.getContext());
                                String token = AccountManager.getUserToken(parent.getContext());
                                String content = user.getHandle();
                                if (handle.compareTo(content) != 0) {
                                    new ApiClient().postRemoveFollowing(handle, token, content);
                                    return 1;
                                } else {
                                    return 2;
                                }
                            } catch (IOException e) {
                                Log.e(PostActivity.class.getName(), "Post failed", e);
                                return 0;
                            }
                        }

                        @Override
                        protected void onPostExecute(Integer success) {
                            if (success == 1) {

                                Toast.makeText(parent.getContext(), "Vous ne suivez plus " + user.getHandle(), Toast.LENGTH_SHORT).show();
                                Log.i(parent.getContext().getClass().getName(), "Je suis dans ce context");



                            }

                            else if (success == 2) {
                                Toast toast = Toast.makeText(parent.getContext(), "Vous ne pouvez ne plus vous suivre. Choisissez un autre utilisateur.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                Toast.makeText(parent.getContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();

                } else {
                    new AlertDialog.Builder(parent.getContext())
                            .setMessage("Veuillez vous connecter")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });
        return convertView;
    }

    @Override
    public void onShow(DialogInterface dialog) {

    }
}
