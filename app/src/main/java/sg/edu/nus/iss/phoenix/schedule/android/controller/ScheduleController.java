package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import sg.edu.nus.iss.phoenix.core.android.controller.ConstantHelper;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.radioprogram.android.controller.ReviewSelectRadioProgramReturnable;
import sg.edu.nus.iss.phoenix.restful.JSONEnvelop;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.CreateProgramSlotDelegate;
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
       getProgramSlots( ZonedDateTime.now());
    }

    private void getProgramSlots(ZonedDateTime date) {
        new RetrieveProgramSlotDelegate(this).execute(date.toString());
    }

    public void onDisplayMaintainScheduleScreen(MaintainScheduleScreen maintainScheduleScreen, ProgramSlot programSlot) {
        this.maintainScheduleScreen = maintainScheduleScreen;
        if (programSlot.getAssignedBy() == null) {
            this.maintainScheduleScreen.createProgramSlot(programSlot);
        } else {
            this.maintainScheduleScreen.editProgramSlot(programSlot);
        }
    }

    public void programSlotsRetrieved(ZonedDateTime dateOfProgram, List<ProgramSlot> programSlots) {
        scheduleProgramsListScreen.displayProgramSlots(dateOfProgram, programSlots);

    }

    public void selectCreateProgramSlot() {
        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        intent.putExtra(ConstantHelper.PROGRAM_SLOT, new ProgramSlot());
        MainController.displayScreen(intent);
    }

    public void selectCreateProgramSlot(ProgramSlot programSlot) {
        programSlot.setAssignedBy(MainController.getUserId());
        new CreateProgramSlotDelegate(this).execute(programSlot);
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

    public void programSlotCreated(JSONEnvelop<Boolean> response) {
        if (response.getError() != null) {
            maintainScheduleScreen.displayErrorMessage(response.getError().getDescription());
        }
        if (response.getData() != null && response.getData()) {
            maintainScheduleScreen.displaySuccessMessage("Program slot has been successfully created.");
        }
    }

    public void onUnloadMaintainScheduleScreen(ProgramSlot programSlot) {
        selectViewProgramSlots(programSlot.getDateOfProgram());
    }

    public void selectViewProgramSlots(ZonedDateTime dateOfProgram) {
        if (dateOfProgram != null) {
            getProgramSlots(dateOfProgram);
        }
    }
}
