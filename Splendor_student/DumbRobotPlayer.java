import java.util.List;
import java.util.Collections;

public class DumbRobotPlayer extends Player {

    public DumbRobotPlayer(int id, String name) {
        super(id, name);
    }

    /**
     * Méthode permettant au robot de choisir l'action à réaliser selon l'ordre :
     *  1 - acheter une carte sur le plateau (en commençant par celle de plus haut niveau)
     *  2 - acheter deux jetons ressources de même type
     *  3 - acheter des jetons ressources de type différents
     *  4 - passer son tour
     * 
     */
    public Action chooseAction(Board plateau) {
        // Essayer d'acheter une carte sur le plateau
        if (verification(plateau, new BuyCardAction())==true){
            return new BuyCardAction();
        }
        
        // tente d'acheter deux jetons de même type
        if (verification(plateau, new PickSameTokensAction())==true){
            return new PickSameTokensAction();
        }

        // tente d'acheter des jetons de types différents
        if (verification(plateau, new PickDiffTokensAction())==true){
            return new PickDiffTokensAction();
        }

        // Si aucune des actions n'est possible, passe son tour
        return new PassAction();
    }

    
    public Resources chooseDiscardingTokens(int i) {
        // Sélection aléatoire des ressources à défausser
        List<Resource> availableResources = getAvailableResources();
        Collections.shuffle(availableResources); // Mélange les jetons

        Resources discardedResources = new Resources(); //Liste des jetons a retirer
        for (int j = 0; j < i && j < availableResources.size(); j++) {
            //On rajoute un jeton à retirer
            Resource chosenResource = availableResources.get(j);
            discardedResources.updateNbResource(chosenResource, 1);
        }
        //Cas où les plus de 10 jetons sont seulement répartis en 2 types de ressources
        if(availableResources.size()==2 && i ==3){
            if(getNbResources(availableResources.get(0))>1){
                discardedResources.updateNbResource(availableResources.get(0), 1);
            }else{
                discardedResources.updateNbResource(availableResources.get(1), 1);
            }
        }

        return discardedResources;
    }
}