package classes.metier;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class TypeIncident {

    @SerializedName("idTypeIncident")
    private int id;
    private String nomType;
    private String descriptionType;

    public TypeIncident(){
        this.id=-1;
        this.nomType =null;
        this.descriptionType=null;
    }

    public TypeIncident(String nomType, String description) {
        this.id = -1;
        this.nomType = nomType;
        this.descriptionType = description;
    }

    public TypeIncident(int id, String nomType, String description) {
        this.id = id;
        this.nomType = nomType;
        this.descriptionType = description;
    }

    public TypeIncident(String nomType) {
        this.id = -1;
        this.nomType = nomType;
        this.descriptionType = "";
    }

    public TypeIncident(int id, String nomType) {
        this.id = id;
        this.nomType = nomType;
        this.descriptionType = "";
    }

    public TypeIncident(TypeIncident t) {
        this.id = t.id;
        this.nomType = t.nomType;
        this.descriptionType = t.descriptionType;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i){
        this.id=i;
    }

    public String getNomType() {
        return this.nomType;
    }

    public String getDescription() {
        return this.descriptionType;
    }

    public void setNomType(String nomType) {
        this.nomType = nomType;
    }

    public void setDescription(String description) {
        this.descriptionType = description;
    }

    @Override
    public String toString(){
        return this.getNomType();
    }

}
