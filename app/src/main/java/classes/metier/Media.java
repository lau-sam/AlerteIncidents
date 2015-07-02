package classes.metier;

import java.net.URL;

public class Media {
    private int id;
    private TypeMedia type;
    private String nom;
    private String description;
    private URL url;
    private String user;

    public Media(){
        this.id=-1;
        this.description=null;
        this.nom=null;
        this.url=null;
        this.type=null;
        this.user=null;
    }

    public Media(TypeMedia t, URL u, String us, float la, float lo){
        this.id=-1;
        this.type=t;
        this.nom="";
        this.description="";
        this.url=u;
        this.user=us;
    }

    public Media(TypeMedia t, String n, String d, URL u, String us, float la, float lo){
        this.id=-1;
        this.type=t;
        this.nom=n;
        this.description=d;
        this.url=u;
        this.user=us;
    }

    public Media(Media m){
        this.id=-1;
        this.type=m.type;
        this.nom=m.nom;
        this.description=m.description;
        this.url=m.url;
        this.user=m.user;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int i){
        this.id=i;
    }

    public TypeMedia getType(){
        return this.type;
    }

    public String getNom(){
        return this.nom;
    }

    public String getDescription(){
        return this.description;
    }

    public URL getURL(){
        return this.url;
    }

    public String getUser(){
        return this.user;
    }

    public void setUser(String u){
        this.user=u;
    }

    public void setType(TypeMedia t){
        this.type=t;
    }

    public void setNom(String n){
        this.nom=n;
    }

    public void setDescription(String d){
        this.description=d;
    }

    public void setURL(URL u){
        this.url=u;
    }

    public void set(TypeMedia t, String n, String d, URL u){
        this.type=t;
        this.nom=n;
        this.description=d;
        this.url=u;
    }
}
