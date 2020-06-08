package com.utec.vmap.ui.home;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.utec.vmap.R;
import com.utec.vmap.Util;

public class Informacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        String title = Util.getTitle();
        String texto = "<p><h1>" + title + "</h1><p><br>" + Util.getText();
        TextView text = findViewById(R.id.info);
        text.setText(Html.fromHtml(texto));
        setTitle(title);
    }
}