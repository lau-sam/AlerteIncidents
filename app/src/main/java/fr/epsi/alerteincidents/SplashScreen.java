package fr.epsi.alerteincidents;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import fr.epsi.database.DbHelper;
import fr.epsi.helper.RequestJson;
import fr.epsi.helper.RequestJson.mCallback;
import fr.epsi.helper.VarsGlobals;

public class SplashScreen extends Activity {

    ProgressBar p_bar;

    class ProgressCallback implements mCallback {
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
        VarsGlobals.set("IMEI", "12345678910111");
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        this.p_bar = (ProgressBar) this.findViewById(R.id.progressBar);

        boolean chargerLaBase = false;

        SharedPreferences sharedpreferences = getSharedPreferences("AlerteIncidents", Context.MODE_PRIVATE);
        Editor editor = sharedpreferences.edit();

        Calendar now_date = Calendar.getInstance();
        long now = now_date.getTimeInMillis();

        if (!sharedpreferences.contains("last_update")) {

            chargerLaBase = true;
            editor.putLong("last_update", now);
        }

        long last = sharedpreferences.getLong("last_update", now);

        if (now - last > 86400000) {
            chargerLaBase = true;
            editor.putLong("last_update", now);
        }

        editor.commit();

// la valeur true chargerLaBase ,force l'insertion dans la base de donnees
        if (isNetworkAvailable()) {
            chargerLaBase = true;
        }
        if (chargerLaBase) {
            DbHelper mDB = new DbHelper(this);
            SQLiteDatabase mDatabase = this.openOrCreateDatabase("DB_AI.db", MODE_PRIVATE, null);
            dropLocalDatabase(mDatabase);
            Log.e("DROPED ?", "NOW");
            RequestJson dl = new RequestJson(this);
            dl.loadData(new ProgressCallback());
        } else {
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    //DROP TABLES
    private void dropLocalDatabase(SQLiteDatabase db) {
        String DROP_HLOC = "DROP TABLE IF EXISTS Hloc;";
        String DROP_INCIDENT = "DROP TABLE IF EXISTS Incident;";
        String CREATE_HLOC = "CREATE TABLE Hloc ("
                + "hloc_id INTEGER PRIMARY KEY," + "hloc_adresse TEXT," + "hloc_date TEXT,"
                + "hloc_titre TEXT," + "hloc_longitude TEXT,"
                + "hloc_latitude TEXT," + " hloc_type_id TEXT,"
                + "hloc_description TEXT," + " hloc_user TEXT"
                + " );";
        String CREATE_INCIDENT = "CREATE TABLE Incident ("
                + "incident_id INT PRIMARY KEY," + "incident_date TEXT,"
                + "incident_titre TEXT," + "incident_longitude TEXT,"
                + "incident_latitude TEXT," + " incident_type_id TEXT,"
                + "incident_description TEXT," + " incident_user TEXT"
                + " );";
        db.execSQL(DROP_HLOC);
        db.execSQL(DROP_INCIDENT);
        db.execSQL(CREATE_INCIDENT);
        db.execSQL(CREATE_HLOC);
        Log.e("SplashScreen.java", "create : " + CREATE_HLOC + " ///// " + CREATE_INCIDENT);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}