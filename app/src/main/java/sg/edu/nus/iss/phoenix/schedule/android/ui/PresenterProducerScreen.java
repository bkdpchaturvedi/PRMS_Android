package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.ConstantHelper;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.schedule.android.ui.adapter.PresenterProducerListAdapter;

public class PresenterProducerScreen extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, PresenterProducerListAdapter.PresenterProducerViewHolderClick {


    private PresenterProducerListAdapter presenterProducerListAdapter;
    private RecyclerView recyclerView;
    private User selectedPresenter;
    private User selectedProducer;
    private String mode;

    public void displayProducer(final List<User> producers) {
        presenterProducerListAdapter.setUsers(producers);
    }

    public void displayPresenter(final List<User> presenters) {
        presenterProducerListAdapter.setUsers(presenters);
    }

    private void setupControls() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_reviewselectpresenterproducer_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        presenterProducerListAdapter = new PresenterProducerListAdapter(new ArrayList<User>());
        presenterProducerListAdapter.setPresenterProducerViewHolderClick(this);
        recyclerView.setAdapter(presenterProducerListAdapter);

        ((RadioGroup) findViewById(R.id.rdo_reviewselectpresenterproducer_filter)).setOnCheckedChangeListener(this);
    }

    public void selectGetAllPresenters() {
        mode = ConstantHelper.PRESENTERS;
        ControlFactory.getReviewSelectPresenterProducerController().selectGetAllPresenters();
    }

    public void selectGetAllProducers() {
        mode = ConstantHelper.PRODUCERS;
        ControlFactory.getReviewSelectPresenterProducerController().selectGetAllProducers();
    }

    public void selectPresenter(User presenter) {
        selectedPresenter = presenter;
        ControlFactory.getReviewSelectPresenterProducerController().selectPresenter(presenter);
    }

    public void selectProducer(User producer) {
        selectedProducer = producer;
        ControlFactory.getReviewSelectPresenterProducerController().selectProducer(producer);
    }

    public void displaySelectedPresenter(User presenter) {
        if (presenter != null) {
            ((TextView) findViewById(R.id.tv_reviewselectpresenterproducer_presenter_name))
                    .setText(presenter.getUserName());
        }
    }

    public void displaySelectedProducer(User producer) {
        if (producer != null) {
            ((TextView) findViewById(R.id.tv_reviewselectpresenterproducer_producer_name))
                    .setText(producer.getUserName());
        }
    }

    public void displayErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }

    public void displaySuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }

    public void displayPresenters(List<User> presenters) {
        if (presenters != null && presenters.size() > 0) {
            ((TextView) findViewById(R.id.tv_reviewselectpresenterproducer_norecoundfound))
                    .setVisibility(View.GONE);
            presenterProducerListAdapter.getSelectedItems().clear();
            if (selectedPresenter != null) {
                for (int i =0; i<presenters.size(); i++) {
                    if (presenters.get(i).getId().equals(selectedPresenter.getId())) {
                        presenterProducerListAdapter.getSelectedItems().put(i, true);
                    }
                }
            }
        } else {
            ((TextView) findViewById(R.id.tv_reviewselectpresenterproducer_norecoundfound))
                    .setVisibility(View.VISIBLE);
        }
        presenterProducerListAdapter.setUsers(presenters);
        presenterProducerListAdapter.notifyDataSetChanged();
    }

    public void displayProducers(List<User> producers) {
        if (producers != null && producers.size() > 0) {
            ((TextView) findViewById(R.id.tv_reviewselectpresenterproducer_norecoundfound))
                    .setVisibility(View.GONE);
            presenterProducerListAdapter.getSelectedItems().clear();
            if (selectedProducer != null) {
                for (int i =0; i<producers.size(); i++) {
                    if (producers.get(i).getId().equals(selectedProducer.getId())) {
                        presenterProducerListAdapter.getSelectedItems().put(i, true);
                    }
                }
            }
        } else {
            ((TextView) findViewById(R.id.tv_reviewselectpresenterproducer_norecoundfound))
                    .setVisibility(View.VISIBLE);
        }
        presenterProducerListAdapter.setUsers(producers);
        presenterProducerListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_select_presenter_producer);
        setupControls();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.selectedPresenter = (User) getIntent().getSerializableExtra(ConstantHelper.PRESENTER);
        this.selectedProducer = (User) getIntent().getSerializableExtra(ConstantHelper.PRODUCER);
        ControlFactory.getReviewSelectPresenterProducerController().onDisplayScreen(this, this.selectedPresenter, this.selectedProducer);
        selectGetAllPresenters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_review_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                ControlFactory.getReviewSelectPresenterProducerController().selectReturnResult(selectedPresenter, selectedProducer);
        }
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rdo_reviewselectpresenterproducer_presenter:
                selectGetAllPresenters();
                break;
            case R.id.rdo_reviewselectpresenterproducer_producer:
                selectGetAllProducers();
                break;
        }
    }

    @Override
    public void onItemClick(PresenterProducerListAdapter.PresenterProducerViewHolder viewHolder) {
        switch (mode) {
            case ConstantHelper.PRESENTERS:
                selectPresenter(presenterProducerListAdapter.getUsers()
                        .get(viewHolder.getAdapterPosition()));
                break;
            case ConstantHelper.PRODUCERS:
                selectProducer(presenterProducerListAdapter.getUsers()
                        .get(viewHolder.getAdapterPosition()));
                break;
        }
    }

    public void unloadDisplayScreen() {
        finish();
    }
}
