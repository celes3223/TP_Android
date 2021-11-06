package com.example.tp_android.Services;

import android.telecom.Call;

import com.example.tp_android.data.model.Lista;
import com.example.tp_android.data.model.Persona;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PersonaService {

    @GET("persona")
    Call<Lista<Persona>> obtenerPersonas();

    @GET("/persona/{idPersona}/agenda")
    Call<Lista<Persona>> obtenerAgenda(@Path("idPersona") String idPersona, @Query("fecha") String fecha, @Query("disponible") String disponible);

    @POST("persona")
    retrofit2.Call<Persona> agregarPersona(@Body Persona persona);

    @PUT("persona")
    Call<Persona> actualizarCategoria(@Body Persona persona);

    @DELETE("persona/{id}")
    Call<Persona> borrarPersona(@Path("id") Integer idPersona);
}
