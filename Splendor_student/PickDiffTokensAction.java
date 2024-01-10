
/**
 * Classe représentant l'action de prendre 3 jetons différents
 */
public class PickDiffTokensAction implements Action
{

    /**
     * Constructeur d'objets de classe PickDiffTokensAction
     */
    public PickDiffTokensAction()
    {
    }

    /**
     * ajoute les jetons dans la liste de jetons du joueur
     */
    public void process(Player p, Resource a, Resource b, Resource c, DevCard card)
    {
        p.updateNbResource(a,1);
        p.updateNbResource(b,1);
        p.updateNbResource(c,1);
        
    }
    
    /**
     * Retourne une représentation sous la forme d'une chaîne de caractères de l'action PickDiffTokensAction
     */
    public String toString(Player p){
        return p.getNom() + " a pris 3 jetons différents.";
    }
}
