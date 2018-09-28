package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class DateTimePickerDialog extends DialogFragment implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    public static final String TAG = DateTimePickerDialog.class.getName();


    public interface DateTimeSetListener {
        void onDateTimeSet(ZonedDateTime value, String field);
    }

    private DateTimeSetListener onDateTimeSetListener;
    private int minuteInterval = 1;
    private int hourInterval = 1;

    private ZonedDateTime selectedDateTime;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    private DatePicker datePicker;
    private TimePicker timePicker;

    private  String field;

    public DateTimePickerDialog() {
        super();
    }

    @SuppressLint("ValidFragment")
    public DateTimePickerDialog(ZonedDateTime selectedDateTime, ZonedDateTime startDateTime, ZonedDateTime endDateTime, Integer hourInterval, Integer minuteInterval, String field)
    {
        super();
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        if (selectedDateTime == null) {
            selectedDateTime = ZonedDateTime.now();
        }
        this.selectedDateTime = selectedDateTime;
        this.hourInterval = hourInterval;
        this.minuteInterval = minuteInterval;
        this.field = field;
    }

    public void setOnDateTimeSetListener(DateTimeSetListener dateTimeSetListener) {
        this.onDateTimeSetListener = dateTimeSetListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_date_time, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view.findViewById(R.id.btn_datetimedialog_set).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (onDateTimeSetListener!=null) {
                    onDateTimeSetListener.onDateTimeSet(selectedDateTime, field);
                }
                dismiss();
            }
        });
        datePicker = (DatePicker) view.findViewById(R.id.dp_datetimedialog);
        timePicker = (TimePicker) view.findViewById(R.id.tp_datetimedialog);
        datePicker.setOnDateChangedListener(this);
        timePicker.setOnTimeChangedListener(this);
        timePicker.setIs24HourView(true);
        if (selectedDateTime != null) {
            datePicker.init(selectedDateTime.getYear(), selectedDateTime.getMonthValue() - 1, selectedDateTime.getDayOfMonth(), this);
            timePicker.setHour(selectedDateTime.getHour());
            timePicker.setMinute(selectedDateTime.getMinute());
        }
        if (startDateTime != null) {
            datePicker.setMinDate(startDateTime.toInstant().toEpochMilli());
        }
        if (endDateTime != null) {
            datePicker.setMaxDate(endDateTime.toInstant().toEpochMilli());
        }

        onDateChanged(datePicker, 0, 0, 0);

        return view;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        onDateTimeChanged();
    }

    private void onDateTimeChanged() {
        refreshSelectedDateTime();
        if (endDateTime != null) {
            if (Period.between(selectedDateTime.toLocalDate(), endDateTime.toLocalDate()).getDays() == 0) {
                if (endDateTime.getHour() - selectedDateTime.getHour() < 1) {
                    setTimePickerInterval(timePicker, 0, endDateTime.getHour(), hourInterval, 0, endDateTime.getMinute(), minuteInterval);
                    return;
                } else {
                    setTimePickerInterval(timePicker, 0, endDateTime.getHour(), hourInterval, 0, 59, minuteInterval);
                    return;
                }
            }
        }
        if (startDateTime != null) {
            if (Period.between(selectedDateTime.toLocalDate(), startDateTime.toLocalDate()).getDays() == 0) {
                if (startDateTime.getHour() - selectedDateTime.getHour() >= 0) {
                    setTimePickerInterval(timePicker, startDateTime.getHour(), 23, hourInterval, startDateTime.getMinute(), 59, minuteInterval);
                    return;
                } else {
                    setTimePickerInterval(timePicker, startDateTime.getHour(), 23, hourInterval, 0, 59, minuteInterval);
                    return;
                }
            }
        }
        setTimePickerInterval(timePicker, 0, 23, 1, 0, 59, minuteInterval);
    }


    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateTimeChanged();
    }

    private void refreshSelectedDateTime() {
        Integer hour = Integer.valueOf(getDisplayValue( (NumberPicker) timePicker
                .findViewById(Resources.getSystem().getIdentifier("hour",
                        "id", "android"))));
        Integer minute = Integer.valueOf(getDisplayValue( (NumberPicker) timePicker
                .findViewById(Resources.getSystem().getIdentifier("minute",
                        "id", "android"))));
        selectedDateTime = selectedDateTime.with(LocalDateTime.of(datePicker.getYear()
                , datePicker.getMonth() + 1
                , datePicker.getDayOfMonth()
                , hour
                , minute
                , 0
                , 0));
        Log.i(TAG, selectedDateTime.toString());
    }

    private String getDisplayValue(NumberPicker numberPicker) {
        if (numberPicker.getDisplayedValues() != null) {
            return numberPicker.getDisplayedValues()[numberPicker.getValue()];
        }
        return String.valueOf(numberPicker.getValue());
    }

    private void setTimePickerInterval(TimePicker timePicker, Integer minHour, Integer maxHour, Integer intervalHour, Integer minMinute, Integer maxMinute, Integer intervalMinute) {
        NumberPicker hourPicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                "hour", "id", "android"));
        setNunberpickerDisplay(hourPicker, minHour, maxHour, intervalHour);

        NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                "minute", "id", "android"));
        setNunberpickerDisplay(minutePicker, minMinute, maxMinute, intervalMinute);
        refreshSelectedDateTime();
    }

    private void setNunberpickerDisplay(NumberPicker numberPicker, Integer min, Integer max, Integer interval) {
        try {
            Integer newValue;
            List<String> displayValues = new ArrayList<String>();

            for (int i = min; i <= max; i += interval) {
                displayValues.add(String.format("%02d", i));
            }

            numberPicker.setMinValue(0);
            if (numberPicker.getMaxValue() >= displayValues.size()) {
                if (numberPicker.getValue() >= displayValues.size()) {
                    newValue = ((displayValues.size() - 1)  - numberPicker.getMaxValue()) + numberPicker.getValue();
                } else {
                    newValue = displayValues.size() - 1;
                }

                numberPicker.setValue(newValue);
                numberPicker.setMaxValue(displayValues.size() - 1);
                numberPicker.setDisplayedValues(displayValues.toArray(new String[0]));
            } else {
                newValue = ((displayValues.size() - 1)  - numberPicker.getMaxValue()) + numberPicker.getValue();
                numberPicker.setDisplayedValues(displayValues.toArray(new String[0]));
                numberPicker.setMaxValue(displayValues.size() - 1);
                numberPicker.setValue(newValue);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

}