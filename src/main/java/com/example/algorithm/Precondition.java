package com.example.algorithm;

import java.util.Objects;
import java.util.Set;

public class Precondition extends Formula {
    private String type;
    private boolean blocked = false;
    private int indexOfV0 = -1;

    public Precondition(String formula, String type, String label) {
        this.formula = formula;
        this.type = type;
        this.labels.add(label);
        setMainSign();
    }

    public Precondition(String formula, String type) {
        this.formula = formula;
        this.type = type;
        setMainSign();
    }

    public Precondition(String formula) {
        this.formula = formula;
        setMainSign();
    }

    public String getType() {
        return type;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked() {
        this.blocked = true;
    }

    @Override
    public String toString() {
        return "\nPrecondition{" +
                "formula='" + formula + '\'' +
                ", labels=" + labels +
                ", type='" + type + '\'' +
                ", index Of V0=" + indexOfV0 +
                ", blocked=" + blocked +
                '}';
    }


    public int getIndexOfV0() {
        return indexOfV0;
    }

    public void setIndexOfV0(int indexOfV0) {
        this.indexOfV0 = indexOfV0;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, blocked, indexOfV0);
    }

    public void removeLabels() {
        this.labels.clear();
    }
}
