package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

public class ScheduleProgramsListScreen extends AppCompatActivity {
    CalendarView scheduleView=null;
    private ProgramSlotAdapter mPSAdapter;
    private ProgramSlot selectedPS = null;
    RecyclerView recyclerView=null;

    SwipeMenuTocuhHelper swipeMenuTocuhHelper=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_programs_list_screen);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControlFactory.getScheduleController().selectCreateProgramSlot();
            }
        });

        setupControls();

    }
    private  void setupControls() {
        Date d = new Date();
        scheduleView = (CalendarView) findViewById(R.id.scheduleCalender);
        scheduleView.setDate(d.getTime());

        scheduleView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Date d = new Date();
                d.setTime(calendarView.getDate());

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.programSlotList);
        ArrayList<ProgramSlot> programSlots = new ArrayList<ProgramSlot>();
        mPSAdapter = new ProgramSlotAdapter(programSlots);
        recyclerView.setAdapter(mPSAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        final SwipeMenuTocuhHelper swipeMenuTocuhHelper = new SwipeMenuTocuhHelper(0, R.layout.partial_swipe_item) {



            @Override
            public void onClicked(Object tag, int position) {
                ProgramSlot selectecPS = mPSAdapter.getProgramSlots().get(position);
                switch (tag.toString()) {
                    case "delete":
                        //implement delete code
                        break;
                    case "edit":
                        //implement edit code
                        break;
                    case "copy":
                        //implement copy code
                        break;
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeMenuTocuhHelper);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration()

        {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeMenuTocuhHelper.onDraw(c);
            }
        });

    }
    @Override
    public void onBackPressed() {
        ControlFactory.getScheduleController().maintainedSchedule();
    }
    public void showProgramSlots(ArrayList<ProgramSlot> programSlots) {
        mPSAdapter.setProgramSlots(programSlots);
       mPSAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Date d = new Date();
        d.setTime(this.scheduleView.getDate());
        ControlFactory.getScheduleController().onDisplayProgramListScreen(this,d);
        ControlFactory.getScheduleController().onDisplayProgramListScreen(this);

    }
}
