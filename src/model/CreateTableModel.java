package model;

public class CreateTableModel {

    private String name, datatype = "INT", limit, constraint, references;
    private String key, constraintAff, tabSelectForReference;

    public String getTabSelectForReference() {
        return tabSelectForReference;
    }

    public void setTabSelectForReference(String tabSelectForReference) {
        this.tabSelectForReference = tabSelectForReference;
    }

    public String getConstraintAff() {
        return constraintAff;
    }

    public void setConstraintAff(String constraintAff) {
        this.constraintAff = constraintAff;
    }

    public CreateTableModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CreateTableModel(String name, String datatype, String limit, String constraint, String references) {
        this.name = name;
        this.datatype = datatype;
        this.limit = limit;
        this.constraint = constraint;
        this.references = references;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

}
