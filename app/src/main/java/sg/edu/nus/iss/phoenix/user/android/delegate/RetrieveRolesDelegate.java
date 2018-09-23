package sg.edu.nus.iss.phoenix.user.android.delegate;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sg.edu.nus.iss.phoenix.core.android.controller.entity.Role;
import sg.edu.nus.iss.phoenix.user.android.controller.UserController;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.user.android.controller.UserController;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_USER;

public class RetrieveRolesDelegate extends AsyncTask<String, Void, String> {
    // Tag for logging
    private static final String TAG = RetrieveRolesDelegate.class.getName();

    private UserController userController = null;

    public RetrieveRolesDelegate(UserController userController) {
        this.userController = userController;
    }

    @Override
    protected String doInBackground(String... params) {
        Uri builtUri1 = Uri.parse( PRMS_BASE_URL_USER).buildUpon().build();
        Uri builtUri = Uri.withAppendedPath(builtUri1, params[0]).buildUpon().build();
        Log.v(TAG, builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
            return e.getMessage();
        }

        String jsonResp = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) jsonResp = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return jsonResp;
    }

    @Override
    protected void onPostExecute(String result) {
        List<Role> roles = new ArrayList<>();

        if (result != null && !result.equals("")) {
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray roleArray = reader.getJSONArray("roleList");

                for (int i = 0; i < roleArray.length(); i++) {
                    JSONObject roleJson = roleArray.getJSONObject(i);
                    String role = roleJson.getString("role");
                    String accesPrivilege = roleJson.getString("accesPrivilege");

                    roles.add( new Role(role,accesPrivilege));
                }
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Log.v(TAG, "JSON response error.");
        }

        if (userController != null)
            userController.rolesRetrieved(roles);
    }
}
