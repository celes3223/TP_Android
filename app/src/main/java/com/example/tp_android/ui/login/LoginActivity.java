package com.example.tp_android.ui.login;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.tp_android.MainActivity;
import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.Lista;
import com.example.tp_android.data.model.Persona;
import com.example.tp_android.data.model.Reserva;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static int theme;

    Persona[] array;
    Reserva[] array2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(
                getString(R.string.settings), Context.MODE_PRIVATE);
        int defaultTheme = getResources().getInteger(R.integer.default_theme);
        theme = settings.getInt(getString(R.string.settings_theme), defaultTheme);
        switch (theme){
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case -1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        ((EditText)findViewById(R.id.username)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            loadingProgressBar.setVisibility(View.VISIBLE);
                            login(usernameEditText.getText().toString());
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                login(usernameEditText.getText().toString());
            }
        });

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void changeTheme(View view) {
        if(theme == 0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            theme = 1;
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.settings_theme), 1);
            editor.apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            theme = 0;
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.settings_theme), 0);
            editor.apply();
        }
        Activity activity = this;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    private void login(final String username){
        try {
            Call<Lista<Persona>> callPersona = Servicios.getPersonaService().obtenerPersonas();

            callPersona.enqueue(new Callback<Lista<Persona>>() {
                @Override
                public void onResponse(Call<Lista<Persona>> call, Response<Lista<Persona>> response) {

                    if (response.isSuccessful()) {
                        array = response.body().getLista();

                        for (Persona persona : array) {
                            if (persona.getUsuarioLogin() != null && persona.getUsuarioLogin().equals(username)) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra(EXTRA_MESSAGE, username);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Bienvenido: " + username + "!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        ProgressBar loadingProgressBar = findViewById(R.id.loading);
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Intento fallido, usuario no encontrado", Toast.LENGTH_SHORT).show();

                    } else {
                        ProgressBar loadingProgressBar = findViewById(R.id.loading);
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call<Lista<Persona>> call, Throwable t) {

                    ProgressBar loadingProgressBar = findViewById(R.id.loading);
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
                    Log.w("warning", t.getCause().toString());
                }
            });

        } catch (Exception ex){
            ProgressBar loadingProgressBar = findViewById(R.id.loading);
            loadingProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
        }
    }

    private void reserva(final String username){
        Call<Lista<Reserva>> callReserva= Servicios.getReservaService().obtenerReservas();

        callReserva.enqueue(new Callback<Lista<Reserva>>() {
            @Override
            public void onResponse(Call<Lista<Reserva>> call, Response<Lista<Reserva>> response) {
                for (Reserva c: response.body().getLista()) {
                    Log.d("w", "Reserva de id "+ c.getIdReserva() +
                            " y fecha "+ c.getFecha() + " el nombre " + c.getIdCliente().getNombre());
                }
                if(response.isSuccessful()) {
                    array2 = response.body().getLista();


                } else {
                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Lista<Reserva>> call, Throwable t) {
                Log.w("warning",t.getCause().toString());
            }
        });
        int i = 1;
        i++;
    }

}
