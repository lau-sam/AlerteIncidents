package fr.epsi.alerteincidents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
 
public class MainActivity extends Activity {
	
	private final String tag = "MainActivity";
	// used to store app title
	private CharSequence mTitle;
    public static final String API_URL = "http://perso.montpellier.epsi.fr/~samuel.thery";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        //change le titre de l'activite
        setTitle("Accueil");
        
        //donne le nom de l'activite
		SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
		Editor edit = mPrefs.edit();
		edit.putString("ActivityName", "MainActivity");
		edit.commit();
        
		//trouve le bouton par son id
		ImageButton buttonMap = (ImageButton) findViewById(R.id.imageBtnMap);
		ImageButton buttonIncident = (ImageButton) findViewById(R.id.imageBtnIncident);
		ImageButton buttonHisto = (ImageButton) findViewById(R.id.imageBtnHisto);
		ImageButton buttonPreference = (ImageButton) findViewById(R.id.imageBtnPreference);
		
		//ecouteur du bouton map
		buttonMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				if (isNetworkAvailable())
					newMap();
				else
					alertbox("Pas d'acces internet", "Desole , nous pouvons pas ouvrir l'activite " +
							"Carte. En effet, vous n'avez pas acces a internet. \nActivez l'option internet" +
							" puis reessayez !");
            }
		});
		
		//ecouteur du bouton incident
		buttonIncident.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newIncident();
            }
		});
		
		//ecouteur du bouton historique
		buttonHisto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newHistorique();
            }
		});
		
		//ecouteur du bouton preferences
		buttonPreference.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newPreferences();
            }
		});
    }
	
    //ouvre CarteActivity.java, termine l'activite actuelle
	public void newMap(){
		Intent intent = new Intent (this, CarteActivity.class);
		startActivity(intent);
		this.finish();
	}
    
	//ouvre IncidentActivity.java, termine l'activite actuelle
	public void newIncident(){
		Intent intent = new Intent (this, IncidentActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	//ouvre HistoriqueActivity.java, termine l'activite actuelle
	public void newHistorique(){
		Intent intent = new Intent (this, HistoriqueActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	//ouvre PreferencesActivity.java, termine l'activite actuelle
	public void newPreferences(){
		Intent intent = new Intent (this, PreferencesActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	//gestion bouton retour
	public void onBackPressed(){
		//recupere le nom de l'activite precedente
		SharedPreferences mPrefs = getSharedPreferences("ActivityName", Context.MODE_PRIVATE);
		String current = mPrefs.getString("ActivityName", "NoName");
		
		//test du nom de l'activite
		if (current.equals("MainActivity")){
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
		if (current.equals("PreferencesActivity")){
			Intent intent = new Intent(this,PreferencesActivity.class);
			startActivity(intent);
			this.finish();
		}
		if (current.equals("CarteActivity")){
			Intent intent = new Intent(this,CarteActivity.class);
			startActivity(intent);
			this.finish();
		}
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

	protected void alertbox(String title, String mymessage)
	{
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

	
/*
	//menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //gestion click sur un item du menu
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
*/
	
}
