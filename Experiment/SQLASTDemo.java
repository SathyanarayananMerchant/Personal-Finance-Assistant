import java.util.*;

// Base AST Node
abstract class ASTNode {
    protected int line;
    protected int column;
    
    public ASTNode(int line, int column) {
        this.line = line;
        this.column = column;
    }
    
    public abstract String toString();
    public abstract <T> T accept(ASTVisitor<T> visitor);
}

// Visitor pattern interface
interface ASTVisitor<T> {
    T visit(SelectStatement node);
    T visit(InsertStatement node);
    T visit(UpdateStatement node);
    T visit(DeleteStatement node);
    T visit(CreateTableStatement node);
    T visit(ColumnReference node);
    T visit(LiteralExpression node);
    T visit(FunctionCall node);
    T visit(BooleanExpression node);
    // Add more visit methods as needed
}

// Root SQL Statement
abstract class SQLStatement extends ASTNode {
    public SQLStatement(int line, int column) {
        super(line, column);
    }
}

// =============== SELECT STATEMENT ===============
class SelectStatement extends SQLStatement {
    private boolean distinct;
    private List<SelectItem> selectList;
    private FromClause fromClause;
    private WhereClause whereClause;
    private GroupByClause groupByClause;
    private HavingClause havingClause;
    private OrderByClause orderByClause;
    private LimitClause limitClause;
    private List<CommonTableExpression> cteList;
    
    public SelectStatement(int line, int column) {
        super(line, column);
        this.selectList = new ArrayList<>();
        this.cteList = new ArrayList<>();
    }
    
    // Getters and setters
    public boolean isDistinct() { return distinct; }
    public void setDistinct(boolean distinct) { this.distinct = distinct; }
    
    public List<SelectItem> getSelectList() { return selectList; }
    public void addSelectItem(SelectItem item) { this.selectList.add(item); }
    
    public FromClause getFromClause() { return fromClause; }
    public void setFromClause(FromClause fromClause) { this.fromClause = fromClause; }
    
    public WhereClause getWhereClause() { return whereClause; }
    public void setWhereClause(WhereClause whereClause) { this.whereClause = whereClause; }
    
    public GroupByClause getGroupByClause() { return groupByClause; }
    public void setGroupByClause(GroupByClause groupByClause) { this.groupByClause = groupByClause; }
    
    public HavingClause getHavingClause() { return havingClause; }
    public void setHavingClause(HavingClause havingClause) { this.havingClause = havingClause; }
    
    public OrderByClause getOrderByClause() { return orderByClause; }
    public void setOrderByClause(OrderByClause orderByClause) { this.orderByClause = orderByClause; }
    
    public LimitClause getLimitClause() { return limitClause; }
    public void setLimitClause(LimitClause limitClause) { this.limitClause = limitClause; }
    
    public List<CommonTableExpression> getCteList() { return cteList; }
    public void addCTE(CommonTableExpression cte) { this.cteList.add(cte); }
    
