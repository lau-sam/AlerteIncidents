package fr.epsi.alerteincidents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import classes.metier.TypeIncident;
import fr.epsi.helper.RestApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class PreferencesActivity extends Activity {
	// used to store app title
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
        //change le titre de l'activite
        setTitle("Preferences");

		CheckBox mInondationsCB = (CheckBox) findViewById(R.id.checkbox_inondations);
		CheckBox mIncendiesCB = (CheckBox) findViewById(R.id.checkbox_incendies);

		SharedPreferences sharedpreferences = getSharedPreferences("AlerteIncidents", Context.MODE_PRIVATE);
		boolean inondationsCheck = sharedpreferences.getBoolean("inondations",false);
		boolean incendiesCheck = sharedpreferences.getBoolean("incendies",false);

		if(inondationsCheck){
			mInondationsCB.setChecked(true);
		}

		if(incendiesCheck) {
			mIncendiesCB.setChecked(true);
		}

		/*
        RecyclerView rv = (RecyclerView)findViewById(R.id.RecyclerViewTypesIncidents);
        rv.setHasFixedSize(true);

        final ArrayAdapter typeIncidentAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.API_URL)
                .build();
        RestApi methods = restAdapter.create(RestApi.class);
        List<TypeIncident> l = new ArrayList<TypeIncident>();
        l = methods.getTypesIncidents();


        rv.setAdapter(new PreferencesAdapter(l));
        rv.setLayoutManager(new LinearLayoutManager(this));
        */
	}

	public void onCheckboxClicked(View view) {
		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();

		SharedPreferences sharedpreferences = getSharedPreferences("AlerteIncidents", Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();

		// Check which checkbox was clicked
		switch(view.getId()) {
			case R.id.checkbox_incendies:
				if (checked){
					editor.putBoolean("incendies",true);
					Log.v("===PREFERENCES","INCENDIES CHECKED");
				}
				else{
					editor.putBoolean("incendies",false);
					Log.v("===PREFERENCES","INCENDIES NOTCHECKED");
				}
				break;
			case R.id.checkbox_inondations:
				if (checked){
					editor.putBoolean("inondations",true);
					Log.v("===PREFERENCES","INONDATIONS CHECKED");
				}
				else{
					editor.putBoolean("inondations",false);
					Log.v("===PREFERENCES","INONDATIONS NOTCHECKED");
				}
				break;
		}
		editor.commit();
	}

	//gestion bouton retour
	public void onBackPressed(){
		//recupere le nom de l'activite precedente
		SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
		String current = mPrefs.getString("ActivityName", "NoName");
		Log.v("onBackPressed",current);
		
        //donne le nom de l'activite
		Editor edit = mPrefs.edit();
		edit.putString("ActivityName", "PreferencesActivity");
		edit.commit();
		
		//test du nom de l'activite
		if (current.equals("MainActivity")){
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("HistoriqueActivity")){
			Intent intent = new Intent(this,HistoriqueActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("IncidentActivity")){
			Intent intent = new Intent(this,IncidentActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("CarteActivity")){
			Intent intent = new Intent(this,CarteActivity.class);
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
}
