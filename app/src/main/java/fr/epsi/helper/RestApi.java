package fr.epsi.helper;

import java.util.List;

import classes.metier.Incident;
import classes.metier.TypeIncident;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by mehdi on 15/06/2015.
 */
public interface RestApi {

    // Activité creation d'incident

    @GET("/incidents/types")
    public void getTypesIncidents(Callback<List<TypeIncident>> cb);

    @GET("/incidents/types")
    public List<TypeIncident> getTypesIncidents();

    @POST("/incident")
    public void createIncident(@Body Incident i, Callback<Incident> cb);

    // Activité Map

    @GET("/incidents/{lat}/{long}")
    public List<TypeIncident> getIncidentsByCoord(@Path("lat") double latitude, @Path("long") double longitude);

    // Activité Historique

    @GET("/incidents/parEMEI/{user}")
    public List<Incident> getIncidentsByUser(@Path("user") String imei);

    @PUT("/incident")
    public void updateIncident(@Body Incident i, Callback<Incident> cb);
}
