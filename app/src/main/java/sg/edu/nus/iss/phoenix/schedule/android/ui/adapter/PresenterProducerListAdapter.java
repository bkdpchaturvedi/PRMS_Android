package sg.edu.nus.iss.phoenix.schedule.android.ui.adapter;

import android.annotation.SuppressLint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;

public class PresenterProducerListAdapter  extends RecyclerView.Adapter<PresenterProducerListAdapter.PresenterProducerViewHolder>  {
    private List<User> users;
    private PresenterProducerViewHolderClick userViewHolderClick;
    private SparseBooleanArray selectedItems  = new SparseBooleanArray();;
    private int currentSelected = -1;

    public PresenterProducerListAdapter(List<User> users) {
        if (users == null) users = new ArrayList<User>();
        this.users = users;
    }

    public void setPresenterProducerViewHolderClick(PresenterProducerViewHolderClick userViewHolderClick) {
        this.userViewHolderClick = userViewHolderClick;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

    @Override
    public PresenterProducerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_presenterproducer,parent,false);
        final PresenterProducerViewHolder userViewHolder = new PresenterProducerViewHolder(itemView);
        itemView.setOnTouchListener(new View.OnTouchListener() {
            int MAX_CLICK_DURATION = 200;
            long startClickTime;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        long clickDuration = System.currentTimeMillis() - startClickTime;
                        if(clickDuration < MAX_CLICK_DURATION) {
                            if (userViewHolder.itemView.isClickable()) {
                                userViewHolderClick.onItemClick(userViewHolder);
                                if (selectedItems.get(userViewHolder.getAdapterPosition(), false)) {
                                    selectedItems.delete(userViewHolder.getAdapterPosition());
                                    userViewHolder.setSelected(false);
                                } else {
                                    if (currentSelected >= 0) {
                                        ((PresenterProducerViewHolder) ((RecyclerView) v.getParent()).findViewHolderForAdapterPosition(currentSelected)).setSelected(false);
                                    }
                                    selectedItems.delete(currentSelected);
                                    selectedItems.put(userViewHolder.getAdapterPosition(), true);
                                    userViewHolder.setSelected(true);
                                    currentSelected = userViewHolder.getAdapterPosition();
                                }
                            }
                        }
                        break;
                }
                return false;
            }
        });
        return  userViewHolder;
    }

    @Override
    public void onBindViewHolder(PresenterProducerViewHolder holder, int position) {
        User user = users.get(position);
        holder.tv_presenterproducer_name.setText(user.getUserName());
        holder.setSelected(selectedItems.get(holder.getAdapterPosition(), false));
    }


    @Override
    public int getItemCount() {
        return users.size();
    }
    public interface  PresenterProducerViewHolderClick {
        void onItemClick(PresenterProducerViewHolder viewHolder);
    }
    public class PresenterProducerViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_presenterproducer_name;
        public PresenterProducerViewHolder(final View itemView) {
            super(itemView);
            tv_presenterproducer_name =  itemView.findViewById(R.id.tv_presenterproducer_name);
        }
        public void setSelected(Boolean selected) {
            if (selected) {
                ((CardView) this.itemView.findViewById(R.id.cv_presenterproducer)).setCardBackgroundColor(ContextCompat.getColor(this.itemView.getContext(), R.color.cardview_highlighted));
            } else {
                ((CardView) this.itemView.findViewById(R.id.cv_presenterproducer)).setCardBackgroundColor(ContextCompat.getColor(this.itemView.getContext(), R.color.cardview_normal));
            }
        }
    }
}