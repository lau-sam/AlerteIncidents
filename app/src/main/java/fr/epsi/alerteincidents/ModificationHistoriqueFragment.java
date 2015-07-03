package fr.epsi.alerteincidents;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import classes.metier.IncidentDB;
import fr.epsi.database.DbHelper;

public class ModificationHistoriqueFragment extends Fragment {
    private View rootView;
    private TextView text_incident_id;
    DbHelper mLocalDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_modification_historique, container, false);

        Bundle args = this.getArguments();
        String incident_id = args.getString("incident_id");
        showIncidentDetail(incident_id);

        //gestion bouttons dans la modification fragment
        Button mButtonAnnuler = (Button) rootView.findViewById(R.id.annuler_button);
        Button mButtonEnregistrer = (Button) rootView.findViewById(R.id.bouton_enregistrer);

        final Spinner typeIncidentSpinner = (Spinner) rootView.findViewById(R.id.historiqueSpinner);
        final ArrayAdapter typeIncidentAdapter = new ArrayAdapter(rootView.getContext(),android.R.layout.simple_spinner_item);

        Log.e("HistoriqueAdapter.java", "return rootView");
        return rootView;

    }

    private void showIncidentDetail(String incident_id) {
        TextView incident_text_id = (TextView) rootView.findViewById(R.id.histo_incident_text_id);
        EditText incident_text_titre = (EditText) rootView.findViewById(R.id.histo_incident_text_titre);
        EditText incident_text_date = (EditText) rootView.findViewById(R.id.histo_incident_text_date);
        EditText incident_text_type_incident = (EditText) rootView.findViewById(R.id.histo_incident_text_type_incident);

        DbHelper mLocalDatabase = new DbHelper(getActivity().getApplication());
        IncidentDB mLocalIncident = mLocalDatabase.getHloc(Integer.valueOf(incident_id));

        incident_text_id.setText(mLocalIncident.getString(DbHelper.COLUMN_HLOC_ID));
        incident_text_titre.setText(mLocalIncident.getString(DbHelper.COLUMN_HLOC_TITRE));
        incident_text_date.setText(mLocalIncident.getString(DbHelper.COLUMN_HLOC_DATE));
        incident_text_type_incident.setText(mLocalIncident.getString(DbHelper.COLUMN_HLOC_TYPE_ID));


    }


}
