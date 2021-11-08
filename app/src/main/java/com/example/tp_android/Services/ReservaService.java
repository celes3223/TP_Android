package com.example.tp_android.Services;

import com.example.tp_android.data.model.Lista;
import com.example.tp_android.data.model.Reserva;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface ReservaService {

    @GET("reserva")
    Call<Lista<Reserva>> obtenerReservas();

    @GET("reserva")
    Call<Reserva> agregarReserva(@Body Reserva reserva);

    @PUT("reserva")
    Call<Reserva> actualizarReserva(@Body Reserva reserva);
}
