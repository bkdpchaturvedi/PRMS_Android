package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.content.Context;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

public class ScheduleProgramsListScreen extends AppCompatActivity {
    CalendarView scheduleView=null;
    private ProgramSlotAdapter mPSAdapter;
    private ProgramSlot selectedPS = null;
    RecyclerView recyclerView=null;
    TextView tv_itemempty_value=null;
    ProgressBar psbar=null;

    SwipeMenuTocuhHelper swipeMenuTocuhHelper=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_programs_list_screen);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.psfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControlFactory.getScheduleController().selectCreateProgramSlot();
            }
        });
        tv_itemempty_value=(TextView) findViewById(R.id.tv_itemempty_value);
        psbar=(ProgressBar)findViewById(R.id.psloadProgressbar);

        setupAdapter();
        setupRecyclerView();
        setupCalender();
    }

    private void setupAdapter() {

        ArrayList<ProgramSlot> programSlots = new ArrayList<ProgramSlot>();
        mPSAdapter = new ProgramSlotAdapter(programSlots);



    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.programSlotList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mPSAdapter);

       /*final SwipeMenuTocuhHelper swipeController = new SwipeMenuTocuhHelper();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);*/

        final SwipeControlHelperExisting swipeMenuTocuhHelper = new SwipeControlHelperExisting(0, R.layout.partial_swipe_item) {



            @Override
            public void onClicked(Object tag, int position) {
                ProgramSlot selectecPS = mPSAdapter.getProgramSlots().get(position);
                switch (tag.toString()) {
                    case "copy":
                        ControlFactory.getScheduleController().selectCopyProgramSlot(selectecPS);
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
    private  void setupCalender() {
        Date d = new Date();
        scheduleView = (CalendarView) findViewById(R.id.scheduleCalender);
        scheduleView.setDate(d.getTime());

        scheduleView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                ZonedDateTime dateOfProgram = ZonedDateTime.of(year, month + 1, dayOfMonth, 0, 0, 0, 0, ZoneId.systemDefault());
                selectViewProgramSlots(dateOfProgram);

            }
        });
    }

    @Override
    public void onBackPressed() {
        ControlFactory.getScheduleController().maintainedSchedule();
    }

    public void displayProgramSlots(ZonedDateTime dateOfProgram, List<ProgramSlot> programSlots) {
        ((CalendarView) findViewById(R.id.scheduleCalender)).setDate(dateOfProgram.toInstant().toEpochMilli());
        mPSAdapter.setProgramSlots(programSlots);
       mPSAdapter.notifyDataSetChanged();
       if(mPSAdapter.getItemCount()>0)
       {
           tv_itemempty_value.setVisibility(View.GONE);
       }
       else
       {
           tv_itemempty_value.setVisibility(View.VISIBLE);
       }
       psbar.setVisibility(View.GONE);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
       ControlFactory.getScheduleController().onDisplayProgramListScreen(this);
        tv_itemempty_value.setVisibility(View.GONE);
        psbar.setVisibility(View.VISIBLE);
    }
}
