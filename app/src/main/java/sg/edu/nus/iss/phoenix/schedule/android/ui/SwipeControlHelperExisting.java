package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class SwipeControlHelperExisting extends ItemTouchHelper.Callback {


    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private RectF buttonInstance = null;

    private RecyclerView.ViewHolder currentItemViewHolder = null;

    private int swipeFlags;

    //    private SwipeControllerActions buttonsActions = null;
    private float leftRevealWidth = 0;
    private float rightRevealWidth = 0;

    private int leftRevealResource = 0;
    private int rightRevealResource = 0;

//    public SwipeController(SwipeControllerActions buttonsActions) {
//        this.buttonsActions = buttonsActions;
//    }

    public SwipeControlHelperExisting(@LayoutRes int leftRevealResource, @LayoutRes int rightRevealResource) {
        if (leftRevealResource != 0 && rightRevealResource == 0) {
            this.swipeFlags = RIGHT;
        }
        else if (rightRevealResource != 0 && leftRevealResource == 0) {
            this.swipeFlags = LEFT;
        }
        else if (rightRevealResource != 0 && leftRevealResource != 0) {
            this.swipeFlags = LEFT | RIGHT;
        }
        this.rightRevealResource = rightRevealResource;
        this.leftRevealResource = leftRevealResource;
//        this.leftRevealWidth = 0;
//        this.rightRevealWidth = 370;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,  swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        setupRevealWidth(viewHolder);
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, leftRevealWidth);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -rightRevealWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -rightRevealWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if (dX > leftRevealWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeControlHelperExisting.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    if (buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                            View view = getHitView(getInflated(leftRevealResource, viewHolder),event.getX(), event.getY());
                            if (view != null) onClicked(view.getTag(), viewHolder.getAdapterPosition());
                        }
                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                            View view = getHitView(getInflated(rightRevealResource, viewHolder),event.getX(), event.getY());
                            if (view != null) onClicked(view.getTag(), viewHolder.getAdapterPosition());
                        }
                    }

//                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
//                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//                            buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
//                        }
//                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
//                        }
//                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawButtons(Canvas canvas, RecyclerView.ViewHolder viewHolder) {
        View itemView;
        if (rightRevealResource != 0) {
            itemView = getInflated(rightRevealResource, viewHolder);
            canvas.save();
            canvas.translate(itemView.getLeft(), itemView.getTop());
            itemView.draw(canvas);
            canvas.restore();
        }
        if (leftRevealResource != 0) {
            itemView = getInflated(leftRevealResource, viewHolder);
            canvas.save();
            canvas.translate(itemView.getLeft(), itemView.getTop());
            itemView.draw(canvas);
            canvas.restore();
        }


        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = new RectF(canvas.getClipBounds());
        }
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = new RectF(canvas.getClipBounds());
        }
    }

    private  void setupRevealWidth(RecyclerView.ViewHolder viewHolder) {
        rightRevealWidth = getRevealWidth(getInflated(rightRevealResource, viewHolder), true);
        leftRevealWidth = getRevealWidth(getInflated(leftRevealResource, viewHolder), false);
    }
    private float getRevealWidth(View view, Boolean right) {
        float result = 0;
        if (view instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) ((LinearLayout) view).getChildAt(0);
            if (right) result = linearLayout.getWidth();
            for (int i = 0 ; i < linearLayout.getChildCount(); i++) {
                if (right) {
                    if (linearLayout.getChildAt(i).getVisibility() == View.VISIBLE && linearLayout.getChildAt(i).getLeft() < result)
                        result = linearLayout.getChildAt(i).getLeft();
                }
                else {
                    if (linearLayout.getChildAt(i).getVisibility() == View.VISIBLE && linearLayout.getChildAt(i).getRight() > result)
                        result = linearLayout.getChildAt(i).getRight();
                }
            }
            if (right) result = linearLayout.getWidth() - result;
        }
        return result;
    }

    private View getHitView(View view, float x, float y) {
        if (view instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) ((LinearLayout) view).getChildAt(0);
            for (int i = 0 ; i < linearLayout.getChildCount(); i++) {
                Rect rect= new Rect();
                linearLayout.getChildAt(i).getHitRect(rect);
                if (rect.contains((int) x - view.getLeft(), (int) y - view.getTop()))
                    return linearLayout.getChildAt(i);
            }
        }
        return  null;
    }
    private LinearLayout getInflated(@LayoutRes int layoutResource, RecyclerView.ViewHolder viewHolder) {
        if (layoutResource != 0) {
            View itemView = viewHolder.itemView;

            LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());

            //   (LayoutInflater) itemView.getContext().getSystemService(itemView.getContext().LAYOUT_INFLATER_SERVICE);
            LinearLayout linearLayout = new LinearLayout(itemView.getContext());

            layoutInflater.inflate(layoutResource, linearLayout, true);
            onRevealInflated(linearLayout, viewHolder.getAdapterPosition());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(itemView.getWidth(), itemView.getHeight()));
            linearLayout.measure(View.MeasureSpec.makeMeasureSpec(itemView.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(itemView.getHeight(), View.MeasureSpec.EXACTLY));
            linearLayout.layout(itemView.getLeft(), itemView.getTop(), itemView.getWidth(), itemView.getHeight());
            return linearLayout;
        }
        return null;
    }

    public void onRevealInflated(View view, int position) {}
    public void onClicked(Object tag, int position) {}

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }
}
