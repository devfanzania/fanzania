package com.yorker.fanzania.views.shared.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;
import com.yorker.fanzania.dependencyinjection.ApplicationModule;
import com.yorker.fanzania.dependencyinjection.DaggerPresenterComponent;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.dialog.NoNetworkDialog;
import com.yorker.fanzania.helper.network.ConnectionDetector;
import com.yorker.fanzania.presenter.Presenter;

import java.util.Date;

import static com.yorker.fanzania.widgets.DateUtils.normalDateFormat;
import static com.yorker.fanzania.widgets.DateUtils.output;

public abstract class BaseFragment<T extends Presenter> extends Fragment implements NoNetworkDialog.INetworkDialog {
    private T presenter;

    protected abstract T onCreatePresenter();
    private CountDownTimer countDownTimer;
    private long startTime;
    protected abstract void injectPresenter(PresenterComponent component, T presenter);

    private ConnectionDetector cd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(getActivity());
        Inject();
    }

    private void Inject() {
        presenter = onCreatePresenter();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .applicationModule(new ApplicationModule(getActivity()))
                .build();
        injectPresenter(presenterComponent, presenter);
    }

    public Boolean CheckInternetConnection() {
        if (cd.isConnectingToInternet()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.OnCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.OnResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.OnPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.OnDestroy();
    }

    public void printDifference(Date startDate, Date endDate, MontserratRegular textView) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;

        long elapsedWeeks;
        long elapsedDays = -1;
        long elapsedHours = -1;
        long elapsedMinutes = -1;
        long elapsedSeconds = -1;

        elapsedWeeks = different / weeksInMilli;
        different = different % weeksInMilli;

        if (elapsedWeeks == 0) {
            elapsedDays = different / daysInMilli;
            different = different % daysInMilli;
        }

        if (elapsedDays == 0) {
            elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            elapsedSeconds = different / secondsInMilli;
        }

        System.out.printf("%d weeks, %d days, %d hours, %d minutes, %d seconds%n",
                elapsedWeeks, elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        if (elapsedWeeks == 1)
            textView.setText(new StringBuffer()
                    .append(elapsedWeeks)
                    .append(" ")
                    .append("Week left"));
        else if (elapsedDays ==1)
            textView.setText(new StringBuffer()
                    .append(elapsedDays)
                    .append(" ")
                    .append("day left"));
        else if (elapsedDays > 1 && elapsedDays < 8)
            textView.setText(new StringBuffer()
                    .append(elapsedDays)
                    .append(" ")
                    .append("days left"));
        else if (elapsedHours > 0)
            startCountdownTimer(startDate,endDate,textView);
        else
            textView.setText(output.format(endDate));
    }

    private void startCountdownTimer(Date startDate,Date endDate, MontserratRegular textView) {
        normalDateFormat.setLenient(false);

        long milliseconds;

        milliseconds = endDate.getTime();

        startTime = startDate.getTime();

        countDownTimer =  new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                startTime = startTime - 1;
                long serverUptimeSeconds =0;

                if (millisUntilFinished>startTime)
                    serverUptimeSeconds =(millisUntilFinished-startTime) / 1000;
                else
                    serverUptimeSeconds =(startTime-millisUntilFinished) / 1000;

                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);

                textView.setText(new StringBuilder().append(hoursLeft)
                        .append("h:")
                        .append(minutesLeft)
                        .append("m:")
                        .append(secondsLeft)
                        .append("s"));
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    public void cancelTimer(){
        if (countDownTimer!=null)
            countDownTimer.cancel();
    }
}
