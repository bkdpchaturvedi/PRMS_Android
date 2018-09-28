package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;

import java.util.ArrayList;


import sg.edu.nus.iss.phoenix.core.android.controller.ConstantHelper;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.radioprogram.android.controller.ReviewSelectRadioProgramReturnable;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.RetrieveProgramSlotDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.schedule.android.ui.MaintainScheduleScreen;
import sg.edu.nus.iss.phoenix.schedule.android.ui.ScheduleProgramsListScreen;

public class ScheduleController implements ReviewSelectRadioProgramReturnable, ReviewSelectPresenterProducerReturnable {

    private static final String TAG = ScheduleController.class.getName();

    private ScheduleProgramsListScreen scheduleProgramsListScreen;
    private MaintainScheduleScreen maintainScheduleScreen;

    public void startUseCase() {
        //rp2edit = null;
        Intent intent = new Intent(MainController.getApp(), ScheduleProgramsListScreen.class);
        MainController.displayScreen(intent);
    }

    public void onDisplayProgramListScreen(ScheduleProgramsListScreen scheduleProgramsListScreen) {
        this.scheduleProgramsListScreen = scheduleProgramsListScreen;
        new RetrieveProgramSlotDelegate(this).execute("all");
    }

    public void onDisplayMaintainScheduleScreen(MaintainScheduleScreen maintainScheduleScreen, ProgramSlot programSlot) {
        this.maintainScheduleScreen = maintainScheduleScreen;
        if (programSlot.getAssignedBy() == null) {
            this.maintainScheduleScreen.createProgramSlot(programSlot);
        } else {
            this.maintainScheduleScreen.editProgramSlot(programSlot);
        }
    }

    public void programSlotsRetrieved(ArrayList<ProgramSlot> programSlots) {
        scheduleProgramsListScreen.showProgramSlots(programSlots);

    }

    public void selectCreateProgramSlot() {
        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        intent.putExtra(ConstantHelper.PROGRAM_SLOT, new ProgramSlot());
        MainController.displayScreen(intent);
    }

    public void selectCopyProgramSlot(ProgramSlot programSlot) {
        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        intent.putExtra(ConstantHelper.PROGRAM_SLOT
                , new ProgramSlot(
                        programSlot.getDateOfProgram()
                        , programSlot.getDuration()
                        , programSlot.getRadioProgram()
                        , programSlot.getPresenter()
                        , programSlot.getProducer()
                        , null));
        MainController.displayScreen(intent);
    }


    public void maintainedSchedule() {
        ControlFactory.getMainController().maintainedSchedule();
    }

    public void selectRadioProgram(RadioProgram radioProgram) {
        ControlFactory.getReviewSelectProgramController().startUseCase(this);
    }

    public void selectPresenterProducer(User presenter, User producer, String field) {
        ControlFactory.getReviewSelectPresenterProducerController().startUseCase(this, presenter, producer, field);
    }

    @Override
    public void radioProgramSelected(RadioProgram radioProgram) {
        maintainScheduleScreen.radioProgramSelected(radioProgram);
    }

    @Override
    public void presenterProducerSelected(User presenter, User producer) {
        maintainScheduleScreen.presenterProducerSelected(presenter, producer);
    }
}
