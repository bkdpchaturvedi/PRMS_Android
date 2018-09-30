package sg.edu.nus.iss.phoenix.user.android.ui;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;

public class UserListScreen extends AppCompatActivity {
    // Tag for logging
    private static final String TAG = UserListScreen.class.getName();

    //private EditText mUserIDEditText;
    //private EditText mUserNameEditText;
    //private EditText mUserPasswordEditText;
    //private EditText mRolesEditText;
    private ListView mListView;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

         //mUserIDEditText = (EditText) findViewById(R.id.maintain_user_id_text_view);
         //mUserNameEditText = (EditText) findViewById(R.id.maintain_user_name_text_view);
         //mUserPasswordEditText = (EditText) findViewById(R.id.maintain_user_password_text_view);
         //mRolesEditText = (EditText) findViewById(R.id.maintain_user_roles_text_view);

        ArrayList<User> users = new ArrayList<User>();
        mUserAdapter = new UserAdapter(this, users);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControlFactory.getUserController().selectCreateUser();
            }
        });

        mListView = (ListView) findViewById(R.id.user_list);
        mListView.setAdapter(mUserAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position,
                                    long arg3)
            {
                Log.v(TAG, "User at position " + position + " clicked.");
                User user = (User) adapterView.getItemAtPosition(position);
                // Log.v(TAG, "User name is " + user.getUserName());
                ControlFactory.getUserController().selectEditUser(user);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setSelection(0);

        ControlFactory.getUserController().onDisplayUserList(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        ControlFactory.getUserController().maintainedUser();
    }

    public void showUsers(List<User> users) {
        mUserAdapter.clear();
        for (int i = 0; i < users.size(); i++) {
            mUserAdapter.add(users.get(i));
        }
    }
}

