package fr.epsi.alerteincidents;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import classes.metier.Incident;
import classes.metier.TypeIncident;
import fr.epsi.database.DbHelper;
import fr.epsi.helper.FusedLocationService;
import fr.epsi.helper.RestApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import static android.location.LocationManager.*;

public class IncidentActivity extends Activity {
    // used to store app title
    private CharSequence mTitle;
    private final String tag = "IncidentActivity";
    private FusedLocationService mFusedLocation;
    private Button validerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);
        //change le titre de l'activite
        setTitle("Ajouter un Incident");

        final Spinner typeIncidentSpinner = (Spinner) findViewById(R.id.typeIncidentSpinner);
        final ArrayAdapter typeIncidentAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.API_URL)
                .build();
        RestApi methods = restAdapter.create(RestApi.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, retrofit.client.Response response) {
                List<TypeIncident> types = (List<TypeIncident>) o;
                typeIncidentAdapter.addAll(types);
                typeIncidentSpinner.setAdapter(typeIncidentAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                typeIncidentSpinner.setPrompt("Pas de connexion internet");
            }
        };
        methods.getTypesIncidents(callback);

        mFusedLocation = new FusedLocationService(this);

        //bouton valider
        validerButton = (Button) findViewById(R.id.validerButton);
        validerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                final EditText titreIncident = (EditText) findViewById(R.id.titreIncidentEditText);
                final EditText descIncident = (EditText) findViewById(R.id.descriptionIncidentEditText);

                if (titreIncident.getText().toString().equals("")) {
                    titreIncident.setError("Entrez un titre d'incident");
                } else {
                    if (lm.isProviderEnabled(GPS_PROVIDER)) {

                        Location mLastLocation = mFusedLocation.getLocation();

                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date now = new Date();

                        final Incident i = new Incident(Build.SERIAL, titreIncident.getText().toString(), (TypeIncident) typeIncidentSpinner.getSelectedItem(), descIncident.getText().toString(),
                                mLastLocation.getLatitude(), mLastLocation.getLongitude(), fmt.format(now).toString());

                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(MainActivity.API_URL)
                                .setLogLevel(RestAdapter.LogLevel.FULL)
                                .setLog(new RestAdapter.Log() {
                                    @Override
                                    public void log(String msg) {
                                        Log.d("Retrofit", msg);
                                    }
                                }).build();
                        RestApi methods = restAdapter.create(RestApi.class);
                        methods.createIncident(i, new Callback<Incident>() {
                            @Override
                            public void success(Incident incident, retrofit.client.Response response) {
                                DbHelper dbHelper = new DbHelper(IncidentActivity.this);

                                dbHelper.insertHloc("", String.valueOf(incident.getIdIncident()), i.getDateIncident(),
                                        i.getTitreIncident(),String.valueOf(i.getLongitude()),
                                        String.valueOf(i.getLatitude()), String.valueOf(i.getIdTypeIncident()),i.getDescriptionIncident(), Build.SERIAL);
                                dbHelper.insertIncident(String.valueOf(incident.getIdIncident()), i.getDateIncident(),
                                        i.getTitreIncident(),String.valueOf(i.getLongitude()),
                                        String.valueOf(i.getLatitude()), String.valueOf(i.getIdTypeIncident()),i.getDescriptionIncident(), Build.SERIAL);

                                Toast.makeText(IncidentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(IncidentActivity.this, HistoriqueActivity.class);
                                startActivity(intent);
                                IncidentActivity.this.finish();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(IncidentActivity.this, "Erreur, vérifiez votre connexion internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(IncidentActivity.this, "Erreur, veuillez activer la localisation GPS et votre connexion internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    //gestion bouton retour
    public void onBackPressed() {
        //recupere le nom de l'activite precedente
        SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
        String current = mPrefs.getString("ActivityName", "NoName");
        Log.v("onBackPressed", current);

        //donne le nom de l'activite
        Editor edit = mPrefs.edit();
        edit.putString("ActivityName", "IncidentActivity");
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
        if (current.equals("CarteActivity")) {
            Intent intent = new Intent(this, CarteActivity.class);
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
        return super.onOptionsItemSelected(item);
    }

    //set app title on actionBar
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

}
