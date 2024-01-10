
/**
 * Classe représentant l'action d'acheter une carte
 */
public class BuyCardAction implements Action
{

    /**
     * Constructeur d'objets de classe BuyCardAction
     */
    public BuyCardAction()
    {
    }

    /**
     * ajoute la carte à la liste de cartes du joueur
     */
    public void process(Player p, Resource a, Resource b, Resource c, DevCard card)
    {
        
        for (Resource r : Resource.values()){
            //On ne retire les ressources que si le joueur n'a pas assez de cartes pour compenser
            if(p.getResFromCards(r)<=card.getCost().getNbResource(r)){
                p.updateNbResource(r,-card.getCost().getNbResource(r)+p.getResFromCards(r));
            }
            
        }
        p.addPurchasedCard(card);
    }
    
    /**
     * Retourne une représentation sous la forme d'une chaîne de caractères de l'action BuyCardAction
     */
    public String toString(Player p){
        return p.getNom() + " a acheté une carte";
    }
}
