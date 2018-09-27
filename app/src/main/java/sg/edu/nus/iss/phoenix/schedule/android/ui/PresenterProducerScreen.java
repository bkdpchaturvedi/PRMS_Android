package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.schedule.android.ui.adapter.PresenterProducerListAdapter;

public class PresenterProducerScreen extends AppCompatActivity {

    private PresenterProducerListAdapter presenterProducerListAdapter;
    private RecyclerView recyclerView;

    public void displayProducer(final List<User> producers) {
        presenterProducerListAdapter.setUsers(producers);
    }

    public void displayPresenter(final List<User> presenters) {
        presenterProducerListAdapter.setUsers(presenters);
    }

    private void setupControls() {
        presenterProducerListAdapter = new PresenterProducerListAdapter(new ArrayList<User>());
        presenterProducerListAdapter.setPresenterProducerViewHolderClick( new PresenterProducerListAdapter.PresenterProducerViewHolderClick() {
            @Override
            public void onItemClick(PresenterProducerListAdapter.PresenterProducerViewHolder viewHolder) {
                //action to be here
            }
        });
        recyclerView.setAdapter(presenterProducerListAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_select_presenter_producer);
        recyclerView = (RecyclerView) findViewById(R.id.rv_reviewselectpresenterproducer_list);
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
        // User clicked on a menu option in the app bar overflow menu
//        switch (item.getItemId()) {
//            // Respond to a click on the "View" menu option
//            case R.id.action_view:
//                if (selectedRP == null) {
//                    // Prompt for the selection of a radio program.
//                    Toast.makeText(this, "Select a radio program first! Use arrow keys on emulator", Toast.LENGTH_SHORT).show();
//                    Log.v(TAG, "There is no selected radio program.");
//                }
//                else {
//                    Log.v(TAG, "Viewing radio program: " + selectedRP.getRadioProgramName() + "...");
//                    ControlFactory.getProgramController().selectEditProgram(selectedRP);
//                }
//        }

        return true;
    }
}
