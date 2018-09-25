package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;


public class DateTimePickerDialog extends DialogFragment {

    public static String TAG = "Confirm Dialog";

    public interface DateTimePickerDialogCompliant {
        void onDateTimeSet();
    }

    private DateTimePickerDialogCompliant caller;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer interval;

    private DatePicker datePicker;
    private TimePicker timePicker;

    public DateTimePickerDialog() {
        super();
    }

    @SuppressLint("ValidFragment")
    public DateTimePickerDialog(DateTimePickerDialogCompliant caller, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer interval){
        super();
        this.caller = caller;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.interval = interval;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_date_time, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view.findViewById(R.id.btn_datetimedialog_set).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                caller.onDateTimeSet();
            }
        });
        datePicker = (DatePicker)  view.findViewById(R.id.dp_datetimedialog);
        timePicker = (TimePicker)  view.findViewById(R.id.tp_datetimedialog);
        timePicker.setIs24HourView(true);
        if (startDateTime != null) {
            datePicker.setMinDate(startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (endDateTime != null) {
            datePicker.setMaxDate(endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        setTimePickerInterval(timePicker);

        return view;
    }
    private void setTimePickerInterval(TimePicker timePicker) {
        try {

            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / interval) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += interval) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }
//
//    @SuppressLint("NewApi")
//    private void setTimePickerInterval(TimePicker timePicker) {
//        try {
//            Class<?> classForid = Class.forName("com.android.internal.R$id");
//            // Field timePickerField = classForid.getField("timePicker");
//
//            Field field = classForid.getField("minute");
//            NumberPicker minutePicker = (NumberPicker) timePicker
//                    .findViewById(field.getInt(null));
//
//            minutePicker.setMinValue(0);
//            minutePicker.setMaxValue(2);
//            List<String> displayedValues = new ArrayList<String>();
//            for (int i = 0; i < 60; i += interval) {
//                displayedValues.add(String.format("%02d", i));
//            }
//            for (int i = 0; i < 60; i += interval) {
//                displayedValues.add(String.format("%02d", i));
//            }
//            minutePicker.setDisplayedValues(displayedValues
//                    .toArray(new String[0]));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}