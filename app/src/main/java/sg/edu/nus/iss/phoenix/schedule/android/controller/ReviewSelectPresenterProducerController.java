package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;

import sg.edu.nus.iss.phoenix.core.android.controller.ConstantHelper;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.schedule.android.ui.PresenterProducerScreen;

public class ReviewSelectPresenterProducerController {

    private  ReviewSelectPresenterProducerReturnable caller;

    void startUseCase(ReviewSelectPresenterProducerReturnable caller, User presenter, User producer) {
        this.caller = caller;
        Intent intent = new Intent(MainController.getApp(), PresenterProducerScreen.class);
        intent.putExtra(ConstantHelper.PRESENTER, presenter);
        intent.putExtra(ConstantHelper.PRODUCER, producer);
        MainController.displayScreen(intent);
    }
}
