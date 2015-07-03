package fr.epsi.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;

import classes.metier.IncidentDB;
import fr.epsi.alerteincidents.AppController;
import fr.epsi.database.DbHelper;

public class RequestJson {
    private DbHelper mLocalDatabase;
    private ArrayList<IncidentDB> mListIncident;
    private ArrayList<IncidentDB> mListIncidentHloc;
    private int mProgress;
    private mCallback callback;

    private static int NONE = 0;
    private static int INCIDENT = 1;
    private static int HLOCINCIDENT = 2;
    private static int FINISH = 3;

    private int load_state;

    public interface mCallback {
        void onProgress(int progress);

        void onFinish();
    }

    public RequestJson(Context c) {
        mLocalDatabase = new DbHelper(c);
        this.mProgress = 0;
        callback = null;
        mListIncident = new ArrayList<IncidentDB>();
        mListIncidentHloc = new ArrayList<IncidentDB>();
        load_state = NONE;
    }

    public void waitProgress() {
        if (mProgress > 99) {
            callback.onFinish();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitProgress();
            }
        }, 1000);
    }

    public void loadData(mCallback progressFunc) {
        if (callback == null && progressFunc != null)
            callback = progressFunc;

        Log.v("initDB", "load_state " + load_state);
        if (mProgress == 0 && load_state == NONE) {
            //300 km
            mListIncident = getIncidents();
            load_state = INCIDENT;
            loadData(null);
        } else if (mProgress == 50 && load_state == INCIDENT) {
            mListIncidentHloc = getIncidentsByIMEI();
            load_state = HLOCINCIDENT;
            loadData(null);
        } else if (mProgress == 100 && load_state == HLOCINCIDENT) {
            Log.e("RequestJson.java", "End load Hloc");
            load_state = FINISH;
            Log.v("End Load Incident", "load_state " + load_state);
            callback.onFinish();
            return;
        }
        if (mProgress != 100) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.v("Waiting", "still wait");
                    loadData(null);
                }
            }, 1000);
        }

        return;
    }

    public ArrayList<IncidentDB> getIncidents() {
        //France
        LatLng latlng = new LatLng(47, 2);
        int rayon = 300;
        Webservice mWbs = new Webservice();
        mWbs.getIncidentByLatLng(latlng, rayon);

        Log.e("Url Done getIncidentsByLatLng", mWbs.getUrl());

        JsonArrayRequest strReq = new JsonArrayRequest(mWbs.getUrl(), new Response.Listener<JSONArray>() {
            //(Method.GET, mWbs.getUrl(), new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.e("RequestJson.java getIncidentsByLatLng ", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        IncidentDB incident_item = new IncidentDB();
                        //recuperation donnees
                        int idIncident = Integer.valueOf(response.getJSONObject(i).getString("idIncident"));
                        String titreIncident = response.getJSONObject(i).getString("titreIncident");
                        String dateIncident = response.getJSONObject(i).getString("dateIncident");
                        Double latIncident = response.getJSONObject(i).getDouble("latitude");
                        Double lngIncident = response.getJSONObject(i).getDouble("longitude");
                        int idTypeIncident = response.getJSONObject(i).getInt("idTypeIncident");
                        String descIncident = response.getJSONObject(i).getString("descriptionIncident");
                        String userIncident = response.getJSONObject(i).getString("userIncident");


                        //insertion dans la db
                        mLocalDatabase.insertIncident(String.valueOf(idIncident), dateIncident, titreIncident,
                                lngIncident.toString(), latIncident.toString(), String.valueOf(idTypeIncident),
                                String.valueOf(descIncident), String.valueOf(userIncident));

                        incident_item.setString(DbHelper.COLUMN_INCIDENT_ID,
                                String.valueOf(idIncident));
                        incident_item.setString(DbHelper.COLUMN_INCIDENT_TITRE,
                                String.valueOf(titreIncident));
                        incident_item.setString(DbHelper.COLUMN_INCIDENT_DATE,
                                String.valueOf(dateIncident));
                        incident_item.setString(DbHelper.COLUMN_INCIDENT_LATITUDE,
                                String.valueOf(latIncident));
                        incident_item.setString(DbHelper.COLUMN_INCIDENT_LONGITUDE,
                                String.valueOf(lngIncident));
                        incident_item.setString(DbHelper.COLUMN_INCIDENT_TYPE_ID,
                                String.valueOf(idTypeIncident));
                        incident_item.setString(DbHelper.COLUMN_INCIDENT_DESCRIPTION,
                                String.valueOf(descIncident));
                        incident_item.setString(DbHelper.COLUMN_INCIDENT_USER,
                                String.valueOf(userIncident));

                        mListIncident.add(incident_item);
                    }
                    updateProgress(mProgress + 50);
                } catch (JSONException e) {
                    Log.d("Incident", "FATAL ERROR");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Incident", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(strReq);
        return mListIncident;
    }


    public ArrayList<IncidentDB> getIncidentsByIMEI() {
        Webservice mWbs = new Webservice();
        //pour avoir le vrai
        String mIMEI = Build.SERIAL;

        //IMEI de test
        //String mIMEI = "47900aacc7eed000";

        mWbs.getIncidentsByImei(mIMEI);

        Log.v("Url Done getIncidentsByIMEI", mWbs.getUrl());

        JsonArrayRequest strReq = new JsonArrayRequest(mWbs.getUrl(), new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        Log.e("REQUESTJSON", "NOT WORK");
                        IncidentDB incident_item = new IncidentDB();
                        //recuperation donnees
                        int idIncident = Integer.valueOf(response.getJSONObject(i).getString("idIncident"));
                        String titreIncident = response.getJSONObject(i).getString("titreIncident");
                        String dateIncident = response.getJSONObject(i).getString("dateIncident");
                        Double latIncident = response.getJSONObject(i).getDouble("latitude");
                        Double lngIncident = response.getJSONObject(i).getDouble("longitude");
                        int idTypeIncident = response.getJSONObject(i).getInt("idTypeIncident");
                        String descIncident = response.getJSONObject(i).getString("descriptionIncident");
                        String userIncident = response.getJSONObject(i).getString("userIncident");

                        //insertion dans la db
                        mLocalDatabase.insertHloc(" ", String.valueOf(idIncident), dateIncident, titreIncident,
                                lngIncident.toString(), latIncident.toString(), String.valueOf(idTypeIncident),
                                String.valueOf(descIncident), String.valueOf(userIncident));

                        incident_item.setString(DbHelper.COLUMN_HLOC_ID,
                                String.valueOf(idIncident));
                        incident_item.setString(DbHelper.COLUMN_HLOC_TITRE,
                                String.valueOf(titreIncident));
                        incident_item.setString(DbHelper.COLUMN_HLOC_DATE,
                                String.valueOf(dateIncident));
                        incident_item.setString(DbHelper.COLUMN_HLOC_LATITUDE,
                                String.valueOf(latIncident));
                        incident_item.setString(DbHelper.COLUMN_HLOC_LONGITUDE,
                                String.valueOf(lngIncident));
                        incident_item.setString(DbHelper.COLUMN_HLOC_TYPE_ID,
                                String.valueOf(idTypeIncident));
                        incident_item.setString(DbHelper.COLUMN_HLOC_DESCRIPTION,
                                String.valueOf(descIncident));
                        incident_item.setString(DbHelper.COLUMN_HLOC_USER,
                                String.valueOf(userIncident));

                        mListIncidentHloc.add(incident_item);
                    }
                    updateProgress(mProgress + 50);
                } catch (JSONException e) {
                    Log.d("Incident", "FATAL ERROR");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Incident", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(strReq);
        return mListIncidentHloc;
    }

    private final void updateProgress(int progress) {
        mProgress = progress;
        this.callback.onProgress(mProgress);

    }
}

