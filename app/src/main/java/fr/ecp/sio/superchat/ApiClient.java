package fr.ecp.sio.superchat;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import fr.ecp.sio.superchat.model.Tweet;
import fr.ecp.sio.superchat.model.User;

/**
 * Created by Michaël on 12/12/2014.
 */
public class ApiClient {
    // private static final String API_BASE = "http://vps130778.ovh.net:5666/mongo/";
    // private static final String API_BASE = "http://10.0.2.2:5660/mongo/";
    private static final String API_BASE = "http://hackndo.com:5667/mongo/";

    public String login(String handle, String password) throws IOException {
        String url = Uri.parse(API_BASE + "session/").buildUpon()
                .appendQueryParameter("handle", handle)
                .appendQueryParameter("password", password)
                .build().toString();
        Log.i(ApiClient.class.getName(), "Login: " + url);
        InputStream stream = new URL(url).openStream();
        String token = IOUtils.toString(stream);
        Log.i(ApiClient.class.getName(), "resultat login: " + token);
        return token;
    }

    public List<User> getUsers() throws IOException {
        InputStream stream = new URL(API_BASE + "users/").openStream();
        String response = IOUtils.toString(stream);
        return Arrays.asList(new Gson().fromJson(response, User[].class));
    }

    public List<User> getUsersConnected(String token) throws IOException {
        String url = Uri.parse(API_BASE + "users/").buildUpon()
                .build().toString();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Authorization", "Bearer-" + token);
        InputStream stream = connection.getInputStream();
        String response = IOUtils.toString(stream);
        return Arrays.asList(new Gson().fromJson(response, User[].class));
    }

    public List<Tweet> getUserTweets(String handle) throws IOException {
        InputStream stream = new URL(API_BASE + handle + "/tweets/").openStream();
        String response = IOUtils.toString(stream);
        return Arrays.asList(new Gson().fromJson(response, Tweet[].class));
    }

    public List<User> getUserFollowers(String handle) throws IOException {
        InputStream stream = new URL(API_BASE + handle + "/followers/").openStream();
        String response = IOUtils.toString(stream);
        return Arrays.asList(new Gson().fromJson(response, User[].class));
    }

    public List<User> getUserFollowing(String handle) throws IOException {
        InputStream stream = new URL(API_BASE + handle + "/followings/").openStream();
        String response = IOUtils.toString(stream);
        return Arrays.asList(new Gson().fromJson(response, User[].class));
    }

    public void postTweet(String handle, String token, String content) throws IOException {
        String url = Uri.parse(API_BASE + handle + "/tweets/post/").buildUpon()
                .appendQueryParameter("content", content)
                .build().toString();
        Log.i(ApiClient.class.getName(), "Ad tweet: " + url + " token :" + token + " handle:" + handle);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Authorization", "Bearer-" + token);
        connection.getInputStream();
    }

    public void postAddFollowing(String handle, String token, String content) throws IOException {
        String url = Uri.parse(API_BASE + handle + "/followings/post/").buildUpon()
                .appendQueryParameter("handle", content)
                .build().toString();
        Log.i(ApiClient.class.getName(), "postaddfollowing: " + url + " token :" + token + " handle:" + handle + "following:" + content);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Authorization", "Bearer-" + token);
        connection.getInputStream();
    }

    public void postDeleteFollowing(String handle, String token, String content) throws IOException {
        String url = Uri.parse(API_BASE + handle + "/followings/delete/").buildUpon()
                .appendQueryParameter("handle", content)
                .build().toString();
        Log.i(ApiClient.class.getName(), "postRemovefollowing: " + url + " token :" + token + " handle:" + handle + "followingdeleted:" + content);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Authorization", "Bearer-" + token);
        connection.getInputStream();
    }

}