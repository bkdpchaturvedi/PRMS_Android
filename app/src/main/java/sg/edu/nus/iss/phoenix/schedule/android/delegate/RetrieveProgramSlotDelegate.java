package sg.edu.nus.iss.phoenix.schedule.android.delegate;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.Scanner;

import sg.edu.nus.iss.phoenix.schedule.android.controller.ScheduleController;
import sg.edu.nus.iss.phoenix.utilities.JSONEnvelopHelper;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_SCHEDULE;

public class RetrieveProgramSlotDelegate extends AsyncTask<ZonedDateTime, Void, String> {
    private static final String TAG = RetrieveProgramSlotDelegate.class.getName();

    private ScheduleController scheduleController = null;
    private ZonedDateTime dateOfProgram;

    public RetrieveProgramSlotDelegate(ScheduleController scheduleController) {

        this.scheduleController = scheduleController;
    }

    @Override
    protected String doInBackground(ZonedDateTime... params) {
        Uri builtUri1 = Uri.parse(PRMS_BASE_URL_SCHEDULE).buildUpon().build();
        Uri builtUri = Uri.withAppendedPath(builtUri1, "?dateOfProgram=" + URLEncoder.encode(params[0].toString())).buildUpon().build();
        dateOfProgram = params[0];
        Log.v(TAG, builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
            return e.getMessage();
        }

        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) jsonResponse = scanner.next();
        } catch (IOException exception) {
            Log.v(TAG, exception.getMessage());
            jsonResponse = exception.getMessage();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String response) {
        if (scheduleController != null)
            scheduleController.programSlotsRetrieved(dateOfProgram, JSONEnvelopHelper.parseEnvelopProgramSlots(response));

    }
}
