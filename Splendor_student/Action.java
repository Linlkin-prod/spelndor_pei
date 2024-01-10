
/**
 * Interface des différentes actions que peuvent réaliser les joueurs
 */

public interface Action
{
    void process(Player p, Resource a, Resource b, Resource c, DevCard card);
    String toString(Player p);
}
