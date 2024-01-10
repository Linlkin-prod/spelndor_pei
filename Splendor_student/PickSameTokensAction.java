
/**
 * Classe représentant l'action de prendre 2 fois le même jeton
 */
public class PickSameTokensAction implements Action
{

    /**
     * Constructeur d'objets de classe PickSameTokensAction
     */
    public PickSameTokensAction()
    {}

    /**
     * ajoute deux fois le même jeton chez le joueur
     */
    public void process(Player p, Resource a, Resource b, Resource c, DevCard card)
    {
        p.updateNbResource(a,2);
        
    }
    
    /**
     * Retourne une représentation sous la forme d'une chaîne de caractères de l'action PickSameTokensAction
     */
    public String toString(Player p){
        return p.getNom() + " a pris 2 jetons identiques.";
    }
}
