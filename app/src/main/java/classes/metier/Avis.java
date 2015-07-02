package classes.metier;

public class Avis {
    private int id;
    private Incident incident;
    private String user;

    public static enum VoteType {POSITIF, NEGATIF};

    private VoteType vote;

    public Avis() {
        this.id = -1;
        this.incident = null;
        this.user = null;
        this.vote = null;
    }

    public Avis(Incident i, String us, VoteType v) {
        this.id = -1;
        this.incident = i;
        this.user = us;
        this.vote = v;
    }

    public Avis(Avis a) {
        this.id = -1;
        this.incident = a.incident;
        this.user = a.user;
        this.vote = a.vote;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public Incident getIncident() {
        return this.incident;
    }

    public void setIncident(Incident i) {
        this.incident = i;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String u) {
        this.user = u;
    }

    public VoteType getVote() {
        return this.vote;
    }

    public void setVote(VoteType v) {
        this.vote = v;
    }
}