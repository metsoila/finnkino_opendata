package com.example.project_opendata;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
RecyclerAdapter-class is for recyclerview. Movies are put to the recyclerview in this class.
 */
public class RecyclerAdapter
        extends RecyclerView.Adapter<com.example.project_opendata.RecyclerAdapter.ViewHolder>{

    private static final String TAG = "RecyclerAdapter";
    List<Movie_class> movies_list;

    public RecyclerAdapter(List<Movie_class> movies_list){
        this.movies_list = movies_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.time.setText(movies_list.get(position).start_time);
        holder.movie.setText(movies_list.get(position).Title);
        holder.theatre.setText(movies_list.get(position).theatre);
        if (movies_list.get(position).age_limit.length() > 3)
            holder.ageLimit.setText("18");
        else
            holder.ageLimit.setText(movies_list.get(position).age_limit);
    }

    @Override
    public int getItemCount() {
        return movies_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView movie, time, ageLimit, theatre;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            movie = itemView.findViewById(R.id.movie_title);
            time = itemView.findViewById(R.id.time);
            theatre = itemView.findViewById(R.id.theatre);
            ageLimit = itemView.findViewById(R.id.age_limit);
        }
    }



}
