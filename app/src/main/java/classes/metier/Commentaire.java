package classes.metier;

public class Commentaire {
    private int id;
    private String date;
    private Incident incident;
    private String user;
    private String texte;

    public Commentaire(){
        this.id=-1;
        this.incident=null;
        this.user=null;
        this.texte=null;
        this.date=null;
    }

    public Commentaire(Incident i, String us, String t, String d){
        this.id=-1;
        this.incident=i;
        this.user=us;
        this.texte=t;
        this.date=d;
    }

    public Commentaire(Commentaire c){
        this.id=c.id;
        this.incident=c.incident;
        this.user=c.user;
        this.texte=c.texte;
        this.date=c.date;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int i){
        this.id=i;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String d){
        this.date=d;
    }

    public String getUser(){
        return this.user;
    }

    public void setUser(String u){
        this.user=u;
    }

    public String getTexte(){
        return this.texte;
    }

    public void setTexte(String t){
        this.texte=t;
    }
}
