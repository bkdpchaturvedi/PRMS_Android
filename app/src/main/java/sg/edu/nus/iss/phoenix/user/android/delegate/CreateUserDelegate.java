package sg.edu.nus.iss.phoenix.user.android.delegate;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import sg.edu.nus.iss.phoenix.core.android.controller.entity.Role;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.user.android.controller.UserController;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_USER;

public class CreateUserDelegate extends AsyncTask<User, Void, Boolean> {
    // Tag for logging
    private static final String TAG = CreateUserDelegate.class.getName();

    private final UserController userController;

    public CreateUserDelegate(UserController userController) {
        this.userController = userController;
    }

    @Override
    protected Boolean doInBackground(User... params) {
        boolean success = false;
        Uri builtUri = Uri.parse(PRMS_BASE_URL_USER).buildUpon().build();
        builtUri = Uri.withAppendedPath(builtUri,"create").buildUpon().build();
        Log.v(TAG, builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
            return new Boolean(false);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("id", params[0].getId());
            json.put("name", params[0].getUserName());
            json.put("password", params[0].getUserPassword());
            JSONArray roles = toJson(params[0].getUserRoles());
            json.put("roles",roles);
        } catch (JSONException e) {
            Log.v(TAG, e.getMessage());
        }

        HttpURLConnection httpURLConnection = null;
        DataOutputStream dos = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            //httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.getOutputStream().write(json.toString().getBytes());
            //dos = new DataOutputStream(httpURLConnection.getOutputStream());
            //dos.writeUTF(json.toString());
            //dos.write(256);
            Log.v(TAG, "Http PUT response " + httpURLConnection.getResponseCode());
            success = true;
        } catch (IOException exception) {
            Log.v(TAG, exception.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.flush();
                    dos.close();
                } catch (IOException exception) {
                    Log.v(TAG, exception.getMessage());
                }
            }
            if (httpURLConnection != null) httpURLConnection.disconnect();
        }
        return new Boolean(success);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        userController.userCreated(result.booleanValue());
    }

    private  JSONArray toJson(ArrayList<Role> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Role obj : list) {
            JSONObject item = new JSONObject();
            item.put("role",obj.getRole());
            item.put("accessPrivilege",obj.getAccessPrivilege());
            jsonArray.put(item);
        }
        return jsonArray;
    }
}
