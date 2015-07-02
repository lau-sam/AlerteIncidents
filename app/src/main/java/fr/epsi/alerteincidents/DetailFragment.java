package fr.epsi.alerteincidents;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import classes.metier.IncidentDB;
import fr.epsi.database.DbHelper;

/**
 * Created by Sam Lau on 26/06/2015.
 */
public class DetailFragment extends Fragment {

    private View rootView;
    private TextView text_incident_id;
    DbHelper mLocalDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail_info_map, container, false);

        Bundle args = this.getArguments();
        String incident_id = args.getString("incident_id");
        showIncidentDetail(incident_id);
        return rootView;

    }

    private void showIncidentDetail(String incident_id) {
        TextView incident_text_id = (TextView) rootView.findViewById(R.id.incident_text_id);
        TextView incident_text_titre = (TextView) rootView.findViewById(R.id.incident_text_titre);
        TextView incident_text_date = (TextView) rootView.findViewById(R.id.incident_text_date);
        TextView incident_text_type_incident = (TextView) rootView.findViewById(R.id.incident_text_type_incident);

        DbHelper mLocalDatabase = new DbHelper(getActivity().getApplication());
        IncidentDB mLocalIncident = mLocalDatabase.getIncidentData(Integer.valueOf(incident_id));

        incident_text_id.setText(mLocalIncident.getString("incident_id"));
        incident_text_titre.setText(mLocalIncident.getString("incident_titre"));
        incident_text_date.setText(mLocalIncident.getString("incident_date"));
        incident_text_type_incident.setText(mLocalIncident.getString("incident_type_id"));

    }

}
