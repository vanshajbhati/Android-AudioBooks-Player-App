package com.google.android.exoplayer.finalmusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class chaptersActivity extends AppCompatActivity {


   private int position;  //TODO used to get file right chapter track
    private String imageUrl;
    private String bookTitle;
    private String bookAuthor;

    ImageView bookImageview;
    TextView bookTitleTextview, bookAuthorTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        bookImageview =findViewById(R.id.chapter_imageView);
        bookTitleTextview =findViewById(R.id.chapter_book_title);
        bookAuthorTextview =findViewById(R.id.chapter_book_author);

        Intent intentHome = getIntent();
        position = intentHome.getIntExtra("position", 0);
        imageUrl = intentHome.getStringExtra("imageUrl");
        bookTitle = intentHome.getStringExtra("title");
        bookAuthor= intentHome.getStringExtra("author");


        bookTitleTextview.setText(bookTitle);
        bookAuthorTextview.setText(bookAuthor);
        Glide.with(this).load(imageUrl).into(bookImageview);


       



    }
}