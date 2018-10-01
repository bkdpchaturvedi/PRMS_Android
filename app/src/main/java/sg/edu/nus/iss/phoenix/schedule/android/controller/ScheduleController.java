package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;

import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.List;


import sg.edu.nus.iss.phoenix.core.android.controller.ConstantHelper;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.radioprogram.android.controller.ReviewSelectRadioProgramReturnable;
import sg.edu.nus.iss.phoenix.restful.JSONEnvelop;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.CreateProgramSlotDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.DeleteProgramSlotDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.RetrieveProgramSlotDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.UpdateProgramSlotDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.schedule.android.ui.MaintainScheduleScreen;
import sg.edu.nus.iss.phoenix.schedule.android.ui.ScheduledProgramsListScreen;

public class ScheduleController implements ReviewSelectRadioProgramReturnable, ReviewSelectPresenterProducerReturnable {

    private static final String TAG = ScheduleController.class.getName();

    private ScheduledProgramsListScreen scheduledProgramsListScreen;
    private MaintainScheduleScreen maintainScheduleScreen;

    public void startUseCase() {
        Intent intent = new Intent(MainController.getApp(), ScheduledProgramsListScreen.class);
        MainController.displayScreen(intent);
    }


    public void onDisplayScheduledProgramListScreen(ScheduledProgramsListScreen scheduledProgramsListScreen) {
        this.scheduledProgramsListScreen = scheduledProgramsListScreen;
       getProgramSlots( ZonedDateTime.now());
    }

    private void getProgramSlots(ZonedDateTime dateOfProgram) {
        new RetrieveProgramSlotDelegate(this).execute(dateOfProgram);
    }

    public void onDisplayMaintainScheduleScreen(MaintainScheduleScreen maintainScheduleScreen, ProgramSlot programSlot) {
        this.maintainScheduleScreen = maintainScheduleScreen;
        if (programSlot.getAssignedBy() == null) {
            this.maintainScheduleScreen.createProgramSlot(programSlot);
        } else {
            this.maintainScheduleScreen.editProgramSlot(programSlot);
        }
    }

    public void programSlotsRetrieved(ZonedDateTime dateOfProgram, JSONEnvelop<List<ProgramSlot>> response) {
        if (response.getError() != null) {
            scheduledProgramsListScreen.displayErrorMessage(response.getError().getDescription());
        }
        if (response.getData() != null) {
            scheduledProgramsListScreen.displayProgramSlots(dateOfProgram, response.getData());
        }
    }

    public void selectCreateProgramSlot() {
        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        intent.putExtra(ConstantHelper.PROGRAM_SLOT, new ProgramSlot());
        MainController.displayScreen(intent);
    }

    public void selectCreateProgramSlot(ProgramSlot programSlot) {
        new CreateProgramSlotDelegate(this).execute(new ProgramSlot(
                programSlot.getDateOfProgram()
                , programSlot.getDuration()
                , programSlot.getRadioProgram()
                , programSlot.getPresenter()
                , programSlot.getProducer()
                , MainController.getUserId()));
    }

    public void selectViewProgramSlot(ProgramSlot programSlot) {
        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        intent.putExtra(ConstantHelper.PROGRAM_SLOT, programSlot);
        MainController.displayScreen(intent);
    }

    public void selectCopyProgramSlot(ProgramSlot programSlot) {
        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        intent.putExtra(ConstantHelper.PROGRAM_SLOT
                , new ProgramSlot(
                        null
                        , programSlot.getDuration()
                        , programSlot.getRadioProgram()
                        , programSlot.getPresenter()
                        , programSlot.getProducer()
                        , null));
        MainController.displayScreen(intent);
    }


    public void scheduleMaintained() {
        ControlFactory.getMainController().scheduleMaintained();
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
            maintainScheduleScreen.displaySuccessMessage("Program slot has been created successfully");
            maintainScheduleScreen.unloadScreen();
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

    public void selectUpdateProgramSlot(ProgramSlot programSlot, ZonedDateTime originalDateOfProgram) {
        new UpdateProgramSlotDelegate(this).execute(new AbstractMap.SimpleEntry<>(originalDateOfProgram, programSlot));
    }

    public void programSlotUpdated(JSONEnvelop<Boolean> response) {
        if (response.getError() != null) {
            maintainScheduleScreen.displayErrorMessage(response.getError().getDescription());
        }
        if (response.getData() != null && response.getData()) {
            maintainScheduleScreen.displaySuccessMessage("Program slot has been updated successfully.");
            maintainScheduleScreen.unloadScreen();
        }
    }

    public void programSlotDeleted(JSONEnvelop<Boolean> response) {
        if (response.getError() != null) {
            maintainScheduleScreen.displayErrorMessage(response.getError().getDescription());
        }
        if (response.getData() != null && response.getData()) {
            maintainScheduleScreen.displaySuccessMessage("Program slot has been deleted successfully.");
            maintainScheduleScreen.unloadScreen();
        }
    }

    public void selectDeleteProgramSlot(ZonedDateTime dateOfProgram) {
        new DeleteProgramSlotDelegate(this).execute(dateOfProgram);
    }
}
