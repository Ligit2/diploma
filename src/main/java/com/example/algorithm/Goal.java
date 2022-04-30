package com.example.algorithm;

import java.util.Objects;
import java.util.Set;

public class Goal extends Formula {

    boolean mainGoal;

    public Goal(String formula, String label, boolean mainGoal) {
        this.formula = formula;
        this.labels.add(label);
        this.mainGoal = mainGoal;
        setMainSign();
    }

    public Goal(String formula, boolean mainGoal) {
        this.formula = formula;
        this.mainGoal = mainGoal;
        setMainSign();
    }

    public boolean isMainGoal() {
        return this.mainGoal;
    }

    public Set<String> getLabels() {
        return this.labels;
    }

    @Override
    public String toString() {
        return "\nGoal{" +
                "formula='" + formula + '\'' +
                ", labels=" + labels +
                ", mainGoal=" + mainGoal +
                ", index of V4=" + getIndexOfV4()+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Goal goal = (Goal) o;
        return mainGoal == goal.mainGoal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mainGoal);
    }
}
