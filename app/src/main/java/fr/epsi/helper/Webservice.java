package fr.epsi.helper;

import com.google.android.gms.maps.model.LatLng;

public class Webservice {
	private String url;
	final static String SERVER_ADRESS = "http://perso.montpellier.epsi.fr/~samuel.thery";
	
	public Webservice(){
		this.url = "";
	}
	
	public String getUrl(){
		return this.url;
	}
	
	//param : id du type d'incident
	public String getIncidentsByType(int typeId){
		this.url += SERVER_ADRESS + "/incidents/";
		this.url += "type/";
		this.url += typeId;
		return this.url;
	}
	
	//param : latlng le couple latitude-longitude, rayon le rayon de la zone
	//mettre 0 pour la valeur par defaut. Rayon par defaut = 10
	public String getIncidentByLatLng(LatLng latlng, int rayon){
		this.url += SERVER_ADRESS + "/incidents/";
		this.url += String.valueOf(latlng.latitude)+"/"+String.valueOf(latlng.longitude);
		if (rayon != 0){
			this.url += "/"+String.valueOf(rayon);
		}
		return this.url;
	}
	
	//param : id de l'incident
	public String getIncidentById(int id){
		this.url += SERVER_ADRESS + "/incident/";
		this.url += String.valueOf(id);
		return this.url;
	}
	
	//param : imei de l'auteur /incidents/parIMEI/[var imei]
	public String getIncidentsByImei(String imei){
		this.url += SERVER_ADRESS + "/incidents/";
		this.url += "parIMEI/";
		this.url += imei;
		return this.url;
	}
	
	//param : id de l'incident
	public String getMediasByIncidentId(int id){
		this.url += SERVER_ADRESS + "/incident/";
		this.url += String.valueOf(id);
		this.url += "/medias";
		return this.url;
	}
	
	//param : id du media
	public String getMediaById(int id){
		this.url += SERVER_ADRESS + "/media/";
		this.url += String.valueOf(id);
		return this.url;
	}
	
	//param : id du type du media
	public String getMediasByType(int typeId){
		this.url += SERVER_ADRESS + "/media/";
		this.url += "type/";
		this.url += typeId;
		return this.url;
	}
	
	//param : id de l'incident
	public String getCommentairesByIncidentId(int id){
		this.url += SERVER_ADRESS + "/incident/";
		this.url += String.valueOf(id);
		this.url += "/commentaires";
		return this.url;
	}
	
	//param : id du commentaire
	public String getCommentaireById(int id){
		this.url += SERVER_ADRESS + "/commentaire/";
		this.url += String.valueOf(id);
		return this.url;
	}
	
	//param : id de l'incident
	public String getAvisByIncidentId(int id){
		this.url += SERVER_ADRESS + "/incident/";
		this.url += String.valueOf(id);
		this.url += "/avis";
		return this.url;
	}
	
	//param : id de l'avis
	public String getAvisById(int id){
		this.url += SERVER_ADRESS + "/avis/";
		this.url += String.valueOf(id);
		return this.url;
	}
}
