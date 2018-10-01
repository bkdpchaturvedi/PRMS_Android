package sg.edu.nus.iss.phoenix.schedule.android.delegate;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import sg.edu.nus.iss.phoenix.schedule.android.controller.ReviewSelectPresenterProducerController;
import sg.edu.nus.iss.phoenix.utilities.JSONEnvelopHelper;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_USER;

public class RetrievePresenterProducerDelegate extends AsyncTask<String, Void, String> {

    private static final String TAG = RetrieveProgramSlotDelegate.class.getName();
    private String param = "";
    private ReviewSelectPresenterProducerController reviewSelectPresenterProducerController = null;

    public RetrievePresenterProducerDelegate(ReviewSelectPresenterProducerController reviewSelectPresenterProducerController) {

        this.reviewSelectPresenterProducerController = reviewSelectPresenterProducerController;
    }

    @Override
    protected String doInBackground(String... params) {
        this.param = params[0];
        Uri builtUri1 = Uri.parse(PRMS_BASE_URL_USER).buildUpon().build();
        Uri builtUri = Uri.withAppendedPath(builtUri1, param).buildUpon().build();
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
            Log.v(TAG, e.getMessage());
            jsonResp = e.getMessage();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return jsonResp;
    }


    @Override
    protected void onPostExecute(String response) {
        if (reviewSelectPresenterProducerController != null) {
            switch (param) {
                case "presenters":
                    reviewSelectPresenterProducerController.presentersRetrieved(JSONEnvelopHelper.parseEnvelopUsers(response));
                    break;
                case "producers":
                    reviewSelectPresenterProducerController.producersRetrieved(JSONEnvelopHelper.parseEnvelopUsers(response));
                    break;
            }

        }
    }
}
