package com.example.project_opendata;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView chosen_date;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<Movie_class> movies_list;
    Context context = null;
    String picked_theatre, picked_id, date, dateStr = "", start_afterStr, start_beforeStr, urlString;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> theatres_spinner = new ArrayList<>();
    ArrayList<Theatre_class> theatres_list = new ArrayList<>();
    TextView start_after, start_before;
    int start_afterHour, start_afterMin, start_beforeHour, start_beforeMin;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_after = findViewById(R.id.start_after);
        start_after.setOnClickListener(v ->
        {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                    (view, hourOfDay, minute) ->
                    {
                        start_afterHour = hourOfDay;
                        start_afterMin = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, start_afterHour, start_afterMin);
                        start_after.setText("Aloita jälkeen " +
                                DateFormat.format("HH:mm", calendar));
                        start_afterStr = (String) DateFormat.format("HH:mm", calendar);
                        Time_class time = Time_class.getInstance();
                        time.set_after(start_afterStr);
                        }, 12, 0, false);
            timePickerDialog.updateTime(start_afterHour, start_afterMin);
            timePickerDialog.show();
        });
        start_before = findViewById(R.id.start_before);
        start_before.setOnClickListener(v ->
        {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                    (view, hourOfDay, minute) ->
                    {
                        start_beforeHour = hourOfDay;
                        start_beforeMin = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, start_beforeHour, start_beforeMin);
                        start_before.setText("Aloita ennen " +
                                DateFormat.format("HH:mm", calendar));
                        start_beforeStr = (String) DateFormat.format("HH:mm", calendar);
                        Time_class time = Time_class.getInstance();
                        time.set_before(start_beforeStr);
                    }, 12, 0, true);
            timePickerDialog.updateTime(start_beforeHour, start_beforeMin);
            timePickerDialog.show();
        });

        chosen_date = findViewById(R.id.showing_date);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        chosen_date.setText(dtf.format(now));
        context = MainActivity.this;
        Spinner spinner = findViewById(R.id.choose_theatre);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //Movies
        movies_list = new ArrayList<>();
        recyclerView = findViewById(R.id.search_results);
        recyclerAdapter = new RecyclerAdapter(movies_list);//movies-list to recyclerAdapter
        recyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        //Theatres
        theatres_spinner.add("");

        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_single_choice,
                theatres_spinner);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                picked_theatre = parent.getItemAtPosition(position).toString();
                picked_id = getID(picked_theatre);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Theatres are received from url and put to a spinner
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc1 = builder.parse(urlString);
            doc1.getDocumentElement().normalize();
            NodeList nList = doc1.getDocumentElement().getElementsByTagName("TheatreArea");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    theatres_spinner.add(i, String.valueOf(
                            element.getElementsByTagName("Name").
                                            item(0).getTextContent()));

                    theatres_list.add(i,
                            new Theatre_class(
                                    String.valueOf(element.getElementsByTagName(
                                            "Name").item(0).getTextContent()),
                                    String.valueOf(element.getElementsByTagName(
                                            "ID").item(0).getTextContent())
                            ));
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        findViewById(R.id.choose_date).setOnClickListener(v -> showDatePickerDialog());


    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        date = ("Näytetään elokuvia päivältä " + day + "." + (month + 1) + "." + year);
        if (month > 8) {
            dateStr = day + "." + (month + 1) + "." + year;
        } else {
            dateStr = day + ".0" + (month + 1) + "." + year;
        }
    }

    //List movies
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ListMovies(View v) throws ParseException {

        movies_list.clear();

        //If theatre not set, app wont list movies
        if (picked_theatre.equals("Valitse alue/teatteri"))
        {
            Toast.makeText(getApplicationContext(),
                    "Valitse ensin teatteri",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            //Retrieve movies to the list
            get_movies();

            //not any movies found
            if (movies_list.size() == 0)
            {
                Toast.makeText(getApplicationContext(),
                        "Valitsemillanne asetuksilla ei löytynyt yhtään näytöstä",
                        Toast.LENGTH_SHORT).show();
            }
            recyclerAdapter = new RecyclerAdapter(movies_list);
            recyclerView.setAdapter(recyclerAdapter);
            chosen_date.setText(date);
        }
    }

    public String getID(String teatteri)
    {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String url = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;

                    if (teatteri.equals(String.valueOf(element.getElementsByTagName("Name").
                            item(0).getTextContent())))
                    {
                        return String.valueOf(element.getElementsByTagName("ID").item(0).
                                getTextContent());
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void get_movies() throws ParseException {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            urlString = ("https://www.finnkino.fi/xml/Schedule/?area=" +
                    picked_id + "&dt=" + dateStr);
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            Time_class time = Time_class.getInstance();
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;
                    String start_time = String.valueOf(
                            element.getElementsByTagName("dttmShowStart").
                                    item(0).getTextContent());

                    if (start_afterStr == null && start_beforeStr == null)
                    {
                        movieToList(element);

                    }
                    else if (time.check_start_time(start_time))
                    {
                        movieToList(element);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    //Create new Movie_class-object with information from element received from url
    public void movieToList(Element element) throws ParseException {
        movies_list.add(new Movie_class(
                String.valueOf(element.getElementsByTagName("Title").
                        item(0).getTextContent()),
                String.valueOf(element.getElementsByTagName("dttmShowStart").
                        item(0).getTextContent()),
                String.valueOf(element.getElementsByTagName("Theatre").
                        item(0).getTextContent()),
                String.valueOf(element.getElementsByTagName("Rating").
                        item(0).getTextContent())
        ));
    }
}











