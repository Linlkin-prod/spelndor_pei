/*
* Classe représentant une carte
*/
public class DevCard implements Displayable {
    
    private int tier; //Tier de la carte
    private Resources cout; //tableau des ressources représentant le prix de la carte
    private int points; //Points de prestige de la carte
    private Resource resourceType; //Type de ressource de la carte
    
    
    /**
     * Concstructeur par défaut d'une carte
     */
    public DevCard(){
        
    }
    
    /* 
     * Constructeur d'une carte
     */
    public DevCard(int tier, int coutD, int coutS, int coutE, int coutR, int coutO, int points, Resource type){
        this.tier = tier;
        cout = new Resources();
        cout.updateNbResource(Resource.DIAMOND, coutD);
        cout.updateNbResource(Resource.SAPPHIRE, coutS);
        cout.updateNbResource(Resource.EMERALD, coutE);
        cout.updateNbResource(Resource.RUBY, coutR);
        cout.updateNbResource(Resource.ONYX, coutO);
        this.points=points;
        resourceType=type;
        
    }
    
    /*
     * Accesseur du tier de la carte
     */
    public int getTier(){
        return tier;
    }
    
    /*
     * Accesseur du cout de la carte
     */
    public Resources getCost(){
        return cout;
    }
    
    /*
     * Accesseur des points de la carte
     */
    public int getPoints(){
        return points;
    }
    
    /*
     * Accesseur du type de la carte
     */
    public Resource getType(){
        return resourceType;
    }
    
    public String[] toStringArray(){
        /** EXAMPLE
         * ┌────────┐
         * │①    ♠S│
         * │        │
         * │        │
         * │2 ♠S    │
         * │2 ♣E    │
         * │3 ♥R    │
         * └────────┘
         */
        String pointStr = "  ";
        
        if(getPoints()>0){
            pointStr = Character.toString(getPoints()+9311);
        }
        String[] cardStr = {"\u250C\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2510",
                            "\u2502"+pointStr+"    "+resourceType.toSymbol()+"\u2502",
                            "\u2502        \u2502",
                            "\u2502        \u2502",
                            "\u2502        \u2502",
                            "\u2502        \u2502",
                            "\u2502        \u2502",
                            "\u2514\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2518"};
        //update cost of the repr
        int i=6;
        for(Resource res : Resource.values()){ //-- parcourir l'ensemble des resources (res)en utilisant l'énumération Resource
            if(getCost().getNbResource(res)>0){
                cardStr[i] = "\u2502"+getCost().getNbResource(res)+" "+res.toSymbol()+"    \u2502";
                i--;
            }
        } 
        return cardStr;
    }

    public static String[] noCardStringArray(){
        /** EXAMPLE
         * ┌────────┐
         * │ \    / │
         * │  \  /  │
         * │   \/   │
         * │   /\   │
         * │  /  \  │
         * │ /    \ │
         * └────────┘
         */
        String[] cardStr = {"\u250C\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2510",
                            "\u2502 \\    / \u2502",
                            "\u2502  \\  /  \u2502",
                            "\u2502   \\/   \u2502",
                            "\u2502   /\\   \u2502",
                            "\u2502  /  \\  \u2502",
                            "\u2502 /    \\ \u2502",
                            "\u2514\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2518"};
        
        return cardStr;
    }

    public String toString(){
        String cardStr = "";
              
        cardStr = getPoints()+"pts, type "+resourceType.toSymbol()+" | coût: ";
        for(Resource res : Resource.values()){ //-- parcourir l'ensemble des resources (res) en utilisant l'énumération Resource
            if(getCost().getNbResource(res)>0){
                cardStr += getCost().getNbResource(res)+res.toSymbol()+" ";
            }
        }
        
        return cardStr;
    }
}