    @Override
    public String toString() {
        return "SelectStatement{distinct=" + distinct + ", selectList=" + selectList + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class SelectItem extends ASTNode {
    private Expression expression;
    private String alias;
    
    public SelectItem(Expression expression, String alias, int line, int column) {
        super(line, column);
        this.expression = expression;
        this.alias = alias;
    }
    
    public Expression getExpression() { return expression; }
    public String getAlias() { return alias; }
    
    @Override
    public String toString() {
        return "SelectItem{expression=" + expression + ", alias=" + alias + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return expression.accept(visitor);
    }
}

// =============== FROM CLAUSE ===============
class FromClause extends ASTNode {
    private TableExpression tableExpression;
    
    public FromClause(TableExpression tableExpression, int line, int column) {
        super(line, column);
        this.tableExpression = tableExpression;
    }
    
    public TableExpression getTableExpression() { return tableExpression; }
    
    @Override
    public String toString() {
        return "FromClause{tableExpression=" + tableExpression + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return tableExpression.accept(visitor);
    }
}

abstract class TableExpression extends ASTNode {
    public TableExpression(int line, int column) {
        super(line, column);
    }
}

class TableReference extends TableExpression {
    private String tableName;
    private String alias;
    
    public TableReference(String tableName, String alias, int line, int column) {
        super(line, column);
        this.tableName = tableName;
        this.alias = alias;
    }
    
    public String getTableName() { return tableName; }
    public String getAlias() { return alias; }
    
    @Override
    public String toString() {
        return "TableReference{tableName=" + tableName + ", alias=" + alias + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        // Handle table reference visit
        return null;
    }
}

class JoinExpression extends TableExpression {
    public enum JoinType {
        INNER, LEFT, RIGHT, FULL, CROSS
    }
    
    private JoinType joinType;
    private TableExpression leftTable;
    private TableExpression rightTable;
    private Expression joinCondition;
    
    public JoinExpression(JoinType joinType, TableExpression leftTable, 
                         TableExpression rightTable, Expression joinCondition,
                         int line, int column) {
        super(line, column);
        this.joinType = joinType;
        this.leftTable = leftTable;
        this.rightTable = rightTable;
        this.joinCondition = joinCondition;
    }
    
    public JoinType getJoinType() { return joinType; }
    public TableExpression getLeftTable() { return leftTable; }
    public TableExpression getRightTable() { return rightTable; }
    public Expression getJoinCondition() { return joinCondition; }
    
    @Override
    public String toString() {
        return "JoinExpression{joinType=" + joinType + ", leftTable=" + leftTable + 
               ", rightTable=" + rightTable + ", joinCondition=" + joinCondition + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return joinCondition.accept(visitor);
    }
}

// =============== WHERE CLAUSE ===============
class WhereClause extends ASTNode {
    private Expression condition;
    
    public WhereClause(Expression condition, int line, int column) {
        super(line, column);
        this.condition = condition;
    }
    
    public Expression getCondition() { return condition; }
    
    @Override
    public String toString() {
        return "WhereClause{condition=" + condition + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return condition.accept(visitor);
    }
}

// =============== EXPRESSIONS ===============
abstract class Expression extends ASTNode {
    public Expression(int line, int column) {
        super(line, column);
    }
}

class ColumnReference extends Expression {
    private String tableName;
    private String columnName;
    
    public ColumnReference(String tableName, String columnName, int line, int column) {
        super(line, column);
        this.tableName = tableName;
        this.columnName = columnName;
    }
    
    public ColumnReference(String columnName, int line, int column) {
        this(null, columnName, line, column);
    }
    
    public String getTableName() { return tableName; }
    public String getColumnName() { return columnName; }
    
    @Override
    public String toString() {
        return "ColumnReference{tableName=" + tableName + ", columnName=" + columnName + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class LiteralExpression extends Expression {
    public enum LiteralType {
        STRING, INTEGER, DECIMAL, BOOLEAN, NULL, DATE
    }
    
    private LiteralType type;
    private Object value;
    
    public LiteralExpression(LiteralType type, Object value, int line, int column) {
        super(line, column);
        this.type = type;
        this.value = value;
    }
    
    public LiteralType getType() { return type; }
    public Object getValue() { return value; }
    
    @Override
    public String toString() {
        return "LiteralExpression{type=" + type + ", value=" + value + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class FunctionCall extends Expression {
    private String functionName;
    private List<Expression> arguments;
    private boolean distinct;
    
    public FunctionCall(String functionName, List<Expression> arguments, 
                       boolean distinct, int line, int column) {
        super(line, column);
        this.functionName = functionName;
        this.arguments = arguments != null ? arguments : new ArrayList<>();
        this.distinct = distinct;
    }
    
    public String getFunctionName() { return functionName; }
    public List<Expression> getArguments() { return arguments; }
    public boolean isDistinct() { return distinct; }
    
    @Override
    public String toString() {
        return "FunctionCall{functionName=" + functionName + 
               ", arguments=" + arguments + ", distinct=" + distinct + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class BinaryExpression extends Expression {
    public enum Operator {
        PLUS, MINUS, MULTIPLY, DIVIDE, MODULO,
        EQUALS, NOT_EQUALS, LESS_THAN, GREATER_THAN, 
        LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL,
        AND, OR, LIKE, IN, EXISTS
    }
    
    private Expression left;
    private Operator operator;
    private Expression right;
    
    public BinaryExpression(Expression left, Operator operator, 
                           Expression right, int line, int column) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    public Expression getLeft() { return left; }
    public Operator getOperator() { return operator; }
    public Expression getRight() { return right; }
    
    @Override
    public String toString() {
        return "BinaryExpression{left=" + left + ", operator=" + operator + ", right=" + right + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle binary expression visit
    }
}

class BooleanExpression extends Expression {
    private Expression condition;
    
    public BooleanExpression(Expression condition, int line, int column) {
        super(line, column);
        this.condition = condition;
    }
    
    public Expression getCondition() { return condition; }
    
    @Override
    public String toString() {
        return "BooleanExpression{condition=" + condition + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class CaseExpression extends Expression {
    private Expression caseOperand; // null for searched case
    private List<WhenClause> whenClauses;
    private Expression elseExpression;
    
    public CaseExpression(Expression caseOperand, List<WhenClause> whenClauses,
                         Expression elseExpression, int line, int column) {
        super(line, column);
        this.caseOperand = caseOperand;
        this.whenClauses = whenClauses;
        this.elseExpression = elseExpression;
    }
    
    public Expression getCaseOperand() { return caseOperand; }
    public List<WhenClause> getWhenClauses() { return whenClauses; }
    public Expression getElseExpression() { return elseExpression; }
    public boolean isSearchedCase() { return caseOperand == null; }
    
    @Override
    public String toString() {
        return "CaseExpression{caseOperand=" + caseOperand + 
               ", whenClauses=" + whenClauses + ", elseExpression=" + elseExpression + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle case expression visit
    }
}

class WhenClause extends ASTNode {
    private Expression whenCondition;
    private Expression thenExpression;
    
    public WhenClause(Expression whenCondition, Expression thenExpression, int line, int column) {
        super(line, column);
        this.whenCondition = whenCondition;
        this.thenExpression = thenExpression;
    }
    
    public Expression getWhenCondition() { return whenCondition; }
    public Expression getThenExpression() { return thenExpression; }
    
    @Override
    public String toString() {
        return "WhenClause{whenCondition=" + whenCondition + ", thenExpression=" + thenExpression + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle when clause visit
    }
}

// =============== INSERT STATEMENT ===============
class InsertStatement extends SQLStatement {
    private String tableName;
    private List<String> columnList;
    private ValuesClause valuesClause;
    private SelectStatement selectStatement;
    
    public InsertStatement(String tableName, int line, int column) {
        super(line, column);
        this.tableName = tableName;
        this.columnList = new ArrayList<>();
    }
    
    public String getTableName() { return tableName; }
    public List<String> getColumnList() { return columnList; }
    public ValuesClause getValuesClause() { return valuesClause; }
    public void setValuesClause(ValuesClause valuesClause) { this.valuesClause = valuesClause; }
    public SelectStatement getSelectStatement() { return selectStatement; }
    public void setSelectStatement(SelectStatement selectStatement) { this.selectStatement = selectStatement; }
    
    @Override
    public String toString() {
        return "InsertStatement{tableName=" + tableName + ", columnList=" + columnList + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ValuesClause extends ASTNode {
    private List<List<Expression>> valuesList;
    
    public ValuesClause(int line, int column) {
        super(line, column);
        this.valuesList = new ArrayList<>();
    }
    
    public List<List<Expression>> getValuesList() { return valuesList; }
    public void addValues(List<Expression> values) { this.valuesList.add(values); }
    
    @Override
    public String toString() {
        return "ValuesClause{valuesList=" + valuesList + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle values clause visit
    }
}

// =============== UPDATE STATEMENT ===============
class UpdateStatement extends SQLStatement {
    private String tableName;
    private String tableAlias;
    private List<SetClause> setClauses;
    private WhereClause whereClause;
    
    public UpdateStatement(String tableName, int line, int column) {
        super(line, column);
        this.tableName = tableName;
        this.setClauses = new ArrayList<>();
    }
    
    public String getTableName() { return tableName; }
    public String getTableAlias() { return tableAlias; }
    public void setTableAlias(String tableAlias) { this.tableAlias = tableAlias; }
    public List<SetClause> getSetClauses() { return setClauses; }
    public void addSetClause(SetClause setClause) { this.setClauses.add(setClause); }
    public WhereClause getWhereClause() { return whereClause; }
    public void setWhereClause(WhereClause whereClause) { this.whereClause = whereClause; }
    
    @Override
    public String toString() {
        return "UpdateStatement{tableName=" + tableName + ", setClauses=" + setClauses + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class SetClause extends ASTNode {
    private String columnName;
    private Expression value;
    
    public SetClause(String columnName, Expression value, int line, int column) {
        super(line, column);
        this.columnName = columnName;
        this.value = value;
    }
    
    public String getColumnName() { return columnName; }
    public Expression getValue() { return value; }
    
    @Override
    public String toString() {
        return "SetClause{columnName=" + columnName + ", value=" + value + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle set clause visit
    }
}

// =============== DELETE STATEMENT ===============
class DeleteStatement extends SQLStatement {
    private String tableName;
    private String tableAlias;
    private WhereClause whereClause;
    
    public DeleteStatement(String tableName, int line, int column) {
        super(line, column);
        this.tableName = tableName;
    }
    
    public String getTableName() { return tableName; }
    public String getTableAlias() { return tableAlias; }
    public void setTableAlias(String tableAlias) { this.tableAlias = tableAlias; }
    public WhereClause getWhereClause() { return whereClause; }
    public void setWhereClause(WhereClause whereClause) { this.whereClause = whereClause; }
    
    @Override
    public String toString() {
        return "DeleteStatement{tableName=" + tableName + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// =============== CREATE TABLE STATEMENT ===============
class CreateTableStatement extends SQLStatement {
    private String tableName;
    private List<ColumnDefinition> columnDefinitions;
    private List<TableConstraint> tableConstraints;
    
    public CreateTableStatement(String tableName, int line, int column) {
        super(line, column);
        this.tableName = tableName;
        this.columnDefinitions = new ArrayList<>();
        this.tableConstraints = new ArrayList<>();
    }
    
    public String getTableName() { return tableName; }
    public List<ColumnDefinition> getColumnDefinitions() { return columnDefinitions; }
    public void addColumnDefinition(ColumnDefinition columnDefinition) { 
        this.columnDefinitions.add(columnDefinition); 
    }
    public List<TableConstraint> getTableConstraints() { return tableConstraints; }
    public void addTableConstraint(TableConstraint tableConstraint) { 
        this.tableConstraints.add(tableConstraint); 
    }
    
    @Override
    public String toString() {
        return "CreateTableStatement{tableName=" + tableName + 
               ", columnDefinitions=" + columnDefinitions + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ColumnDefinition extends ASTNode {
    private String columnName;
    private DataType dataType;
    private List<ColumnConstraint> constraints;
    
    public ColumnDefinition(String columnName, DataType dataType, int line, int column) {
        super(line, column);
        this.columnName = columnName;
        this.dataType = dataType;
        this.constraints = new ArrayList<>();
    }
    
    public String getColumnName() { return columnName; }
    public DataType getDataType() { return dataType; }
    public List<ColumnConstraint> getConstraints() { return constraints; }
    public void addConstraint(ColumnConstraint constraint) { this.constraints.add(constraint); }
    
    @Override
    public String toString() {
        return "ColumnDefinition{columnName=" + columnName + 
               ", dataType=" + dataType + ", constraints=" + constraints + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle column definition visit
    }
}

// =============== DATA TYPES ===============
abstract class DataType extends ASTNode {
    public DataType(int line, int column) {
        super(line, column);
    }
}

class CharacterType extends DataType {
    public enum CharType {
        CHAR, VARCHAR, TEXT
    }
    
    private CharType type;
    private Integer length;
    
    public CharacterType(CharType type, Integer length, int line, int column) {
        super(line, column);
        this.type = type;
        this.length = length;
    }
    
    public CharType getType() { return type; }
    public Integer getLength() { return length; }
    
    @Override
    public String toString() {
        return "CharacterType{type=" + type + ", length=" + length + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle character type visit
    }
}

class NumericType extends DataType {
    public enum NumType {
        INTEGER, BIGINT, SMALLINT, DECIMAL, NUMERIC, REAL, FLOAT, DOUBLE
    }
    
    private NumType type;
    private Integer precision;
    private Integer scale;
    
    public NumericType(NumType type, Integer precision, Integer scale, int line, int column) {
        super(line, column);
        this.type = type;
        this.precision = precision;
        this.scale = scale;
    }
    
    public NumType getType() { return type; }
    public Integer getPrecision() { return precision; }
    public Integer getScale() { return scale; }
    
    @Override
    public String toString() {
        return "NumericType{type=" + type + ", precision=" + precision + ", scale=" + scale + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle numeric type visit
    }
}

// =============== CONSTRAINTS ===============
abstract class Constraint extends ASTNode {
    protected String constraintName;
    
    public Constraint(String constraintName, int line, int column) {
        super(line, column);
        this.constraintName = constraintName;
    }
    
    public String getConstraintName() { return constraintName; }
}

abstract class ColumnConstraint extends Constraint {
    public ColumnConstraint(String constraintName, int line, int column) {
        super(constraintName, line, column);
    }
}

class NotNullConstraint extends ColumnConstraint {
    public NotNullConstraint(String constraintName, int line, int column) {
        super(constraintName, line, column);
    }
    
    @Override
    public String toString() {
        return "NotNullConstraint{constraintName=" + constraintName + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle not null constraint visit
    }
}

class PrimaryKeyConstraint extends ColumnConstraint {
    public PrimaryKeyConstraint(String constraintName, int line, int column) {
        super(constraintName, line, column);
    }
    
    @Override
    public String toString() {
        return "PrimaryKeyConstraint{constraintName=" + constraintName + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle primary key constraint visit
    }
}

abstract class TableConstraint extends Constraint {
    public TableConstraint(String constraintName, int line, int column) {
        super(constraintName, line, column);
    }
}

// =============== OTHER CLAUSES ===============
class GroupByClause extends ASTNode {
    private List<Expression> groupingExpressions;
    
    public GroupByClause(List<Expression> groupingExpressions, int line, int column) {
        super(line, column);
        this.groupingExpressions = groupingExpressions;
    }
    
    public List<Expression> getGroupingExpressions() { return groupingExpressions; }
    
    @Override
    public String toString() {
        return "GroupByClause{groupingExpressions=" + groupingExpressions + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle group by clause visit
    }
}

class HavingClause extends ASTNode {
    private Expression condition;
    
    public HavingClause(Expression condition, int line, int column) {
        super(line, column);
        this.condition = condition;
    }
    
    public Expression getCondition() { return condition; }
    
    @Override
    public String toString() {
        return "HavingClause{condition=" + condition + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return condition.accept(visitor);
    }
}

class OrderByClause extends ASTNode {
    private List<OrderByElement> orderByElements;
    
    public OrderByClause(List<OrderByElement> orderByElements, int line, int column) {
        super(line, column);
        this.orderByElements = orderByElements;
    }
    
    public List<OrderByElement> getOrderByElements() { return orderByElements; }
    
    @Override
    public String toString() {
        return "OrderByClause{orderByElements=" + orderByElements + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle order by clause visit
    }
}

class OrderByElement extends ASTNode {
    public enum SortDirection {
        ASC, DESC
    }
    
    private Expression expression;
    private SortDirection direction;
    
    public OrderByElement(Expression expression, SortDirection direction, int line, int column) {
        super(line, column);
        this.expression = expression;
        this.direction = direction != null ? direction : SortDirection.ASC;
    }
    
    public Expression getExpression() { return expression; }
    public SortDirection getDirection() { return direction; }
    
    @Override
    public String toString() {
        return "OrderByElement{expression=" + expression + ", direction=" + direction + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return expression.accept(visitor);
    }
}

class LimitClause extends ASTNode {
    private Expression rowCount;
    private Expression offset;
    
    public LimitClause(Expression rowCount, Expression offset, int line, int column) {
        super(line, column);
        this.rowCount = rowCount;
        this.offset = offset;
    }
    
    public Expression getRowCount() { return rowCount; }
    public Expression getOffset() { return offset; }
    
    @Override
    public String toString() {
        return "LimitClause{rowCount=" + rowCount + ", offset=" + offset + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null; // Handle limit clause visit
    }
}

// =============== COMMON TABLE EXPRESSIONS ===============
class CommonTableExpression extends ASTNode {
    private String name;
    private List<String> columnList;
    private SelectStatement query;
    private boolean recursive;
    
    public CommonTableExpression(String name, List<String> columnList, 
                                SelectStatement query, boolean recursive, 
                                int line, int column) {
        super(line, column);
        this.name = name;
        this.columnList = columnList;
        this.query = query;
        this.recursive = recursive;
    }
    
    public String getName() { return name; }
    public List<String> getColumnList() { return columnList; }
    public SelectStatement getQuery() { return query; }
    public boolean isRecursive() { return recursive; }
    
    @Override
    public String toString() {
        return "CommonTableExpression{name=" + name + ", columnList=" + columnList + 
               ", recursive=" + recursive + "}";
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return query.accept(visitor);
    }
}

// =============== EXAMPLE USAGE ===============
class SQLASTBuilder {
    
    // Example: Build AST for "SELECT name, age FROM users WHERE age > 18 ORDER BY name"
    public static SelectStatement buildExampleQuery() {
        SelectStatement selectStmt = new SelectStatement(1, 1);
        
        // SELECT name, age
        ColumnReference nameCol = new ColumnReference("name", 1, 8);
        ColumnReference ageCol = new ColumnReference("age", 1, 14);
        selectStmt.addSelectItem(new SelectItem(nameCol, null, 1, 8));
        selectStmt.addSelectItem(new SelectItem(ageCol, null, 1, 14));
        
        // FROM users
        TableReference usersTable = new TableReference("users", null, 1, 23);
        selectStmt.setFromClause(new FromClause(usersTable, 1, 18));
        
        // WHERE age > 18
        ColumnReference ageColWhere = new ColumnReference("age", 1, 35);
        LiteralExpression eighteenLiteral = new LiteralExpression(
            LiteralExpression.LiteralType.INTEGER, 18, 1, 41);
        BinaryExpression ageCondition = new BinaryExpression(
            ageColWhere, BinaryExpression.Operator.GREATER_THAN, eighteenLiteral, 1, 35);
        selectStmt.setWhereClause(new WhereClause(ageCondition, 1, 29));
        
        // ORDER BY name
        ColumnReference nameColOrder = new ColumnReference("name", 1, 53);
        OrderByElement orderElement = new OrderByElement(
            nameColOrder, OrderByElement.SortDirection.ASC, 1, 53);
        List<OrderByElement> orderElements = Arrays.asList(orderElement);
        selectStmt.setOrderByClause(new OrderByClause(orderElements, 1, 44));
        
        return selectStmt;
    }
    
    // Example: Build AST for "INSERT INTO users (name, age) VALUES ('John', 25)"
    public static InsertStatement buildExampleInsert() {
        InsertStatement insertStmt = new InsertStatement("users", 1, 1);
        
        // Column list: (name, age)
        insertStmt.getColumnList().add("name");
        insertStmt.getColumnList().add("age");
        
        // VALUES ('John', 25)
        ValuesClause valuesClause = new ValuesClause(1, 35);
        List<Expression> valueRow = Arrays.asList(
            new LiteralExpression(LiteralExpression.LiteralType.STRING, "John", 1, 43),
            new LiteralExpression(LiteralExpression.LiteralType.INTEGER, 25, 1, 51)
        );
        valuesClause.addValues(valueRow);
        insertStmt.setValuesClause(valuesClause);
        
        return insertStmt;
    }
    
    // Example: Build AST for "UPDATE users SET age = 26 WHERE name = 'John'"
    public static UpdateStatement buildExampleUpdate() {
        UpdateStatement updateStmt = new UpdateStatement("users", 1, 1);
        
        // SET age = 26
        LiteralExpression twentySix = new LiteralExpression(
            LiteralExpression.LiteralType.INTEGER, 26, 1, 19);
        SetClause setClause = new SetClause("age", twentySix, 1, 13);
        updateStmt.addSetClause(setClause);
        
        // WHERE name = 'John'
        ColumnReference nameCol = new ColumnReference("name", 1, 28);
        LiteralExpression johnLiteral = new LiteralExpression(
            LiteralExpression.LiteralType.STRING, "John", 1, 35);
        BinaryExpression nameCondition = new BinaryExpression(
            nameCol, BinaryExpression.Operator.EQUALS, johnLiteral, 1, 28);
        updateStmt.setWhereClause(new WhereClause(nameCondition, 1, 22));
        
        return updateStmt;
    }
    
    // Example: Build AST for "CREATE TABLE products (id INT PRIMARY KEY, name VARCHAR(100) NOT NULL)"
    public static CreateTableStatement buildExampleCreateTable() {
        CreateTableStatement createStmt = new CreateTableStatement("products", 1, 1);
        
        // id INT PRIMARY KEY
        NumericType intType = new NumericType(NumericType.NumType.INTEGER, null, null, 1, 27);
        ColumnDefinition idColumn = new ColumnDefinition("id", intType, 1, 24);
        idColumn.addConstraint(new PrimaryKeyConstraint(null, 1, 31));
        createStmt.addColumnDefinition(idColumn);
        
        // name VARCHAR(100) NOT NULL
        CharacterType varcharType = new CharacterType(
            CharacterType.CharType.VARCHAR, 100, 1, 54);
        ColumnDefinition nameColumn = new ColumnDefinition("name", varcharType, 1, 49);
        nameColumn.addConstraint(new NotNullConstraint(null, 1, 66));
        createStmt.addColumnDefinition(nameColumn);
        
        return createStmt;
    }
}

// =============== AST VISITOR IMPLEMENTATIONS ===============

// Pretty printer visitor
class SQLPrettyPrinter implements ASTVisitor<String> {
    private int indentLevel = 0;
    private static final String INDENT = "  ";
    
    private String indent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            sb.append(INDENT);
        }
        return sb.toString();
    }
    
    @Override
    public String visit(SelectStatement node) {
        StringBuilder sb = new StringBuilder();
        
        // CTEs
        if (!node.getCteList().isEmpty()) {
            sb.append("WITH ");
            for (int i = 0; i < node.getCteList().size(); i++) {
                if (i > 0) sb.append(", ");
                CommonTableExpression cte = node.getCteList().get(i);
                sb.append(cte.getName());
                if (cte.getColumnList() != null && !cte.getColumnList().isEmpty()) {
                    sb.append(" (").append(String.join(", ", cte.getColumnList())).append(")");
                }
                sb.append(" AS (");
                indentLevel++;
                sb.append("\n").append(indent()).append(cte.getQuery().accept(this));
                indentLevel--;
                sb.append("\n").append(indent()).append(")");
            }
            sb.append("\n");
        }
        
        // SELECT clause
        sb.append("SELECT ");
        if (node.isDistinct()) {
            sb.append("DISTINCT ");
        }
        
        for (int i = 0; i < node.getSelectList().size(); i++) {
            if (i > 0) sb.append(", ");
            SelectItem item = node.getSelectList().get(i);
            sb.append(item.getExpression().accept(this));
            if (item.getAlias() != null) {
                sb.append(" AS ").append(item.getAlias());
            }
        }
        
        // FROM clause
        if (node.getFromClause() != null) {
            sb.append("\nFROM ").append(node.getFromClause().getTableExpression().accept(this));
        }
        
        // WHERE clause
        if (node.getWhereClause() != null) {
            sb.append("\nWHERE ").append(node.getWhereClause().getCondition().accept(this));
        }
        
        // GROUP BY clause
        if (node.getGroupByClause() != null) {
            sb.append("\nGROUP BY ");
            List<Expression> groupExprs = node.getGroupByClause().getGroupingExpressions();
            for (int i = 0; i < groupExprs.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(groupExprs.get(i).accept(this));
            }
        }
        
        // HAVING clause
        if (node.getHavingClause() != null) {
            sb.append("\nHAVING ").append(node.getHavingClause().getCondition().accept(this));
        }
        
        // ORDER BY clause
        if (node.getOrderByClause() != null) {
            sb.append("\nORDER BY ");
            List<OrderByElement> orderElements = node.getOrderByClause().getOrderByElements();
            for (int i = 0; i < orderElements.size(); i++) {
                if (i > 0) sb.append(", ");
                OrderByElement element = orderElements.get(i);
                sb.append(element.getExpression().accept(this));
                sb.append(" ").append(element.getDirection().toString());
            }
        }
        
        // LIMIT clause
        if (node.getLimitClause() != null) {
            sb.append("\nLIMIT ").append(node.getLimitClause().getRowCount().accept(this));
            if (node.getLimitClause().getOffset() != null) {
                sb.append(" OFFSET ").append(node.getLimitClause().getOffset().accept(this));
            }
        }
        
        return sb.toString();
    }
    
    @Override
    public String visit(InsertStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(node.getTableName());
        
        if (!node.getColumnList().isEmpty()) {
            sb.append(" (").append(String.join(", ", node.getColumnList())).append(")");
        }
        
        if (node.getValuesClause() != null) {
            sb.append(" VALUES ");
            List<List<Expression>> valuesList = node.getValuesClause().getValuesList();
            for (int i = 0; i < valuesList.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append("(");
                List<Expression> values = valuesList.get(i);
                for (int j = 0; j < values.size(); j++) {
                    if (j > 0) sb.append(", ");
                    sb.append(values.get(j).accept(this));
                }
                sb.append(")");
            }
        } else if (node.getSelectStatement() != null) {
            sb.append("\n").append(node.getSelectStatement().accept(this));
        }
        
        return sb.toString();
    }
    
    @Override
    public String visit(UpdateStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(node.getTableName());
        
        if (node.getTableAlias() != null) {
            sb.append(" AS ").append(node.getTableAlias());
        }
        
        sb.append(" SET ");
        for (int i = 0; i < node.getSetClauses().size(); i++) {
            if (i > 0) sb.append(", ");
            SetClause setClause = node.getSetClauses().get(i);
            sb.append(setClause.getColumnName()).append(" = ");
            sb.append(setClause.getValue().accept(this));
        }
        
        if (node.getWhereClause() != null) {
            sb.append(" WHERE ").append(node.getWhereClause().getCondition().accept(this));
        }
        
        return sb.toString();
    }
    
    @Override
    public String visit(DeleteStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(node.getTableName());
        
        if (node.getTableAlias() != null) {
            sb.append(" AS ").append(node.getTableAlias());
        }
        
        if (node.getWhereClause() != null) {
            sb.append(" WHERE ").append(node.getWhereClause().getCondition().accept(this));
        }
        
        return sb.toString();
    }
    
    @Override
    public String visit(CreateTableStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(node.getTableName()).append(" (\n");
        
        indentLevel++;
        
        // Column definitions
        for (int i = 0; i < node.getColumnDefinitions().size(); i++) {
            if (i > 0) sb.append(",\n");
            sb.append(indent());
            
            ColumnDefinition colDef = node.getColumnDefinitions().get(i);
            sb.append(colDef.getColumnName()).append(" ");
            sb.append(formatDataType(colDef.getDataType()));
            
            // Column constraints
            for (ColumnConstraint constraint : colDef.getConstraints()) {
                if (constraint instanceof NotNullConstraint) {
                    sb.append(" NOT NULL");
                } else if (constraint instanceof PrimaryKeyConstraint) {
                    sb.append(" PRIMARY KEY");
                }
            }
        }
        
        // Table constraints
        for (TableConstraint constraint : node.getTableConstraints()) {
            sb.append(",\n").append(indent());
            sb.append("-- Table constraint: ").append(constraint.getClass().getSimpleName());
        }
        
        indentLevel--;
        sb.append("\n)");
        
        return sb.toString();
    }
    
    private String formatDataType(DataType dataType) {
        if (dataType instanceof CharacterType) {
            CharacterType charType = (CharacterType) dataType;
            String typeName = charType.getType().toString();
            if (charType.getLength() != null) {
                return typeName + "(" + charType.getLength() + ")";
            }
            return typeName;
        } else if (dataType instanceof NumericType) {
            NumericType numType = (NumericType) dataType;
            String typeName = numType.getType().toString();
            if (numType.getPrecision() != null) {
                if (numType.getScale() != null) {
                    return typeName + "(" + numType.getPrecision() + "," + numType.getScale() + ")";
                } else {
                    return typeName + "(" + numType.getPrecision() + ")";
                }
            }
            return typeName;
        }
        return dataType.getClass().getSimpleName();
    }
    
    @Override
    public String visit(ColumnReference node) {
        if (node.getTableName() != null) {
            return node.getTableName() + "." + node.getColumnName();
        }
        return node.getColumnName();
    }
    
    @Override
    public String visit(LiteralExpression node) {
        switch (node.getType()) {
            case STRING:
                return "'" + node.getValue().toString().replace("'", "''") + "'";
            case INTEGER:
            case DECIMAL:
                return node.getValue().toString();
            case BOOLEAN:
                return node.getValue().toString().toUpperCase();
            case NULL:
                return "NULL";
            case DATE:
                return "'" + node.getValue().toString() + "'";
            default:
                return node.getValue().toString();
        }
    }
    
    @Override
    public String visit(FunctionCall node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.getFunctionName()).append("(");
        
        if (node.isDistinct()) {
            sb.append("DISTINCT ");
        }
        
        for (int i = 0; i < node.getArguments().size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(node.getArguments().get(i).accept(this));
        }
        
        sb.append(")");
        return sb.toString();
    }
    
    @Override
    public String visit(BooleanExpression node) {
        return node.getCondition().accept(this);
    }
}

// AST Analyzer visitor - counts different node types
class SQLASTAnalyzer implements ASTVisitor<Integer> {
    private Map<String, Integer> nodeCounts = new HashMap<>();
    
    public Map<String, Integer> getNodeCounts() {
        return nodeCounts;
    }
    
    private void incrementCount(String nodeType) {
        nodeCounts.put(nodeType, nodeCounts.getOrDefault(nodeType, 0) + 1);
    }
    
    @Override
    public Integer visit(SelectStatement node) {
        incrementCount("SelectStatement");
        
        // Visit child nodes
        for (SelectItem item : node.getSelectList()) {
            item.getExpression().accept(this);
        }
        
        if (node.getFromClause() != null) {
            node.getFromClause().accept(this);
        }
        
        if (node.getWhereClause() != null) {
            node.getWhereClause().accept(this);
        }
        
        if (node.getOrderByClause() != null) {
            for (OrderByElement element : node.getOrderByClause().getOrderByElements()) {
                element.getExpression().accept(this);
            }
        }
        
        return nodeCounts.get("SelectStatement");
    }
    
    @Override
    public Integer visit(InsertStatement node) {
        incrementCount("InsertStatement");
        return nodeCounts.get("InsertStatement");
    }
    
    @Override
    public Integer visit(UpdateStatement node) {
        incrementCount("UpdateStatement");
        
        for (SetClause setClause : node.getSetClauses()) {
            setClause.getValue().accept(this);
        }
        
        if (node.getWhereClause() != null) {
            node.getWhereClause().accept(this);
        }
        
        return nodeCounts.get("UpdateStatement");
    }
    
    @Override
    public Integer visit(DeleteStatement node) {
        incrementCount("DeleteStatement");
        
        if (node.getWhereClause() != null) {
            node.getWhereClause().accept(this);
        }
        
        return nodeCounts.get("DeleteStatement");
    }
    
    @Override
    public Integer visit(CreateTableStatement node) {
        incrementCount("CreateTableStatement");
        return nodeCounts.get("CreateTableStatement");
    }
    
    @Override
    public Integer visit(ColumnReference node) {
        incrementCount("ColumnReference");
        return nodeCounts.get("ColumnReference");
    }
    
    @Override
    public Integer visit(LiteralExpression node) {
        incrementCount("LiteralExpression");
        return nodeCounts.get("LiteralExpression");
    }
    
    @Override
    public Integer visit(FunctionCall node) {
        incrementCount("FunctionCall");
        
        for (Expression arg : node.getArguments()) {
            arg.accept(this);
        }
        
        return nodeCounts.get("FunctionCall");
    }
    
    @Override
    public Integer visit(BooleanExpression node) {
        incrementCount("BooleanExpression");
        node.getCondition().accept(this);
        return nodeCounts.get("BooleanExpression");
    }
}

// =============== MAIN DEMONSTRATION CLASS ===============
public class SQLASTDemo {
    public static void main(String[] args) {
        System.out.println("=== SQL AST Demonstration ===\n");
        
        // Build example SELECT statement
        SelectStatement selectStmt = SQLASTBuilder.buildExampleQuery();
        System.out.println("1. SELECT Statement AST:");
        System.out.println(selectStmt);
        System.out.println();
        
        // Pretty print the SELECT statement
        SQLPrettyPrinter printer = new SQLPrettyPrinter();
        System.out.println("Pretty printed SQL:");
        System.out.println(printer.visit(selectStmt));
        System.out.println();
        
        // Build and print INSERT statement
        InsertStatement insertStmt = SQLASTBuilder.buildExampleInsert();
        System.out.println("2. INSERT Statement:");
        System.out.println(printer.visit(insertStmt));
        System.out.println();
        
        // Build and print UPDATE statement
        UpdateStatement updateStmt = SQLASTBuilder.buildExampleUpdate();
        System.out.println("3. UPDATE Statement:");
        System.out.println(printer.visit(updateStmt));
        System.out.println();
        
        // Build and print CREATE TABLE statement
        CreateTableStatement createStmt = SQLASTBuilder.buildExampleCreateTable();
        System.out.println("4. CREATE TABLE Statement:");
        System.out.println(printer.visit(createStmt));
        System.out.println();
        
        // Analyze AST
        SQLASTAnalyzer analyzer = new SQLASTAnalyzer();
        selectStmt.accept(analyzer);
        
        System.out.println("5. AST Analysis (Node counts):");
        for (Map.Entry<String, Integer> entry : analyzer.getNodeCounts().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}