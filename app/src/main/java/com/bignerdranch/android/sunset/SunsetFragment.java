package com.bignerdranch.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Admin on 2017/2/26 0026.
 */

public class SunsetFragment extends Fragment {
    public enum SUNRISE_SET {
        SUNSET,
        SUNRISE,
    }

    private View mScreenView;
    private View mSunView;
    private View mSkyView;
    private View mSeaView;
    private View mSunReflectionView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private AnimatorSet sunsetAnimatorSet = new AnimatorSet();
    private AnimatorSet sunriseAnimatorSet = new AnimatorSet();

    private SUNRISE_SET mSunrise_set = SUNRISE_SET.SUNRISE;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mScreenView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);
        mSeaView = view.findViewById(R.id.sea);
        mSunReflectionView = view.findViewById(R.id.sunReflection);

        mBlueSkyColor = ContextCompat.getColor(getContext(), R.color.blue_sky);
        mSunsetSkyColor = ContextCompat.getColor(getContext(), R.color.sunset_sky);
        mNightSkyColor = ContextCompat.getColor(getContext(), R.color.night_sky);

        mScreenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopAnimation();
                mSunrise_set = mSunrise_set == SUNRISE_SET.SUNSET ? SUNRISE_SET.SUNRISE : SUNRISE_SET.SUNSET;
                startAnimation();
            }
        });

        return view;
    }

    private void startAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = (float) (mSeaView.getTop());
        float sunYStartReverse = mSunReflectionView.getBottom();
        float sunYEndReverse = (float) (-mSunView.getTop());

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator sunReflectionAnimator = ObjectAnimator
                .ofFloat(mSunReflectionView, "y", sunYStartReverse, sunYEndReverse)
                .setDuration(3000);
        sunReflectionAnimator.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        sunsetAnimatorSet.play(heightAnimator).with(sunsetSkyAnimator).with(sunReflectionAnimator)
                .before(nightSkyAnimator);

        // corresponding reverse
        ObjectAnimator reverseHeightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYEnd,
                sunYStart).setDuration(3000);
        reverseHeightAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator reverseSunAnimator = ObjectAnimator
                .ofFloat(mSunReflectionView, "y", sunYEndReverse, sunYStartReverse)
                .setDuration(3000);
        reverseSunAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator reverseSunsetAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        reverseSunsetAnimator.setEvaluator(new ArgbEvaluator());
        ObjectAnimator reverseNightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                .setDuration(1500);
        reverseNightSkyAnimator.setEvaluator(new ArgbEvaluator());

        sunriseAnimatorSet.play(reverseHeightAnimator).with(reverseSunsetAnimator).with(reverseSunAnimator)
        .after(reverseNightSkyAnimator);

        startAnimation(sunsetAnimatorSet, mSunrise_set, sunriseAnimatorSet);
    }

    private void startAnimation(AnimatorSet sunsetAnimatorSet, SUNRISE_SET mSunrise_set,
                                AnimatorSet sunriseAnimatorSet) {
        if (mSunrise_set == SUNRISE_SET.SUNRISE) {
            sunriseAnimatorSet.start();
        } else {
            sunsetAnimatorSet.start();
        }
    }
}
