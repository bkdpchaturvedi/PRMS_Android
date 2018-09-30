package sg.edu.nus.iss.phoenix.schedule.android.delegate;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.AbstractMap;

import sg.edu.nus.iss.phoenix.schedule.android.controller.ScheduleController;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.utilities.JSONEnvelopHelper;
import sg.edu.nus.iss.phoenix.utilities.JSONHelper;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_SCHEDULE;

public class DeleteProgramSlotDelegate extends AsyncTask <ZonedDateTime, Void, String> {
    private static final String TAG = DeleteProgramSlotDelegate.class.getName();

    private final ScheduleController scheduleController;

    public DeleteProgramSlotDelegate(ScheduleController scheduleController) {
        this.scheduleController = scheduleController;
    }

    @Override
    protected String doInBackground(ZonedDateTime... params) {
        Uri builtUri = Uri.parse(PRMS_BASE_URL_SCHEDULE).buildUpon().build();
        builtUri = Uri.withAppendedPath(builtUri, URLEncoder.encode(params[0].toString())).buildUpon().build();
        Log.v(TAG, builtUri.toString());
        URL url;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
            return e.getMessage();
        }

        HttpURLConnection httpURLConnection = null;
        String jsonResponse = "";
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setUseCaches (false);
            httpURLConnection.setDoInput(true);
            Log.v(TAG, "Http DELETE response " + httpURLConnection.getResponseCode());
        } catch (IOException exception) {
            Log.v(TAG, exception.getMessage());
            jsonResponse = exception.getMessage();
        } finally {
            if (httpURLConnection != null) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        jsonResponse += line;
                    }
                    Log.v(TAG, jsonResponse);
                } catch (IOException exception) {
                    Log.v(TAG, exception.getMessage());
                    jsonResponse = exception.getMessage();
                }
            }
            if (httpURLConnection != null) httpURLConnection.disconnect();
        }
       return jsonResponse;
    }

    @Override
    protected void onPostExecute(String response) {
        if (scheduleController != null) {
            scheduleController.programSlotDeleted(JSONEnvelopHelper.parseEnvelopBoolean(response));
        }
    }
}
