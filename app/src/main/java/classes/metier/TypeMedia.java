package classes.metier;

public class TypeMedia {

    private int id;
    private String nom;
    private String description;

    public TypeMedia(){
        this.id=-1;
        this.nom=null;
        this.description=null;
    }

    public TypeMedia(String n){
        this.id=-1;
        this.nom=n;
        this.description="";
    }

    public TypeMedia(String n, String d){
        this.id=-1;
        this.nom=n;
        this.description=d;
    }

    public TypeMedia(TypeMedia t){
        this.id=-1;
        this.nom=t.nom;
        this.description=t.description;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int i){
        this.id=i;
    }

    public String getNom(){
        return this.nom;
    }

    public String getDescription(){
        return this.description;
    }

    public void setNom(String n){
        this.nom=n;
    }

    public void setDescription(String d){
        this.description=d;
    }

    public void set(String n, String d){
        this.nom=n;
        this.description=d;
    }
}