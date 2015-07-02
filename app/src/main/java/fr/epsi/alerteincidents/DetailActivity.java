package fr.epsi.alerteincidents;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DetailActivity extends Activity {
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Fragment detail_frag = new DetailFragment();
        fragment = detail_frag;

        //recuperation de l'id
        Intent i = getIntent();
        String incident_id = i.getStringExtra("incident_id");
        Bundle args = new Bundle();
        args.putString("incident_id", incident_id);
        fragment.setArguments(args);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frame_detail, fragment, null);
        ft.commit();
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

    //gestion bouton retour
    public void onBackPressed(){
        Intent intent = new Intent(this,CarteActivity.class);
        startActivity(intent);
        this.finish();
    }
}
