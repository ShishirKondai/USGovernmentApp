package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.time.*;
import android.view.*;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private final MainActivity mainActivity;
    private final List<Article> list;

    public NewsAdapter(MainActivity mainActivity, List<Article> articleDetailsList) {
        this.mainActivity = mainActivity;
        this.list = articleDetailsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_display, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = list.get(position);

        String checkHeadline = article.getHeadline();
        if (checkHeadline != null){
            String headlineText = article.getHeadline();
            holder.headline.setText(headlineText);

            holder.headline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String thisURL = article.getUrl();
                    intent.setData(Uri.parse(thisURL));
                    mainActivity.startActivity(intent);
                }
            });
        }

        boolean flag = article.getAuthor().isEmpty();
        String nullString = "null";
        if(flag || article.getAuthor().equals(nullString)) {
            holder.authors.setVisibility(View.GONE);
        }
        else {
            String authorNames = article.getAuthor();
            holder.authors.setText(authorNames);
        }

        String checkArticle = article.getDate();
        if(checkArticle != null) {
            String date = getArticeDate(article.getDate());
            holder.dateTime.setText(date);
        }

        String checkURL = article.getUrl();
        if (checkURL == null) {
            holder.image.setImageResource(R.drawable.noimage);
        }
        else {
            Picasso picasso = Picasso.with(mainActivity);
            picasso.setLoggingEnabled(true);
            picasso.load(article.getImageURL())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.brokenimage)
                    .into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(article.getUrl()));
                    mainActivity.startActivity(intent);
                }
            });
        }

        boolean descriptionFlag = article.getAuthor().isEmpty();
        String articleDescription = article.getDescription();
        if(descriptionFlag || articleDescription.equals("null")){
            holder.description.setVisibility(View.GONE);
        }
        else {
            holder.description.setText(articleDescription);
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(article.getUrl()));
                    mainActivity.startActivity(intent);
                }
            });
        }
        int  startPage = position + 1;
        int lastPage = list.size();
        String pageNumbers = String.format("%d of %d", startPage, lastPage);
        holder.pageNumbers.setText(pageNumbers);
    }

    @Override
    public int getItemCount() {
        int len = list.size();
        return len;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getArticeDate(String date) {
        String APIDATE = "";
        try{
            DateTimeFormatter tf = DateTimeFormatter.ISO_INSTANT;
            TemporalAccessor accessor = tf.parse(date);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
            LocalDateTime ldt = LocalDateTime.ofInstant(Instant.from(accessor), ZoneId.systemDefault());
            APIDATE = ldt.format(dateTimeFormatter);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        boolean check = APIDATE.isEmpty();
        if(check){
            try{
                DateTimeFormatter tf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                TemporalAccessor accessor = tf.parse(date);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
                LocalDateTime ldt = LocalDateTime.ofInstant(Instant.from(accessor), ZoneId.systemDefault());
                APIDATE = ldt.format(dtf);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        String formattedDate = APIDATE;
        return formattedDate;
    }

}
