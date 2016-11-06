/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baoyz.bigbang.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by baoyongzhang on 2016/11/1.
 */
public class FloatingView extends ImageView {

    private static final int ANIMATION_DURATION = 500;

    private final WindowManager mWindowManager;
    private final int mMargin;
    private Runnable mDismissTask = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };
    private boolean isShow;
    private String mText;

    public FloatingView(Context context) {
        super(context);

        setImageResource(R.mipmap.floating_button);
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("bigBang://?extra_text=" + URLEncoder.encode(mText, "utf-8")));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
    }

    public void show() {
        if (!isShow) {
            int w = WindowManager.LayoutParams.WRAP_CONTENT;
            int h = WindowManager.LayoutParams.WRAP_CONTENT;
            int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            int type;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                type = WindowManager.LayoutParams.TYPE_PHONE;
            }

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
            layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            layoutParams.x = mMargin;
            layoutParams.y = mMargin;

            mWindowManager.addView(this, layoutParams);

            isShow = true;

            setScaleX(0);
            setScaleY(0);
            animate().cancel();
            animate().scaleY(1).scaleX(1).setDuration(ANIMATION_DURATION).setListener(null).start();
        }

        removeCallbacks(mDismissTask);
        postDelayed(mDismissTask, 3000);
    }

    public void dismiss() {
        if (isShow) {
            animate().cancel();
            animate().scaleX(0).scaleY(0).setDuration(ANIMATION_DURATION).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mWindowManager.removeView(FloatingView.this);
                    isShow = false;
                }
            }).start();
        }
        removeCallbacks(mDismissTask);
    }

    public void setText(String text) {
        mText = text;
    }
}
