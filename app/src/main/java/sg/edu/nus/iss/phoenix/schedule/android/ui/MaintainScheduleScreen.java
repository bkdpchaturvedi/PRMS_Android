package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;


import java.time.LocalDate;
import java.time.LocalDateTime;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

public class MaintainScheduleScreen extends AppCompatActivity implements DateTimePickerDialog.DateTimePickerDialogCompliant {
    private static final String TAG = MaintainScheduleScreen.class.getName();
    private LocalDate originalDateOfProgram;
    private ProgramSlot currentProgramSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_slot);
        ((Button) findViewById(R.id.btn_programslot_radioprogram)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mm();
            }
        });
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
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
    public void onDateTimeSet() {

    }

    private void mm() {

        DateTimePickerDialog confirm_dialog = new DateTimePickerDialog(this, LocalDateTime.now(), LocalDateTime.now().plusDays(6), 60);
        confirm_dialog.show(this.getSupportFragmentManager(), DateTimePickerDialog.TAG);

//        final View dialogView = View.inflate(this, R.layout.dialog_date_time, null);
//        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//
//        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
//                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
//
////                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
////                        datePicker.getMonth(),
////                        datePicker.getDayOfMonth(),
////                        timePicker.getCurrentHour(),
////                        timePicker.getCurrentMinute());
////
////                time = calendar.getTimeInMillis();
//                alertDialog.dismiss();
//            }});
//        alertDialog.setView(dialogView);
//        alertDialog.show();
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
