package com.example.julia.newsapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    private TextView sectionTextView;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private ImageView thumbnailView;

    public ArticleViewHolder(final View itemView) {
        super(itemView);
        sectionTextView = itemView.findViewById(R.id.section_name);
        titleTextView = itemView.findViewById(R.id.article_title);
        authorTextView = itemView.findViewById(R.id.author);
        dateTextView = itemView.findViewById(R.id.publication_date);
        thumbnailView = itemView.findViewById(R.id.image);
    }

    public void bindData(final Article viewModel){
        sectionTextView.setText(viewModel.getSection());
        titleTextView.setText(viewModel.getTitle());
        authorTextView.setText(viewModel.getAuthor());
        dateTextView.setText(formatDate(viewModel.getPublishedDate()));

        if (!viewModel.getImageUrl().equals("")) {
            Picasso.get().load(viewModel.getImageUrl()).into(thumbnailView);
        }
    }

    private String formatDate(String initialDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'", Locale.UK);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss",Locale.UK);
        newDateFormat.setTimeZone(TimeZone.getDefault());

        String formattedDate = initialDate;

        try {
            Date dateObject = dateFormat.parse(initialDate);
            formattedDate = newDateFormat.format(dateObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
