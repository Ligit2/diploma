package com.example.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Algorithm {
    List<Precondition> preconditions = new ArrayList<>();
    Stack<Goal> goals = new Stack<>();
    Stack<Goal> goalsToTrack = new Stack<>();
    int count = 0;
    private static final Logger logger = LoggerFactory.getLogger(Algorithm.class);

    Algorithm(List<Precondition> preconditions, Stack<Goal> goals) {
        this.goals = goals;
        this.preconditions = preconditions;

    }

    private void fillTheStack() {
        this.goalsToTrack.addAll(this.goals);
    }

    public Algorithm() {

    }


    public void isReachable() throws MetaStatementNotProvableException {
        boolean checkIfPreconditionsPresent = secondPoint();
        if (checkIfPreconditionsPresent) {
            thirdPoint();
        } else {
            forthPoint();
        }
    }

    public void start(String mainFormula, String preconditionsList) throws MetaStatementNotProvableException, InterruptedException {
        firstPoint(mainFormula, preconditionsList);
        fillTheStack();
        boolean checkIfPreconditionsPresent = secondPoint();
        if (checkIfPreconditionsPresent) {
            thirdPoint();
        } else {
            forthPoint();
        }
    }


    public void firstPoint(String mainFormula, String preconditionsList) {
        logger.info("1 point is called");
        count++;
        if (preconditionsList.length() != 0) {
            String[] split = preconditionsList.split(",");
            for (String s : split) {
                preconditions.add(new Precondition(s, "parcel"));
            }
        }
        goals.add(new Goal(mainFormula, true));
    }

    public boolean secondPoint() {
        count++;
        logger.info("2 point is called");
        return !preconditions.isEmpty();
    }

    public void thirdPoint() {
        count++;
        logger.info("3 point is called");
        for (int i = 0; i < preconditions.size(); i++) {
            if (preconditions.get(i).getFormula().length() != 1 &&
                    !preconditions.get(i).getLabels().contains("V0") &&
                    !preconditions.get(i).isBlocked()
            ) {
                switch (preconditions.get(i).getMainSign()) {
                    case "&": {
                        Utils.updatePreconditions(this.goals, this.preconditions, preconditions.get(i).getRightPart(), i);
                        Utils.updatePreconditions(this.goals, this.preconditions, preconditions.get(i).getLeftPart(), i);
                        preconditions.get(i).addLabel("V0");
                        logger.info("Updating label of precondition - {}", preconditions.get(i));
                        break;
                    }
                    case "∨": {
                        if (Utils.isPreconditionPresent(this.preconditions,
                                constructDenial(preconditions.get(i).getLeftPart()))) {
                            Utils.updatePreconditions(this.goals, this.preconditions, i, "∨e");
                        }

                        break;
                    }
                    case "¬": {
                        Precondition precondition = new Precondition(preconditions.get(i).getRightPart());
                        if (!(precondition.getFormula().length() == 1) && precondition.getMainSign().equals("¬")) {
                            Utils.updatePreconditions(this.goals, this.preconditions, i, precondition);
                        }
                        break;
                    }
                    case "⊃": {
                        if (Utils.isPreconditionPresent(this.preconditions, preconditions.get(i).getLeftPart())) {
                            Utils.updatePreconditions(this.goals, this.preconditions, i, "⊃e");
                        }
                        break;
                    }
                }
            }
        }
        eleventhPoint();
    }

    private String constructDenial(String formula) {
        return "(¬" + formula + ")";
    }

    public void forthPoint() {
        logger.info("4 point is called");
        count++;
        Goal currentGoal = goals.peek();
        String currentGoalMainSign = currentGoal.getMainSign();
        if ((currentGoal.formula.length() == 1 &&
                !currentGoal.getFormula().equals("⊥")) ||
                currentGoalMainSign.equals("¬")) {
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
            throw new FormulaContainsUnknownMainSignException();
        }
    }

    public void fifthPoint(Goal currentGoal) {
        System.out.println("5 called");
        count++;
        String formulaWithDenial = "(¬" + currentGoal.formula + ")";
        Precondition newPrecondition = new Precondition(formulaWithDenial, "parcel");
        logger.info("Adding new precondition - {} ", newPrecondition);
        preconditions.add(newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        Goal newGoal = new Goal("⊥", false);
        logger.info("Adding new goal - {} ", newGoal);
        newGoal.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        goals.push(newGoal);
        goalsToTrack.push(newGoal);
    }

    public void sixthPoint(Goal currentGoal) {
        logger.info("6 point is called");
        count++;
        Precondition newPrecondition = new Precondition(currentGoal.getLeftPart(), "parcel");
        logger.info("Adding new precondition - {} ", newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
        Goal newGoal = new Goal(currentGoal.getRightPart(), false);
        logger.info("Adding new goal - {} ", newGoal);
        newGoal.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        goals.push(newGoal);
        goalsToTrack.push(newGoal);
    }

    public void seventhPoint(Goal currentGoal) {
        logger.info("7 point is called");
        count++;
        Goal Wj = new Goal(currentGoal.getRightPart(), true);
        Goal Wi = new Goal(currentGoal.getLeftPart(), true);
        Wj.addLabel("V-2");
        Wi.addLabel("V-1");
        Goal goal = new Goal(Wj.getFormula(), "V-2", false);

        this.goals.push(goal);
        this.goalsToTrack.push(goal);

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
        logger.info("8 point is called");
        count++;
        Goal Wj = new Goal(currentGoal.getRightPart(), true);
        Goal Wi = new Goal(currentGoal.getLeftPart(), true);
        Wj.addLabel("V-4");
        Wi.addLabel("V-3");
        Goal goal = new Goal(Wi.getFormula(), "V-3", false);
        this.goals.push(goal);

        //this.goalsToTrack.push(goal);

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
    }

    public void thirdSubPointOfEight(Goal currentGoal, Algorithm algorithm) {
        logger.info("8.3 point is called");
        count++;
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

    public void firstSubPointOfEight(Algorithm algorithm) {
        logger.info("8.1 point is called");
        count++;
        algorithm.isReachable();
        eleventhPoint();
    }

    public void secondSubPointOfEight(Algorithm algorithm, Goal Wj) {
        logger.info("8.2 point is called");
        count++;
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
        logger.info("11 point is called");
        count++;
        Goal currentGoal = goals.peek();
        if (Utils.isPreconditionPresent(this.preconditions, currentGoal.getFormula())) {
            logger.info("Current goal - {} is reached as it is present in preconditions", currentGoal);
            twelfthPoint();
        } else if (currentGoal.getFormula().equals("⊥") && Utils.doesPredicationsHaveContradiction(this.preconditions)) {
            logger.info("Current goal - {} is reached as contradiction is present in preconditions", currentGoal);
            twelfthPoint();
        } else {
            thirteenthPoint(currentGoal);
        }
    }

    public void twelfthPoint() {
        logger.info("12 point is called");
        count++;
        Goal currentReachedGoal = goals.pop();
        if (currentReachedGoal.isMainGoal()) {
            logger.info("The main goal - {} is reached", currentReachedGoal);
        } else {
            logger.info("Current goal - {}  is removed from goals as it is reached", currentReachedGoal);
            Goal previousGoal = goals.peek();
            int index = Utils.getTheLastParcel(this.preconditions);
            if (currentReachedGoal.formula.equals("⊥") && index != -1) {
                updatePreconditionsAsCurrentReachedGoalIsContradiction(index);
            } else if (!previousGoal.isSimple() &&
                    previousGoal.getMainSign().equals("⊃") &&
                    previousGoal.getRightPart().equals(currentReachedGoal.formula)) {
                updatePreconditionsAsCurrentGoalIsImplication(previousGoal);
            } else if (!previousGoal.isSimple() &&
                    previousGoal.getMainSign().equals("&") &&
                    previousGoal.getRightPart().equals(currentReachedGoal.formula)) {
                updatePreconditionsAsCurrentReachedGoalIsConjunction(previousGoal);
            } else if (!previousGoal.isSimple() &&
                    previousGoal.getMainSign().equals("∨") &&
                    (previousGoal.getRightPart().equals(currentReachedGoal.formula) ||
                            previousGoal.getLeftPart().equals(currentReachedGoal.formula))) {
                updatePreconditionsAsCurrentReachedGoalIsDisjunction(previousGoal);
            } else if (currentReachedGoal.getLabels().contains("V4")) {
                removeCurrentReachedGoalAsItHasV4Label(currentReachedGoal);
            }
            thirdPoint();
        }
    }

    public void thirteenthPoint(Goal currentGoal) {
        logger.info("13 point is called");
        count++;
        if (currentGoal.getFormula().equals("⊥")) {
            fourteenthPoint();
        } else {
            forthPoint();
        }
    }

    public void fourteenthPoint() {
        logger.info("14 point is called");
        count++;
        int indexOfFirstPreconditionToInspect = Utils.findFirstPreconditionToInspect(this.preconditions);
        if (indexOfFirstPreconditionToInspect != -1) {
            Precondition preconditionToInspect = preconditions.get(indexOfFirstPreconditionToInspect);
            if (preconditionToInspect.getMainSign().equals("⊃")) {
                Goal newGoal = new Goal(preconditionToInspect.getLeftPart(), "V4", false);
                addNewGoal(indexOfFirstPreconditionToInspect, preconditionToInspect, newGoal);
                forthPoint();
            } else if (preconditionToInspect.getMainSign().equals("¬") &&
                    preconditionToInspect.getRightPart().length() != 1) {
                Utils.updateGoals(this.goalsToTrack, this.goals, this.preconditions,
                        indexOfFirstPreconditionToInspect, preconditionToInspect);
                forthPoint();
            } else if (preconditionToInspect.getMainSign().equals("∨")) {
                Goal newGoal = new Goal(constructDenial(preconditionToInspect.getLeftPart()),
                        "V4", false);
                addNewGoal(indexOfFirstPreconditionToInspect, preconditionToInspect, newGoal);
                forthPoint();
            } else {
                fifteenthPoint();
            }
        } else {
            fifteenthPoint();
        }
    }

    public void fifteenthPoint() throws MetaStatementNotProvableException {
        logger.info("15 point is called");
        count++;
        seventeenthPoint();
    }

    public void seventeenthPoint() throws MetaStatementNotProvableException {
        logger.info("17 point is called");
        count++;
        Goal currentGoal = goals.peek();
        if (currentGoal.getFormula().equals("⊥")) {
            if (currentGoal.getLabels().contains("V-2") || currentGoal.getLabels().contains("V-1")) {
                logger.error("The current goal - {} is unreachable , algorithm finishes its work", currentGoal);
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
                logger.error("The current goal - {} is unreachable , algorithm finishes its work", currentGoal);
                throw new MetaStatementNotProvableException("The current goal is ⊥ and cant be proved", 17);
            }
        }
        throw new MetaStatementNotProvableException("The current goal is ⊥ and cant be proved", 17);
    }

    private void addNewGoal(int indexOfFirstPreconditionToInspect, Precondition preconditionToInspect, Goal newGoal) {
        logger.info("Adding new goal -{}", newGoal);
        newGoal.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        newGoal.setIndexOfV4(indexOfFirstPreconditionToInspect);
        goals.push(newGoal);
        goalsToTrack.push(newGoal);

        logger.info("Updating label for precondition - {}", preconditionToInspect);
        preconditions.get(indexOfFirstPreconditionToInspect).addLabel("V4");
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

    private void removeCurrentReachedGoalAsItHasV4Label(Goal currentReachedGoal) {
        if (Utils.getLabelsFromMainGoal(this.goals).contains("V-3") ||
                Utils.getLabelsFromMainGoal(this.goals).contains("V-4")
        ) {
            this.preconditions.get(currentReachedGoal.getIndexOfV4()).removeLabel("V4");
        }
        logger.info("Current goal - {} has V4 label", currentReachedGoal);
    }

    private void updatePreconditionsAsCurrentReachedGoalIsDisjunction(Goal previousGoal) {
        Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "∨a");
        logger.info("Adding new precondition -{}", newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
        if (previousGoal.getLabels().contains("V4")) {
            preconditions.get(previousGoal.getIndexOfV4()).removeLabel("V4");
        }
    }

    private void updatePreconditionsAsCurrentReachedGoalIsConjunction(Goal previousGoal) {
        Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "&a", "V0");
        logger.info("Adding new precondition -{}", newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
    }

    private void updatePreconditionsAsCurrentGoalIsImplication(Goal previousGoal) {
        Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "⊃a");
        logger.info("Adding new precondition -{}", newPrecondition);
        int indexOfLeftPart = Utils.indexOf(this.preconditions, previousGoal.getLeftPart());
        if (indexOfLeftPart != -1) {
            logger.info("Blocking from - {} to - {}", preconditions.get(indexOfLeftPart), newPrecondition);
            Utils.blockPreconditionsAndResetLabels(this.preconditions, indexOfLeftPart);
        }
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
        if (previousGoal.getLabels().contains("V4")) {
            preconditions.get(previousGoal.getIndexOfV4()).removeLabel("V4");
        }
    }

    private void updatePreconditionsAsCurrentReachedGoalIsContradiction(int index) {
        Precondition newPrecondition = new Precondition("(¬" + preconditions.get(index).getFormula() + ")", "¬a");
        logger.info("Blocking from - {} to - {}", preconditions.get(index), newPrecondition);
        Utils.blockPreconditionsAndResetLabels(this.preconditions, index);
        logger.info("Adding new precondition -{}", newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
    }

}
