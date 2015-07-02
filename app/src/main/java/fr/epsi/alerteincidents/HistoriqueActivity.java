package fr.epsi.alerteincidents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import classes.metier.Incident;
import classes.metier.IncidentDB;
import classes.metier.TypeIncident;
import fr.epsi.database.DbHelper;
import fr.epsi.helper.RestApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class HistoriqueActivity extends Activity {
	// used to store app title
	private CharSequence mTitle;
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        //change le titre de l'activite
        setTitle("Historique");

        /*final ArrayAdapter typeIncidentAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.API_URL)
                .build();
        RestApi methods = restAdapter.create(RestApi.class);

        List<Incident> mlist = methods.getIncidentsByUser(Build.SERIAL);
*/

		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		//recuperation de la liste des incidents locaux
		DbHelper mLocalDatabase = new DbHelper(this);
		ArrayList<IncidentDB> list_Incident = mLocalDatabase.getAllIncidents();
		ArrayList<IncidentDB> myDataset = new ArrayList<>();

		for (int i=0;i<list_Incident.size();i++){
			IncidentDB item = list_Incident.get(i);
			myDataset.add(i,item);
		}

		mAdapter = new HistoriqueAdapter(myDataset);
		mRecyclerView.setAdapter(mAdapter);
	}

	//gestion bouton retour
	public void onBackPressed(){
		//recupere le nom de l'activite precedente
		SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
		String current = mPrefs.getString("ActivityName", "NoName");
		Log.v("onBackPressed", current);
		
        //donne le nom de l'activite
		Editor edit = mPrefs.edit();
		edit.putString("ActivityName", "HistoriqueActivity");
		edit.commit();
		
		//test du nom de l'activite
		if (current.equals("MainActivity")){
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("CarteActivity")){
			Intent intent = new Intent(this,CarteActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("IncidentActivity")){
			Intent intent = new Intent(this,IncidentActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("PreferencesActivity")){
			Intent intent = new Intent(this,PreferencesActivity.class);
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
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        if (id == R.id.action_carte) {
			Intent intent = new Intent(this,CarteActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        if (id == R.id.action_historique) {
			Intent intent = new Intent(this,HistoriqueActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        if (id == R.id.action_incident) {
			Intent intent = new Intent(this,IncidentActivity.class);
			startActivity(intent);
			this.finish();
            return true;
        }
        if (id == R.id.action_preferences) {
			Intent intent = new Intent(this,PreferencesActivity.class);
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

	public void onButtonClick(View v){
		switch (v.getId()){
			case R.id.bouton_retour:
				android.support.v7.widget.RecyclerView mRView = (android.support.v7.widget.RecyclerView)
						((Activity) v.getContext()).findViewById(R.id.my_recycler_view);
				mRView.setVisibility(View.VISIBLE);
				FrameLayout mFrameLayout = (FrameLayout) ((Activity) v.getContext()).findViewById(R.id.frame_modificationHistorique);//findViewById(R.layout.fragment_modification_historique);
				mFrameLayout.setVisibility(View.GONE);

				break;
			case R.id.bouton_enregistrer:
				EditText incident_text_titre = (EditText)
						((Activity) v.getContext()).findViewById(R.id.histo_incident_text_titre);
                EditText incident_text_date = (EditText)
                        ((Activity) v.getContext()).findViewById(R.id.histo_incident_text_date);
                EditText incident_text_type = (EditText)
                        ((Activity) v.getContext()).findViewById(R.id.histo_incident_text_type_incident);
                TextView incident_text_id = (TextView)
                        ((Activity) v.getContext()).findViewById(R.id.histo_incident_text_id);

                Log.e("HistoriqueActivity.java/onButtonClick() : PUT a realiser",String.valueOf(incident_text_titre.getText()));

                Incident i = new Incident(Integer.parseInt(incident_text_id.getText().toString()), Build.SERIAL, incident_text_titre.getText().toString(), Integer.parseInt(incident_text_type.getText().toString()), null,
                        java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));

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
                methods.updateIncident(i, new Callback<Incident>() {
                    @Override
                    public void success(Incident incident, retrofit.client.Response response) {
                        /* update bd locale */

                        Toast.makeText(HistoriqueActivity.this, "L'incident a bien été modifié", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(HistoriqueActivity.this, "Erreur, vérifiez votre connexion internet", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
		}
	}

	protected void alertbox(String title, String mymessage)
	{
		new AlertDialog.Builder(this)
				.setMessage(mymessage)
				.setTitle(title)
				.setCancelable(true)
				.setNeutralButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton){}
						})
				.show();
	}
}