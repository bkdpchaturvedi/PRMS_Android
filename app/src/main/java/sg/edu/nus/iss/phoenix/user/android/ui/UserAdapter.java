package sg.edu.nus.iss.phoenix.user.android.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(@NonNull Context context,  ArrayList<User> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_user, parent, false);
        }
        //    Word currentWord = getItem(position);
        User currentUser = getItem(position);

        EditText userId = (EditText)listItemView.findViewById(R.id.maintain_user_id_text_view);
        userId.setText(currentUser.getId(), TextView.BufferType.NORMAL);
        userId.setKeyListener(null);

        EditText userName = (EditText)listItemView.findViewById(R.id.maintain_user_name_text_view);
        userName.setText(currentUser.getUserName(), TextView.BufferType.NORMAL);
        userName.setKeyListener(null); // This disables editing.

        EditText userPassword = (EditText)listItemView.findViewById(R.id.maintain_user_password_text_view);
        userPassword.setText(currentUser.getUserPassword(), TextView.BufferType.NORMAL);
        userPassword.setKeyListener(null);

        EditText userRoles = (EditText)listItemView.findViewById(R.id.maintain_user_roles_text_view);
        userRoles.setText(currentUser.getUserRoles(), TextView.BufferType.NORMAL);
        userRoles.setKeyListener(null);

        return listItemView;
    }
}
