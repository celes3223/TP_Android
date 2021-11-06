package com.example.tp_android.ui.paciente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp_android.MainActivity;
import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.Persona;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Paciente_ind extends AppCompatActivity {

    Persona per;
    TextView idPersona;
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
        setContentView(R.layout.activity_paciente_ind);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            Gson gS = new Gson();
            per = gS.fromJson(bundle.getString("persona"), Persona.class);
            idPersona = findViewById(R.id.text_id2);
            txtNombre = findViewById(R.id.text_nombre2);
            txtApellido = findViewById(R.id.text_apellido2);
            txtEmail = findViewById(R.id.text_email2);
            txtRuc= findViewById(R.id.text_ruc2);
            txtCedula = findViewById(R.id.text_cedula2);
            txtTipoPersona= findViewById(R.id.text_tipo2);
            txtFechaNacimiento = findViewById(R.id.text_fecha2);
            txtTelefono = findViewById(R.id.text_telefono2);

            idPersona.setText(per.getIdPersona().toString());
            txtNombre.setText(per.getNombre());
            txtApellido.setText(per.getApellido());
            txtEmail.setText(per.getEmail());
            txtRuc.setText(per.getRuc());
            txtCedula.setText(per.getCedula());
            txtTipoPersona.setText(per.getTipoPersona());
            txtFechaNacimiento.setText(per.getFechaNacimiento());
            txtTelefono.setText(per.getTelefono());

        }

        Button button= findViewById(R.id.button_mod);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                modificar(view);
                Intent intent = new Intent(Paciente_ind.this, MainActivity.class);
                startActivity(intent);
            }

        });

        Button button2= findViewById(R.id.button_eliminar);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Eliminar(view);
                Intent intent = new Intent(Paciente_ind.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }

    public void modificar(View view) {
        Persona c=new Persona();
        idPersona= findViewById(R.id.text_id2);
        txtNombre = findViewById(R.id.text_nombre2);
        txtApellido= findViewById(R.id.text_apellido2);
        txtEmail= findViewById(R.id.text_email2);
        txtTelefono= findViewById(R.id.text_telefono2);
        txtRuc= findViewById(R.id.text_ruc2);
        txtCedula= findViewById(R.id.text_cedula2);
        txtTipoPersona= findViewById(R.id.text_tipo2);
        txtFechaNacimiento= findViewById(R.id.text_fecha2);


        c.setIdPersona(Integer.valueOf(idPersona.getText().toString()));
        c.setNombre(txtNombre.getText().toString());
        c.setApellido(txtApellido.getText().toString());
        c.setEmail(txtEmail.getText().toString());
        c.setTelefono(txtTelefono.getText().toString());
        c.setRuc(txtRuc.getText().toString());
        c.setCedula(txtCedula.getText().toString());
        c.setTipoPersona(txtTipoPersona.getText().toString());
        c.setFechaNacimiento(txtFechaNacimiento.getText().toString()+" 00:00:00");

        Call<Persona> callPersona = null;

        callPersona= Servicios.getPersonaService().actualizarCategoria(c);

        callPersona.enqueue(new Callback<Persona>() {
            @Override
            public void onResponse(Call<Persona> call, Response<Persona> response) {
                Toast.makeText(Paciente_ind.this,"Paciente modificado exitosamente",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Persona> call, Throwable t) {
                Log.w("warning", t.getLocalizedMessage());
            }
        });
    }

    public void Eliminar(View view) {

        idPersona= findViewById(R.id.text_id2);

        Integer id = Integer.valueOf(idPersona.getText().toString());

        Call<Persona> callPersona = null;

        callPersona= Servicios.getPersonaService().borrarPersona(id);

        callPersona.enqueue(new Callback<Persona>() {
            @Override
            public void onResponse(Call<Persona> call, Response<Persona> response) {
                Toast.makeText(Paciente_ind.this,"Paciente eliminado exitosamente",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Persona> call, Throwable t) {
                Log.w("warning", t.getLocalizedMessage());
            }
        });
    }
}
