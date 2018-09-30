package sg.edu.nus.iss.phoenix.radioprogram.android.ui;

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
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;

public class RadioProgramAdapter extends ArrayAdapter<RadioProgram> {

    public RadioProgramAdapter(@NonNull Context context,  ArrayList<RadioProgram> radioPrograms) {
        super(context, 0, radioPrograms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_radio_program_list_item, parent, false);
        }
        //    Word currentWord = getItem(position);
        RadioProgram currentRP = getItem(position);

        TextView radioPMName = (TextView)listItemView.findViewById(R.id.list_item_radio_program_name_text_view);
        radioPMName.setText(currentRP.getRadioProgramName(), TextView.BufferType.NORMAL);
        radioPMName.setKeyListener(null); // This disables editing.


        TextView radioPMDuration = (TextView)listItemView.findViewById(R.id.list_item_radio_program_duration_text_view);
        radioPMDuration.setText(currentRP.getRadioProgramDuration(), TextView.BufferType.NORMAL);
        radioPMDuration.setKeyListener(null);

        return listItemView;
    }
}
