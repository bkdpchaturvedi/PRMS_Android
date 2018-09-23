package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;

import java.util.List;

import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.RetrieveProgramSlotDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

public class ScheduleController {
    // Tag for logging.
    private static final String TAG = ScheduleController.class.getName();
    public void startUseCase() {
        //rp2edit = null;
        //Intent intent = new Intent(MainController.getApp(), ProgramListScreen.class);
       // MainController.displayScreen(intent);
    }

    public void onDisplayProgramList(Object programListScreen) {
       // this.programListScreen = programListScreen;
        new RetrieveProgramSlotDelegate(this).execute("all");
    }

    public void programsRetrieved(List<ProgramSlot> programSlots) {
       // programListScreen.showPrograms(programSlots);
    }
}
