package fr.ecp.sio.superchat.Adapters;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import fr.ecp.sio.superchat.R;
import fr.ecp.sio.superchat.model.User;

/**
 * Created by Michaël on 05/12/2014.
 */
public class FollowerAdapter extends BaseAdapter implements DialogInterface.OnShowListener {

    private List<User> mUsers;
    Integer BUTTON_ACTIVATE = 0;

    public List<User> getUsers() {
        return mUsers;
    }


    public void setUsers(List<User> users) {
        mUsers = users;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_item, parent, false);
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


        ImageView profilePictureView = (ImageView) convertView.findViewById(R.id.profile_picture);
        Picasso.with(convertView.getContext()).load(user.getProfilePicture()).into(profilePictureView);
        return convertView;
    }

    @Override
    public void onShow(DialogInterface dialog) {

    }
}
