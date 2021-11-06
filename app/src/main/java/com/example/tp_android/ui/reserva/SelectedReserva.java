package com.example.tp_android.ui.reserva;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tp_android.MainActivity;
import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.Reserva;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedReserva extends AppCompatActivity {

    Reserva reserva;
    TextView idView ;
    TextView empleadoView;
    TextView nombreView;
    TextView fechaView;
    EditText obs;
    EditText asistencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_reserva);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            Gson gS = new Gson();
            reserva = gS.fromJson(bundle.getString("reserva"), Reserva.class);

            idView = findViewById(R.id.reservaId);
            empleadoView = findViewById(R.id.empleadoApellido);
            nombreView = findViewById(R.id.clienteNombreCompleto);
            fechaView = findViewById(R.id.fechaReserva);
            obs = findViewById(R.id.obs);
            asistencia = findViewById(R.id.asistencia);

            idView.setText(reserva.getIdReserva().toString());
            empleadoView.setText(reserva.getIdEmpleado().getApellido().toUpperCase());
            nombreView.setText(reserva.getIdCliente().getNombreCompleto());
            obs.setText(reserva.getObservacion());
            asistencia.setText(reserva.getFlagAsistido());
            SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
            try {
                Date date = parseador.parse(reserva.getFecha());
                fechaView.setText(formateador.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button button= findViewById(R.id.guardar);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                update(view);
                Intent intent = new Intent(SelectedReserva.this, MainActivity.class);
                startActivity(intent);
            }

        });


    }

    public void update(View view) {
        Reserva reserva = new Reserva();

        idView = findViewById(R.id.reservaId);
        asistencia = findViewById(R.id.asistencia);
        obs = findViewById(R.id.obs);

        reserva.setIdReserva(Integer.valueOf(idView.getText().toString()));
        reserva.setObservacion(obs.getText().toString());
        reserva.setFlagAsistido(asistencia.getText().toString());

        Call<Reserva> callReserva = null;


        callReserva = Servicios.getReservaService().actualizarReserva(reserva);


        callReserva.enqueue(new Callback<Reserva>() {
            @Override
            public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                if(response.code()==500){
                    Toast.makeText(SelectedReserva.this,"No se puede  Modificar ERROR= " + response.code(),Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SelectedReserva.this, "Se inserto correctamente " + response.code(), Toast.LENGTH_LONG).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<Reserva> call, Throwable t) {
                Log.w("warning", t.getCause().toString());
            }
        });
    }
}
