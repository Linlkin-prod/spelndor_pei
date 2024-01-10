import java.util.Scanner;

/**
 * Classe représentant un joueur humain
 */
public class HumanPlayer extends Player
{

    /**
     * Constructeur d'objets de classe HumanPlayer
     */
    public HumanPlayer(int id, String name)
    {
        super(id,name);
    }

    /**
     * 
     */
    public Action chooseAction(Board plateau)
    {
        //Affichage du message
        String s = "Quelle action ? \n A: Prendre 3 gemmes différentes \n B: Prendre 2 gemmes identiques \n C: Acheter une carte \n D: Passer son tour \n";
        Game.display.out.println(s);
        
        //Deux tableaux de correspondance
        char[] choix = {'A','B','C', 'D'};
        Action[] action = {new PickDiffTokensAction(),new PickSameTokensAction(),new BuyCardAction(),new PassAction()};
        
        boolean verif =false;
        boolean saisi = false;
        String temp ="";
        Action act=null;
        
        Saisie saisie = new Saisie(); //Vérification de la saisie
        
        //On vérifie que la saisie est correcte
        while (verif == false){
            while (saisi == false){
                //On répète la demande de saisie tant qu'elle est incorrecte
                temp = saisie.verifAction();
                if (temp!="M"){
                    saisi = true;
                }
            }
        
            //La saisie est correcte, on va alors retourner l'action choisie
            char ch = temp.charAt(0);
            for (int a=0; a<4; a++){
                if(choix[a] == ch){
                    //On retourne l'action si c'est possible, sinon on retourne faire la demande
                    if(verification(plateau, action[a])==true){
                        act= action[a];
                        verif=true;
                    }else{
                        saisi = false;
                        Game.display.out.println("Action Impossible !");
                    }
                    
                }
            }
        }
        
        //On retourne l'action
        return act;
    }       
    
    
    /**
     * Méthode permettant au joueur de choisir les i jetons en trop 
     * qu'il souhaite défausser, et retournant le tableau de ces jetons
     */
    public Resources chooseDiscardingTokens(int i){
        //Affichage du message
        String s = "Vous avez " + i + " jetons en trop. \n Choisissez le.s jeton.s à remettre : \n D - 1 DIAMOND \n S - 1 SAPPHIRE \n E - 1 EMERALD \n O - 1 ONYX \n R - 1 RUBY";
        Game.display.out.println(s);
        
        //Deux tableaux de correspondance
        char[] lettres = {'D','S','E', 'O', 'R'};
        Resource[] tab = {Resource.DIAMOND, Resource.SAPPHIRE, Resource.EMERALD, Resource.ONYX, Resource.RUBY};
        
        //On crée la liste de ressources à renvoyer
        Resources defausse = new Resources();
        Saisie saisie = new Saisie();
        //Booléens permettant d'être surs de la saisie
        boolean saisi = false;
        boolean ress = false;
        String temp ="";
        Scanner scan = new Scanner(Game.display.in);
        
        //On vérifie que les ressources saisies sont dans les ressources disponibles
        while (ress == false){
            //On vérifie que la saisie est correcte
            while (saisi == false){
                temp = scan.nextLine().toUpperCase();
                //On enlève la potentielle erreur (caractère non affichable)
                temp = temp.replaceAll("\\p{C}", "");
                if(saisie.verif(i,temp)==true){
                    saisi = true;
                }
            }
            
            //La saisie est correcte, on va alors ajouter les ressources dans le tableau de ressources (si le joueur dispose de ces ressources)
            
            //On crée ce tableau si le joueur a choisi plusieurs fois la même ressource
            int occurences[] = new int[5];
            //On remets à 0 défausse, car l'ancien remplissage a connu une erreur
            defausse = new Resources();
            //On vérifie que les ressources demandées sont disponibles
            for (int lettre=0; lettre<i; lettre++){
                char ch = temp.charAt(lettre);
                for (int a=0; a<5; a++){
                    if(lettres[a] == ch){
                        occurences[a]+=1;
                        //On a l'indice de la Ressource, on vérifie alors si le joueur en a assez
                        if (getNbResources(tab[a]) < occurences[a]){
                            //Le joueur n'en a pas assez
                            saisi=false;
                        }else{
                            defausse.updateNbResource(tab[a],1);
                        }
                        break;
                        
                    }
                }
            }
            
            //On vérifie si on peut enfin sortir de la boucle
            if (saisi==true){
                ress = true;
            }else{
                //Il y a eu un problème de ressources
                Game.display.out.println("Pas assez de ressources !");
            }
        }
        
        //On retourne le tableau
        scan.close();
        return defausse;
    }
}