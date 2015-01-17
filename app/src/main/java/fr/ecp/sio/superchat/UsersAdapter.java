package fr.ecp.sio.superchat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import fr.ecp.sio.superchat.model.User;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Michaël on 05/12/2014.
 */
public class UsersAdapter extends BaseAdapter implements DialogInterface.OnShowListener {

    private List<User> mUsers;

    public List<User> getUsers() {
        return mUsers;
    }
    public Context context;
    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    public UsersAdapter(Context context ) {
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        }
        final User user = getItem(position);
        TextView handleView = (TextView) convertView.findViewById(R.id.handle);
        handleView.setText(user.getHandle());
        TextView statusView = (TextView) convertView.findViewById(R.id.status);
        if (user.getStatus() != null) {
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
        }

        ImageButton button_add = (ImageButton) convertView.findViewById(R.id.add_follower);
        button_add.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                       if (AccountManager.isConnected(context)) {
                           new AsyncTask<String, Void, Integer>() {
                               @Override
                               protected Integer doInBackground(String... arg) {
                                   try {
                                       String handle = AccountManager.getUserHandle(context);
                                       String token = AccountManager.getUserToken(context);
                                       String content = user.getHandle();
                                       if(handle.compareTo(content) != 0){
                                           new ApiClient().postAddFollowing(handle, token, content);
                                           return 1;}
                                       else {
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
                                       Toast.makeText(context, "Vous suivez "+ user.getHandle(), Toast.LENGTH_SHORT).show();
                                   }
                                   else if (success == 2){
                                     Toast toast =  Toast.makeText(context, "Vous ne pouvez pas vous suivre. Choisissez un autre utilisateur à suivre", Toast.LENGTH_SHORT);
                                       toast.setGravity(Gravity.CENTER, 0, 0);
                                       toast.show();
                                   }
                                   else{
                                       Toast.makeText(context, "Une erreur s'est produite", Toast.LENGTH_SHORT).show();

                                   }
                               }
                           }.execute();

                       } else {
                           new AlertDialog.Builder(context)
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

        ImageButton button_delete = (ImageButton) convertView.findViewById(R.id.rem_follower);
        button_delete.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                if (AccountManager.isConnected(context)) {
                    new AsyncTask<String, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(String... arg) {
                            try {
                                String handle = AccountManager.getUserHandle(context);
                                String token = AccountManager.getUserToken(context);
                                String content = user.getHandle();
                                if(handle.compareTo(content) != 0)
                                    {
                                    new ApiClient().postRemoveFollowing(handle, token, content);
                                    return 1;}
                                else {
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
                                Toast.makeText(context, "Vous ne suivez plus " + user.getHandle(), Toast.LENGTH_SHORT).show();
                                Log.i(context.getClass().getName(), "Je suis dans ce context");
                                if (context.getClass().getName().compareTo(FollowingActivity.class.getName()) == 0) {
                                    Intent intent = new Intent(context, FollowerActivity.class);
                                    context.startActivity(intent);
                                }
                            }
                            else if (success == 2){
                                Toast toast =  Toast.makeText(context, "Vous ne pouvez ne plus vous suivre. Choisissez un autre utilisateur.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            else{
                                Toast.makeText(context, "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();

                } else {
                    new AlertDialog.Builder(context)
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

        ImageView profilePictureView = (ImageView) convertView.findViewById(R.id.profile_picture);
        Picasso.with(convertView.getContext()).load(user.getProfilePicture()).into(profilePictureView);
        return convertView;
    }


    @Override
    public void onShow(DialogInterface dialog) {

    }
}
