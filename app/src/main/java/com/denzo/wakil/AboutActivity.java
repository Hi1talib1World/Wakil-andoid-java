package com.denzo.wakil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.denzo.wakil.Models.Member;
import com.denzo.wakil.adapters.AboutRecyclerviewAdapter;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {
    public static String jsonUrl;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager manager;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imageViewOfficeLogo;
    ImageView imageViewGooglePlay, imageViewFacebook, imageViewGroup, imageViewYoutube, imageViewGithub, imageViewWeb;
    LinearLayout linearLayoutParent;

    ArrayList<Member> members = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        swipeRefreshLayout = findViewById(R.id.srl);
        linearLayoutParent = findViewById(R.id.parentLL);
        imageViewOfficeLogo = findViewById(R.id.officeLogoImage);

        imageViewGooglePlay = findViewById(R.id.googlePlayLogo);
        imageViewFacebook = findViewById(R.id.facebookLogo);
        imageViewGroup = findViewById(R.id.groupLogo);
        imageViewYoutube = findViewById(R.id.youtubeLogo);
        imageViewGithub = findViewById(R.id.githubLogo);
        imageViewWeb = findViewById(R.id.webLogo);

        recyclerView = findViewById(R.id.officeAboutRecyclerView);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new AboutRecyclerviewAdapter(AboutActivity.this, members);
        recyclerView.setAdapter(adapter);
    }
}
