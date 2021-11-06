package com.example.tp_android.ui.fisio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.Lista;
import com.example.tp_android.data.model.Persona;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FisioFragment extends Fragment {

    private FisioViewModel fisioViewModel;
    private Persona[] personas;
    FisioAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fisioViewModel =
                ViewModelProviders.of(this).get(FisioViewModel.class);


        Call<Lista<Persona>> callPersona= Servicios.getPersonaService().obtenerPersonas();

        View root = inflater.inflate(R.layout.fragment_fisio, container, false);
        ListView listView = root.findViewById(R.id.list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Persona item = adapter.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), SelectedFisio.class);
                Gson gS = new Gson();
                String persona = gS.toJson(item);
                Bundle b = new Bundle();
                b.putString("persona",persona);

                intent.putExtras(b);

                startActivity(intent);
            }


        });



        //Button bt_delete = root.findViewById(R.id.buttonDelete);

        callPersona.enqueue(new Callback<Lista<Persona>>() {

            @Override
            public void onResponse(Call<Lista<Persona>> call, Response<Lista<Persona>> response) {

                if(response.isSuccessful()) {
                    personas = response.body().getLista();

                    ListView listView = (ListView)getView().findViewById(R.id.list_view);
                    adapter = new FisioAdapter(getContext(), personas);
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
