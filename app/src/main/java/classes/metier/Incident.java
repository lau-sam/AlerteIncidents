package classes.metier;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Incident {

    private Integer idIncident;

    private String dateIncident;

    private String user;

    private String titreIncident;

    @SerializedName("typeIncidentObject")
    private transient TypeIncident typeIncident;

    @SerializedName("typeIncident")
    private int idTypeIncident;

    private String descriptionIncident;

    private double latitude;

    private double longitude;

    private ArrayList<Media> listeMedia;

    private ArrayList<Avis> listeAvis;

    private ArrayList<Commentaire> listeCommentaire;

    public Incident(){
        this.idIncident = null;
        this.dateIncident =null;
        this.descriptionIncident =null;
        this.latitude=-1;
        this.longitude=-1;
        this.listeAvis=null;
        this.listeCommentaire=null;
        this.titreIncident=null;
        this.user=null;
    }

    public Incident(String user, String titre, TypeIncident typeIncident, String descriptionIncident,
                    double latitude, double longitude, String d) {
        this.idIncident = null;
        this.dateIncident = d;
        this.user = user;
        this.titreIncident = titre;
        this.typeIncident = typeIncident;
        this.idTypeIncident = typeIncident.getId();
        this.descriptionIncident = descriptionIncident;
        this.latitude = latitude;
        this.longitude = longitude;
        this.listeMedia = new ArrayList<Media>();
        this.listeAvis = new ArrayList<Avis>();
        this.listeCommentaire = new ArrayList<Commentaire>();
    }

    /**
     * Constructeur complet avec liste des mÃˆdias recopiÃˆe.
     */
    public Incident(String user, String titre, TypeIncident typeIncident, String descriptionIncident,
                    double latitude, double longitude, ArrayList<Media> listeMedia, String d) {
        this(user, titre, typeIncident, descriptionIncident, latitude, longitude, d);
        for(int i=0; i<listeMedia.size(); i++)
            this.listeMedia.add(listeMedia.get(i));
    }

    public Incident(Incident incident) {
        this.idIncident = incident.idIncident;
        this.dateIncident = incident.dateIncident;
        this.user = incident.user;
        this.titreIncident = incident.titreIncident;
        this.typeIncident = incident.typeIncident;
        this.descriptionIncident = incident.descriptionIncident;
        this.latitude = incident.latitude;
        this.longitude = incident.longitude;
        this.listeMedia = new ArrayList<Media>();
        for(int i=0; i<listeMedia.size(); i++)
            this.listeMedia.add(listeMedia.get(i));
        this.listeAvis = new ArrayList<Avis>();
        for(int j=0; j<listeAvis.size(); j++)
            this.listeAvis.add(listeAvis.get(j));
        this.listeCommentaire = new ArrayList<Commentaire>();
        for(int k=0; k<listeMedia.size(); k++)
            this.listeCommentaire.add(listeCommentaire.get(k));
    }

    public int getIdIncident() {
        return this.idIncident;
    }

    public void setIdIncident(int i){
        this.idIncident =i;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String u){
        this.user=u;
    }

    public String getTitreIncident() {
        return this.titreIncident;
    }

    public TypeIncident getTypeIncident() {
        return this.typeIncident;
    }

    public String getDescriptionIncident() {
        return this.descriptionIncident;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public ArrayList<Media> getListeMedia() {
        return this.listeMedia;
    }

    public ArrayList<Avis> getListeAvis() {
        return this.listeAvis;
    }

    public ArrayList<Commentaire> getListeCommentaire() {
        return this.listeCommentaire;
    }

    public String getDateIncident(){
        return this.dateIncident;
    }

    public void setTitreIncident (String titre) {
        this.titreIncident = titre;
    }

    public void setTypeIncident(TypeIncident typeIncident) {
        this.typeIncident = typeIncident;
    }

    public void setDescriptionIncident(String descriptionIncident) {
        this.descriptionIncident = descriptionIncident;
    }

    public void setLatitude (double latitude){
        this.latitude = latitude;
    }

    public void setLongitude (double longitude){
        this.longitude = longitude;
    }

    public void setPosition (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setDateIncident(String dateIncident){
        this.dateIncident = dateIncident;
    }

    public void addMedia(Media m) {
        this.listeMedia.add(m);
    }

    public void deleteMedia(Media m) {
        this.listeMedia.remove(m);
    }

    public void addAvis(Avis a) {
        this.listeAvis.add(a);
    }

    public void deleteAvis(Avis a) {
        this.listeAvis.remove(a);
    }

    public void addCommentaire(Commentaire c) {
        this.listeCommentaire.add(c);
    }

    public void deleteCommentaire(Commentaire c) {
        this.listeCommentaire.remove(c);
    }
}