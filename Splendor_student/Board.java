import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;


public class Board implements Displayable {
    
    private StackCard[] stackedCard = new StackCard[3];
    private DevCard[][] visibleCard = new DevCard[3][4];
    private Resources jetons;
    
    public Board (int numberOfPlayers) throws FileNotFoundException { 
        jetons = new Resources();

        for (int i=0;i<3;i++){
            stackedCard[i] = new StackCard();
        }
        initializeBoard(numberOfPlayers);
        initializeDevCards();
    }
    
    public void initializeBoard(int numberOfPlayers){
        /*  Pour initialiser les jetons au bon nombre
         *  en fonction du nombre de joueur
         */
        if (numberOfPlayers < 4){
            for (Resource r : Resource.values()){
                jetons.setNbResource(r,5);
            }
        }
        else{
            for (Resource r : Resource.values()){
                jetons.setNbResource(r,7);
            }
        }
    }


    public void initializeDevCards() throws FileNotFoundException{

        //initialise les stacks dans le array des stacks
        for (int i=0;i<stackedCard.length;i++){
            stackedCard[i].clearStack();
        }
        String[] cacheCardInfo = new String[8]; //un cache pour les statistique des differante carte du csv
        ArrayList<DevCard> cacheCardLists = new ArrayList<>(); //un cache pour les carte en elle même avant d'çetre emplilé
        try {
            //On utilise la classe BufferedReader pour lire le csv
            BufferedReader br = new BufferedReader(new FileReader("stats.csv"));
            Random r = new Random();
            String line;
            
            while((line = br.readLine()) != null){ //boucle principal, on lit chaque ligne tant qu'elle existe
                DevCard card = new DevCard();
                cacheCardInfo = line.split(",");
                try{
                    switch (cacheCardInfo[7]) { //on crée les cartes a partir des info de chacune des cartes
                        case "DIAMOND":
                            card = new DevCard(Integer.parseInt(cacheCardInfo[0]), Integer.parseInt(cacheCardInfo[1]), Integer.parseInt(cacheCardInfo[2]), Integer.parseInt(cacheCardInfo[3]), 
                                            Integer.parseInt(cacheCardInfo[4]), Integer.parseInt(cacheCardInfo[5]), Integer.parseInt(cacheCardInfo[6]), Resource.DIAMOND);

                            break;
                        case "SAPPHIRE":
                            card = new DevCard(Integer.parseInt(cacheCardInfo[0]), Integer.parseInt(cacheCardInfo[1]), Integer.parseInt(cacheCardInfo[2]), Integer.parseInt(cacheCardInfo[3]), 
                                            Integer.parseInt(cacheCardInfo[4]), Integer.parseInt(cacheCardInfo[5]), Integer.parseInt(cacheCardInfo[6]), Resource.SAPPHIRE);
                            break;
                        case "EMERALD":
                            card = new DevCard(Integer.parseInt(cacheCardInfo[0]), Integer.parseInt(cacheCardInfo[1]), Integer.parseInt(cacheCardInfo[2]), Integer.parseInt(cacheCardInfo[3]), 
                                            Integer.parseInt(cacheCardInfo[4]), Integer.parseInt(cacheCardInfo[5]), Integer.parseInt(cacheCardInfo[6]), Resource.EMERALD);
                            break;
                        case "ONYX":
                            card = new DevCard(Integer.parseInt(cacheCardInfo[0]), Integer.parseInt(cacheCardInfo[1]), Integer.parseInt(cacheCardInfo[2]), Integer.parseInt(cacheCardInfo[3]), 
                                            Integer.parseInt(cacheCardInfo[4]), Integer.parseInt(cacheCardInfo[5]), Integer.parseInt(cacheCardInfo[6]), Resource.ONYX);
                            break;
                        case "RUBY":
                            card = new DevCard(Integer.parseInt(cacheCardInfo[0]), Integer.parseInt(cacheCardInfo[1]), Integer.parseInt(cacheCardInfo[2]), Integer.parseInt(cacheCardInfo[3]), 
                                            Integer.parseInt(cacheCardInfo[4]), Integer.parseInt(cacheCardInfo[5]), Integer.parseInt(cacheCardInfo[6]), Resource.RUBY);
                            break;
                        default:
                            throw new Exception("/////// Exception not intended card //////");

                    }
                    try { //on mets les cartes dans l'arrayList tout en les mélangeants
                        cacheCardLists.add(r.nextInt(cacheCardLists.size()),card);
                    } catch (Exception e3) {
                        cacheCardLists.add(card);
                    }
            
                }
                catch (Exception e){
                    System.err.println("Not a Card or Noble Card, check the csv file : Type of card '"+cacheCardInfo[7]+"'");
                    System.out.println(e.getMessage());
                }   
            }
            try { //on empile les cartes dans les dans chacune des stacks
                for (int i=0; i<cacheCardLists.size();i++){
                    stackedCard[cacheCardLists.get(i).getTier()-1].addStack(cacheCardLists.get(i));
                }   
            } catch (Exception e1) {
                System.err.println(e1.getMessage()); 
            }
            br.close();   
        }
        catch(Exception e2){
            System.err.println(e2.getMessage()); 
            System.err.println("wrong file or something else");
        }
        for(int i=1;i<=3;i++){ //on mets les carte dans le top de la stack en jeux
            for(int j=1;j<=4;j++){
                updateCard(i, j);
            }
        }
    }

