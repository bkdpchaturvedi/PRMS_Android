package sg.edu.nus.iss.phoenix.schedule.android.delegate;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.schedule.android.controller.ScheduleController;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_SCHEDULE;

public class RetrieveProgramSlotDelegate extends AsyncTask<String,Void,String> {
    private static final String TAG = RetrieveProgramSlotDelegate.class.getName();

    private ScheduleController scheduleController = null;

    public RetrieveProgramSlotDelegate(ScheduleController scheduleController) {

        this.scheduleController = scheduleController;
    }
    @Override
    protected String doInBackground(String... params) {
        Uri builtUri1 = Uri.parse( PRMS_BASE_URL_SCHEDULE).buildUpon().build();
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
        ArrayList<ProgramSlot> programSlots = new ArrayList<>();

        if (result != null && !result.equals("")) {
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray rpArray = reader.getJSONArray("data");

                for (int i = 0; i < rpArray.length(); i++) {
                    JSONObject rpJson = rpArray.getJSONObject(i);
                    JSONObject rp = rpJson.getJSONObject("radioProgram");
                    RadioProgram radioProgram=new RadioProgram(rp.getString("name"),"","");
                    JSONObject presenterJson = rpJson.getJSONObject("presenter");
                    User presenter =new User(presenterJson.getString("id"),"","","");
                    JSONObject producerJson = rpJson.getJSONObject("producer");
                    User producer =new User(producerJson.getString("id"),"","","");

                    String duration=rpJson.getString("duration");
                    String dateOfProgram=rpJson.getString("dateOfProgram");
                    String assignedBy=rpJson.getString("assignedBy");

                   ProgramSlot temProgramSlot=new ProgramSlot();
                   temProgramSlot.setAssignedBy(assignedBy);
                   // String dateString = "03/26/2012 11:49:00 AM";
                    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-mm-dd hh:mm:ss aa");
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(dateOfProgram);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                   temProgramSlot.setDateOfProgram(convertedDate);
                    temProgramSlot.setPresenter(presenter);
                    temProgramSlot.setProducer(producer);
                    temProgramSlot.setRadioProgram(radioProgram);
                    programSlots.add(temProgramSlot);
                }
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Log.v(TAG, "JSON response error.");
        }

        if (scheduleController != null)
            scheduleController.programSlotsRetrieved(programSlots);

    }
}
