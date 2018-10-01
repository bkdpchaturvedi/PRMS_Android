package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

public class ProgramSlotAdapter extends RecyclerView.Adapter<ProgramSlotAdapter.PSViewHolder> {

    List<ProgramSlot> programSlots;
    private ProgramSlotViewHolderClick psViewHolderClick;

    public ProgramSlotAdapter(ArrayList<ProgramSlot> programSlots) {
        this.programSlots = programSlots;
    }

    public void setProgramSlotViewHolderClick(ProgramSlotViewHolderClick psViewHolderClick) {
        this.psViewHolderClick = psViewHolderClick;
    }

    public List<ProgramSlot> getProgramSlots() {
        return programSlots;
    }

    public void setProgramSlots(List<ProgramSlot> programSlots) {
        this.programSlots = programSlots;
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
    public void onBindViewHolder(final PSViewHolder holder, final int position) {

        ProgramSlot currentPS = programSlots.get(position);

        TextView radioPMName = holder.radioPMName;
        radioPMName.setText(currentPS.getRadioProgram().getRadioProgramName());
        TextView producer = holder.producer;
        producer.setText(currentPS.getProducer().getUserName());
        TextView presenter = holder.presenter;
        presenter.setText(currentPS.getPresenter().getUserName());
        //TextView radioPMDesc = holder.radioPMDesc;
        //radioPMDesc.setText(currentPS.getRadioProgram().getRadioProgramDescription());
        TextView radioPSstartDuration = holder.radioPSstartDuration;
        //  SimpleDateFormat formatter =new SimpleDateFormat("hh:mm tt");

        String startTime = DateTimeFormatter.ofPattern("HH:mm").format(currentPS.getDateOfProgram());
        radioPSstartDuration.setText(
                startTime);
        TextView radioPSEndDuration = holder.radioPSEndDuration;

        long durationinhours = (currentPS.getDuration().toMinutes());
        radioPSEndDuration.setText("" + durationinhours + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psViewHolderClick.onItemClick(holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.programSlots.size();
    }

    public interface ProgramSlotViewHolderClick {
        void onItemClick(ProgramSlotAdapter.PSViewHolder viewHolder);
    }

    public class PSViewHolder extends RecyclerView.ViewHolder {
        TextView producer;
        TextView radioPMName;
        //TextView radioPMDesc;
        TextView radioPSstartDuration;
        TextView radioPSEndDuration;
        TextView presenter;

        public PSViewHolder(View psView) {
            super(psView);
            this.radioPMName = (TextView) psView.findViewById(R.id.programTitleTextView);
            // this.radioPMDesc = (TextView)psView.findViewById(R.id.rpDescTextView);
            this.radioPSstartDuration = (TextView) psView.findViewById(R.id.fromTimeTextView);
            this.radioPSEndDuration = (TextView) psView.findViewById(R.id.toTimeTextView);
            this.producer = (TextView) psView.findViewById(R.id.producerTextView);
            this.presenter = (TextView) psView.findViewById(R.id.presenterTextView);

        }
    }


}