    private String[] deckToStringArray(int tier){
        /** EXAMPLE
         * ┌────────┐
         * │        │╲ 
         * │ reste: │ │
         * │   16   │ │
         * │ cartes │ │
         * │ tier 3 │ │
         * │        │ │
         * └────────┘ │
         *  ╲________╲│
         */

        int nbCards = stackedCard[tier-1].sizeStack();
        String[] deckStr = {"\u250C\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2510  ",
                            "\u2502        \u2502\u2572 ",
                            "\u2502 reste: \u2502 \u2502",
                            "\u2502   "+String.format("%02d", nbCards)+"   \u2502 \u2502",
                            "\u2502 carte"+(nbCards>1 ? "s" : " ")+" \u2502 \u2502",
                            "\u2502 tier "+tier+" \u2502 \u2502",
                            "\u2502        \u2502 \u2502",
                            "\u2514\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2518 \u2502",
                            " \u2572________\u2572\u2502"};
        return deckStr;
    }

    private String[] resourcesToStringArray(){
        /** EXAMPLE
         * Resources disponibles : 4♥R 4♣E 4♠S 4♦D 4●O
         */
        String[] resStr = {"Resources disponibles : "};
       
        for(Resource res : Resource.values() ){ //-- parcourir l'ensemble des resources (res) en utilisant l'énumération Resource
            resStr[0] += jetons.getNbResource(res)+res.toSymbol()+" ";
        }

        resStr[0] += "        ";
        return resStr;
    }

    private String[] boardToStringArray(){
        String[] res = Display.emptyStringArray(0, 0);


        //Deck display
        String[] deckDisplay = Display.emptyStringArray(0, 0);
        
        for(int i=3;i>0;i--){
            deckDisplay = Display.concatStringArray(deckDisplay, deckToStringArray(i), true);
        }

        //Card display
        String[] cardDisplay = Display.emptyStringArray(0, 0);
        for(int i=2;i>=0;i--){ //-- parcourir les différents niveaux de carte (i)
            String[] tierCardsDisplay = Display.emptyStringArray(8, 0);
            for(int j=0;j<4;j++){ //-- parcourir les 4 cartes faces visibles pour un niveau donné (j)
                tierCardsDisplay = Display.concatStringArray(tierCardsDisplay, visibleCard[i][j]!=null ? visibleCard[i][j].toStringArray() : DevCard.noCardStringArray(), false);
            }
            cardDisplay = Display.concatStringArray(cardDisplay, Display.emptyStringArray(1, 40), true);
            cardDisplay = Display.concatStringArray(cardDisplay, tierCardsDisplay, true);
        }
        
        res = Display.concatStringArray(deckDisplay, cardDisplay, false);
        res = Display.concatStringArray(res, Display.emptyStringArray(1, 52), true);
        res = Display.concatStringArray(res, resourcesToStringArray(), true);
        res = Display.concatStringArray(res, Display.emptyStringArray(35, 1, " \u250A"), false);
        res = Display.concatStringArray(res, Display.emptyStringArray(1, 54, "\u2509"), true);

        return res;
    }

    @Override
    public String[] toStringArray() {
        return boardToStringArray();
    }

    public int getNbResource(Resource type){
        return jetons.getNbResource(type);
    }

    public void updateNbResource(Resource type, int nbUp){
        jetons.updateNbResource(type, nbUp);
    }

    public ArrayList<Resource> getAvailableResources(){
        return jetons.getAvailableResources();
    }

    public DevCard getCard(int level, int column){
        return visibleCard[level-1][column-1];
    }

    public DevCard drawCard(int tier){
        return stackedCard[tier -1].popStack();
    }
    
    /**
     * Méthode permettant de retirer une carte du plateau et de la changer par une carte face cachée du même tier
     * tier et colonne sont les coordonnées de la carte sur le plateau
     */
    public void updateCard(int tier, int colonne){
        //On remplace la carte par une nouvelle carte du même tiers
        if(stackedCard[tier-1].sizeStack()>0){
            visibleCard[tier-1][colonne-1] = drawCard(tier);
        }
    }
    
    /**
     * Méthode permettant de savoir si l'on peut prendre 2 jetons de la ressource r
     */
    public boolean canGiveSameTokens(Resource r){
        if (getNbResource(r)>=4){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Méthode permettant de savoir si au moins 3 jetons différents sont disponibles sur le plateau
     */
    public boolean canGiveDiffTokens(){
        if(getAvailableResources().size()>=3){
            return true;
        }else{
            return false;
        }
    }
}