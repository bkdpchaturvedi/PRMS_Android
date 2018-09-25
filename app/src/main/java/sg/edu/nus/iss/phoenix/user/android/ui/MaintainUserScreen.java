package sg.edu.nus.iss.phoenix.user.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.Role;

public class MaintainUserScreen extends AppCompatActivity {
    // Tag for logging
    private static final String TAG = MaintainUserScreen.class.getName();

    private EditText mUserIDEditText;
    private EditText mUserNameEditText;
    private EditText mUserPasswordEditText;
    private EditText mRoleEditText;
    private User user2edit = null;
    KeyListener mUserIDEditTextKeyListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Find all relevant views that we will need to read user input from
        mUserIDEditText = (EditText) findViewById(R.id.maintain_user_id_text_view);
        mUserNameEditText = (EditText) findViewById(R.id.maintain_user_name_text_view);
        mUserPasswordEditText = (EditText) findViewById(R.id.maintain_user_password_text_view);
        mRoleEditText = (EditText) findViewById(R.id.maintain_user_roles_text_view);
        // Keep the KeyListener for name EditText so as to enable editing after disabling it.
        mUserIDEditTextKeyListener = mUserIDEditText.getKeyListener();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ControlFactory.getUserController().onDisplayUser(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new user, hide the "Delete" menu item.
        if (user2edit == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save  user.
                //YIJI TODO CONVERT CHECK BOX ROLES TO ARRAYLIST<ROLE>

                ArrayList<Role> roles = new ArrayList<Role>();
                roles.add(new Role(mRoleEditText.getText().toString()));

                if (user2edit == null) { // Newly created.
                    Log.v(TAG, "Saving user" + mUserNameEditText.getText().toString() + "...");
                    User user = new User(mUserIDEditText.getText().toString(),mUserNameEditText.getText().toString(),
                            mUserPasswordEditText.getText().toString(), roles);
                    ControlFactory.getUserController().selectCreateUser(user);
                }
                else { // Edited.
                    Log.v(TAG, "Saving user " + user2edit.getUserName() + "...");
                    user2edit.setUserName(mUserIDEditText.getText().toString());
                    user2edit.setUserPassword(mUserPasswordEditText.getText().toString());
                    user2edit.setUserRoles(roles);
                    ControlFactory.getUserController().selectUpdateUser(user2edit);
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                Log.v(TAG, "Deleting user " + user2edit.getUserName() + "...");
                ControlFactory.getUserController().selectDeleteUser(user2edit);
                return true;
            // Respond to a click on the "Cancel" menu option
            case R.id.action_cancel:
                Log.v(TAG, "Canceling creating/editing user...");
                ControlFactory.getUserController().selectCancelCreateEditUser();
                return true;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "Canceling creating/editing user...");
        ControlFactory.getUserController().selectCancelCreateEditUser();
    }

    public void createUser() {
        this.user2edit = null;
        mUserIDEditText.setText("", TextView.BufferType.EDITABLE);
        mUserNameEditText.setText("", TextView.BufferType.EDITABLE);
        mUserPasswordEditText.setText("", TextView.BufferType.EDITABLE);
        mRoleEditText.setText("", TextView.BufferType.EDITABLE);
        mUserNameEditText.setKeyListener(mUserIDEditTextKeyListener);
    }

    public void editUser(User user2edit) {
        this.user2edit = user2edit;
        if (user2edit != null) {
            mUserIDEditText.setText(user2edit.getId(), TextView.BufferType.NORMAL);
            mUserNameEditText.setText(user2edit.getUserName(), TextView.BufferType.NORMAL);
            mUserPasswordEditText.setText(user2edit.getUserPassword(), TextView.BufferType.EDITABLE);
            //YIJIE TODO
            mRoleEditText.setText(user2edit.getUserRoles().get(0).getRole(), TextView.BufferType.EDITABLE);
            mUserNameEditText.setKeyListener(null);
        }
    }

    public void showRoles(List<Role> roles) {
//YIJIE TODO
        mRoleEditText.setText(roles.get(0).getRole(), TextView.BufferType.EDITABLE);
    }
}
