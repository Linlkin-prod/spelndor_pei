
/**
 * Classe représentant l'action de passer son tour
 */
public class PassAction implements Action
{

    /**
     * Constructeur d'objets de classe PassAction
     */
    public PassAction()
    {
    }

    /**
     * Le joueur passe son tour, on ne fait donc rien
     */
    public void process(Player p, Resource a, Resource b, Resource c, DevCard card){}
    
    /**
     * Retourne une représentation sous la forme d'une chaîne de caractères de l'action PassAction
     */
    public String toString(Player p){
        return p.getNom() + " a passé son tour.";
    }
}
