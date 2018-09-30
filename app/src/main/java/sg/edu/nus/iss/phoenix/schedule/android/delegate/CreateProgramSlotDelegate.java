package sg.edu.nus.iss.phoenix.schedule.android.delegate;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import sg.edu.nus.iss.phoenix.restful.JSONEnvelop;
import sg.edu.nus.iss.phoenix.schedule.android.controller.ScheduleController;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.radioprogram.android.controller.ProgramController;
import sg.edu.nus.iss.phoenix.utilities.JSONEnvelopHelper;
import sg.edu.nus.iss.phoenix.utilities.JSONHelper;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_SCHEDULE;

public class CreateProgramSlotDelegate extends AsyncTask<ProgramSlot, Void, String> {
    private static final String TAG = CreateProgramSlotDelegate.class.getName();

    private final ScheduleController scheduleController;

    public CreateProgramSlotDelegate(ScheduleController scheduleController) {
        this.scheduleController = scheduleController;
    }

    @Override
    protected String doInBackground(ProgramSlot... params) {
        Uri builtUri = Uri.parse(PRMS_BASE_URL_SCHEDULE).buildUpon().build();
        builtUri = Uri.withAppendedPath(builtUri,"").buildUpon().build();
        Log.v(TAG, builtUri.toString());
        URL url;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
            return e.getMessage();
        }

        JSONObject jsonProgramSlot = JSONHelper.toJSON(params[0]);
        Log.v(TAG, jsonProgramSlot.toString());
        HttpURLConnection httpURLConnection = null;
        OutputStream dos = null;
        String jsonResponse = "";
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            dos = httpURLConnection.getOutputStream();
            dos.write(jsonProgramSlot.toString().getBytes());
            Log.v(TAG, "Http POST response " + httpURLConnection.getResponseCode());
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                jsonResponse += line;
            }
            Log.v(TAG, jsonResponse);
//            jsonResp = httpURLConnection.getResponseMessage();
//            InputStream in = httpURLConnection.getInputStream();
//            Scanner scanner = new Scanner(in);
//            scanner.useDelimiter("\\A");
//            if (scanner.hasNext()) jsonResp = scanner.next();
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
       return jsonResponse;
    }

    @Override
    protected void onPostExecute(String response) {
        if (scheduleController != null) {
            scheduleController.programSlotCreated(JSONEnvelopHelper.parseEnvelopBoolean(response));
        }
    }
}
