package com.example.tp_android.ui.fisio;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tp_android.data.model.Persona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class SelectedFisio extends AppCompatActivity {

    Persona persona;
    TextView idView ;
    TextView nombreCompleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_agenda);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            Gson gS = new Gson();
            persona = gS.fromJson(bundle.getString("persona"), Persona.class);

            idView = findViewById(R.id.personaId);
            nombreCompleto = findViewById(R.id.nombreCompleto);

            idView.setText(persona.getIdPersona().toString());
            nombreCompleto.setText(persona.getNombreCompleto());

        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

}
