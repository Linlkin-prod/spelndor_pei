import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Classe permettant de vérifier si l'utilisateur a réalisé une saisie correcte
 */
public class Saisie
{

    /**
     * Constructeur d'objets de classe Saisie
     */
    public Saisie(){}

    /**
     * Méthode permettant de vérifier si la chaine de caractères entrée par l'utilisateur est conforme
     * i est la taille de la chaine attendue
     * s est la chaine de caractères (ne devant contenir que des D,S,E,O ou R)
     */
    public boolean verif(int i, String s)
    {
        if (s.length()==i){
            for (int lettre=0; lettre<i; lettre++){
                char ch = s.charAt(lettre);
                if (ch!='D' && ch!='S' && ch!='E' && ch!='O' && ch!='R'){
                    //Au moins l'un des caractères donnés par l'utilisateur n'est pas correct
                    Game.display.out.println("Saisie incorrecte !");
                    return false;
                }
            }
            return true;       
        }else{
            //Le nombre de caractères donnés par l'utilisateur est incorrect
            Game.display.out.println("Saisie incorrecte !");
            
        }
        return false;
    }
    
    /**
     * Méthode permettant de retourner l'entier saisi par l'utilisateur s'il est conforme
     * max est l'entier maximum attendu
     * min est l'entier minimum attendu
     * s aide pour le message (ligne ou colonne)
     */
    public int verifEntier(int max, int min, String s)
    {
        Scanner scan = new Scanner(Game.display.in);
        int entier = -1;
        Game.display.out.println("Saisir la coordonnée de la carte souhaitée ("+s+") : ");
        try{
            entier = scan.nextInt();
            if (entier>max ||entier<min){
                Game.display.out.println("Votre choix n'est pas valide.");
                scan.close();
                return -1;
            }else{
                scan.close();
                return entier;
        }
        }catch(InputMismatchException e){
            Game.display.out.println("Votre choix n'est pas valide.");
        }
        
        scan.close();

        return -1;
    }
    
    /**
     * Méthode renvoyant la chaine de caractères entrée par l'utilisateur si elle est conforme (s'il est n'est que A, B,C ou D)
     * 
     */
    public String verifAction()
    {
        Scanner scan = new Scanner(Game.display.in);
        //On met en majuscule pour pouvoir comparer
        String temp = scan.next().toUpperCase();
        if (temp.length()==1){
            char ch= temp.charAt(0);
            if (ch!='A' && ch!='B' && ch!='C' && ch!='D'){
                //La lettre saisie n'est pas dans les lettres proposées
                Game.display.out.println("Saisie incorrecte !");
            }else{
                scan.close();
                return temp;
            }
        }else{
            //Le nombre de caractères donnés par l'utilisateur est incorrect
            Game.display.out.println("Saisie incorrecte !");
        }
        scan.close();
        return "M";
    }
    
    /**
     * Méthode permettant de vérifier si la chaine de caractères entrée par l'utilisateur est conforme
     * s est la chaine de caractères (ne devant contenir que des D,S,E,O ou R, et de manière unique)
     */
    public boolean verifRessourcesDiff(String s)
    {
        if (s.length()==3){
            //On vérifie que les lettres sont les bonnes
            for (int lettre=0; lettre<3; lettre++){
                char ch = s.charAt(lettre);
                if (ch!='D' && ch!='S' && ch!='E' && ch!='O' && ch!='R'){
                    //Au moins l'un des caractères donnés par l'utilisateur n'est pas correct
                    Game.display.out.println("Saisie incorrecte !");
                    return false;
                }
            }
            //On vérifie qu'elles sont toutes différentes
            if(s.charAt(0)!=s.charAt(1) && s.charAt(1)!=s.charAt(2) && s.charAt(0)!=s.charAt(2)){
                return true;
            }else{
                Game.display.out.println("Les 3 ressources doivent être différentes !");
            }
        }else{
            //Le nombre de caractères donnés par l'utilisateur est incorrect
            Game.display.out.println("Saisie incorrecte !");
        }
        return false;
    }
}
