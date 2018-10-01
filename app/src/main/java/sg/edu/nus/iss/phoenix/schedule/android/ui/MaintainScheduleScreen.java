package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.ConstantHelper;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.utilities.DateHelper;

public class MaintainScheduleScreen extends AppCompatActivity implements DateTimePickerDialog.DateTimeSetListener {
    private static final String TAG = MaintainScheduleScreen.class.getName();
    private static final String START_DATETIME = "START_DATETIME";
    private static final String END_DATETIME = "END_DATETIME";

    private EditText etRadioProgramName;
    private TextView tvRadioProgramDescription;
    private EditText etPresenterName;
    private EditText etProducerName;
    private EditText etDateOfProgramStart;
    private EditText etDateOfProgramEnd;
    private TextView tvDateOfProgramEndLabel;

    private ZonedDateTime originalDateOfProgram;
    private ProgramSlot currentProgramSlot;


    public void createProgramSlot(ProgramSlot programSlot) {
        currentProgramSlot = programSlot;
        displayProgramSlot();
    }

    public void editProgramSlot(ProgramSlot programSlot) {
        currentProgramSlot = programSlot;
        originalDateOfProgram = currentProgramSlot.getDateOfProgram();
        displayProgramSlot();
    }

    private void setupLabels() {
        etRadioProgramName = ((EditText) findViewById(R.id.et_programslot_radioprogram_name));
        tvRadioProgramDescription = ((TextView) findViewById(R.id.tv_programslot_radioprogram_description));
        etPresenterName = ((EditText) findViewById(R.id.et_programslot_presenter_name));
        etProducerName = ((EditText) findViewById(R.id.et_programslot_producer_name));
        etDateOfProgramStart = ((EditText) findViewById(R.id.et_programslot_dateofprogram_start));
        etDateOfProgramEnd = ((EditText) findViewById(R.id.et_programslot_dateofprogram_end));
        tvDateOfProgramEndLabel = ((TextView) findViewById(R.id.tv_programslot_dateofprogram_end_label));
    }

    private void displayProgramSlot() {
        if (currentProgramSlot.getRadioProgram() != null) {
            etRadioProgramName.setText(currentProgramSlot.getRadioProgram().getRadioProgramName());
            tvRadioProgramDescription.setText(currentProgramSlot.getRadioProgram().getRadioProgramDescription());
        }

        if (currentProgramSlot.getPresenter() != null) {
            etPresenterName.setText(currentProgramSlot.getPresenter().getUserName());
        }

        if (currentProgramSlot.getProducer() != null) {
            etProducerName.setText(currentProgramSlot.getProducer().getUserName());
        }

        if (currentProgramSlot.getDateOfProgram() != null) {
            etDateOfProgramStart.setText(currentProgramSlot.getDateOfProgram().toString());
        }

        if (currentProgramSlot.getDateOfProgram() != null && currentProgramSlot.getDuration() != null) {
            etDateOfProgramEnd.setText(currentProgramSlot.getDateOfProgram()
                    .plusSeconds(currentProgramSlot.getDuration()
                            .getSeconds()).toString()
            );
        }

        int visibility = View.VISIBLE;
        if (currentProgramSlot.getDateOfProgram() == null) {
            visibility = View.GONE;
        }
        tvDateOfProgramEndLabel.setVisibility(visibility);
        etDateOfProgramEnd.setVisibility(visibility);
    }

    private void selectRadioProgram() {
        ControlFactory.getScheduleController().selectRadioProgram(currentProgramSlot.getRadioProgram());
    }

    private void selectPresenterProducer(String field) {
        ControlFactory.getScheduleController().selectPresenterProducer(currentProgramSlot.getPresenter(), currentProgramSlot.getProducer(), field);
    }


    private void showDateTimePickerDialog(String field, ZonedDateTime selectedDateTime, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        switch (field) {
            case END_DATETIME:
                startDateTime = startDateTime.plusMinutes(30);
                break;
        }
        DateTimePickerDialog confirm_dialog = new DateTimePickerDialog(selectedDateTime
                , startDateTime
                , endDateTime
                , 1
                , 30
                , field);
        confirm_dialog.setOnDateTimeSetListener(this);
        confirm_dialog.show(this.getSupportFragmentManager()
                , DateTimePickerDialog.TAG);
    }

    public void radioProgramSelected(RadioProgram radioProgram) {
        currentProgramSlot.setRadioProgram(radioProgram);
        currentProgramSlot.setDuration(Duration.between(
                LocalTime.MIN,
                LocalTime.parse(radioProgram.getRadioProgramDuration())
        ));
        displayProgramSlot();
    }

