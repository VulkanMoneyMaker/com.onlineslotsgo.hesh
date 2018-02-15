package com.onlineslotsgo.hesh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onlineslotsgo.hesh.interpolator.src.kankan.wheel.widget.OnWheelScrollListener;
import com.onlineslotsgo.hesh.interpolator.src.kankan.wheel.widget.WheelView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity implements OnWheelScrollListener {
    private static final String TAG = Game.class.getSimpleName();

    private static final int INIT_CREDIT = 1200;

    private ArrayList<Integer> mListImages;
    private ArrayList<WheelView> mListWheel;
    private ArrayList<Boolean> mStateWheels;

    private TextView textViewOlderData;
    private TextView nedtDaa;
    private TextView firstData;

    private AlertDialog selectedDialog;

    private int tti_1;
    private int tti_2;
    private int tti_3;

    private boolean bolean_data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_game);

        textViewOlderData = findViewById(R.id.total_text_view);
        nedtDaa = findViewById(R.id.bet_text_view);
        firstData = findViewById(R.id.credit_text_view);

        mListWheel = new ArrayList<>();
        mStateWheels = new ArrayList<>();
        TypedArray wheels = getResources().obtainTypedArray(R.array.wheel_items);
        for (int i = 0; i < wheels.length(); ++i) {
            WheelView view = findViewById(wheels.getResourceId(i, 0));
            mListWheel.add(view);
            mStateWheels.add(false);
        }
        wheels.recycle();
        initSlots();

        Button playButton = findViewById(R.id.btn_play);
        playButton.setOnClickListener(__ -> {
            bolean_data = false;
            initStateWheels();
            generateRandomScrollWheels();
        });

        Button increaseTotalBet = findViewById(R.id.increase_total_button);
        increaseTotalBet.setOnClickListener(__ -> {
            int temp = tti_1 + 1;
            if (temp <= tti_2) {
                tti_1 = temp;
            }
            updateViewData();
        });

        Button decreaseTotal = findViewById(R.id.decrease_total_button);
        decreaseTotal.setOnClickListener(__ -> {
            int temp = tti_1 - 1;
            if (temp >= 1) {
                tti_1 = temp;
            }
            updateViewData();
        });

        initViewData();
        updateViewData();
    }

    @Override
    public void onStart() {
        super.onStart();
        selectedDialog = initDialogWin();
        for (WheelView view : mListWheel) {
            view.addScrollingListener(this);
        }
    }

    @Override
    public void onStop() {
        if (selectedDialog != null) {
            selectedDialog.dismiss();
        }
        selectedDialog = null;
        for (WheelView view : mListWheel) {
            view.removeScrollingListener(this);
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        selectedDialog = null;
        super.onDestroy();
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
        Log.d(TAG, "onScrollingStarted - " + String.valueOf(wheel.getCurrentItem()));
    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        Log.d(TAG, "onScrollingFinished - " + String.valueOf(wheel.getCurrentItem()));

        mStateWheels.set(mListWheel.indexOf(wheel), true);
        if (checkAllWheelsFinished() && !bolean_data) {
            generateWin();
        }
    }

    @NonNull
    public static Intent getGameActivityIntent(Context context) {
        return new Intent(context, Game.class);
    }



    private void initViewData() {
        tti_2 = INIT_CREDIT;
        tti_1 = 1;
        tti_3 = 1;
    }

    private void updateViewData() {
        textViewOlderData.setText(String.format(Locale.getDefault(),"%d", tti_1));
        firstData.setText(String.format(Locale.getDefault(),"%d", tti_2));
        nedtDaa.setText(String.format(Locale.getDefault(),"%d", tti_3));
    }

    private void increaseCredit(boolean isSuperWin) {
        if (isSuperWin) { // Если джек-пот
            tti_2 += tti_2 * 0.3;
        } else {
            tti_2 += tti_1;
        }
    }

    private void decreaseCredit() {
        tti_2 -= tti_1;
        if (tti_2 < 0) {
            tti_2 = 0;
        }
    }

    private void initSlots() {
        for (WheelView view : mListWheel) {
            iniWheel(view, getListImages());
        }
    }

    private ArrayList<Integer> getListImages(){
        if (mListImages == null) {
            final ArrayList<Integer> list = new ArrayList<>();
            TypedArray images = getResources().obtainTypedArray(R.array.slots_images);
            for (int i = 0; i < images.length(); ++i) {
                list.add(images.getResourceId(i, 0));
            }
            images.recycle();
            this.mListImages = list;
        }
        return mListImages;
    }

    private void iniWheel(WheelView wheelView, ArrayList<Integer> list) {
        wheelView.setViewAdapter(new Adapter(this, list));
        wheelView.setCurrentItem((int) (Math.random() * 10.0d));
        wheelView.setVisibleItems(4);
        wheelView.setCyclic(true);
        wheelView.setEnabled(false);
        wheelView.addScrollingListener(this);
    }

    private void generateRandomScrollWheels() {
        Random random = new Random();
        for (WheelView view : mListWheel) {
            view.scroll(((int) ((Math.random() * ((double) random.nextInt(30))) + 20.0d)) - 350,
                    random.nextInt(3000) + 2000);
        }
    }

    @SuppressLint("InflateParams")
    private AlertDialog initDialogWin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.item_dialog_win, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        return dialog;
    }

    private void showDialogWin() {
        if (selectedDialog != null) {
            if (!selectedDialog.isShowing()) {
                selectedDialog.show();
            }
        }
    }

    private void generateWin() {
        bolean_data = true;
        Random random = new Random();
        int value = random.nextInt(10 + 1); // [1;10]
        Log.d(TAG, "Random win value - " + value);
        if (value % 2 == 0) { // Если четное то победил
            if (value == 4 || value == 8) { // Если джек-пот
                showDialogWin();
                increaseCredit(true);
            } else {
                increaseCredit(false);
            }
        } else {
            decreaseCredit();
        }
        updateViewData();
    }

    private void initStateWheels() {
        for (int i = 0; i < mStateWheels.size(); ++i){
            mStateWheels.set(i, false);
        }
    }

    private boolean checkAllWheelsFinished() {
        return !mStateWheels.contains(false);
    }
}
