import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.FileNotFoundException;
import java.util.Collections;

public class Game {
    /* L'affichage et la lecture d'entrée avec l'interface de jeu se fera entièrement via l'attribut display de la classe Game.
     * Celui-ci est rendu visible à toutes les autres classes par souci de simplicité.
     * L'intéraction avec la classe Display est très similaire à celle que vous auriez avec la classe System :
     *    - affichage de l'état du jeu (méthodes fournies): Game.display.outBoard.println("Nombre de joueurs: 2");
     *    - affichage de messages à l'utilisateur: Game.display.out.println("Bienvenue sur Splendor ! Quel est ton nom?");
     *    - demande d'entrée utilisateur: new Scanner(Game.display.in);
     */
    private static final int ROWS_BOARD=36, ROWS_CONSOLE=8, COLS=82;
    public static final  Display display = new Display(ROWS_BOARD, ROWS_CONSOLE, COLS);

    private Board board;
    private List<Player> players;

    public static void main(String[] args) throws IllegalArgumentException, FileNotFoundException{
        display.outBoard.clean();
        display.out.clean();
        
        
        display.outBoard.println("Bienvenue sur Splendor !");
        display.outBoard.println("Saisissez le nombre de joueurs pour votre partie : ");
        boolean saisie = false;
        while(saisie==false){
            //Tant que le joueur n'a pas saisi un entier, on recommence la saisie
            Scanner scan = new Scanner(display.in);
            try{
                int temp = scan.nextInt();
                Game game = new Game(temp); 
                game.play();
                //Une pause d'une minute, pour laisser le temps de voir le résultat
                try{
                    Thread.sleep(60000);
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                }
                display.close();
                saisie = true;
            }catch(InputMismatchException e){
                Game.display.outBoard.println("Veuillez saisir un entier.");
            }
            scan.close();
        }
        
    }

    public Game(int nbOfPlayers) throws IllegalArgumentException, FileNotFoundException{
        players = new ArrayList<Player>();
        String nom = "";
        if (nbOfPlayers>=2 && nbOfPlayers<=4){
            //On rajoute le joueur humain
            Scanner scan = new Scanner(display.in);
            display.outBoard.print("Saisissez votre nom : ");
            nom = scan.next();
            players.add(new HumanPlayer(0, nom));
            display.outBoard.println(nom);
            //On ajoute le reste en joueurs robots
            for(int i=1; i<nbOfPlayers;i++){
                players.add(new DumbRobotPlayer(i, "Robot " + i));
            }
            scan.close();
            //On réalise l'affichage du plateau
            board = new Board(nbOfPlayers);
        }else{
            throw new IllegalArgumentException();
        }
        
    }

    public int getNbPlayers(){
        return players.size();
    }

    private void display(int currentPlayer){
        String[] boardDisplay = board.toStringArray();
        String[] playerDisplay = Display.emptyStringArray(0, 0);
        for(int i=0;i<players.size();i++){
            String[] pArr = players.get(i).toStringArray();
            if(i==currentPlayer){
                pArr[0] = "\u27A4 " + pArr[0];
            }
            playerDisplay = Display.concatStringArray(playerDisplay, pArr, true);
            playerDisplay = Display.concatStringArray(playerDisplay, Display.emptyStringArray(1, COLS-54, "┉"), true);
        }
        String[] mainDisplay = Display.concatStringArray(boardDisplay, playerDisplay, false);

        display.outBoard.clean();
        display.outBoard.print(String.join("\n", mainDisplay));
    }

    public void play(){
        int indice = 0;
        //Tant que la partie n'est pas finie
        while(isGameOver()!=true){
            display.out.println("");
            //On remets l'indice entre 0 et le nombre maximal de joueurs, s'il est trop haut
            if (indice >=getNbPlayers()){
                indice -= getNbPlayers();
            }
            Player player = players.get(indice);
            display.out.println("Tour du joueur " + (indice+1) + ": " + player.getNom());
            display(indice);
            move(player);
            if(player.getNbTokens()>10){
                discardToken(player);
            }
            indice+=1;
        }
        
        display.out.println(" \n Dernier tour ! \n");
        //Le dernier joueur à jouer a gagné, on actualise ses points
        if (indice>=getNbPlayers()){
            display(getNbPlayers()-1);
        }
        
        //On termine le tour
        for (int i=indice ; i<getNbPlayers(); i++){
            display.out.println("");
            Player player = players.get(indice);
            display.out.println("Tour du joueur " + (indice+1) + ": " + player.getNom());
            display(indice);
            move(player);
            if(player.getNbTokens()>10){
                discardToken(player);
            }
            indice+=1;
        }

        //On termine la partie
        gameOver();
    }

