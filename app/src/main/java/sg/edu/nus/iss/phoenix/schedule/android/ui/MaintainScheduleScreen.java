package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.time.Duration;
import java.time.LocalDate;
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

    private LocalDate originalDateOfProgram;
    private ProgramSlot currentProgramSlot;


    public void createProgramSlot(ProgramSlot programSlot) {
        this.currentProgramSlot = programSlot;
        displayProgramSlot();
    }

    public void editProgramSlot(ProgramSlot programSlot) {
        this.currentProgramSlot = programSlot;
        displayProgramSlot();
    }

    private void displayProgramSlot() {
        if (currentProgramSlot.getRadioProgram() != null) {
            ((TextView) findViewById(R.id.tv_programslot_radioprogram_name))
                    .setText(currentProgramSlot.getRadioProgram().getRadioProgramName());
            ((TextView) findViewById(R.id.tv_programslot_radioprogram_description))
                    .setText(currentProgramSlot.getRadioProgram().getRadioProgramDescription());
        }
        if (currentProgramSlot.getPresenter() != null) {
            ((TextView) findViewById(R.id.tv_programslot_presenter_name))
                    .setText(currentProgramSlot.getPresenter().getUserName());
        }
        if (currentProgramSlot.getProducer() != null) {
            ((TextView) findViewById(R.id.tv_programslot_producer_name))
                    .setText(currentProgramSlot.getProducer().getUserName());
        }
        if (currentProgramSlot.getDateOfProgram() != null) {
            ((TextView) findViewById(R.id.tv_programslot_dateofprogram_start))
                    .setText(currentProgramSlot.getDateOfProgram().toString());
        }
        if (currentProgramSlot.getDateOfProgram() != null && currentProgramSlot.getDuration() != null) {
            ((TextView) findViewById(R.id.tv_programslot_dateofprogram_start))
                    .setText(currentProgramSlot.getDateOfProgram()
                            .plusSeconds(currentProgramSlot.getDuration()
                            .getSeconds()).toString()
                    );
        }
    }

    public void selectRadioProgram(){
        ControlFactory.getScheduleController().selectRadioProgram(currentProgramSlot.getRadioProgram());
    }

    public void selectPresenterProducer() {
        ControlFactory.getScheduleController().selectPresenterProducer(currentProgramSlot.getPresenter(), currentProgramSlot.getProducer());
    }


    private void showDateTimePickerDialog(String field, ZonedDateTime selectedDateTime, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
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

        setupControls();
    }

    private void setupControls() {

        ((Button) findViewById(R.id.btn_programslot_radioprogram)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectRadioProgram();
                    }
                }
        );

        ((Button) findViewById(R.id.btn_programslot_presenterproducer)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPresenterProducer();
                    }
                }
        );

        ((Button) findViewById(R.id.btn_programslot_dateofprogram_start)).setOnClickListener(
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
                                    , null
                                    , null
                                    , null);
                        }
                    }
                }
        );

        ((Button) findViewById(R.id.btn_programslot_dateofprogram_end)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentProgramSlot.getDateOfProgram() != null && currentProgramSlot.getAssignedBy() != null) {
                            long duration = 0;
                            if (currentProgramSlot.getDuration() != null) {
                                duration = currentProgramSlot.getDuration().getSeconds();
                            }
                            showDateTimePickerDialog(END_DATETIME
                                    , currentProgramSlot.getDateOfProgram().plusSeconds(duration)
                                    , currentProgramSlot.getDateOfProgram()
                                    , DateHelper.getWeekEndDate(currentProgramSlot.getDateOfProgram()
                                            .toLocalDate()).atStartOfDay(ZoneOffset.UTC));
                        } else {
                            showDateTimePickerDialog(END_DATETIME
                                    , null
                                    , null
                                    , null);
                        }
                    }
                }
        );
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
        if (currentProgramSlot == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public void onDateTimeSet(ZonedDateTime value, String field) {
        switch (field) {
            case START_DATETIME:
                currentProgramSlot.setDateOfProgram(value);
                break;
            case END_DATETIME:
                currentProgramSlot.setDuration(Duration.between(currentProgramSlot.getDateOfProgram(), value));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_save:
//                // Save radio program.
//                if (currentProgramSlot == null) { // Newly created.
//                    Log.v(TAG, "Saving program slot " + currentProgramSlot.toString() + "...");
//                    RadioProgram rp = new RadioProgram(mRPNameEditText.getText().toString(),
//                            mRPDescEditText.getText().toString(), mDurationEditText.getText().toString());
//                    ControlFactory.getProgramController().selectCreateProgram(rp);
//                }
//                else { // Edited.
//                    Log.v(TAG, "Saving radio program " + currentProgramSlot.getRadioProgramName() + "...");
//                    currentProgramSlot.setRadioProgramDescription(mRPDescEditText.getText().toString());
//                    currentProgramSlot.setRadioProgramDuration(mDurationEditText.getText().toString());
//                    ControlFactory.getProgramController().selectUpdateProgram(currentProgramSlot);
//                }
//                return true;
//            // Respond to a click on the "Delete" menu option
//            case R.id.action_delete:
//                Log.v(TAG, "Deleting radio program " + currentProgramSlot.getRadioProgramName() + "...");
//                ControlFactory.getProgramController().selectDeleteProgram(currentProgramSlot);
//                return true;
//            // Respond to a click on the "Cancel" menu option
//            case R.id.action_cancel:
//                Log.v(TAG, "Canceling creating/editing radio program...");
//                ControlFactory.getProgramController().selectCancelCreateEditProgram();
//                return true;
//        }
//
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        Log.v(TAG, "Canceling creating/editing radio program...");
//        ControlFactory.getProgramController().selectCancelCreateEditProgram();
//    }
}
