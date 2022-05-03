package com.example.newapplication;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.*;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView headline;
    TextView dateTime;
    TextView authors;
    ImageView image;
    TextView description;
    TextView pageNumbers;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        headline = itemView.findViewById(R.id.headline);
        dateTime = itemView.findViewById(R.id.date_time);
        authors = itemView.findViewById(R.id.author);
        image = itemView.findViewById(R.id.news_image);
        pageNumbers = itemView.findViewById(R.id.page_numbers);
        description = itemView.findViewById(R.id.description);

        this.description.setMovementMethod(new ScrollingMovementMethod());
    }
}