    private void move(Player player){
        Action action = player.chooseAction(board);
        Scanner scan = new Scanner(Game.display.in);
        
        //Des booléens pour vérifier certaines conditions
        boolean verif = false;
        boolean verifLigne = false;
        boolean verifColonne = false;
        boolean verifRessource = false;
        
        Saisie saisie = new Saisie();
        int ligne =0;
        int colonne=0;
        String chaine = ""; //Chaine de caractère stockant les chaines entrées par le joueur humain
        
        //Le joueur est humain, et l'action n'est pas de simplement passer
        if(player.getClass().getSimpleName()=="HumanPlayer" && action.getClass().getSimpleName()!="PassAction"){
            if(action.getClass().getSimpleName()=="BuyCardAction"){
                //Le joueur veut acheter une carte 
                while(verif==false){
                    //Coordonnées de la ligne
                    while(verifLigne==false){
                        //On teste une saisie tant que ce n'est pas conforme
                        ligne = saisie.verifEntier(3,1, "tier");
                        if(ligne>0){
                            verifLigne=true;
                        }
                    }
                    
                    //Coordonnées de la colonne
                    while(verifColonne==false){
                        //On teste une saisie tant que ce n'est pas conforme
                        colonne = saisie.verifEntier(4,1, "colonne");
                        if(colonne>0){
                            verifColonne=true;
                        }
                    }
                    
                    //On vérifie que le joueur peut acheter la carte
                    if(player.canBuyCard(board.getCard(ligne,colonne))){
                        verif=true;
                        //On ne doit pas prendre ne compte la carte qui vient d'être rajouté, on actualise les ressources sur le plateau avant
                        //On remet les ressources sur le plateau
                        for (Resource r : Resource.values()){
                            //On ne dépose que les jetons dépensés, pas les cartes
                            if (player.getResFromCards(r)<=board.getCard(ligne,colonne).getCost().getNbResource(r)){
                                board.updateNbResource(r,board.getCard(ligne,colonne).getCost().getNbResource(r)-player.getResFromCards(r));
                            }
                            
                        }
                        
                        //On réalise l'action
                        action.process(player, null,null,null,board.getCard(ligne,colonne));
                        
                        //On retire la carte et on la remplace
                        board.updateCard(ligne,colonne);
                    }else{
                        //La saisie est incorrecte, on va redemander
                        Game.display.out.println("Vous ne pouvez pas acheter cette carte.");
                        verifLigne=false;
                        verifColonne=false;

                    }
                }
            
            }else{
                //Deux tableaux de correspondance
                char[] lettres = {'D','S','E', 'O', 'R'};
                Resource[] tab = {Resource.DIAMOND, Resource.SAPPHIRE, Resource.EMERALD, Resource.ONYX, Resource.RUBY};
                boolean saisi = false;
                

                //Le joueur souhaite acheter 2 jetons de la même ressource
                if(action.getClass().getSimpleName()=="PickSameTokensAction"){
                    Game.display.out.println("Choisissez la ressource pour laquelle vous souhaitez prendre 2 jetons : \n D - 2 DIAMOND \n S - 2 SAPPHIRE \n E - 2 EMERALD \n O - 2 ONYX \n R - 2 RUBY");

                    while(verifRessource==false){
                        while(saisi==false){
                            //On vérifie si la saisie est correcte
                            chaine = scan.next().toUpperCase();
                            chaine = chaine.replaceAll("\\p{C}", ""); //Potentielle erreur avec un charactère spécial
                            if(saisie.verif(1,chaine)==true){
                                saisi = true;
                            }
                        }
                        
                        //La saisie est correcte, on va alors vérifier si le plateau a assez de la ressource demandée
                        char ch = chaine.charAt(0);
                        for (int a=0; a<5; a++){
                            if(lettres[a] == ch){
                                //On a l'indice de la Ressource, on vérifie alors si le plateau en a assez
                                if (board.canGiveSameTokens(tab[a])==false){
                                    //Il n'y en a pas assez sur le terrain
                                    saisi=false;
                                    Game.display.out.println("Pas assez de ressources de ce type !");
                                }else{
                                    //On fait jouer le joueur
                                    verifRessource = true;
                                    action.process(player,tab[a],null,null,null);
                                    board.updateNbResource(tab[a],-2);
                                }
                                break;
                            }
                        }
                    }
            
                }else{
                    int occurences[] = new int[5];
                    
                    //Le joueur souhaite acheter 3 jetons différents
                    if(action.getClass().getSimpleName()=="PickDiffTokensAction"){
                        while(verifRessource==false){
                            Game.display.out.println("Choisissez les 3 ressources que vous souhaitez prendre : \n D - 1 DIAMOND \n S - 1 SAPPHIRE \n E - 1 EMERALD \n O - 1 ONYX \n R - 1 RUBY");
                            while(saisi==false){
                                //On vérifie si la saisie est correcte
                                chaine = scan.next().toUpperCase();
                                chaine = chaine.replaceAll("\\p{C}", ""); //Potentielle erreur avec un charactère spécial
                                if(saisie.verifRessourcesDiff(chaine)==true){
                                    saisi = true;
                                }
                            }
                            
                            //La saisie est correcte, on va alors vérifier si le plateau a les ressources
                            //On crée (ou recrée) ce tableau pour savoir quelles ressources le joueur a choisi
                            occurences = new int[5];
                            //On vérifie que les ressources demandées sont disponibles
                            for (int lettre=0; lettre<3; lettre++){
                                char ch = chaine.charAt(lettre);
                                for (int a=0; a<5; a++){
                                    if(lettres[a] == ch){
                                        occurences[a]+=1;
                                        //On a l'indice de la Ressource, on vérifie alors si le plateau en a assez
                                        if (board.getNbResource(tab[a]) < occurences[a]){
                                            //Le plateau n'en a pas assez
                                            saisi=false;
                                        }
                                    }
                                }
                            }
                            
                            //On vérifie si on peut enfin sortir de la boucle
                            if (saisi==true){
                                verifRessource = true;
                            }else{
                                //Il y a eu un problème de ressources
                                Game.display.out.println("Pas assez de ressources sur le plateau !");
                            }
                        }
                        
                        //On a alors les ressources, on va donc réaliser l'action
                        ArrayList<Resource> listeR = new ArrayList<Resource>();
                        for (int a=0; a<5; a++){
                            //On récupère toutes les ressources choisies
                            if(occurences[a] >0){
                                listeR.add(tab[a]);
                            }
                        }
                        action.process(player,listeR.get(0), listeR.get(1), listeR.get(2),null);
                        //On actualise les ressources sur le terrain
                        board.updateNbResource(listeR.get(0),-1);
                        board.updateNbResource(listeR.get(1),-1);
                        board.updateNbResource(listeR.get(2),-1);
                    }
                }
            }
        }
        
        if(player.getClass().getSimpleName()=="DumbRobotPlayer"){
            //On doit faire jouer un robot
            if(action.getClass().getSimpleName()=="BuyCardAction"){
                //L'action est d'acheter une carte
                for(int i=3; i>=1;i--){
                    for (int j=1; j<=4;j++){
                        if (player.canBuyCard(board.getCard(i,j))) {
                            if (verif == false){
                                //La carte a été choisie
                                verif=true;
                                //On ne doit pas prendre ne compte la carte qui vient d'être rajouté, on actualise les ressources sur le plateau avant
                                
                                //On remet les ressources sur le plateau
                                for (Resource r : Resource.values()){
                                    //On ne dépose que les jetons dépensés, pas les cartes
                                    if (player.getResFromCards(r)<=board.getCard(i,j).getCost().getNbResource(r)){
                                        board.updateNbResource(r,board.getCard(i,j).getCost().getNbResource(r)-player.getResFromCards(r));
                                    
                                    }
                                    
                                }
                                
                                //On réalise l'action
                                action.process(player, null,null,null,board.getCard(i,j));
                                
                                //On retire la carte et on la remplace
                                board.updateCard(i,j);
                            }
                        }
                    }
                }
            }
            else{
                //On récupère le tableau de ressources disponibles du robot
                ArrayList<Resource> l = board.getAvailableResources();
                
                //L'action est d'acheter 2 jetons du même type de ressource
                if(action.getClass().getSimpleName()=="PickSameTokensAction"){
                    for(int i = 0; i<l.size(); i++){
                        Resource chosenResource = l.get(i);
                        if (board.canGiveSameTokens(chosenResource)==true){
                            //Le robot peut prendre cette ressource
                            if (verif == false){
                                //La ressource a été choisie
                                verif=true;
                                action.process(player,chosenResource,null,null,null);
                                //On actualise les ressources sur le terrain
                                board.updateNbResource(chosenResource,-2);
                            }
                            break;
                        }
                    }
                }else{
                    //L'action est d'acheter 3 jetons de différents types de ressources
                    if(action.getClass().getSimpleName()=="PickDiffTokensAction"){
                        // Sélection aléatoire des ressources à prendre
                        Collections.shuffle(l); // Mélange des jetons
                        
                        action.process(player, l.get(0),l.get(1),l.get(2),null);
                        //On actualise les ressources sur le terrain
                        board.updateNbResource(l.get(0),-1);
                        board.updateNbResource(l.get(1),-1);
                        board.updateNbResource(l.get(2),-1);
                    }
                }
            }
        }
        scan.close();
        
        if(action.getClass().getSimpleName()=="PassAction"){
            //l'action est de passer, ce qui n'a pas besoin de vérifications supplémentaires
            action.process(player, null,null,null,null);
        }
        Game.display.out.println(action.toString(player));
    }

