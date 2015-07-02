package fr.epsi.alerteincidents;

/**
 * Created by Sam Lau on 30/06/2015.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import classes.metier.IncidentDB;
import fr.epsi.database.DbHelper;

public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.ViewHolder> {
    private ArrayList<IncidentDB> mDataset;
    private Activity currentActivity;
    private boolean backClicked;

    private Fragment fragment;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public ImageView typeIncidentImg;
        public TextView txtTitre;
        public TextView txtDate;
        public ViewHolder(View v) {
            super(v);
            //v.findViewById(R.id.historiqueTitreList).setOnClickListener(this);
            txtTitre = (TextView) v.findViewById(R.id.historiqueTitreList);
            txtDate = (TextView) v.findViewById(R.id.historiqueDateList);
            typeIncidentImg = (ImageView) v.findViewById(R.id.historiqueIcon);
        }
    }

    public void add(int position, IncidentDB item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(IncidentDB item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoriqueAdapter(ArrayList<IncidentDB> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoriqueAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_historique_item_adapter, parent, false);

        //Click on item
        v.setOnClickListener(new MyCustomOnclick());

        // set the view's size, margins, paddings and layout parameters
        final ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    class MyCustomOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            backClicked = false;
            RecyclerView mRecyclerView = new RecyclerView(v.getContext());
            int itemPosition = mRecyclerView.getChildPosition(v);
            IncidentDB mLocalIncident = mDataset.get(itemPosition);

            Bundle args = new Bundle();
            args.putString("incident_id", mLocalIncident.getString(DbHelper.COLUMN_INCIDENT_ID));
            fragment = new ModificationHistoriqueFragment();
            fragment.setArguments(args);
            FragmentManager fm = ((Activity) v.getContext()).getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame_modificationHistorique, fragment);
            ft.addToBackStack(null);
            ft.commit();

            FrameLayout mFrameLayout = (FrameLayout) ((Activity) v.getContext()).findViewById(R.id.frame_modificationHistorique);//findViewById(R.layout.fragment_modification_historique);
            if(!mFrameLayout.isActivated()){
                mFrameLayout.setVisibility(View.VISIBLE);
            }

            android.support.v7.widget.RecyclerView mRView = (android.support.v7.widget.RecyclerView)
                    ((Activity) v.getContext()).findViewById(R.id.my_recycler_view);
            mRView.setVisibility(View.GONE);

            //gestion bouton retour du fragment
            ((Activity) v.getContext()).findViewById(R.id.frame_modificationHistorique).setFocusableInTouchMode(true);
            ((Activity) v.getContext()).findViewById(R.id.frame_modificationHistorique).requestFocus();
            ((Activity) v.getContext()).findViewById(R.id.frame_modificationHistorique).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
                            && !backClicked) {
                        android.support.v7.widget.RecyclerView mRView = (android.support.v7.widget.RecyclerView)
                                ((Activity) v.getContext()).findViewById(R.id.my_recycler_view);
                        mRView.setVisibility(View.VISIBLE);
                        FragmentManager fm = ((Activity) v.getContext()).getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.detach(fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                        backClicked = true;
                        return true;
                    }
                    return false;
                }
            });

        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final IncidentDB mLocalIncident = mDataset.get(position);

        holder.txtTitre.setText("Titre : " + mLocalIncident.getString(DbHelper.COLUMN_INCIDENT_TITRE));
        holder.txtDate.setText("Date : " + mLocalIncident.getString(DbHelper.COLUMN_INCIDENT_DATE));

        if(mLocalIncident.getString(DbHelper.COLUMN_INCIDENT_TYPE_ID).equals("1"))
        {
            holder.typeIncidentImg.setImageResource(R.drawable.inondation);
        }
        else {
            if (mLocalIncident.getString(DbHelper.COLUMN_INCIDENT_TYPE_ID).equals("2"))
                holder.typeIncidentImg.setImageResource(R.drawable.incendie);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}