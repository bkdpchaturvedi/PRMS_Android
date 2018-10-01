package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

public class ScheduledProgramsListScreen extends AppCompatActivity implements ProgramSlotAdapter.ProgramSlotViewHolderClick {
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
                selectCreateProgramSlot();
            }
        });
        tv_itemempty_value=(TextView) findViewById(R.id.tv_itemempty_value);
        psbar=(ProgressBar)findViewById(R.id.psloadProgressbar);

        setupAdapter();
        setupRecyclerView();
        setupCalender();
    }

    private  void selectCreateProgramSlot() {
        ControlFactory.getScheduleController().selectCreateProgramSlot();
    }

    private void setupAdapter() {

        ArrayList<ProgramSlot> programSlots = new ArrayList<ProgramSlot>();
        mPSAdapter = new ProgramSlotAdapter(programSlots);
        mPSAdapter.setProgramSlotViewHolderClick(this);
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
                        selectCopyProgramSlot(selectecPS);
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

    private void selectCopyProgramSlot(ProgramSlot programSlot) {
        ControlFactory.getScheduleController().selectCopyProgramSlot(programSlot);
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
        ControlFactory.getScheduleController().scheduleMaintained();
    }

    private void selectViewProgramSlots(ZonedDateTime dateOfProgram) {
        tv_itemempty_value.setVisibility(View.GONE);
        psbar.setVisibility(View.VISIBLE);
        Log.i("calender",dateOfProgram.toString());
        ControlFactory.getScheduleController().selectViewProgramSlots(dateOfProgram);
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
       ControlFactory.getScheduleController().onDisplayScheduledProgramListScreen(this);
        tv_itemempty_value.setVisibility(View.GONE);
        psbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(ProgramSlotAdapter.PSViewHolder viewHolder) {
        int position =viewHolder.getAdapterPosition();
        ProgramSlot programSlot = mPSAdapter.getProgramSlots().get(position);
        selectViewProgramSlot(programSlot);
    }

    private void selectViewProgramSlot(ProgramSlot programSlot) {
        ControlFactory.getScheduleController().selectViewProgramSlot(programSlot);
    }

    public void displayErrorMessage(String message) {
        Toast.makeText(this, "ERROR: " + message, Toast.LENGTH_SHORT).show();
    }
}
