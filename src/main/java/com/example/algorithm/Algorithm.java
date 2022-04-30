package com.example.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Algorithm {
    List<Precondition> preconditions = new ArrayList<>();
    Stack<Goal> goals = new Stack<>();

    public void start(String mainFormula, String preconditionsList) {
        firstPoint(mainFormula, preconditionsList);
        boolean checkIfPreconditionsPresent = secondPoint();
        if (checkIfPreconditionsPresent) {
            thirdPoint();
        } else {
            forthPoint();
        }
    }

    public void firstPoint(String mainFormula, String preconditionsList) {
        System.out.println("1 called");
        if (preconditionsList.length() != 0) {
            String[] split = preconditionsList.split(",");
            for (String s : split) {
                preconditions.add(new Precondition(s, "parcel"));
            }
        }
        goals.add(new Goal(mainFormula, true));
        System.out.println(preconditionsList);
        System.out.println(goals);
    }

    public boolean secondPoint() {
        System.out.println("2 called");
        return !preconditions.isEmpty();
    }

    public void thirdPoint() {
        System.out.println("3 called");
        for (int i = 0; i < preconditions.size(); i++) {
            if (preconditions.get(i).getFormula().length() != 1 && !preconditions.get(i).getLabels().contains("V0") && !preconditions.get(i).isBlocked()) {
                switch (preconditions.get(i).getMainSign()) {
                    case "&": {
                        boolean accepted = false;
                        if (!isPreconditionPresent(preconditions.get(i).getRightPart())) {
                            accepted = updatePreconditions(preconditions.get(i).getRightPart(), i);
                        }
                        if (!isPreconditionPresent(preconditions.get(i).getLeftPart())) {
                            accepted = updatePreconditions(preconditions.get(i).getLeftPart(), i);
                        }
                        if (accepted) {
                            preconditions.get(i).addLabel("V0");
                            System.out.println("Updating label of precondition -> " + preconditions.get(i));
                            i = -1;
                        }
                        break;
                    }
                    case "∨": {
                        if (!isPreconditionPresent(preconditions.get(i).getRightPart()) && isPreconditionPresent("¬" + preconditions.get(i).getLeftPart())) {
                            updatePreconditions(i, "∨e");
                            i = -1;
                        }
                        break;
                    }
                    case "¬": {
                        Precondition precondition = new Precondition(preconditions.get(i).getRightPart());
                        if (!(precondition.getFormula().length() == 1) && precondition.getMainSign().equals("¬")) {
                            if (!isPreconditionPresent(precondition.getRightPart())) {
                                preconditions.get(i).addLabel("V0");
                                System.out.println("Updating label of precondition -> " + preconditions.get(i));
                                Precondition p = new Precondition(precondition.getRightPart(), "¬¬e", "V+0");
                                p.setIndexOfV0(i);
                                System.out.println("Adding precondition -> " + p);
                                preconditions.add(p);
                                i = -1;
                            }
                        }
                        break;
                    }
                    case "⊃": {
                        if (!isPreconditionPresent(preconditions.get(i).getRightPart()) && isPreconditionPresent(preconditions.get(i).getLeftPart())) {
                            updatePreconditions(i, "⊃e");
                            i = -1;
                        }
                        break;
                    }
                }
            }
        }
        eleventhPoint();
    }

    public void forthPoint() {
        System.out.println("4 called");
        Goal currentGoal = goals.peek();
        String currentGoalMainSign = currentGoal.getMainSign();
        if (currentGoal.formula.length() == 1 || currentGoalMainSign.equals("¬")) {
            fifthPoint(currentGoal);
            thirdPoint();
        } else if (currentGoalMainSign.equals("⊃")) {
            sixthPoint(currentGoal);
            thirdPoint();
        }
        if (currentGoal.getFormula().equals("⊥")) {
            fourteenthPoint();
        }
    }

    public void fifthPoint(Goal currentGoal) {
        System.out.println("5 called");
        String formula = "(¬" + currentGoal.formula + ")";
        if (!isPreconditionPresent(formula)) {
            Precondition newPrecondition = new Precondition(formula, "parcel");
            System.out.println("Adding new precondition -> " + newPrecondition);
            preconditions.add(newPrecondition);
        }
        Goal newGoal = new Goal("⊥", false);
        System.out.println("Adding new goal -> " + newGoal);
        goals.push(newGoal);
    }

    public void sixthPoint(Goal currentGoal) {
        System.out.println("6 called");
        if (!isPreconditionPresent(currentGoal.getLeftPart())) {
            Precondition newPrecondition = new Precondition(currentGoal.getLeftPart(), "parcel");
            System.out.println("Adding new precondition -> " + newPrecondition);
            preconditions.add(newPrecondition);
        }
        Goal newGoal = new Goal(currentGoal.getRightPart(), false);
        System.out.println("Adding new goal -> " + newGoal);
        goals.push(newGoal);
    }

    public void eleventhPoint() {
        System.out.println("11 called");
        Goal currentGoal = goals.peek();
        if (isPreconditionPresent(currentGoal.getFormula())) {
            System.out.println("heyy current goal " + currentGoal + "is reached as it is present in preconditions ");
            twelfthPoint();
        } else if (currentGoal.getFormula().equals("⊥") && doesPredicationsHaveContradiction()) {
            System.out.println("heyy current goal " + currentGoal + "is reached as it contradiction present in preconditions ");
            twelfthPoint();
        } else {
            thirteenthPoint(currentGoal);
        }
    }

    public void twelfthPoint() {
        System.out.println("12 called");
        Goal currentReachedGoal = goals.pop();
        if (currentReachedGoal.isMainGoal()) {
            System.out.println("algorithm done his work , goal reached !!!!!!" + currentReachedGoal);


        } else {
            System.out.println("current goal removed from goals as it is reached " + currentReachedGoal);
            Goal previousGoal = goals.peek();
            int index = getTheLastParcel();
            if (currentReachedGoal.formula.equals("⊥") && index != -1) {
                Precondition newPrecondition = new Precondition("(¬" + preconditions.get(index).getFormula() + ")", "¬a");
                System.out.println("block from " + preconditions.get(index) + " to " + newPrecondition);
                blockPreconditionsAndResetLabels(index);
                System.out.println("Adding new precondition -> " + newPrecondition);
                preconditions.add(newPrecondition);
                preconditions.get(index).setType("no label");
            } else if (previousGoal.getMainSign() != null && previousGoal.getMainSign().equals("⊃") && previousGoal.getRightPart().equals(currentReachedGoal.formula)) {
                Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "⊃a");
                int indexOfLeftPart = indexOf(previousGoal.getLeftPart());
                if (indexOfLeftPart != -1) {
                    System.out.println("block from " + preconditions.get(indexOfLeftPart) + " to " + newPrecondition);
                    blockPreconditionsAndResetLabels(indexOfLeftPart);
                }
                System.out.println("Adding new precondition -> " + newPrecondition);
                preconditions.add(newPrecondition);
                if (previousGoal.getLabels().contains("V4")) {
                    preconditions.get(previousGoal.getIndexOfV4()).removeLabel("V4");
                }
            } else if (currentReachedGoal.getLabels().contains("V4")) {
                System.out.println("current goal has a label V4");
            }
            thirdPoint();
        }
    }

    public void thirteenthPoint(Goal currentGoal) {
        System.out.println("13 called");
        if (currentGoal.getFormula().equals("⊥")) {
            fourteenthPoint();
        } else {
            forthPoint();
        }
    }

    public void fourteenthPoint() {
        System.out.println("14 called");
        int indexOfFirstPreconditionToInspect = findFirstPreconditionToInspect();
        if (indexOfFirstPreconditionToInspect != -1) {
            Precondition preconditionToInspect = preconditions.get(indexOfFirstPreconditionToInspect);
            if (preconditionToInspect.getMainSign().equals("⊃")) {
                Goal newGoal = new Goal(preconditionToInspect.getLeftPart(), "V4", false);
                System.out.println("Adding new goal -> " + newGoal);
                newGoal.setIndexOfV4(indexOfFirstPreconditionToInspect);
                goals.push(newGoal);
                System.out.println("Updating preconditions label -> " + preconditionToInspect);
                preconditions.get(indexOfFirstPreconditionToInspect).addLabel("V4");
                forthPoint();
            } else if (preconditionToInspect.getMainSign().equals("¬") && preconditionToInspect.getRightPart().length() != 1) {
                Goal newGoal = new Goal(preconditionToInspect.getRightPart(), "V4", false);
                System.out.println("Adding new goal -> " + newGoal);
                newGoal.setIndexOfV4(indexOfFirstPreconditionToInspect);
                goals.push(newGoal);
                System.out.println("Updating preconditions label -> " + preconditionToInspect);
                preconditions.get(indexOfFirstPreconditionToInspect).addLabel("V4");
                forthPoint();
            }
            else {
                fifteenthPoint();
            }
        }
        else {
            fifteenthPoint();
        }
    }

    public void fifteenthPoint(){
        boolean check = preconditions.stream().anyMatch(precondition -> !precondition.isBlocked() && precondition.getLabels().contains("V1"));
        if(check){
            //sixteenthPoint();
        }
        else{
            seventeenthPoint();
        }
    }

    public void seventeenthPoint() {
        System.out.println("Seventeen point is called");
        Goal currentGoal = goals.peek();
        if(currentGoal.getFormula().equals("⊥")){
            if(currentGoal.getLabels().contains("V-2") || currentGoal.getLabels().contains("V-1")){
                System.out.println("The main goal is unreachable, algorithm finishes its work");
                return;
            }
            if(currentGoal.getLabels().contains("V-3")){
               // eighthPoint().2;
            }
            else if(currentGoal.getLabels().contains("V-4")){
                //eighthPoint().3
            }
            else if(currentGoal.getLabels().contains("V-5")){
                //tenthPoint().2
            }
            else{
                System.out.println("The main goal is unreachable, algorithm finishes its work");
            }
        }

    }

    public boolean updatePreconditions(String part, int parentIndex) {
        Precondition p = new Precondition(part, "&e", "V+0");
        p.setIndexOfV0(parentIndex);
        System.out.println("Adding precondition -> " + p);
        preconditions.add(p);
        return true;
    }


    public void updatePreconditions(int parentIndex, String s) {
        preconditions.get(parentIndex).addLabel("V0");
        System.out.println("Updating label of precondition -> " + preconditions.get(parentIndex));
        Precondition p = new Precondition(preconditions.get(parentIndex).getRightPart(), s, "V+0");
        p.setIndexOfV0(parentIndex);
        System.out.println("Adding precondition -> " + p);
        preconditions.add(p);
    }


    public boolean isPreconditionPresent(String preconditionFormula) {
        return preconditions.stream().anyMatch(precondition -> !precondition.isBlocked() && precondition.getFormula().equals(preconditionFormula));
    }

    public int findFirstPreconditionToInspect() {
        for (int i = 0; i < preconditions.size(); i++) {
            if (preconditions.get(i).getFormula().length() != 1 && !preconditions.get(i).getLabels().contains("V0") && !preconditions.get(i).getLabels().contains("V4")) {
                return i;
            }
        }
        return -1;
    }

    public boolean doesPredicationsHaveContradiction() {
        return preconditions.stream().anyMatch(precondition -> !precondition.isBlocked() && isPreconditionPresent("(¬" + precondition.getFormula() + ")"));
    }

    public int getTheLastParcel() {
        for (int i = preconditions.size() - 1; i >= 0; i--) {
            if (!preconditions.get(i).isBlocked() && preconditions.get(i).getType().equals("parcel")) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(String formula) {
        for (int i = 0; i < preconditions.size(); i++) {
            if (preconditions.get(i).getFormula().equals(formula) && !preconditions.get(i).isBlocked())
                return i;
        }
        return -1;
    }

    public void blockPreconditionsAndResetLabels(int index) {
        for (int i = index; i < preconditions.size(); i++) {
            int indexOfV0 = preconditions.get(i).getIndexOfV0();
            if (indexOfV0 != -1) {
                System.out.println("Remove V0 label from " + preconditions.get(indexOfV0));
                preconditions.get(indexOfV0).removeLabel("V0");
            }
            preconditions.get(i).setBlocked();
        }
    }

}
