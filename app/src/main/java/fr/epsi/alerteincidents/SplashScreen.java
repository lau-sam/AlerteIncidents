package fr.epsi.alerteincidents;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.ProgressBar;

import fr.epsi.helper.RequestJson;
import fr.epsi.helper.RequestJson.mCallback;
import fr.epsi.helper.VarsGlobals;

public class SplashScreen extends Activity {
	 
	ProgressBar p_bar;
    
    class ProgressCallback implements mCallback
    {
		@Override
		public void onProgress(int progress) {
			p_bar.setProgress(progress);
		}

		@Override
		public void onFinish() {
			Intent i = new Intent(SplashScreen.this, MainActivity.class);
	        startActivity(i);
	        finish();	
		}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        VarsGlobals.set("IMEI","12345678910111");
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        this.p_bar = (ProgressBar) this.findViewById(R.id.progressBar);
        
        boolean chargerLaBase = false;
        
        SharedPreferences sharedpreferences = getSharedPreferences("AlerteIncidents", Context.MODE_PRIVATE);
        Editor editor = sharedpreferences.edit();
        
        Calendar now_date = Calendar.getInstance();
    	long now = now_date.getTimeInMillis();

        if (!sharedpreferences.contains("last_update"))
        {
        	chargerLaBase = true;
        	editor.putLong("last_update", now);
        }
        
        long last = sharedpreferences.getLong("last_update", now);
        	
        if (now - last > 86400000 )
        {
        	chargerLaBase = true;
        	editor.putLong("last_update", now);
        }
        	
        editor.commit();
        
        // la valeur true chargerLaBase ,force l'insertion dans la base de donnees
        chargerLaBase = true;
        if (chargerLaBase)
        {
            RequestJson dl = new RequestJson(this);
            dl.loadData(new ProgressCallback());
        }
        else
        {
        	Intent i = new Intent(SplashScreen.this,MainActivity.class);
        	startActivity(i);
        	finish();
        }
    }

}