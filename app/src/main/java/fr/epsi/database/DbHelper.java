package fr.epsi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import classes.metier.Incident;
import classes.metier.IncidentDB;

public class DbHelper extends SQLiteOpenHelper{

	//list of tables
	public static final String NOM_TABLE_INCIDENT = "Incident";
	public static final String NOM_TABLE_HLOC = "Hloc";

	//name of db
	public static final String DATABASE_NAME = "DB_AI.db";
	//version
	public static int DATABASE_VERSION = 8;
	// Database creation sql statement

	// Columns Incident Table
	public static final String COLUMN_INCIDENT_ID = "incident_id";
	public static final String COLUMN_INCIDENT_DATE = "incident_date";
	public static final String COLUMN_INCIDENT_TITRE = "incident_titre";
	public static final String COLUMN_INCIDENT_LONGITUDE = "incident_longitude";
	public static final String COLUMN_INCIDENT_LATITUDE = "incident_latitude";
	public static final String COLUMN_INCIDENT_TYPE_ID = "incident_type_id";
	public static final String COLUMN_INCIDENT_DESCRIPTION = "incident_description";
	public static final String COLUMN_INCIDENT_USER = "incident_user";


	// Columns Historique Local Table
	public static final String COLUMN_HLOC_ID = "hloc_id";
	public static final String COLUMN_HLOC_ADRESSE = "hloc_adresse";
	public static final String COLUMN_HLOC_DATE = "hloc_date";
	public static final String COLUMN_HLOC_TITRE = "hloc_titre";
	public static final String COLUMN_HLOC_LATITUDE = "hloc_latitude";
	public static final String COLUMN_HLOC_LONGITUDE = "hloc_longitude";
	public static final String COLUMN_HLOC_TYPE_ID = "hloc_type_id";
	public static final String COLUMN_HLOC_DESCRIPTION = "hloc_description";
	public static final String COLUMN_HLOC_USER = "hloc_user";



	//constructeur avec un context
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(
				"CREATE TABLE Incident ("
						+ "incident_id INTEGER PRIMARY KEY," + "incident_date TEXT,"
						+ "incident_titre TEXT," + "incident_longitude TEXT,"
						+ "incident_latitude TEXT," + " incident_type_id TEXT,"
						+ "incident_description TEXT," + " incident_user TEXT"
						+ " );"
		);

