package com.example.tp_android.ui.fichaClinica;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.FichaClinica;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FichaClinicaNew extends AppCompatActivity {

    EditText motivo;
    EditText diagnostico;
    EditText observacion;
    EditText paciente;
    EditText fisioterapeuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_new);

        motivo=findViewById(R.id.motivo_consulta);
        diagnostico=findViewById(R.id.diagnostico);
        observacion=findViewById(R.id.observacion);
        paciente=findViewById(R.id.paciente);
        fisioterapeuta=findViewById(R.id.fisioterapeuta);

    }

    public void guardar(View view) {
        FichaClinica c=new FichaClinica();
        c.setMotivoConsulta(motivo.getText().toString());
        c.setDiagnostico(diagnostico.getText().toString());
        c.setObservacion(observacion.getText().toString());
        //c.setIdCliente(paciente.getText().toString());
        //c.setIdEmpleado(fisioterapeuta.getText().toString());

        Call<FichaClinica> callPersona = null;

        callPersona = Servicios.getFichaClinicaService().agregarFichaClinica(c);


        callPersona.enqueue(new Callback<FichaClinica>() {
            @Override
            public void onResponse(Call<FichaClinica> call, Response<FichaClinica> response) {
                Toast.makeText(FichaClinicaNew.this,"Paciente agregado exitosamente",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<FichaClinica> call, Throwable t) {
                Log.w("warning", t.getCause().toString());
            }
        });
    }

}
