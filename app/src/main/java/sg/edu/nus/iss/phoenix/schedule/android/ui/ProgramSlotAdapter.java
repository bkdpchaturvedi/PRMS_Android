package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import sg.edu.nus.iss.phoenix.R;

import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

public class ProgramSlotAdapter extends RecyclerView.Adapter<ProgramSlotAdapter.PSViewHolder> {

    public ArrayList<ProgramSlot> getProgramSlots() {
        return programSlots;
    }

    public void setProgramSlots(ArrayList<ProgramSlot> programSlots) {
        this.programSlots = programSlots;
    }

    ArrayList<ProgramSlot> programSlots;
    public ProgramSlotAdapter(ArrayList<ProgramSlot> programSlots) {
        this.programSlots=programSlots;
    }

    @Override
    public PSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View psView = inflater.inflate(R.layout.view_programslot_item, parent, false);

        // Return a new holder instance
        PSViewHolder viewHolder = new PSViewHolder(psView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PSViewHolder holder, final int position) {

        ProgramSlot currentPS=programSlots.get(position);

        TextView radioPMName = holder.radioPMName;
        radioPMName.setText(currentPS.getRadioProgram().getRadioProgramName());
        TextView producer = holder.producer;
        producer.setText(currentPS.getProducer().getId());
        TextView presenter = holder.presenter;
        radioPMName.setText(currentPS.getPresenter().getId());
        TextView radioPMDesc = holder.radioPMDesc;
        radioPMDesc.setText(currentPS.getRadioProgram().getRadioProgramDescription());
        TextView radioPSstartDuration = holder.radioPSstartDuration;
        radioPSstartDuration.setText(currentPS.getDateOfProgram().toString());
        TextView radioPSEndDuration = holder.radioPSEndDuration;
        radioPSEndDuration.setText(currentPS.getDateOfProgram().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA","Program Slot clicked");
                ProgramSlot programSlot = programSlots.get(position);
                ControlFactory.getScheduleController().selectEditProgramSlot(programSlot);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.programSlots.size();
    }

    public class PSViewHolder extends RecyclerView.ViewHolder{
        TextView producer;
        TextView radioPMName;
        TextView radioPMDesc;
        TextView radioPSstartDuration;
        TextView radioPSEndDuration;
        TextView presenter;

        public PSViewHolder(View psView) {
            super(psView);
             this.radioPMName = (TextView) psView.findViewById(R.id.programTitleTextView);
             this.radioPMDesc = (TextView)psView.findViewById(R.id.rpDescTextView);
             this.radioPSstartDuration = (TextView)psView.findViewById(R.id.fromTimeTextView);
             this.radioPSEndDuration = (TextView) psView.findViewById(R.id.toTimeTextView);
             this.producer = (TextView)psView.findViewById(R.id.producerTextView);
             this.presenter = (TextView)psView.findViewById(R.id.presenterTextView);

        }
    }


}
