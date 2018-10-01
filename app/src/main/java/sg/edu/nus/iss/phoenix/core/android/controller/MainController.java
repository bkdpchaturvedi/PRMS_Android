package sg.edu.nus.iss.phoenix.core.android.controller;

import android.app.Application;
import android.content.Intent;

import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.core.android.ui.MainScreen;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;

public class MainController {
    private static Application app = null;
    private static String userId;
    private MainScreen mainScreen;

    public static Application getApp() {
        return app;
    }
    
    public static String getUserId() {return userId;}

    public static void setApp(Application app) {
        MainController.app = app;
    }

    public static void displayScreen(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);
    }

    public void startUseCase(String userId) {
        this.userId = userId;

        Intent intent = new Intent(MainController.getApp(), MainScreen.class);
        MainController.displayScreen(intent);
    }

    public void onDisplay(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        mainScreen.showUsername(userId);
    }

    public void selectMaintainProgram() {
        ControlFactory.getProgramController().startUseCase();
    }

    public void selectMaintainUser() {
        ControlFactory.getUserController().startUseCase();
    }

    public void maintainedProgram() {
        startUseCase(userId);
    }

    public void maintainedUser() {
        startUseCase(userId);
    }

    public void selectLogout() {
        userId = "<not logged in>";
        ControlFactory.getLoginController().logout();
    }
    public void scheduleMaintained() {
        startUseCase(userId);
    }
    public void selectMaintainSchedule() {
        // This is the placeholder for starting the Maintain Schedule use case.
        // At present, it ii used to test the invocation of  Review Select Radio Program use case.
        ControlFactory.getScheduleController().startUseCase();
    }

    // This is a dummy operation to test the invocation of Review Select Radio Program use case.
    public void selectedProgram(RadioProgram rpSelected) {
        startUseCase(userId);
    }

    public void selectedUser(User userSelected) {
        startUseCase(userId);
    }
}
