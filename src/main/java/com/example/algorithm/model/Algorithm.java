package com.example.algorithm.model;

import com.example.algorithm.exception.FormulaContainsUnknownMainSignException;
import com.example.algorithm.exception.MetaStatementNotProvableException;
import com.example.algorithm.exception.TemporaryMovingToSecondSubPointOrEight;
import com.example.algorithm.exception.TemporaryMovingToThirdSubPointOrEight;
import com.example.algorithm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Algorithm {
    List<Precondition> preconditions = new ArrayList<>();
    Stack<Goal> goals = new Stack<>();

    Algorithm(List<Precondition> preconditions, Stack<Goal> goals) {
        this.goals = goals;
        this.preconditions = preconditions;
    }

    public Algorithm() {
    }

    public List<Precondition> getPreconditions() {
        return preconditions;
    }

    public void setPreconditions(List<Precondition> preconditions) {
        this.preconditions = preconditions;
    }

    public void setGoals(Stack<Goal> goals) {
        this.goals = goals;
    }

    public void prove() {
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
        countOfSteps++;
        if (preconditionsList.length() != 0) {
            String[] split = preconditionsList.split(",");
            for (String formula : split) {
                preconditions.add(new Precondition(formula, "parcel"));
            }
        }
        goals.add(new Goal(mainFormula, true));
    }

    public boolean secondPoint() {
        logger.info("2 point is called");
        return !preconditions.isEmpty();
    }

    public void thirdPoint() {
        logger.info("3 point is called");
        countOfSteps++;
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
                    }
                }
            }
        }
        ninthPoint();
    }

    public void forthPoint() {
        logger.info("4 point is called");
        countOfSteps++;
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
            twelfthPoint();
        } else if (currentGoal.getMainSign().equals("&")) {
            seventhPoint(currentGoal);
        } else if (currentGoal.getMainSign().equals("∨")) {
            eightPoint(currentGoal);
        } else {
            throw new FormulaContainsUnknownMainSignException();
        }
    }

    public void fifthPoint(Goal currentGoal) {
        logger.info("4 point is called");
        countOfSteps++;
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
        countOfSteps++;
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
        countOfSteps++;
        Goal Wj = new Goal(currentGoal.getRightPart(), "V-1", true);
        Goal Wi = new Goal(currentGoal.getLeftPart(), "V-2", true);
        Goal goalWj = new Goal(Wj.getFormula(), "V-2", false);
        Goal goalWi = new Goal(Wi.getFormula(), "V-1", false);
        this.goals.push(goalWj);
        this.goalsToTrack.push(goalWi);
        this.goalsToTrack.push(goalWj);
        Stack<Goal> newGoals = new Stack<>();
        newGoals.push(Wi);
        Algorithm algorithm = new Algorithm(preconditions, newGoals);
        algorithm.prove();
        Utils.removeLabelsFromPreconditions(algorithm.preconditions);
        algorithm.goals.push(Wj);
        algorithm.prove();
        Utils.removeLabelsFromPreconditions(algorithm.preconditions);
        ninthPoint();
    }

    public void eightPoint(Goal currentGoal) {
        logger.info("8 point is called");
        Goal Wj = new Goal(currentGoal.getRightPart(), true);
        Goal Wi = new Goal(currentGoal.getLeftPart(), true);
        Wj.addLabel("V-4");
        Wi.addLabel("V-3");
        Goal goal = new Goal(Wi.getFormula(), "V-3", false);
        this.goals.push(goal);
        this.goalsToTrack.push(goal);
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

    public void firstSubPointOfEight(Algorithm algorithm) {
        logger.info("8.1 point is called");
        countOfSteps++;
        algorithm.prove();
        ninthPoint();
    }

    public void secondSubPointOfEight(Algorithm algorithm, Goal Wj) {
        logger.info("8.2 point is called");
        countOfSteps++;
        while (!algorithm.goals.isEmpty()) {
            Goal pop = algorithm.goals.pop();
            if (pop.getLabels().contains("V4")) {
                this.preconditions.get(pop.getIndexOfV4()).removeLabel("V4");
            }
        }
        this.goals.pop();
        Utils.removePreconditionsWithLabelsV3(algorithm.preconditions);
        algorithm.goals.push(Wj);
        Goal goal = new Goal(Wj.getFormula(), "V-4", false);
        this.goals.push(goal);
        this.goalsToTrack.push(goal);
        algorithm.prove();
        ninthPoint();
    }

    public void thirdSubPointOfEight(Goal currentGoal, Algorithm algorithm) {
        logger.info("8.3 point is called");
        countOfSteps++;
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
            Goal goal = new Goal("⊥", false);
            this.goals.push(goal);
            this.goalsToTrack.push(goal);
        }
        thirdPoint();
    }

    public void ninthPoint() {
        logger.info("9 point is called");
        countOfSteps++;
        Goal currentGoal = goals.peek();
        if (Utils.isPreconditionPresent(this.preconditions, currentGoal.getFormula())) {
            logger.info("Current goal - {} is reached as it is present in preconditions", currentGoal);
            tenthPoint();
        } else if (currentGoal.getFormula().equals("⊥") && Utils.doesPredicationsHaveContradiction(this.preconditions)) {
            logger.info("Current goal - {} is reached as contradiction is present in preconditions", currentGoal);
            tenthPoint();
        } else {
            eleventhPoint(currentGoal);
        }
    }

    public void tenthPoint() {
        logger.info("10 point is called");
        countOfSteps++;
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
                updatePreconditionsAsPreviousGoalContainsImplication(previousGoal);
            } else if (!previousGoal.isSimple() &&
                    previousGoal.getMainSign().equals("&") &&
                    previousGoal.getRightPart().equals(currentReachedGoal.formula)) {
                updatePreconditionsAsPreviousGoalContainsConjunction(previousGoal);
            } else if (!previousGoal.isSimple() &&
                    previousGoal.getMainSign().equals("∨") &&
                    (previousGoal.getRightPart().equals(currentReachedGoal.formula) ||
                            previousGoal.getLeftPart().equals(currentReachedGoal.formula))) {
                updatePreconditionsAsPreviousGoalContainsDisjunction(previousGoal);
            } else if (currentReachedGoal.getLabels().contains("V4")) {
                removeCurrentReachedGoalAsItHasV4Label(currentReachedGoal);
            }
            thirdPoint();
        }
    }

    public void eleventhPoint(Goal currentGoal) {
        logger.info("11 point is called");
        countOfSteps++;
        if (currentGoal.getFormula().equals("⊥")) {
            twelfthPoint();
        } else {
            forthPoint();
        }
    }

    public void twelfthPoint() {
        logger.info("12 point is called");
        countOfSteps++;
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
                thirteenthPoint();
            }
        } else {
            thirteenthPoint();
        }
    }


    public void thirteenthPoint() {
        logger.info("13 point is called");
        countOfSteps++;
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
                throw new MetaStatementNotProvableException();
            }
            if (currentGoal.getLabels().contains("V-3")) {
                throw new TemporaryMovingToSecondSubPointOrEight();
            } else if (currentGoal.getLabels().contains("V-4")) {
                throw new TemporaryMovingToThirdSubPointOrEight();
            } else {
                logger.error("The current goal - {} is unreachable , algorithm finishes its work", currentGoal);
                throw new MetaStatementNotProvableException();
            }
        }
        throw new MetaStatementNotProvableException();
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
        logger.info("Current goal - {} has label V4", currentReachedGoal);
    }

    private void updatePreconditionsAsPreviousGoalContainsDisjunction
            (Goal previousGoal) {
        Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "∨a");
        logger.info("Adding new precondition -{}", newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
        if (previousGoal.getLabels().contains("V4")) {
            preconditions.get(previousGoal.getIndexOfV4()).removeLabel("V4");
        }
    }

    private void updatePreconditionsAsPreviousGoalContainsConjunction
            (Goal previousGoal) {
        Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "&a", "V0");
        logger.info("Adding new precondition -{}", newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
    }

    private void updatePreconditionsAsPreviousGoalContainsImplication
            (Goal previousGoal) {
        Precondition newPrecondition = new Precondition(previousGoal.getFormula(), "⊃a");
        logger.info("Adding new precondition -{}", newPrecondition);
        int indexOfLeftPart = Utils.indexOf(this.preconditions, previousGoal.getLeftPart());
        if (indexOfLeftPart != -1) {
            logger.info("Blocking preconditions from - {} to - {}", preconditions.get(indexOfLeftPart), newPrecondition);
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
        logger.info("Blocking preconditions from - {} to - {}", preconditions.get(index), newPrecondition);
        Utils.blockPreconditionsAndResetLabels(this.preconditions, index);
        logger.info("Adding new precondition -{}", newPrecondition);
        newPrecondition.addLabel(Utils.getLabelsFromMainGoal(this.goals));
        preconditions.add(newPrecondition);
    }

    private String constructDenial(String formula) {
        return "(¬" + formula + ")";
    }

    Stack<Goal> goalsToTrack = new Stack<>();
    private static final Logger logger = LoggerFactory.getLogger(Algorithm.class);
    public int countOfSteps;

    public Stack<Goal> getGoalsToTrack() {
        return goalsToTrack;
    }

    private void fillTheStack() {
        this.goalsToTrack.addAll(this.goals);
    }

}
