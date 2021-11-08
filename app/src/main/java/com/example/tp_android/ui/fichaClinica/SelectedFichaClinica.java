package com.example.tp_android.ui.fichaClinica;

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

import com.example.tp_android.FileUpload;
import com.example.tp_android.MainActivity;
import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.FichaClinica;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedFichaClinica extends AppCompatActivity {

    FichaClinica ficha;
    TextView textView ;
    TextView empleadoView;
    TextView nombreView;
    EditText obs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_ficha_clinica);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            Gson gS = new Gson();
            ficha = gS.fromJson(bundle.getString("ficha"), FichaClinica.class);

            textView = findViewById(R.id.fichaId);
            empleadoView = findViewById(R.id.empleadoApellido);
            nombreView = findViewById(R.id.clienteNombreCompleto);
            obs = findViewById(R.id.obs);
            TextView mtvoConsultaView = findViewById(R.id.motivo_consultaView);
            mtvoConsultaView.setText(ficha.getMotivoConsulta());
            textView.setText(ficha.getIdFichaClinica().toString());
            empleadoView.setText(ficha.getIdEmpleado().getApellido().toUpperCase());
            nombreView.setText(ficha.getIdCliente().getNombreCompleto());
            obs.setText(ficha.getObservacion());
        }

        Button button= findViewById(R.id.button10);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("fichaClinicaId", ficha.getIdFichaClinica());

                Intent intent = new Intent(SelectedFichaClinica.this, FileUpload.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

        });

        Button button3= findViewById(R.id.button11);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Eliminar(view);
                Intent intent = new Intent(SelectedFichaClinica.this, MainActivity.class);
                startActivity(intent);
            }

        });


        Button button2= findViewById(R.id.button9);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                update(view);
                Intent intent = new Intent(SelectedFichaClinica.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }
    public void update(View view) {
        FichaClinica ficha = new FichaClinica();

        textView = findViewById(R.id.fichaId);
        obs = findViewById(R.id.obs);

        ficha.setIdFichaClinica(Integer.valueOf(textView.getText().toString()));
        ficha.setObservacion(obs.getText().toString());

        Call<FichaClinica> callReserva = null;


        callReserva = Servicios.getFichaClinicaService().actualizarFichaClinica(ficha);


        callReserva.enqueue(new Callback<FichaClinica>() {
            @Override
            public void onResponse(Call<FichaClinica> call, Response<FichaClinica> response) {
                if(response.code()==500){
                    Toast.makeText(SelectedFichaClinica.this,"No se puede  Modificar ERROR= " + response.code(),Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SelectedFichaClinica.this, "Se inserto correctamente " + response.code(), Toast.LENGTH_LONG).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<FichaClinica> call, Throwable t) {
                Log.w("warning", t.getCause().toString());
            }
        });
    }

    public void Eliminar(View view) {

        textView= findViewById(R.id.fichaId);

        Integer id = Integer.valueOf(textView.getText().toString());

        Call<FichaClinica> callPersona = null;

        callPersona= Servicios.getFichaClinicaService().borrarFichaClinica(id);


        callPersona.enqueue(new Callback<FichaClinica>() {
            @Override
            public void onResponse(Call<FichaClinica> call, Response<FichaClinica> response) {
                Toast.makeText(SelectedFichaClinica.this,"Ficha clinica eliminada exitosamente",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<FichaClinica> call, Throwable t) {
                Log.w("warning", t.getLocalizedMessage());
            }
        });
    }

}
