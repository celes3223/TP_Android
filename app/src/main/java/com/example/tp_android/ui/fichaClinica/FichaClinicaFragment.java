package com.example.tp_android.ui.fichaClinica;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.FichaClinica;
import com.example.tp_android.data.model.Lista;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FichaClinicaFragment extends Fragment {

    private com.example.tp_android.ui.fichaClinica.FichaClinicaViewModel fichaClinicaViewModel;

    private FichaClinica[] fichas;
    FichaClinicaAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fichaClinicaViewModel =
                ViewModelProviders.of(this).get(com.example.tp_android.ui.fichaClinica.FichaClinicaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ficha_clinica, container, false);
        Button button= root.findViewById(R.id.buttonAgregarFicha);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), FichaClinicaNew.class);
                startActivity(intent);
            }

        });
        ListView listView = root.findViewById(R.id.list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FichaClinica item = adapter.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), SelectedFichaClinica.class);
                Gson gS = new Gson();
                String ficha = gS.toJson(item);
                Bundle b = new Bundle();
                b.putString("ficha", ficha);

                intent.putExtras(b);

                startActivity(intent);
            }


        });

        Call<Lista<FichaClinica>> callPersona = Servicios.getFichaClinicaService().obtenerFichaClinica();

        callPersona.enqueue(new Callback<Lista<FichaClinica>>() {
            @Override
            public void onResponse(Call<Lista<FichaClinica>> call, Response<Lista<FichaClinica>> response) {
                for (FichaClinica c : response.body().getLista()) {
                    Log.d("w", "Persona de id " + c.getIdFichaClinica());
                }
                if (response.isSuccessful()) {
                    fichas = response.body().getLista();

                    int i = 0;
                    i++;

                    ListView listView = (ListView) getView().findViewById(R.id.list_view);
                    adapter = new FichaClinicaAdapter(getContext(), fichas);
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
            public void onFailure(Call<Lista<FichaClinica>> call, Throwable t) {
                Log.w("warning", t.getCause().toString());
            }
        });
        return root;

    }

}
