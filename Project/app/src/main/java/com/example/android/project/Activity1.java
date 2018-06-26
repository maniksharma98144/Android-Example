package com.example.android.project;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Activity1 extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = null;
    int windowwidth;
    int windowheight;
    private ViewGroup mRootLayout;
    private int _xDelta;
    private int _yDelta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        mRootLayout = findViewById(R.id.root);
        ImageView mImageView = mRootLayout.findViewById(R.id.im_move_zoom_rotate);
        mImageView.setOnTouchListener(this);

        mRootLayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = mRootLayout.getWidth();
                windowheight = mRootLayout.getHeight();
            }
        });
    }

    private boolean isOutReported = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        if (isOut(v)) {
            if (!isOutReported) {
                isOutReported = true;
                Toast.makeText(this, "OUT", Toast.LENGTH_SHORT).show();
            }
        } else {
            isOutReported = false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                _xDelta = X - v.getLeft();
                _yDelta = Y - v.getTop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                    lp.removeRule(RelativeLayout.CENTER_VERTICAL);
                } else {
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                    lp.addRule(RelativeLayout.CENTER_VERTICAL, 0);
                }
                lp.leftMargin = X - _xDelta;
                lp.topMargin = Y - _yDelta;
                lp.rightMargin = v.getWidth() - lp.leftMargin - windowwidth;
                lp.bottomMargin = v.getHeight() - lp.topMargin - windowheight;
                Log.d(TAG, "onTouch:"+lp.leftMargin+" "+lp.topMargin+" "+lp.rightMargin+" "+lp.bottomMargin);
                v.setLayoutParams(lp);
                break;
        }
        return true;
    }

    private boolean isOut(View view) {
        float percentageOut = 0.50f;
        int viewPctWidth = (int) (view.getWidth() * percentageOut);
        int viewPctHeight = (int) (view.getHeight() * percentageOut);

        return ((-view.getLeft() >= viewPctWidth) ||
                (view.getRight() - windowwidth) > viewPctWidth ||
                (-view.getTop() >= viewPctHeight) ||
                (view.getBottom() - windowheight) > viewPctHeight);
    }
}
