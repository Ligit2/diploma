package com.example.algorithm;

import java.util.*;

public class Algorithm {
    List<Precondition> preconditions = new ArrayList<>();
    Stack<Goal> goals = new Stack<>();

    Algorithm() {
    }

    Algorithm(List<Precondition> preconditions, Stack<Goal> goals) {
        this.goals = goals;
        this.preconditions = preconditions;
    }

    public void isReachable() throws MetaStatementNotProvableException {
        boolean checkIfPreconditionsPresent = secondPoint();
        if (checkIfPreconditionsPresent) {
            thirdPoint();
        } else {
            forthPoint();
        }
    }

    public void start(String mainFormula, String preconditionsList) throws MetaStatementNotProvableException {
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
            if (preconditions.get(i).getFormula().length() != 1 &&
                    !preconditions.get(i).getLabels().contains("V0") &&
                    !preconditions.get(i).isBlocked()
                //!preconditions.get(i).getLabels().contains("V+0")
            ) {
                switch (preconditions.get(i).getMainSign()) {
                    case "&": {
                        boolean accepted = false;
                        //if (!Helper.isPreconditionPresent(this.preconditions, preconditions.get(i).getRightPart())) {
                        accepted = Utils.updatePreconditions(this.goals, this.preconditions, preconditions.get(i).getRightPart(), i);
                        //}
                        //if (!Helper.isPreconditionPresent(this.preconditions, preconditions.get(i).getLeftPart())) {
                        accepted = Utils.updatePreconditions(this.goals, this.preconditions, preconditions.get(i).getLeftPart(), i);
                        //}
                        if (accepted) {
                            preconditions.get(i).addLabel("V0");
                            System.out.println("Updating label of precondition -> " + preconditions.get(i));
                            i = -1;
                        }
                        break;
                    }
                    case "∨": {
                        // !Helper.isPreconditionPresent(this.preconditions, preconditions.get(i).getRightPart()) &&
                        if (Utils.isPreconditionPresent(this.preconditions, "(¬" + preconditions.get(i).getLeftPart() + ")")) {
                            Utils.updatePreconditions(this.goals, this.preconditions, i, "∨e");
                            i = -1;
                        }
                        break;
                    }
                    case "¬": {
                        Precondition precondition = new Precondition(preconditions.get(i).getRightPart());
                        if (!(precondition.getFormula().length() == 1) && precondition.getMainSign().equals("¬")) {
                            //if (!Helper.isPreconditionPresent(this.preconditions, precondition.getRightPart())) {
                            preconditions.get(i).addLabel("V0");
                            System.out.println("Updating label of precondition -> " + preconditions.get(i));
                            Precondition p = new Precondition(precondition.getRightPart(), "¬¬e", "V+0");
                            p.setIndexOfV0(i);
                            System.out.println("Adding precondition -> " + p);
                            List<String> labelsFromMainGoal = Utils.getLabelsFromMainGoal(this.goals);
                            if (!labelsFromMainGoal.isEmpty()) {
                                p.addLabel(labelsFromMainGoal);
                            }
                            preconditions.add(p);
                            i = -1;
                            //}
                        }
                        break;
                    }
                    case "⊃": {
                        //!Helper.isPreconditionPresent(this.preconditions, preconditions.get(i).getRightPart()) &&
                        if (Utils.isPreconditionPresent(this.preconditions, preconditions.get(i).getLeftPart())) {
                            Utils.updatePreconditions(this.goals, this.preconditions, i, "⊃e");
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
        if ((currentGoal.formula.length() == 1 && !currentGoal.getFormula().equals("⊥")) || currentGoalMainSign.equals("¬")) {
            fifthPoint(currentGoal);
            thirdPoint();
        } else if (currentGoalMainSign.equals("⊃")) {
            sixthPoint(currentGoal);
            thirdPoint();
        } else if (currentGoal.getFormula().equals("⊥")) {
            fourteenthPoint();
        } else if (currentGoal.getMainSign().equals("&")) {
            seventhPoint(currentGoal);
        } else if (currentGoal.getMainSign().equals("∨")) {
            eightPoint(currentGoal);
        } else {
            throw new MetaStatementNotProvableException("Current goal's main sign is unknown", 4);
        }
    }

    public void fifthPoint(Goal currentGoal) {
        System.out.println("5 called");
        String formula = "(¬" + currentGoal.formula + ")";
        Precondition newPrecondition = new Precondition(formula, "parcel");
        System.out.println("Adding new precondition -> " + newPrecondition);
        preconditions.add(newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        Goal newGoal = new Goal("⊥", false);
        System.out.println("Adding new goal -> " + newGoal);
        newGoal.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        goals.push(newGoal);
    }

    public void sixthPoint(Goal currentGoal) {
        System.out.println("6 called");
        Precondition newPrecondition = new Precondition(currentGoal.getLeftPart(), "parcel");
        System.out.println("Adding new precondition -> " + newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
        Goal newGoal = new Goal(currentGoal.getRightPart(), false);
        System.out.println("Adding new goal -> " + newGoal);
        newGoal.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        goals.push(newGoal);
    }

    public void seventhPoint(Goal currentGoal) {
        System.out.println("7 called");
        Goal Wj = new Goal(currentGoal.getRightPart(), true);
        Goal Wi = new Goal(currentGoal.getLeftPart(), true);
        Wj.addLabel("V-2");
        Wi.addLabel("V-1");
        this.goals.push(new Goal(Wj.getFormula(), "V-2", false));
        Stack<Goal> newGoals = new Stack<>();
        newGoals.push(Wi);
        Algorithm algorithm = new Algorithm(preconditions, newGoals);
        try {
            algorithm.isReachable();
            Utils.removeLabelsFromPreconditions(algorithm.preconditions);
            algorithm.goals.push(Wj);
            algorithm.isReachable();
            Utils.removeLabelsFromPreconditions(algorithm.preconditions);
            eleventhPoint();
        } catch (MetaStatementNotProvableException ex) {
            throw new MetaStatementNotProvableException("", 7);
        }
    }

    public void eightPoint(Goal currentGoal) {
        System.out.println("8 called");
//        if (currentGoal.getLabels().contains("V2")) {
//            addPreconditionsBasedOnProvableStatement(currentGoal);
//            this.goals.pop();
//            thirdPoint();
//        } else {
            Goal Wj = new Goal(currentGoal.getRightPart(), true);
            Goal Wi = new Goal(currentGoal.getLeftPart(), true);
            Wj.addLabel("V-4");
            Wi.addLabel("V-3");
            this.goals.push(new Goal(Wi.getFormula(), "V-3", false));
            Stack<Goal> newGoals = new Stack<>();
            newGoals.push(Wi);
            Algorithm algorithm = new Algorithm(preconditions, newGoals);
            try {
                firstSubPointOfEight(algorithm);
            } catch (MetaStatementNotProvableException | TemporaryMovingToSecondSubPointOrEight ex) {
                try {
                    secondSubPointOfEight(algorithm, Wj);
                } catch (MetaStatementNotProvableException | TemporaryMovingToThirdSubPointOrEight ex1) {
                    thirdSubPointOfEight(currentGoal, algorithm);
                }
            }
        //}
    }

    public void thirdSubPointOfEight(Goal currentGoal, Algorithm algorithm) {
        System.out.println("8.3 called");
        while (!algorithm.goals.isEmpty()) {
            Goal pop = algorithm.goals.pop();
            if (pop.getLabels().contains("V4")) {
                this.preconditions.get(pop.getIndexOfV4()).removeLabel("V4");
            }
        }
        this.goals.pop();
        Utils.removePreconditionsWithLabelsV4(preconditions);
        if (currentGoal.getLabels().contains("V2")) {
            addPreconditionsBasedOnProvableStatement(currentGoal);
            this.goals.pop();
        } else {
            this.preconditions.add(new Precondition("(¬" + currentGoal.formula + ")", "parcel", "V2"));
            this.goals.push(new Goal("⊥", false));
        }
        thirdPoint();
    }

    private void addPreconditionsBasedOnProvableStatement(Goal currentGoal) {
        Precondition precondition = this.preconditions.get(currentGoal.getIndexOfV2());
        precondition.removeLabels();
        precondition.addLabel("V0");
        Precondition Wi = new Precondition("(¬" + currentGoal.getLeftPart() + ")", "⊢");
        Precondition Wj = new Precondition("(¬" + currentGoal.getRightPart() + ")", "⊢");
        this.preconditions.add(Wi);
        this.preconditions.add(Wj);
    }

    public void firstSubPointOfEight(Algorithm algorithm) {
        System.out.println("8.1 called");
        algorithm.isReachable();
        eleventhPoint();
    }

    public void secondSubPointOfEight(Algorithm algorithm, Goal Wj) {
        System.out.println("8.2 called ");
        while (!algorithm.goals.isEmpty()) {
            Goal pop = algorithm.goals.pop();
            if (pop.getLabels().contains("V4")) {
                this.preconditions.get(pop.getIndexOfV4()).removeLabel("V4");
            }
        }
        this.goals.pop();
        Utils.removePreconditionsWithLabelsV3(algorithm.preconditions);
        algorithm.goals.push(Wj);
        this.goals.push(new Goal(Wj.getFormula(), "V-4", false));
        algorithm.isReachable();
        eleventhPoint();
    }

    public void eleventhPoint() {
        System.out.println("11 called");
        Goal currentGoal = goals.peek();
        if (Utils.isPreconditionPresent(this.preconditions, currentGoal.getFormula())) {
            System.out.println("hey current goal " + currentGoal + "is reached as it is present in preconditions ");
            twelfthPoint();
        } else if (currentGoal.getFormula().equals("⊥") && Utils.doesPredicationsHaveContradiction(this.preconditions)) {
            System.out.println("hey current goal " + currentGoal + "is reached as it contradiction present in preconditions ");
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
            int index = Utils.getTheLastParcel(this.preconditions);
            if (currentReachedGoal.formula.equals("⊥") && index != -1) {
                Precondition newPrecondition = new Precondition("(¬" + preconditions.get(index).getFormula() + ")", "¬a");
                System.out.println("block from " + preconditions.get(index) + " to " + newPrecondition);
                Utils.blockPreconditionsAndResetLabels(this.preconditions, index);
                System.out.println("Adding new precondition -> " + newPrecondition);
                newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
                preconditions.add(newPrecondition);
            } else if (previousGoal.getMainSign() != null && previousGoal.getMainSign().equals("⊃") && previousGoal.getRightPart().equals(currentReachedGoal.formula)) {
                Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "⊃a");
                int indexOfLeftPart = Utils.indexOf(this.preconditions, previousGoal.getLeftPart());
                if (indexOfLeftPart != -1) {
                    System.out.println("block from " + preconditions.get(indexOfLeftPart) + " to " + newPrecondition);
                    Utils.blockPreconditionsAndResetLabels(this.preconditions, indexOfLeftPart);
                }
                System.out.println("Adding new precondition -> " + newPrecondition);
                newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
                preconditions.add(newPrecondition);
                if (previousGoal.getLabels().contains("V4")) {
                    preconditions.get(previousGoal.getIndexOfV4()).removeLabel("V4");
                }
            } else if (previousGoal.getMainSign() != null && previousGoal.getMainSign().equals("&") &&
                    previousGoal.getRightPart().equals(currentReachedGoal.formula)) {
                Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "&a", "V0");
                System.out.println("Adding new precondition -> " + newPrecondition);
                newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
                preconditions.add(newPrecondition);
            } else if (previousGoal.getMainSign() != null && previousGoal.getMainSign().equals("∨") &&
                    (previousGoal.getRightPart().equals(currentReachedGoal.formula)
                            || previousGoal.getLeftPart().equals(currentReachedGoal.formula))) {
                Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "∨a");
                System.out.println("Adding new precondition -> " + newPrecondition);
                newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
                preconditions.add(newPrecondition);
                if (previousGoal.getLabels().contains("V4")) {
                    preconditions.get(previousGoal.getIndexOfV4()).removeLabel("V4");
                }
            } else if (currentReachedGoal.getLabels().contains("V4")) {
                if(Utils.getLabelsFromMainGoal(this.goals).contains("V-3") ||
                        Utils.getLabelsFromMainGoal(this.goals).contains("V-4")
                ){
                    this.preconditions.get(currentReachedGoal.getIndexOfV4()).removeLabel("V4");
                }
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
        int indexOfFirstPreconditionToInspect = Utils.findFirstPreconditionToInspect(this.preconditions);
        if (indexOfFirstPreconditionToInspect != -1) {
            Precondition preconditionToInspect = preconditions.get(indexOfFirstPreconditionToInspect);
            if (preconditionToInspect.getMainSign().equals("⊃")) {
                Goal newGoal = new Goal(preconditionToInspect.getLeftPart(), "V4", false);
                addNewGoal(indexOfFirstPreconditionToInspect, preconditionToInspect, newGoal);
                forthPoint();
            } else if (preconditionToInspect.getMainSign().equals("¬") && preconditionToInspect.getRightPart().length() != 1) {
                Goal newGoal = new Goal(preconditionToInspect.getRightPart(), "V4", false);
                System.out.println("Adding new goal -> " + newGoal);
                newGoal.addLabel(Utils.getLabelsFromMainGoal(this.goals));
                if (preconditionToInspect.getLabels().contains("V2")) {
                    newGoal.addLabel("V2");
                    newGoal.setIndexOfV2(indexOfFirstPreconditionToInspect);
                }
                newGoal.setIndexOfV4(indexOfFirstPreconditionToInspect);
                goals.push(newGoal);
                System.out.println("Updating preconditions label -> " + preconditionToInspect);
                preconditions.get(indexOfFirstPreconditionToInspect).addLabel("V4");
                forthPoint();
            } else if (preconditionToInspect.getMainSign().equals("∨")) {
                Goal newGoal = new Goal("(¬" + preconditionToInspect.getLeftPart() + ")", "V4", false);
                addNewGoal(indexOfFirstPreconditionToInspect, preconditionToInspect, newGoal);
                forthPoint();
            } else {
                fifteenthPoint();
            }
        } else {
            fifteenthPoint();
        }
    }

    public String fifteenthPoint() throws MetaStatementNotProvableException {
        return seventeenthPoint();
    }

    public String seventeenthPoint() throws MetaStatementNotProvableException {
        System.out.println("17 point is called");
        Goal currentGoal = goals.peek();
        if (currentGoal.getFormula().equals("⊥")) {
            if (currentGoal.getLabels().contains("V-2") || currentGoal.getLabels().contains("V-1")) {
                System.out.println("The current goal is unreachable, algorithm finishes its work");
                for (Precondition next : this.preconditions) {
                    if ((next.getLabels().contains("V-1") || next.getLabels().contains("V-2")) && next.getLabels().contains("V+0")) {
                        this.preconditions.get(next.getIndexOfV0()).removeLabel("V0");
                    }
                }
                this.preconditions.removeIf(next -> next.getLabels().contains("V-1") || next.getLabels().contains("V-2"));

                throw new MetaStatementNotProvableException("The current goal is ⊥ with label V-1 and cant be proved", 17);
            }
            if (currentGoal.getLabels().contains("V-3")) {
                throw new TemporaryMovingToSecondSubPointOrEight();
            } else if (currentGoal.getLabels().contains("V-4")) {
                throw new TemporaryMovingToThirdSubPointOrEight();
            } else {
                System.out.println("The current goal is unreachable, algorithm finishes its work");
                throw new MetaStatementNotProvableException("The current goal is ⊥ and cant be proved", 17);
            }
        }
        throw new MetaStatementNotProvableException("The current goal is ⊥ and cant be proved", 17);
    }

    private void addNewGoal(int indexOfFirstPreconditionToInspect, Precondition preconditionToInspect, Goal newGoal) {
        System.out.println("Adding new goal -> " + newGoal);
        newGoal.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        newGoal.setIndexOfV4(indexOfFirstPreconditionToInspect);
        goals.push(newGoal);
        System.out.println("Updating preconditions label -> " + preconditionToInspect);
        preconditions.get(indexOfFirstPreconditionToInspect).addLabel("V4");
    }

}
