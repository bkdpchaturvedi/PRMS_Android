package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;
import android.os.AsyncTask;

import java.util.List;

import sg.edu.nus.iss.phoenix.core.android.controller.ConstantHelper;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.restful.JSONEnvelop;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.RetrievePresenterProducerDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.ui.PresenterProducerScreen;

public class ReviewSelectPresenterProducerController {

    private  ReviewSelectPresenterProducerReturnable caller;
    private PresenterProducerScreen presenterProducerScreen;

    public void startUseCase(ReviewSelectPresenterProducerReturnable caller, User presenter, User producer) {
        this.caller = caller;
        Intent intent = new Intent(MainController.getApp(), PresenterProducerScreen.class);
        intent.putExtra(ConstantHelper.PRESENTER, presenter);
        intent.putExtra(ConstantHelper.PRODUCER, producer);
        MainController.displayScreen(intent);
    }

    public void onDisplayScreen(PresenterProducerScreen presenterProducerScreen, User presenter, User producer) {
        this.presenterProducerScreen = presenterProducerScreen;
        presenterProducerScreen.displaySelectedPresenter(presenter);
        presenterProducerScreen.displaySelectedProducer(producer);
    }

    public void presentersRetrieved(JSONEnvelop<List<User>> response) {
        if (response.getError() != null) {
            presenterProducerScreen.displayErrorMessage(response.getError().getError() + ": "+ response.getError().getDescription());
        }
        presenterProducerScreen.displayPresenters(response.getData());
    }

    public void producersRetrieved(JSONEnvelop<List<User>> response) {
        if (response.getError() != null) {
            presenterProducerScreen.displayErrorMessage(response.getError().getError() + ": "+ response.getError().getDescription());
        }
        presenterProducerScreen.displayProducers(response.getData());
    }

    public void selectGetAllPresenters() {
        new RetrievePresenterProducerDelegate(this).execute("presenters");
    }

    public void selectGetAllProducers() {
        new RetrievePresenterProducerDelegate(this).execute("producers");
    }

    public void selectPresenter(User presenter) {
        presenterProducerScreen.displaySelectedPresenter(presenter);
    }

    public void selectProducer(User producer) {
        presenterProducerScreen.displaySelectedProducer(producer);
    }

    public void selectReturnResult(User presenter, User producer) {
        presenterProducerScreen.unloadDisplayScreen();
        caller.presenterProducerSelected(presenter, producer);
    }
}
