package com.example.algorithm.model;

import java.util.*;

public class Formula {
    public String formula;
    protected Set<String> labels = new HashSet<>();
    protected String mainSign;
    protected int indexOfMainSign;


    public String getMainSign() {
        return this.mainSign;
    }

    public void addLabel(String label) {
        this.labels.add(label);
    }

    public void addLabel(List<String> labels) {
        labels.forEach(this::addLabel);
    }

    public String getFormula() {
        return formula;
    }

    protected void setMainSign() {
        if (this.formula.length() == 1) {
            indexOfMainSign = -1;
            this.mainSign = null;
            return;
        }
        boolean closingBracketExists = false;
        int bracketsCount = -1;
        if (formula.charAt(1) == '¬') {
            indexOfMainSign = 1;
        } else if (formula.charAt(1) != '(') {
            indexOfMainSign = 2;
        } else {
            for (int i = 0; i < formula.length(); i++) {
                if (formula.charAt(i) == '(') {
                    bracketsCount++;
                } else if (formula.charAt(i) == ')') {
                    bracketsCount--;
                    closingBracketExists = true;
                }
                if (closingBracketExists && i == formula.length() - 1) {
                    indexOfMainSign = i - 2;
                }
                if (bracketsCount == 0 && closingBracketExists) {
                    indexOfMainSign = i + 1;
                    break;
                }

            }
        }
        this.mainSign = formula.substring(indexOfMainSign, indexOfMainSign + 1);
    }

    public void removeLabel(String label) {
        labels.remove(label);
    }

    public int getIndexOfV4() {
        return indexOfV4;
    }

    public void setIndexOfV4(int indexOfV4) {
        this.indexOfV4 = indexOfV4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Formula formula1 = (Formula) o;
        return indexOfMainSign == formula1.indexOfMainSign && indexOfV4 == formula1.indexOfV4 && Objects.equals(formula, formula1.formula) && Objects.equals(labels, formula1.labels) && Objects.equals(mainSign, formula1.mainSign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formula, labels, mainSign, indexOfMainSign, indexOfV4);
    }

    private int indexOfV4 = -1;

    public String getRightPart() {
        if (this.indexOfMainSign == -1)
            return null;
        return formula.substring(indexOfMainSign + 1, formula.length() - 1);
    }

    public String getLeftPart() {
        if (this.indexOfMainSign == -1 || formula.substring(1, indexOfMainSign).length() == 0)
            return null;
        return formula.substring(1, indexOfMainSign);
    }
}
