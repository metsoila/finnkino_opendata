package com.example.project_opendata;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time_class
{
    private static final Time_class time_instance = new Time_class();

    Date start_before, start_after, starting_time;


    public static Time_class getInstance(){

        return time_instance;
    }


    //Return bool-value depending on whether movies' start-time follows given settings
    public boolean check_start_time(String new_date) throws ParseException
    {
        SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        System.out.println(new_date);
        Date date = formatInput.parse(new_date);
        SimpleDateFormat formatOutput = new SimpleDateFormat("HH:mm");
        String new_time = formatOutput.format(date);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            starting_time = sdf.parse(new_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long start_time = starting_time.getTime();

        if(start_after == null){

            long before = getBefore().getTime();
            return start_time <= before;

        } else if (start_before == null){

            long after = getAfter().getTime();
            return start_time >= after;
        } else {

            long after = getAfter().getTime();
            long before = getBefore().getTime();
            return start_time >= after && start_time <= before;
        }

    }



    public void set_before(String time){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            start_before = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getBefore(){
        return start_before;
    }



    public void set_after(String time){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            start_after = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public Date getAfter(){
        return start_after;
    }
}
