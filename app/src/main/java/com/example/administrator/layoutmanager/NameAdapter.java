package com.example.administrator.layoutmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Size;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LiuXiaocong on 1/6/2016.
 */
public class NameAdapter extends RecyclerView.Adapter<NameAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private List<String> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private int initMyCount = 12;

    public int getInitMyCount() {
        return initMyCount;
    }

    public void setTargetRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public NameAdapter() {
        super();
    }

    public NameAdapter(List<String> data) {
        super();
        setData(data);
    }


    public void setData(List<String> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, String name);
    }

    private OnStartDragListener mDragStartListener;

    public interface OnStartDragListener {
        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);

    }

    public void setOnStartDragListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }


    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (position == initMyCount) {
            return 3;
        } else {
            return 0;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.drop_down_list_item, parent);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View containView = inflater.inflate(R.layout.drop_down_list_item, parent, false);

        return new ViewHolder(containView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Log.d("onBindViewHolder", "value:" + mData.get(position));
        String name = mData.get(position);
        if (name == "") return;
        holder.textView.setText(name);
        if (position == initMyCount) {

            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            lp.width = RecyclerView.LayoutParams.MATCH_PARENT;
            holder.itemView.setLayoutParams(lp);

            holder.itemView.setBackgroundColor(Color.parseColor("#ffff00"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ff0000"));
        }

        if (position < initMyCount) {
            setHolderType(holder, 0);
        } else if (position > initMyCount) {
            setHolderType(holder, 1);
        } else {
            holder.textView.setOnTouchListener(null);
            holder.itemView.setOnClickListener(null);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private long mTouchStartTime = System.currentTimeMillis();
    private int SPACE_TIME = 100;

    private void setHolderType(final ViewHolder holder, int type) {
        if (type == 0) {
            //top item
            holder.itemView.setOnClickListener(null);
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    switch (MotionEventCompat.getActionMasked(event)) {
                        case MotionEvent.ACTION_DOWN:
                            mTouchStartTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.d("OnTouchListener", "ACTION_MOVE");
                            Log.d("OnTouchListener", "time:" + (System.currentTimeMillis() - mTouchStartTime));
                            if (System.currentTimeMillis() - mTouchStartTime > SPACE_TIME) {
                                mDragStartListener.onStartDrag(holder);
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            mTouchStartTime = 0;
                            break;
                    }

//                    }
                    return false;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] targetPosition = new int[2];
                    calculateTargetXY(holder.itemView, targetPosition, initMyCount + 1);
                    startAnimation(mRecyclerView, holder.itemView, targetPosition[0], targetPosition[1]);
                    onItemMove(holder, holder.getAdapterPosition(), initMyCount);

                }
            });


        } else {
            holder.itemView.setOnTouchListener(null);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int[] targetPosition = new int[2];
                    calculateTargetXY(holder.itemView, targetPosition, initMyCount);
                    startAnimation(mRecyclerView, holder.itemView, targetPosition[0], targetPosition[1]);
                    onItemMove(holder, holder.getAdapterPosition(), initMyCount);
//                    Handler mHandler=new Handler();
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            onItemMove(holder, holder.getAdapterPosition(), initMyCount);
//                        }
//                    },100);

                }
            });

        }
    }

    @Override
    public boolean onItemMove(RecyclerView.ViewHolder holder, int fromPosition, int toPosition) {
        ViewHolder mHolder = (ViewHolder) holder;


        if (fromPosition < initMyCount && toPosition >= initMyCount) {
            initMyCount--;
            setHolderType(mHolder, 1);
        }
        if (fromPosition > initMyCount && toPosition <= initMyCount) {
            initMyCount++;
            setHolderType(mHolder, 0);
        }

        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        //notifyDataSetChanged();
        return true;
    }


    @Override
    public void onItemDismiss(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

    }


    private void calculateTargetXY(View moveView, @Size(2) int[] targetPosition, int targetIndex) {
        if (initMyCount == 0) {
            targetPosition[0] = 0;
            targetPosition[1] = 0;

        } else if (initMyCount == mData.size()) {


        } else {
            View targetView = mRecyclerView.getLayoutManager().findViewByPosition(initMyCount - 1);
            if (targetIndex > initMyCount) {
                //top to btm
                targetView = mRecyclerView.getLayoutManager().findViewByPosition(initMyCount + 1);
            }


            int itemWidth = targetView.getWidth();
            int itemHeight = targetView.getHeight();

            int targetLeft = targetView.getLeft();
            int targetTop = targetView.getTop();

            if (targetIndex == initMyCount) {
                //need recorrect postion when move to top

                Log.d("calculateTargetXY", "mRecyclerView.getRight():" + mRecyclerView.getRight());
                Log.d("calculateTargetXY", "targetLeft" + targetLeft);
                Log.d("calculateTargetXY", "itemWidth" + itemWidth);
                Log.d("calculateTargetXY", "moveView.getWidth()" + moveView.getWidth());
                if (mRecyclerView.getRight() - (targetLeft + itemWidth) < moveView.getWidth()) {
                    targetLeft = 0;
                    targetTop = targetTop + itemHeight;

                } else {
                    targetLeft = targetLeft + itemWidth;
                }
            }
            targetPosition[0] = targetLeft;
            targetPosition[1] = targetTop;
        }
    }


    private ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {

        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);

        final ImageView mirrorView = new ImageView(recyclerView.getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);

        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int[] parenLocations = new int[2];
        recyclerView.getLocationOnScreen(parenLocations);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0] - parenLocations[0], locations[1] - parenLocations[1], 0, 0);

        parent.addView(mirrorView, params);

        return mirrorView;
    }

    private void startAnimation(RecyclerView recyclerView, final View currentView, float targetX, float targetY) {

        Log.d("target", "X:" + targetX);
        Log.d("target", "Y:" + targetY);

        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);

        Animation animation = getTranslateAnimator(
                targetX - currentView.getLeft(), targetY - currentView.getTop());
        currentView.setVisibility(View.INVISIBLE);
        mirrorView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(mirrorView);
                if (currentView.getVisibility() == View.INVISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }
}
