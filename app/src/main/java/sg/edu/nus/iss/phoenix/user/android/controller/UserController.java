package sg.edu.nus.iss.phoenix.user.android.controller;

import android.content.Intent;
import android.util.Log;

import java.util.List;

import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.Role;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.user.android.delegate.UpdateUserDelegate;
import sg.edu.nus.iss.phoenix.user.android.delegate.CreateUserDelegate;
import sg.edu.nus.iss.phoenix.user.android.delegate.DeleteUserDelegate;
import sg.edu.nus.iss.phoenix.user.android.delegate.RetrieveUsersDelegate;
import sg.edu.nus.iss.phoenix.user.android.ui.MaintainUserScreen;
import sg.edu.nus.iss.phoenix.user.android.ui.UserListScreen;

public class UserController {
    // Tag for logging.
    private static final String TAG = UserController.class.getName();

    private UserListScreen userListScreen;
    private MaintainUserScreen maintainUserScreen;
    private User user2edit = null;

    public void startUseCase() {
        user2edit = null;
        Intent intent = new Intent(MainController.getApp(), UserListScreen.class);
        MainController.displayScreen(intent);
    }

    public void onDisplayUserList(UserListScreen userListScreen) {
        this.userListScreen = userListScreen;
        new RetrieveUsersDelegate(this).execute("all");
    }

    public void usersRetrieved(List<User> users) {
       userListScreen.showUsers(users);
    }

    public void rolesRetrieved(List<Role> roles) {
        maintainUserScreen.showRoles(roles);
    }

    public void selectCreateUser() {
        user2edit = null;
        Intent intent = new Intent(MainController.getApp(), MaintainUserScreen.class);
        MainController.displayScreen(intent);
    }

    public void selectEditUser(User user) {
        user2edit = user;
        Log.v(TAG, "Editing user: " + user.getUserName() + "...");

        Intent intent = new Intent(MainController.getApp(), MaintainUserScreen.class);
/*        Bundle b = new Bundle();
        b.putString("Id", user.getId());
        b.putString("Name", user.getUserName());
        b.putString("Password", user.getUserPassword());
        b.putString("Roles", user.getUserRoles());
        intent.putExtras(b);
*/
        MainController.displayScreen(intent);
    }

    public void onDisplayUser(MaintainUserScreen maintainUserScreen) {
        this.maintainUserScreen = maintainUserScreen;
        if (user2edit == null)
            maintainUserScreen.createUser();
        else
            maintainUserScreen.editUser(user2edit);
    }

    public void selectUpdateUser(User user) {
        new UpdateUserDelegate(this).execute(user);
    }

    public void selectDeleteUser(User user) {
        new DeleteUserDelegate(this).execute(user.getId());
    }

    public void userDeleted(boolean success) {
        // Go back to UserList screen with refreshed users.
        startUseCase();
    }

    public void userUpdated(boolean success) {
        // Go back to UserList screen with refreshed users.
        startUseCase();
    }

    public void selectCreateUser(User user) {
        new CreateUserDelegate(this).execute(user);
    }

    public void userCreated(boolean success) {
        // Go back to UserList screen with refreshed users.
        startUseCase();
    }

    public void selectCancelCreateEditUser() {
        // Go back to UserList screen with refreshed users.
        startUseCase();
    }

    public void maintainedUser() {
        ControlFactory.getMainController().maintainedUser();
    }
}