    private void discardToken(Player player){
        Resource temp[] = {Resource.DIAMOND, Resource.SAPPHIRE, Resource.EMERALD, Resource.ONYX, Resource.RUBY};
        ArrayList<Resource> listeRess = new ArrayList<Resource>();
        //On récupère le tableau de ressources que le joueur veut redéposer
        int enTrop = player.getNbTokens()-10;
        Resources tab = player.chooseDiscardingTokens(enTrop);
        for(int i=0;i<5;i++){
            if(tab.getNbResource(temp[i])>0){
                for(int j=0;j<tab.getNbResource(temp[i]);j++){
                    listeRess.add(temp[i]);
                }
            }
        }
        //Toutes les ressources à défausser ont été mises dans l'ArrayList
        DiscardTokensAction action = new DiscardTokensAction();
        while(listeRess.size() <3){
            listeRess.add(null);
        }
        action.process(player,listeRess.get(0),listeRess.get(1),listeRess.get(2),null);
        //On remets les ressources sur le plateau
        board.updateNbResource(listeRess.get(0),1);
        board.updateNbResource(listeRess.get(1),1);
        board.updateNbResource(listeRess.get(2),1);
        
        Game.display.out.println(action.toString(player));
    }

    public boolean isGameOver(){
        for(Player p : players){
            if(p.getPoints() >=15){
                return true;
            }
        }
        return false;
    }