		database.execSQL(
				"CREATE TABLE Hloc ("
						+ "hloc_id INTEGER PRIMARY KEY," + "hloc_adresse TEXT," + "hloc_date TEXT,"
						+ "hloc_titre TEXT," + "hloc_longitude TEXT,"
						+ "hloc_latitude TEXT," + " hloc_type_id TEXT,"
						+ "hloc_description TEXT," + " hloc_user TEXT"
						+ " );"
		);


	}

	//upgrade db
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(
				"DROP TABLE IF EXISTS " + NOM_TABLE_INCIDENT + ";"
		);

		db.execSQL(
				"DROP TABLE IF EXISTS " + NOM_TABLE_HLOC + ";"
		);
		onCreate(db);
	}

	/**INCIDENT HELPERS*/
	public boolean insertIncident (String incident_date, String incident_titre,
								   String incident_longitude,String incident_latitude,
								   String type_incident_id, String incident_description,
								   String incident_user)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("incident_date", incident_date);
		contentValues.put("incident_titre", incident_titre);
		contentValues.put("incident_longitude", incident_longitude);
		contentValues.put("incident_latitude", incident_latitude);
		contentValues.put("incident_type_id", type_incident_id);
		contentValues.put("incident_description", incident_description);
		contentValues.put("incident_user", incident_user);
		db.insert("Incident", null, contentValues);
		return true;
	}

	public IncidentDB getIncidentData(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor =  db.query(NOM_TABLE_INCIDENT, new String[] { COLUMN_INCIDENT_ID,
						COLUMN_INCIDENT_DATE, COLUMN_INCIDENT_LATITUDE,COLUMN_INCIDENT_LONGITUDE,
						COLUMN_INCIDENT_TITRE, COLUMN_INCIDENT_TYPE_ID, COLUMN_INCIDENT_DESCRIPTION, COLUMN_INCIDENT_USER}, COLUMN_INCIDENT_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		IncidentDB mLocalIncident = new IncidentDB();
		mLocalIncident.setString("incident_id",cursor.getString(0));
		mLocalIncident.setString("incident_date",cursor.getString(1));
		mLocalIncident.setString("incident_latitude",cursor.getString(2));
		mLocalIncident.setString("incident_longitude",cursor.getString(3));
		mLocalIncident.setString("incident_titre",cursor.getString(4));
		mLocalIncident.setString("incident_type_id",cursor.getString(5));
		mLocalIncident.setString("incident_description",cursor.getString(6));
		mLocalIncident.setString("incident_user",cursor.getString(7));

		return mLocalIncident;
	}

	public int getNumberOfRowsIncident(){
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, NOM_TABLE_INCIDENT);
		return numRows;
	}
	public boolean updateIncident (Integer incident_id, String incident_date,String incident_titre,
								   String incident_longitude,String incident_latitude,
								   String type_incident_id, String incident_description,
								   String incident_user)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("incident_date", incident_date);
		contentValues.put("incident_titre", incident_titre);
		contentValues.put("incident_longitude", incident_longitude);
		contentValues.put("incident_latitude", incident_latitude);
		contentValues.put("incident_type_id", type_incident_id);
		contentValues.put("incident_description", incident_description);
		contentValues.put("incident_user", incident_user);
		db.update(NOM_TABLE_INCIDENT, contentValues, "id = ? ", new String[] { Integer.toString(incident_id) } );
		return true;
	}

	public Integer deleteIncident (Integer incident_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(NOM_TABLE_INCIDENT,"id = ? ",new String[] { Integer.toString(incident_id) });
	}

	public ArrayList<IncidentDB> getAllIncidents()
	{
		ArrayList<IncidentDB> array_list = new ArrayList<IncidentDB>();
		//hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery( "SELECT * FROM "+ NOM_TABLE_INCIDENT, null );
		res.moveToFirst();

		while(res.isAfterLast() == false){
			IncidentDB incident_item = new IncidentDB();
			String idIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_ID));
			String titreIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_TITRE));
			String dateIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_DATE));
			String latIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_LATITUDE));
			String lngIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_LONGITUDE));
			String idTypeIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_TYPE_ID));
			String descIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_DESCRIPTION));
			String userIncident = res.getString(res.getColumnIndex(COLUMN_INCIDENT_USER));

			incident_item.setString(DbHelper.COLUMN_INCIDENT_ID,
					String.valueOf(idIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_TITRE,
					String.valueOf(titreIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_DATE,
					String.valueOf(dateIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_LATITUDE,
					String.valueOf(latIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_LONGITUDE,
					String.valueOf(lngIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_TYPE_ID,
					String.valueOf(idTypeIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_DESCRIPTION,
					String.valueOf(descIncident));
			incident_item.setString(DbHelper.COLUMN_INCIDENT_USER,
					String.valueOf(userIncident));

			array_list.add(incident_item);
			res.moveToNext();
		}
		return array_list;
	}
/** EOF INCIDENT HELPERS*/

	/** HLOC HELPERS*/
	/*
	insertHloc(...){}
	updateHloc(...){}
	deleteHloc(...){}
	getAllHloc(...){}
	getHloc(...){}
	getNumberOfRowsHloc(...){}
	*/

	public boolean insertHloc (String hloc_adresse,
							   String hloc_date,
							   String hloc_titre,
							   String hloc_longitude,
							   String hloc_latitude,
							   String hloc_type_id,
							   String hloc_description,
							   String hloc_user)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("hloc_adresse", hloc_adresse);
		contentValues.put("hloc_date", hloc_date);
		contentValues.put("hloc_titre", hloc_titre);
		contentValues.put("hloc_longitude", hloc_longitude);
		contentValues.put("hloc_latitude", hloc_latitude);
		contentValues.put("hloc_type_id", hloc_type_id);
		contentValues.put("hloc_description", hloc_description);
		contentValues.put("hloc_user", hloc_user);
		db.insert("Hloc", null, contentValues);
		Log.v("===DBHELPER","INSERT HLOC");
		return true;
	}

	public boolean updateHloc (Integer hloc_id,
							   String hloc_adresse,
							   String hloc_date,
							   String hloc_titre,
							   String hloc_longitude,
							   String hloc_latitude,
							   String hloc_type_id,
							   String hloc_description,
							   String hloc_user
	)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("hloc_adresse", hloc_adresse);
		contentValues.put("hloc_date", hloc_date);
		contentValues.put("hloc_titre", hloc_titre);
		contentValues.put("hloc_longitude", hloc_longitude);
		contentValues.put("hloc_latitude", hloc_latitude);
		contentValues.put("hloc_type_id", hloc_type_id);
		contentValues.put("hloc_description", hloc_description);
		contentValues.put("hloc_user", hloc_user);
		db.update(NOM_TABLE_HLOC, contentValues, "id = ? ", new String[]{Integer.toString(hloc_id)});
		return true;
	}

	public IncidentDB getHloc(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor =  db.query(NOM_TABLE_HLOC, new String[]{COLUMN_HLOC_ID, COLUMN_HLOC_ADRESSE,
						COLUMN_HLOC_DATE, COLUMN_HLOC_LATITUDE, COLUMN_HLOC_LONGITUDE,
						COLUMN_HLOC_TITRE, COLUMN_HLOC_TYPE_ID, COLUMN_HLOC_DESCRIPTION, COLUMN_HLOC_USER},
				COLUMN_HLOC_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		IncidentDB mLocalIncident = new IncidentDB();
		mLocalIncident.setString("hloc_id", cursor.getString(0));
		mLocalIncident.setString("hloc_adresse", cursor.getString(1));
		mLocalIncident.setString("hloc_date", cursor.getString(2));
		mLocalIncident.setString("hloc_latitude", cursor.getString(3));
		mLocalIncident.setString("hloc_longitude", cursor.getString(4));
		mLocalIncident.setString("hloc_titre", cursor.getString(5));
		mLocalIncident.setString("hloc_type_id", cursor.getString(6));
		mLocalIncident.setString("hloc_description", cursor.getString(7));
		mLocalIncident.setString("hloc_user", cursor.getString(8));

		return mLocalIncident;
	}

	/** EOF HLOC HELPERS*/

	public static List<IncidentDB> incidentsToIncidentsDB(List <Incident> incidents){
		List<IncidentDB> incidentsDB = new ArrayList<>();
		for(Incident i : incidents){
			IncidentDB iDB = new IncidentDB();
			iDB.setString(DbHelper.COLUMN_INCIDENT_ID,
					String.valueOf(i.getIdIncident()));
			iDB.setString(DbHelper.COLUMN_INCIDENT_TITRE,
					String.valueOf(i.getTitreIncident()));
			iDB.setString(DbHelper.COLUMN_INCIDENT_DATE,
					String.valueOf(i.getDateIncident()));
			iDB.setString(DbHelper.COLUMN_INCIDENT_LATITUDE,
					String.valueOf(i.getLatitude()));
			iDB.setString(DbHelper.COLUMN_INCIDENT_LONGITUDE,
					String.valueOf(i.getLongitude()));
			iDB.setString(DbHelper.COLUMN_INCIDENT_TYPE_ID,
					String.valueOf(i.getTypeIncident().getId()));
			iDB.setString(DbHelper.COLUMN_INCIDENT_DESCRIPTION,
					String.valueOf(i.getDescriptionIncident()));
			iDB.setString(DbHelper.COLUMN_INCIDENT_USER,
					String.valueOf(i.getUser()));
			incidentsDB.add(iDB);
		}
		return incidentsDB;
	}



}
