import java.util.Stack;
public class StackCard
{
    private Stack<DevCard> stack;

    /**
     * Constructeur d'objets de classe StackCard
     */
    public StackCard()
    {
        // initialisation des attributs
        stack = new Stack<DevCard>();
    }
    
    public void addStack(DevCard card){
        stack.push(card);
    }
    
    public DevCard popStack(){
        return stack.pop();
    }

    public int sizeStack (){
        return stack.size();
    }

    public DevCard topCardInStack(){
        return stack.peek();
    }

    public void clearStack(){
        stack.clear();
    }
}