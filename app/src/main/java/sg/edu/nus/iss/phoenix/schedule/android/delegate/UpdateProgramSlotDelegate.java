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

public class UpdateProgramSlotDelegate extends AsyncTask<AbstractMap.SimpleEntry<ZonedDateTime, ProgramSlot>, Void, String> {
    private static final String TAG = UpdateProgramSlotDelegate.class.getName();

    private final ScheduleController scheduleController;

    public UpdateProgramSlotDelegate(ScheduleController scheduleController) {
        this.scheduleController = scheduleController;
    }

    @Override
    protected String doInBackground(AbstractMap.SimpleEntry<ZonedDateTime, ProgramSlot>... params) {
        Uri builtUri = Uri.parse(PRMS_BASE_URL_SCHEDULE).buildUpon().build();
        builtUri = Uri.withAppendedPath(builtUri, URLEncoder.encode(params[0].getKey().toString())).buildUpon().build();
        Log.v(TAG, builtUri.toString());
        URL url;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
            return e.getMessage();
        }

        JSONObject jsonProgramSlot = JSONHelper.toJSON(params[0].getValue());
        Log.v(TAG, jsonProgramSlot.toString());
        HttpURLConnection httpURLConnection = null;
        OutputStream dos = null;
        String jsonResponse = "";
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            dos = httpURLConnection.getOutputStream();
            dos.write(jsonProgramSlot.toString().getBytes());
            Log.v(TAG, "Http PUT response " + httpURLConnection.getResponseCode());
        } catch (IOException exception) {
            Log.v(TAG, exception.getMessage());
            jsonResponse = exception.getMessage();
        } finally {
            if (dos != null) {
                try {
                    dos.flush();
                    dos.close();
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
            scheduleController.programSlotUpdated(JSONEnvelopHelper.parseEnvelopBoolean(response));
        }
    }
}
