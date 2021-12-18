package com.example.project_opendata;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie_class {
    String Title;
    String start_time;
    String theatre;
    String age_limit;

    final String EMPTY = "";
    final String date_format = "yyyy-MM-dd'T'HH:mm:ss";
    final String time_format = "HH:mm";

    public Movie_class(String title,
                       String startTime,
                       String Theatre,
                       String Age_limit) throws ParseException
    {
        Title = title;
        start_time = startTime;
        theatre = Theatre;
        age_limit = Age_limit;

        if (start_time.equals(EMPTY) )
        {
            System.out.println("Empty date");
        }
        else
            {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatInput = new SimpleDateFormat(date_format);
            Date parsed_date = formatInput.parse(start_time);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatOutput = new SimpleDateFormat(time_format);
            String parsed_time = formatOutput.format(parsed_date);
            start_time = parsed_time;
        }

    }


}