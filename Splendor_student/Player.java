import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public abstract class Player implements Displayable {
    private int id;
    private String name;
    private int points;
    private List<DevCard> purchasedCards;
    private Resources resources;
    private Resources resourceCards;
    
    /*
     * Constructeur du joueur
     */
    public Player(int id, String name){
        this.id = id;
        this.name = name;
        this.points = 0;
        this.purchasedCards = new ArrayList<>();
        this.resources = new Resources();
        resourceCards = new Resources();
    }
    
    /*
     * Accesseur du nom du joueur
     */
    public String getNom(){
        return name;
    }
    
    /*
     * Accesseur du nombre de points du joueur
     */
    public int getPoints(){
        return points;
    }
    
    public String[] toStringArray(){
        /** EXAMPLE. The number of resource tokens is shown in brackets (), and the number of cards purchased from that resource in square brackets [].
         * Player 1: Camille
         * ⓪pts
         * 
         * ♥R (0) [0]
         * ●O (0) [0]
         * ♣E (0) [0]
         * ♠S (0) [0]
         * ♦D (0) [0]
         */
        String pointStr = " ";
        String[] strPlayer = new String[8];

        if(points>0){
            pointStr = Character.toString(points+9311);
        }else{
            pointStr = "\u24EA";
        }

        
        strPlayer[0] = "Player "+(id+1)+": "+name;
        strPlayer[1] = pointStr + "pts";
        strPlayer[2] = "";
        for (Resource res : Resource.values()) {
            strPlayer[3 + (Resource.values().length - 1 - res.ordinal())] =
            res.toSymbol() + " (" + resources.getNbResource(res) + ") [" + getResFromCards(res) + "]";
        }
        
        return strPlayer;
    }
    
    /*
     * Retourne le nombre total de cartes achetées par le joueur
     */
    public int getNBPurchasedCards(){
        return purchasedCards.size();
    }
    
    /*
     * Retourne le nombre de ressources achetées pour un type donné
     */
    public int getNbResources(Resource r){
        return resources.getNbResource(r);
    }
    
    /*
     * Retourne le nombre total de ressources achetées
     */
    public int getNbTokens() {
        int nbTotal = 0;
        for (Resource r : Resource.values()){
            nbTotal += getNbResources(r);
        }
        return nbTotal;
    }

    /*
     * Retourne la liste des ressources disponibles
     */
    public ArrayList<Resource> getAvailableResources() {
        return resources.getAvailableResources();
    }

    /*
     * Retourne le nombre de ressources d'un type donné 
     * présentes sur les cartes achetées
     */
    public int getResFromCards(Resource resourceType) {
        int total = 0;
        for(DevCard card : purchasedCards){
            if (card.getType().toString() == resourceType.toString()){
                total +=1;
            }
        }
        return total;
    }
    
    /*
     * Ajoute ou supprime une quantité de ressources
     */
    public void updateNbResource(Resource r, int v) {
        resources.updateNbResource(r,v);
    }
    
    /* 
     *  Incrémente le nombre de points prestige
     */
    public void updatePoints(int points) {
        this.points += points;
    }
    
    /*
     * Ajoute une carte à la liste des cartes achetées
     */
    public void addPurchasedCard(DevCard card) {
        purchasedCards.add(card);
        //Ajoute également dans le tableau de cartes
        resourceCards.updateNbResource(card.getType(), 1);
        //On actualise les points
        updatePoints(card.getPoints());
    }
    
    /*
     * Vérifie si le joueur a assez de ressources pour acheter une carte donnée
     */
    public boolean canBuyCard(DevCard card) {
        if (card!=null){
            for (Resource r : Resource.values()){
                int canBuy = card.getCost().getNbResource(r) - resources.getNbResource(r) - resourceCards.getNbResource(r);
                if (canBuy > 0){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * reçoit un plateau et une action, et retourne vrai si le joueur
     * peut effectuer cette action, faux sinon
     */
    public boolean verification(Board board, Action action)
    {
        // Essayer d'acheter une carte sur le plateau
        if(action.getClass().getSimpleName()=="BuyCardAction"){
            for(int i=1; i<=3;i++){
                for (int j=1; j<=4;j++){
                    if (canBuyCard(board.getCard(i,j))) {
                        //Au moins l'une des cartes est achetable
                        return true;
                    }
                }
            }
            return false;
        }
        
        ArrayList<Resource> availableResources = board.getAvailableResources();
        // tente d'acheter deux jetons de même type
        if(action.getClass().getSimpleName()=="PickSameTokensAction"){
            if (availableResources.size() >= 1) {
                //On vérifie que 2 jetons peuvent être retirés pour au moins une ressource
                for(int i = 0; i<availableResources.size(); i++){
                    Resource chosenResource = availableResources.get(i);
                    if (board.canGiveSameTokens(chosenResource)){
                        //C'est le cas pour au moins une
                        return true;
                    }
                }
            }
            return false;
        }

        // tente d'acheter des jetons de types différents
        if(action.getClass().getSimpleName()=="PickDiffTokensAction"){
            if (board.canGiveDiffTokens()) {
                return true;
            }
            return false;
        }

        // Si aucune des actions précédente n'est la bonne, alors le joueur passe son tour (forcément possible)
        return true;
    }
    
    /*
     * Méthode pour choisir une action
     */
    public abstract Action chooseAction(Board board);
    
    /*
     * Méthode pour choisir les ressources à défausser
     */
    public abstract Resources chooseDiscardingTokens(int i);
}
