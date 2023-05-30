package com.dawalisoftwares.easycheck;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AboutApp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        LinearLayout changeLog, privacy, email, author, comp, location;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        changeLog = findViewById(R.id.changelog);
        changeLog.setOnClickListener(view -> new MaterialAlertDialogBuilder(AboutApp.this).setMessage(getString(R.string.easy_check_1_0_nno_changes))
                .setTitle("ChangeLog")
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.cancel()).create().show());

        privacy = findViewById(R.id.privacy_policy);
        privacy.setOnClickListener(view -> {
            Uri webpage = Uri.parse("https://www.example.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        email = findViewById(R.id.author_mail);
        author = findViewById(R.id.author);
        author.setOnClickListener(view -> {
            openFaceBook();
        });
        email.setOnClickListener(view -> {
            openFaceBook();
        });

        comp = findViewById(R.id.company);
        location = findViewById(R.id.location);
        comp.setOnClickListener(view -> {
            openFaceBook();
        });
        location.setOnClickListener(view -> {
            openFaceBook();
        });
    }

    private void openFaceBook() {
        Uri facebookUri = Uri.parse("https://www.facebook.com/yourpage");

        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, facebookUri);

        startActivity(facebookIntent);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }
}