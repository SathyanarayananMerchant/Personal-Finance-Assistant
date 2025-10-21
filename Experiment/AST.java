import java.util.*;

abstract class ASTNode {
    protected int line;
    protected int column;
    
    public ASTNode(int line, int column){
        this.line=line;
        this.column=column;
    }

    public abstract String toString();
    
}


public class AST {
    
}
