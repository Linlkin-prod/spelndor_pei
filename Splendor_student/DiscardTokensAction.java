
/**
 * Classe représentant l'action de défausser des jetons (en trop)
 */
public class DiscardTokensAction implements Action
{
    

    /**
     * Constructeur d'objets de classe DiscardTokensAction
     */
    public DiscardTokensAction()
    {
    }

    /**
     * retire les jetons voulus (max 3)
     */
    public void process(Player p, Resource a, Resource b, Resource c, DevCard card)
    {
        Resource[] tab = {a,b,c};
        for (Resource r : tab){
            if (r != null){
                p.updateNbResource(r,-1);
            }
        }
    }
    
    /**
     * Retourne une représentation sous la forme d'une chaîne de caractères de l'action DiscardTokensAction
     */
    public String toString(Player p){
        return p.getNom() + " a défaussé des jetons.";
    }
}
