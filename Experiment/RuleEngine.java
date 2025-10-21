// Abstract base class for AST nodes
abstract class ASTNode {
   abstract boolean evaluate(Context context);
}

// Leaf node for variables or constants
class ValueNode extends ASTNode {
   private final String variable;

   ValueNode(String variable) {
       this.variable = variable;
   }

   @Override
   boolean evaluate(Context context) {
       return context.getValue(variable);
   }
}

// Node for logical operations
class LogicalNode extends ASTNode {
   private final ASTNode left;
   private final ASTNode right;
   private final String operator;

   LogicalNode(ASTNode left, ASTNode right, String operator) {
       this.left = left;
       this.right = right;
       this.operator = operator;
   }

   @Override
   boolean evaluate(Context context) {
       switch (operator) {
           case "AND": return left.evaluate(context) && right.evaluate(context);
           case "OR": return left.evaluate(context) || right.evaluate(context);
           default: throw new IllegalArgumentException("Unknown operator: " + operator);
       }
   }
}

class ASTParser {
   ASTNode parse(String rule) {
       // Example: Simplified hard-coded parsing
       ASTNode left = new ValueNode("price > 100");
       ASTNode right = new ValueNode("stock < 50");
       return new LogicalNode(left, right, "AND");
   }
}

class Context {
   private final Map<String, Boolean> values;

   Context(Map<String, Boolean> values) {
       this.values = values;
   }

   boolean getValue(String variable) {
       return values.getOrDefault(variable, false);
   }
}

public class RuleEngine {
   public static void main(String[] args) {
       // Define the rule
       String rule = "price > 100 AND stock < 50";

       // Parse the rule into an AST
       ASTParser parser = new ASTParser();
       ASTNode ast = parser.parse(rule);

       // Create a context with variable values
       Map<String, Boolean> contextValues = new HashMap<>();
       contextValues.put("price > 100", true);
       contextValues.put("stock < 50", true);
       Context context = new Context(contextValues);

       // Evaluate the rule
       boolean result = ast.evaluate(context);
       System.out.println("Rule evaluation result: " + result);
   }
}