package sg.edu.nus.iss.phoenix.user.android.controller;

import android.content.Intent;
import android.util.Log;

import java.util.List;

import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.user.android.delegate.RetrieveUsersDelegate;
import sg.edu.nus.iss.phoenix.user.android.ui.ReviewSelectUserScreen;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;

public class ReviewSelectUserController {
    // Tag for logging.
    private static final String TAG = ReviewSelectUserController.class.getName();

    private ReviewSelectUserScreen reviewSelectUserScreen;
    private User userSelected = null;

    public void startUseCase() {
        userSelected = null;
        Intent intent = new Intent(MainController.getApp(), ReviewSelectUserScreen.class);
        MainController.displayScreen(intent);
    }

    public void onDisplay(ReviewSelectUserScreen reviewSelectUserScreen) {
        this.reviewSelectUserScreen = reviewSelectUserScreen;
        new RetrieveUsersDelegate(this).execute("all");
    }

    public void usersRetrieved(List<User> users) {
        reviewSelectUserScreen.showUsers(users);
    }

    public void selectUser(User user) {
        userSelected = user;
        Log.v(TAG, "Selected user: " + user.getUserName() + ".");
        // To call the base use case controller with the selected user.
        // At present, call the MainController instead.
        ControlFactory.getMainController().selectedUser(userSelected);
    }

    public void selectCancel() {
        userSelected = null;
        Log.v(TAG, "Cancelled the seleciton of user.");
        // To call the base use case controller without selection;
        // At present, call the MainController instead.
        ControlFactory.getMainController().selectedUser(userSelected);
    }
}
