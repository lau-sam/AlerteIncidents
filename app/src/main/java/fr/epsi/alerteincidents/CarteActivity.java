package fr.epsi.alerteincidents;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import classes.metier.IncidentDB;
import fr.epsi.database.DbHelper;
import fr.epsi.helper.FusedLocationService;

public class CarteActivity extends Activity {

    static final LatLng FRANCE = new LatLng(47, 2);
    private GoogleMap map;
    private FusedLocationService mFusedLocation;
    private CharSequence mTitle;
    private HashMap<Marker, IncidentDB> marker2incident;
    private DbHelper mLocalDatabase = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            setContentView(R.layout.layout_google_play_services_not_available);
            alertbox("GooglePlayServices pas a jour !", "Desole, pas de carte pour toi. Cliques sur \"Annuler\", puis \"Mettre a jour\".");
        } else {
            if (!isNetworkAvailable()){
                alertbox("Pas d'acces internet", "Aucune connexion internet, la carte peut s'afficher si elle a déjà été chargée");
            }
                setContentView(R.layout.activity_carte);
            //change le titre de l'activite
            setTitle("Carte");
            //recupere le map par son id
            final MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            map = mapFragment.getMap();
            //affiche le point de notre position
            map.setMyLocationEnabled(true);
            //zoom sur notre position si gps active, sinon zoom sur France
            mFusedLocation = new FusedLocationService(this);
            Location location = mFusedLocation.getLocation();

            marker2incident = new HashMap<Marker, IncidentDB>();

            //ajout markers
            addMarkersIncidents(mLocalDatabase);

            // est appele lorsqu'on clique sur la fenetre info
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    IncidentDB item = marker2incident.get(marker);
                    startDetailActivity(item.getString(DbHelper.COLUMN_INCIDENT_ID));

                }
            });

            //Log.v("===CARTE / Latitude : " + String.valueOf(location.getLatitude())
            //		, "Longitude : " + String.valueOf(location.getLongitude()));
            // Move the camera instantly with a zoom .
            //if (!String.valueOf(location.getLatitude()).isEmpty() && !String.valueOf(location.getLongitude()).isEmpty()){
            //	map.moveCamera(CameraUpdateFactory.newLatLngZoom(FRANCE, 8));
            //}
            //else {
            //	map.moveCamera(CameraUpdateFactory.newLatLngZoom(FRANCE, 5));
            //}

            //MONTPEL
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.642934, 3.838436), 13));
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(FRANCE, 8));
        }

    }

    public void startDetailActivity(String item_id) {
        Intent i = new Intent(CarteActivity.this, DetailActivity.class);
        i.putExtra("incident_id", item_id);
        startActivity(i);
        this.finish();
    }

    //add Incidents on map with marker
    private void addMarkersIncidents(DbHelper mLocalDatabase) {
        List<IncidentDB> list_Incident = mLocalDatabase.getAllIncidents();

        for (int i = 0; i < list_Incident.size(); i++) {
            IncidentDB item = (IncidentDB) list_Incident.get(i);
            LatLng latlng = new LatLng(Double.valueOf(item.getString(DbHelper.COLUMN_INCIDENT_LATITUDE)),
                    Double.valueOf(item.getString(DbHelper.COLUMN_INCIDENT_LONGITUDE)));

            Marker temp = map.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(item.getString(DbHelper.COLUMN_INCIDENT_TITRE))
                            .snippet("Date d'ajout : " + item.getString(DbHelper.COLUMN_INCIDENT_DATE))
                            .visible(true)
            );

            if (item.getString(DbHelper.COLUMN_INCIDENT_TYPE_ID).equals("1")) {
                temp.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.innondation_marker));
            } else if (item.getString(DbHelper.COLUMN_INCIDENT_TYPE_ID).equals("2")) {
                temp.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.incendie_marker));

            } else
                temp.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));

            marker2incident.put(temp, item);
        }
    }

    //get coordonates with adress
    //@param : String adress
    public LatLng getCoordonatesWithAdress(String adress) {
        double latitude = 0;
        double longitude = 0;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(adress, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addresses.get(0);
        if (addresses.size() > 0) {
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
        }
        return new LatLng(latitude, longitude);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    //gestion bouton retour
    public void onBackPressed() {
        //recupere le nom de l'activite precedente
        SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
        String current = mPrefs.getString("ActivityName", "NoName");
        Log.v("onBackPressed", current);

        //donne le nom de l'activite
        Editor edit = mPrefs.edit();
        edit.putString("ActivityName", "CarteActivity");
        edit.commit();

        //test du nom de l'activite
        if (current.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        if (current.equals("HistoriqueActivity")) {
            Intent intent = new Intent(this, HistoriqueActivity.class);
            startActivity(intent);
            this.finish();
        }
        if (current.equals("IncidentActivity")) {
            Intent intent = new Intent(this, IncidentActivity.class);
            startActivity(intent);
            this.finish();
        }
        if (current.equals("PreferencesActivity")) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (id == R.id.action_carte) {
            Intent intent = new Intent(this, CarteActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (id == R.id.action_historique) {
            Intent intent = new Intent(this, HistoriqueActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (id == R.id.action_incident) {
            Intent intent = new Intent(this, IncidentActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (id == R.id.action_preferences) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
/*
        if (id == R.id.addIncidentMap){
			callAjoutDialog();
		}*/
        return super.onOptionsItemSelected(item);
    }

    protected void alertbox(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .show();
    }

    private void callAjoutDialog() {
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.layout_ajout_incident_map);
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setTitle("Ajouter un incident en local");
        Button mButtonAjouter = (Button) myDialog.findViewById(R.id.ajouter_button);
        Button mButtonAnnuler = (Button) myDialog.findViewById(R.id.annuler_button);

        //affichage de la boite de dialogue
        myDialog.show();

        //ecouteur bouton ajouter
        mButtonAjouter.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        EditText user_adress = (EditText) myDialog.findViewById(R.id.user_adress);

                        //contient l'adresse saisie par l'utilisateur, a saisir dans la base
                        final String userAdress = user_adress.getText().toString();
                        //

                        myDialog.dismiss();
                        alertbox("Felicitation", "Ajout le l'adresse : \"" + userAdress + "\" effectue !");

                        LatLng userAdressLatLng = getCoordonatesWithAdress(userAdress);
                        Log.v("Latitude : " + String.valueOf(userAdressLatLng.latitude),
                                "Longitude " + String.valueOf(userAdressLatLng.longitude));
                        //DbHelper mLocalDatabase = new DbHelper(getApplicationContext());
                        //get current date
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        //a recuperer depuis le formulaire d'ajout, pour l'instant le formulaire ne contient
                        //que la chaine d'adresse
                        //a faire aussi : latitude et longitude, ne peut pas les avoir si pas internet
                        String hloc_titre = "Titre incident local"; //titre de test, a recuperer
                        String hloc_longitude = String.valueOf(userAdressLatLng.longitude);
                        String hloc_latitude = String.valueOf(userAdressLatLng.latitude);
                        String hloc_type_id = "1"; //type id de test, a recuperer

                        mLocalDatabase.insertHloc(userAdress, null, date.toString(), hloc_titre,
                                hloc_longitude, hloc_latitude, hloc_type_id, "", "");

                        //Log.v("===CARTE", "Begin");
                        //IncidentDB mLocalIncident = mLocalDatabase.getHloc(1);
                        //Log.v("===CARTE","Success");
                        //String titreDeTest = mLocalIncident.getString("hloc_titre");
                        //Log.v("===CARTE ACTIVITY"," TITRE : "+titreDeTest);
                    }
                });

        //ecouteur bouton annuler
        mButtonAnnuler.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
    }

    //set app title on actionBar
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
