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
                    R.layout.activity_user_list_item, parent, false);
        }
        //    Word currentWord = getItem(position);
        User currentUser = getItem(position);

        TextView userId = (TextView)listItemView.findViewById(R.id.list_item_user_id_text_view);
        userId.setText(currentUser.getId(), TextView.BufferType.NORMAL);
        userId.setKeyListener(null);

        TextView userName = (TextView)listItemView.findViewById(R.id.list_item_user_name_text_view);
        userName.setText(currentUser.getUserName(), TextView.BufferType.NORMAL);
        userName.setKeyListener(null); // This disables editing.

        return listItemView;
    }
}
