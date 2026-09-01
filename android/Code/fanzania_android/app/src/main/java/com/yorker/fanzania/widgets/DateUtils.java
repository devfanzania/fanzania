package com.yorker.fanzania.widgets;

import android.os.CountDownTimer;
import com.yorker.fanzania.customviews.customfonts.montserrat.MontserratRegular;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static SimpleDateFormat normalDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
    private static SimpleDateFormat outputDate = new SimpleDateFormat("dd-MMM",Locale.getDefault());
    private static SimpleDateFormat input = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());
    private static long startTime;

    public static void printDifference(Date startDate, Date endDate, MontserratRegular textView) {
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

    private static void startCountdownTimer(Date startDate,Date endDate, MontserratRegular textView) {
        normalDateFormat.setLenient(false);

        long milliseconds;

        milliseconds = endDate.getTime();

        startTime = startDate.getTime();

        new CountDownTimer(milliseconds, 1000) {
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

    public static String getDate(String strDate,MontserratRegular tv) {
        String otpDate = null;
        try {
            Date date = DateUtils.input.parse(strDate);  // parse input

            otpDate = DateUtils.output.format(date);
            System.out.println("dob " + otpDate);
            tv.setText(otpDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return otpDate;
    }

    public static void getDateFromISO(String strDate,MontserratRegular tv) {
        try {
            Date date = DateUtils.normalDateFormat.parse(strDate);  // parse input

            String otpDate = DateUtils.outputDate.format(date);
            System.out.println("dob " + otpDate);
            tv.setText(otpDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void getFullDateFromISO(String strDate,MontserratRegular tv) {
        String otpDate = null;
        try {
            Date date = DateUtils.normalDateFormat.parse(strDate);  // parse input

            otpDate = DateUtils.output.format(date);
            System.out.println("dob " + otpDate);
            tv.setText(otpDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
