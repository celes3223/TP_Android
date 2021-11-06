package com.example.tp_android.Services;

import com.example.tp_android.data.model.FichaClinica;
import com.example.tp_android.data.model.Persona;
import com.example.tp_android.data.model.Reserva;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Servicios {

    public static PersonaService getPersonaService () {
        return getClient ("https://equipoyosh.com/stock-nutrinatalia/").create(PersonaService.class);
    }

    public static Retrofit getClient(String baseUrl) {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ReservaService getReservaService() {
        return getClient("https://equipoyosh.com/stock-nutrinatalia/").create(ReservaService.class);
    }

    public static Retrofit getReserva(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static FichaClinicaService getFichaClinicaService (){
        return getFichas ( "https://equipoyosh.com/stock-nutrinatalia/" ).create(FichaClinicaService.class);
    }

    public static Retrofit getFichas (String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