    private void gameOver(){
        ArrayList<Player> liste = new ArrayList<Player>(); //Liste des joueurs ayant + de 15 points
        ArrayList<Player> egalite = new ArrayList<Player>(); //Liste des joueurs ayant le moins de cartes
        
        for(Player p : players){
            if(p.getPoints() >=15){
                liste.add(p);
            }
        }
        
        if (liste.size() >1){
            //Il y a une égalité
            int cartesMini = liste.get(0).getNBPurchasedCards();
            egalite.add(liste.get(0));
            for(int i=1;i<liste.size();i++){
                if (liste.get(i).getNBPurchasedCards()<cartesMini){
                    cartesMini=liste.get(i).getNBPurchasedCards();
                    //On remet la liste à vide
                    egalite = new ArrayList<Player>();
                    egalite.add(liste.get(i));
                }else{
                    if(liste.get(i).getNBPurchasedCards()==cartesMini){
                        egalite.add(liste.get(i));
                    }
                }
            }
            
            if (egalite.size()>1){
                //Il y a une nouvelle égalité en nombre de cartes
                display.outBoard.println("Partie nulle ! ");
            }else{
                display.outBoard.println("Bravo ! " + egalite.get(0).getNom() + " a gagné la partie !");
            }
        }else{
            //Il n'y a pas d'égalité
            display.outBoard.println("Bravo ! " + liste.get(0).getNom() + " a gagné la partie !");
        }
    }
}
