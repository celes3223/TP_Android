package com.example.tp_android.Services;

import android.telecom.Call;

import com.example.tp_android.data.model.FichaClinica;
import com.example.tp_android.data.model.Lista;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FichaClinicaService {

    @GET("fichaClinica")
    Call<Lista<FichaClinica>> obtenerFichaClinica();

    @POST("fichaClinica")
    Call<FichaClinica> agregarFichaClinica ( @Body FichaClinica fichaClinica);

    @PUT("fichaClinica")
    Call<FichaClinica> actualizarFichaClinica(@Body FichaClinica fichaClinica);

    @DELETE("fichaClinica/{id}")
    Call<FichaClinica> borrarFichaClinica(@Path("id") Integer idFicha);
}
