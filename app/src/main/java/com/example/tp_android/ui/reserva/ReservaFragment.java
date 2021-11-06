package com.example.tp_android.ui.reserva;

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
import androidx.lifecycle.ViewModelProviders;

import com.example.tp_android.R;
import com.example.tp_android.Services.Servicios;
import com.example.tp_android.data.model.Lista;
import com.example.tp_android.data.model.Reserva;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservaFragment extends Fragment {

    private ReservaViewModel reservaViewModel;
    private Reserva[] reservas;
    ReservaAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reservaViewModel =
                ViewModelProviders.of(this).get(ReservaViewModel.class);


        Call<Lista<Reserva>> callReserva= Servicios.getReservaService().obtenerReservas();

        View root = inflater.inflate(R.layout.fragment_reserva, container, false);
        ListView listView = root.findViewById(R.id.list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Reserva item = adapter.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), SelectedReserva.class);
                Gson gS = new Gson();
                String reserva = gS.toJson(item);
                Bundle b = new Bundle();
                b.putString("reserva",reserva);

                intent.putExtras(b);

                startActivity(intent);
            }


        });


        callReserva.enqueue(new Callback<Lista<Reserva>>() {

            @Override
            public void onResponse(Call<Lista<Reserva>> call, Response<Lista<Reserva>> response) {

                if(response.isSuccessful()) {
                    reservas = response.body().getLista();

                    ListView listView = (ListView)getView().findViewById(R.id.list_view);
                    adapter = new ReservaAdapter(getContext(), reservas);
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
            public void onFailure(Call<Lista<Reserva>> call, Throwable t) {
                Log.w("warning",t.getCause().toString());
            }
        });
        return root;
    }

}
