package com.example.tp_android.ui.paciente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp_android.MainActivity;
import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.Persona;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Paciente_New extends AppCompatActivity {

    EditText txtNombre;
    EditText txtApellido;
    EditText txtEmail;
    EditText txtTelefono;
    EditText txtRuc;
    EditText txtCedula;
    EditText txtTipoPersona;
    EditText txtFechaNacimiento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente__new);

        txtNombre=findViewById(R.id.nombre);
        txtApellido=findViewById(R.id.apellido);
        txtEmail=findViewById(R.id.email);
        txtTelefono=findViewById(R.id.telefono);
        txtRuc=findViewById(R.id.ruc);
        txtCedula=findViewById(R.id.cedula);
        txtTipoPersona=findViewById(R.id.tipopersona);
        txtFechaNacimiento=findViewById(R.id.fechaNacimiento);

        Button button= findViewById(R.id.button_newPaciente);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                guardar(view);
                Intent intent = new Intent(Paciente_New.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }



   /* //Método para volver a la lista de pacientes
    public void NewPaciente(View view)
    {
        Intent Pac= new Intent(this,PacienteFragment.class);
        startActivity(Pac);
    }*/

    public void guardar(View view) {
        Persona c=new Persona();
        c.setNombre(txtNombre.getText().toString());
        c.setApellido(txtApellido.getText().toString());
        c.setEmail(txtEmail.getText().toString());
        c.setTelefono(txtTelefono.getText().toString());
        c.setRuc(txtRuc.getText().toString());
        c.setCedula(txtCedula.getText().toString());
        c.setTipoPersona(txtTipoPersona.getText().toString());
        c.setFechaNacimiento(txtFechaNacimiento.getText().toString()+" 00:00:00");

        Call<Persona> callPersona = null;

        callPersona = Servicios.getPersonaService().agregarPersona(c);

        callPersona.enqueue(new Callback<Persona>() {
            @Override
            public void onResponse(Call<Persona> call, Response<Persona> response) {
                Toast.makeText(Paciente_New.this,"Paciente agregado exitosamente",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Persona> call, Throwable t) {
                Log.w("warning", t.getCause().toString());
            }
        });
    }


}
