package fr.ecp.sio.superchat.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import fr.ecp.sio.superchat.AccountManager;
import fr.ecp.sio.superchat.ApiClient;
import fr.ecp.sio.superchat.MainActivity;
import fr.ecp.sio.superchat.R;
import fr.ecp.sio.superchat.TabHostActivity;

/**
 * Created by Michaël on 12/12/2014.
 */
public class LoginFragment extends DialogFragment implements DialogInterface.OnShowListener {

    private EditText mHandleText;
    private EditText mPasswordText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.login_fragment, null);
        mHandleText = (EditText) view.findViewById(R.id.handle);
        mPasswordText = (EditText) view.findViewById(R.id.password);

        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.login)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        final String handle = mHandleText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (handle.isEmpty()) {
            mHandleText.setError(getString(R.string.required));
            mHandleText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPasswordText.setError(getString(R.string.required));
            mPasswordText.requestFocus();
            return;
        }

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String handle = params[0];
                    String password = params[1];
                    String token = new ApiClient().login(handle, password);
                    return token;
                } catch (IOException e) {
                    Log.e(LoginFragment.class.getName(), "Login failed", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String token) {
                if (token != null) {
                    AccountManager.login(getActivity(), token, handle);
                    Fragment target = getTargetFragment();
                    if (target != null) {
                        target.onActivityResult(getTargetRequestCode(), MainActivity.RESULT_OK, null);
                    }
                    dismiss();
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).ListChanged();
                    } else {
                        ((TabHostActivity) getActivity()).ListChanged();
                    }
                    Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.login_error, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(handle, password);
    }
}