    public void presenterProducerSelected(User presenter, User producer) {
        currentProgramSlot.setPresenter(presenter);
        currentProgramSlot.setProducer(producer);
        displayProgramSlot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_slot);
        currentProgramSlot = (ProgramSlot) getIntent().getSerializableExtra(ConstantHelper.PROGRAM_SLOT);
        setupRadioProgramControls();
        setupPresenterProducerControls();
        setupDateOfProgramControls();
        setupLabels();
    }

    private void setupRadioProgramControls() {

        findViewById(R.id.et_programslot_radioprogram_name).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectRadioProgram();
                    }
                }
        );
    }

    private void setupPresenterProducerControls() {
        findViewById(R.id.et_programslot_presenter_name).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPresenterProducer(ConstantHelper.PRESENTER);
                    }
                }
        );

        findViewById(R.id.et_programslot_producer_name).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPresenterProducer(ConstantHelper.PRODUCER);
                    }
                }
        );
    }

    private void setupDateOfProgramControls() {
        findViewById(R.id.et_programslot_dateofprogram_start).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentProgramSlot.getDateOfProgram() != null && currentProgramSlot.getAssignedBy() != null) {
                            showDateTimePickerDialog(START_DATETIME
                                    , currentProgramSlot.getDateOfProgram()
                                    , DateHelper.getWeekStartDate(currentProgramSlot.getDateOfProgram()
                                            .toLocalDate()).atStartOfDay(ZoneOffset.UTC)
                                    , DateHelper.getWeekEndDate(currentProgramSlot.getDateOfProgram()
                                            .toLocalDate()).atStartOfDay(ZoneOffset.UTC));
                        } else {
                            showDateTimePickerDialog(START_DATETIME
                                    , currentProgramSlot.getDateOfProgram()
                                    , null
                                    , null);
                        }
                    }
                }
        );

        findViewById(R.id.et_programslot_dateofprogram_end).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentProgramSlot.getDateOfProgram() != null) {
                            long duration = 0;
                            if (currentProgramSlot.getDuration() != null) {
                                duration = currentProgramSlot.getDuration().getSeconds();
                            }
                            showDateTimePickerDialog(END_DATETIME
                                    , currentProgramSlot.getDateOfProgram().plusSeconds(duration)
                                    , currentProgramSlot.getDateOfProgram()
                                    , DateHelper.getWeekEndDate(currentProgramSlot.getDateOfProgram()
                                            .toLocalDate()).plusDays(1).atStartOfDay(ZoneOffset.systemDefault()));
                        }
                    }
                }
        );
    }

    private boolean validateFormat() {
        return true;
    }

    private boolean validate() {
        if (currentProgramSlot.getRadioProgram() == null) {
            displayErrorMessage("Please select radio program to proceed.");
            return false;
        }
        if (currentProgramSlot.getPresenter() == null) {
            displayErrorMessage("Please assign presenter to proceed.");
            return false;
        }
        if (currentProgramSlot.getProducer() == null) {
            displayErrorMessage("Please assign producer to proceed.");
            return false;
        }
        if (currentProgramSlot.getDateOfProgram() == null) {
            displayErrorMessage("Please set the start date of program to proceed.");
            return false;
        }
        if (currentProgramSlot.getDuration() == null) {
            displayErrorMessage("Please set the end date of program to proceed.");
            return false;
        }
        if (currentProgramSlot.getDuration().getSeconds() < 1800) {
            displayErrorMessage("Please set the program slot duration at least 30 minutes to proceed.");
            return false;
        }
        if ((currentProgramSlot.getDuration().getSeconds() % 1800) > 0) {
            displayErrorMessage("Please set the program slot duration to multiple of 30 minutes to proceed.");
            return false;
        }
        if (Duration.between(currentProgramSlot.getDateOfProgram()
                        .plusSeconds(currentProgramSlot.getDuration().getSeconds()).toLocalDateTime()
                , DateHelper.getWeekEndDate(currentProgramSlot.getDateOfProgram().toLocalDate()).plusDays(1).atStartOfDay())
                .getSeconds() < 0) {
            displayErrorMessage("Please set the end date of program not to cross the current week.");
            return false;
        }
        return true;
    }

    public void displayErrorMessage(String message) {
        Toast.makeText(this, "ERROR: " + message, Toast.LENGTH_SHORT).show();
    }

    public void displaySuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ControlFactory.getScheduleController().onDisplayMaintainScheduleScreen(this, currentProgramSlot);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentProgramSlot.getAssignedBy() == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public void onDateTimeSet(ZonedDateTime value, String field) {
        switch (field) {
            case START_DATETIME:
                if (currentProgramSlot.getDuration() != null && currentProgramSlot.getDateOfProgram() != null) {
                    if (Duration.between(value, currentProgramSlot.getDateOfProgram().plusSeconds(currentProgramSlot.getDuration().getSeconds())).getSeconds() > 0) {
                        currentProgramSlot.setDuration(currentProgramSlot.getDuration()
                                .plusSeconds(Duration.between(value
                                        , currentProgramSlot.getDateOfProgram())
                                        .getSeconds()));
                    }

                }
                currentProgramSlot.setDateOfProgram(value);
                break;
            case END_DATETIME:
                currentProgramSlot.setDuration(Duration.between(currentProgramSlot.getDateOfProgram(), value));
                break;
        }
        displayProgramSlot();
    }

    public void selectSaveProgramSlot() {
        if (validate()) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure want to save?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.v(TAG, "Creating program slot " + currentProgramSlot.toString() + "...");
                            if (currentProgramSlot.getAssignedBy() == null) {
                                ControlFactory.getScheduleController().selectCreateProgramSlot(currentProgramSlot);
                            } else {

                                ControlFactory.getScheduleController().selectUpdateProgramSlot(currentProgramSlot, originalDateOfProgram);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                selectSaveProgramSlot();
                break;
            case R.id.action_cancel:
                onBackPressed();
                break;
            case R.id.action_delete:
                selectDeleteProgramSlot();
        }

        return true;
    }

    private void selectDeleteProgramSlot() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure want to delete?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.v(TAG, "Deleting program slot " + currentProgramSlot.toString() + "...");
                        ControlFactory.getScheduleController().selectDeleteProgramSlot(originalDateOfProgram);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onBackPressed() {
        unloadScreen();
    }

    public void unloadScreen() {
        Log.v(TAG, "Canceling/finishing of creating/editing program slot...");
        ControlFactory.getScheduleController().onUnloadMaintainScheduleScreen(currentProgramSlot);
        finish();
    }
}
