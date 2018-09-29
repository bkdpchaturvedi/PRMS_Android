package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.RetrieveProgramSlotDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.schedule.android.ui.ScheduleProgramsListScreen;

public class ScheduleController {
    // Tag for logging.
    private static final String TAG = ScheduleController.class.getName();

    private ScheduleProgramsListScreen scheduleProgramsListScreen;
    public void startUseCase() {
        //rp2edit = null;
        Intent intent = new Intent(MainController.getApp(), ScheduleProgramsListScreen.class);
        MainController.displayScreen(intent);
    }

    public void onDisplayProgramSlotList(ScheduleProgramsListScreen scheduleProgramsListScreen, Date date) {
        this.scheduleProgramsListScreen = scheduleProgramsListScreen;
        SimpleDateFormat format =new SimpleDateFormat("YYYY-mm-DDThh:MM:ss");
        new RetrieveProgramSlotDelegate(this).execute(format.format(date));
    }

    public void programSlotsRetrieved(ArrayList<ProgramSlot> programSlots) {
        scheduleProgramsListScreen.showProgramSlots(programSlots);

    }
    public void maintainedSchedule() {
        ControlFactory.getMainController().maintainedSchedule();
    }
}
