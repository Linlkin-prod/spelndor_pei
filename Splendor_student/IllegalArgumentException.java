
/**
 * Excpetion lancée lorsque le nombre de joueurs n'est pas conforme
 */
public class IllegalArgumentException extends Exception
{

    /**
     * Constructeur d'objets de classe IllegalArgumentException
     */
    public IllegalArgumentException()
    {
        super("Le nombre de joueurs doit être compris entre 2 et 4");
    }
}
