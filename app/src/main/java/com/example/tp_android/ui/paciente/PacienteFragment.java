package com.example.tp_android.ui.paciente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.Lista;
import com.example.tp_android.data.model.Persona;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacienteFragment extends Fragment {

    private com.example.tp_android.ui.paciente.PacienteViewModel pacienteViewModel;
    private Persona[] paciente;
    PacienteAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pacienteViewModel =
                ViewModelProviders.of(this).get(com.example.tp_android.ui.paciente.PacienteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_paciente, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        /*pacienteViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        ListView listView = root.findViewById(R.id.paciente);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Persona item = adapter.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), Paciente_ind.class);
                Gson gS = new Gson();
                String per = gS.toJson(item);
                Bundle b = new Bundle();
                b.putString("persona",per);

                intent.putExtras(b);

                startActivity(intent);
            }


        });


        Button button= root.findViewById(R.id.button_agregarPaciente);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getContext(), Paciente_New.class);
                startActivity(intent);
            }

        });

        Call<Lista<Persona>> callPersona= Servicios.getPersonaService().obtenerPersonas();
        callPersona.enqueue(new Callback<Lista<Persona>>() {
            @Override
            public void onResponse(Call<Lista<Persona>> call, Response<Lista<Persona>> response) {
                for (Persona c: response.body().getLista()) {
                    Log.d("w", "Persona de id "+ c.getIdPersona());
                }
                if(response.isSuccessful()) {
                    paciente = response.body().getLista();

                    int i = 0;
                    i++;

                    ListView listView = (ListView)getView().findViewById(R.id.paciente);
                    adapter= new PacienteAdapter(getContext(), paciente);
                    listView.setAdapter(adapter);
                    SearchView searchView = getView().findViewById(R.id.search_view);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            adapter.getFilter().filter(newText);

                            return false;
                        }
                    });
                } else {
                    Toast.makeText(getView().getContext(), response.message(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Lista<Persona>> call, Throwable t) {
                Log.w("warning",t.getCause().toString());
            }
        });


        return root;
    }

}
