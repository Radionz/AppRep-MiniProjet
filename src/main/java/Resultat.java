import java.io.Serializable;

/**
 * Created by Dorian on 04/04/2016.
 */
public class Resultat implements Serializable {

    public int entier;
    public String chaine;

    @Override
    public String toString() {
        return "Résultat: " + entier + " -- " + chaine;
    }
}
