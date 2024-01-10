import java.util.ArrayList;

/**
 * Classe représentant une liste de ressources, et pouvant être utilisée 
 * pour le prix des cartes, les jetons que possède un joueur ou 
 * les jetons restants sur le plateau de jeu
 * 
 * Les indices de la liste représentent les ressources dans l'ordre suivant:
 *   Diamond, Sapphire, Emerald, Ruby, Onyx
 *
 * @author 3A
 * @version 1
 */
public class Resources
{
    // liste de ressources
    private int[] ressources;

    /**
     * Constructeur d'objets de classe Resources
     */
    public Resources()
    {
        // création du tableau
        ressources = new int[5];
    }

    /**
     * méthode qui retourne le nombre de ressource disponible en fonction
     * du type de ressource demandé
     *
     */
    public int getNbResource(Resource r)
    {
        if (r!=null){
            String temp[] = {"DIAMANT \u2666", "SAPHIR \u2660", "EMERAUDE \u2663", "ONYX \u25CF","RUBIS \u2665"};
            String s = r.toString();
            for (int i=0; i<temp.length; i++){
                //On parcourt le tableau pour trouver l'indice de la ressource
                if (temp[i]==s){
                    return ressources[i];
                }
            }
        }
        return 0;
    }
    
    /**
     * méthode qui permet de modifier le nombre de ressources d'un
     * type de ressource demandé
     *
     */
    public void setNbResource(Resource r, int v)
    {
        if (r!=null && v>=0){
            String temp[] = {"DIAMANT \u2666", "SAPHIR \u2660", "EMERAUDE \u2663", "ONYX \u25CF","RUBIS \u2665"};
            String s = r.toString();
            for (int i=0; i<temp.length; i++){
                //On parcourt le tableau pour trouver l'indice de la ressource
                if (temp[i]==s){
                    ressources[i]=v;
                }
            }
            
        }
    }
    
    /**
     * méthode qui ajoute ou retire une quantité de ressource en fonction
     * du type de ressource demandé et de la valeur à enlever ou ajouter
     *
     */
    public void updateNbResource(Resource r, int v)
    {
        if (r!=null){
            int i = getNbResource(r);
            //On vérifie que la somme sera positive, car un nombre de ressources ne peut pas être négatif
            if(i+v>=0){
                setNbResource(r, i+v);
            }
        }
    }
    
    /**
     * méthode qui retourne les types de ressources pour lesquels 
     * des ressources sont disponibles (tableau de Resource)
     *
     */
    public ArrayList<Resource> getAvailableResources()
    {
        ArrayList<Resource> tableau = new ArrayList<>();
        //Tableau temporaire des types de Resource
        Resource temp[] = {Resource.DIAMOND, Resource.SAPPHIRE, Resource.EMERALD, Resource.ONYX, Resource.RUBY};
        
        for(int j=0; j<ressources.length; j++){
            if(ressources[j]>0){
                //On ajoute l'indice à la liste d'indices
                tableau.add(temp[j]);
            }
        }
        return tableau;
    }
}