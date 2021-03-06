package com.example.algorithm.utils;

import com.example.algorithm.model.Goal;
import com.example.algorithm.model.Precondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static void updatePreconditions(Stack<Goal> goals, List<Precondition> preconditions, String part, int parentIndex) {
        Precondition p = new Precondition(part, "&e", "V+0");
        p.setIndexOfV0(parentIndex);
        logger.info("Adding precondition - {}", p);
        p.addLabel(getLabelsFromMainGoal(goals));
        preconditions.add(p);
    }

    public static void updatePreconditions(Stack<Goal> goals, List<Precondition> preconditions, int i, Precondition precondition) {
        preconditions.get(i).addLabel("V0");
        logger.info("Updating label of precondition - {}", preconditions.get(i));
        Precondition p = new Precondition(precondition.getRightPart(), "¬¬e", "V+0");
        p.setIndexOfV0(i);
        logger.info("Adding precondition - {}", p);
        List<String> labelsFromMainGoal = Utils.getLabelsFromMainGoal(goals);
        if (!labelsFromMainGoal.isEmpty()) {
            p.addLabel(labelsFromMainGoal);
        }
        preconditions.add(p);
    }

    public static void updateGoals(Stack<Goal> goalsToTrack, Stack<Goal> goals, List<Precondition> preconditions, int indexOfFirstPreconditionToInspect, Precondition preconditionToInspect) {
        Goal newGoal = new Goal(preconditionToInspect.getRightPart(), "V4", false);
        logger.info("Adding new goal - {}", newGoal);
        newGoal.addLabel(Utils.getLabelsFromMainGoal(goals));
        if (preconditionToInspect.getLabels().contains("V2")) {
            newGoal.addLabel("V2");
            newGoal.setIndexOfV2(indexOfFirstPreconditionToInspect);
        }
        newGoal.setIndexOfV4(indexOfFirstPreconditionToInspect);
        goals.push(newGoal);
        goalsToTrack.push(newGoal);
        logger.info("Updating preconditions label - {} ", preconditionToInspect);
        preconditions.get(indexOfFirstPreconditionToInspect).addLabel("V4");
    }


    public static void updatePreconditions(Stack<Goal> goals, List<Precondition> preconditions, int parentIndex, String s) {
        preconditions.get(parentIndex).addLabel("V0");
        logger.info("Updating label of precondition - {} ", preconditions.get(parentIndex));
        Precondition precondition = new Precondition(preconditions.get(parentIndex).getRightPart(), s, "V+0");
        precondition.setIndexOfV0(parentIndex);
        logger.info("Adding new precondition - {}", precondition);
        precondition.addLabel(getLabelsFromMainGoal(goals));
        preconditions.add(precondition);
    }


    public static boolean isPreconditionPresent(List<Precondition> preconditions, String preconditionFormula) {
        return preconditions.stream().anyMatch(precondition -> !precondition.isBlocked() &&
                precondition.getFormula().equals(preconditionFormula));
    }

    public static int findFirstPreconditionToInspect(List<Precondition> preconditions) {
        for (int i = 0; i < preconditions.size(); i++) {
            if (!preconditions.get(i).isBlocked() &&
                    preconditions.get(i).getFormula().length() != 1 &&
                    !preconditions.get(i).getLabels().contains("V0") &&
                    !preconditions.get(i).getLabels().contains("V4")) {
                return i;
            }
        }
        return -1;
    }

   public static boolean doesPredicationsHaveContradiction(List<Precondition> preconditions) {
        return preconditions.stream().anyMatch(precondition -> !precondition.isBlocked() && isPreconditionPresent(preconditions, "(¬" + precondition.getFormula() + ")"));
    }

    public static int getTheLastParcel(List<Precondition> preconditions) {
        for (int i = preconditions.size() - 1; i >= 0; i--) {
            if (!preconditions.get(i).isBlocked() && preconditions.get(i).getType().equals("parcel")) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(List<Precondition> preconditions, String formula) {
        for (int i = 0; i < preconditions.size(); i++) {
            if (preconditions.get(i).getFormula().equals(formula) && !preconditions.get(i).isBlocked())
                return i;
        }
        return -1;
    }

    public static void blockPreconditionsAndResetLabels(List<Precondition> preconditions, int index) {
        for (int i = index; i < preconditions.size(); i++) {
            int indexOfV0 = preconditions.get(i).getIndexOfV0();
            if (indexOfV0 != -1) {
                logger.info("Remove V0 label from - {}", preconditions.get(indexOfV0));
                preconditions.get(indexOfV0).removeLabel("V0");
            }
            preconditions.get(i).setBlocked();
        }
    }

    public static Goal getMainGoal(Stack<Goal> goals) {
        return goals.stream().filter(Goal::isMainGoal).findFirst().get();
    }

    public static void removePreconditionsWithLabelsV4(List<Precondition> preconditions) {
        for (Precondition next : preconditions) {
            if (next.getLabels().contains("V-4")) {
                if (next.getIndexOfV0() != -1) {
                    preconditions.get(next.getIndexOfV0()).removeLabel("V0");
                }
                if (next.getIndexOfV4() != -1) {
                    preconditions.get(next.getIndexOfV4()).removeLabel("V4");
                }
            }
        }
        preconditions.removeIf(next -> next.getLabels().contains("V-4"));
    }

    public static void removePreconditionsWithLabelsV3(List<Precondition> preconditions) {
        for (Precondition next : preconditions) {
            if (next.getLabels().contains("V-3")) {
                if (next.getIndexOfV0() != -1) {
                    preconditions.get(next.getIndexOfV0()).removeLabel("V0");
                }
                if (next.getIndexOfV4() != -1) {
                    preconditions.get(next.getIndexOfV4()).removeLabel("V4");
                }
            }
        }
        preconditions.removeIf(next -> next.getLabels().contains("V-3"));
    }

    public static void removeLabelsFromPreconditions(List<Precondition> preconditions) {
        preconditions.forEach(precondition -> {
            precondition.removeLabel("V-1");
            precondition.removeLabel("V-2");
        });
    }

    public static List<String> getLabelsFromMainGoal(Stack<Goal> goals) {
        Goal mainGoal = getMainGoal(goals);
        return mainGoal.getLabels().stream().filter(label ->
                        label.equals("V-1") ||
                                label.equals("V-2") ||
                                label.equals("V-3") ||
                                label.equals("V-4"))
                .collect(Collectors.toList());
    }

    public static String detectAndReplaceDisjunctionWithImplication(String formula) {
        String leftFormula = "";
        String rightFormula = "";
        for (int i = 0; i < formula.length(); i++) {
            if (Character.toString(formula.charAt(i)).equals("∨")) {
                int rightIndex = i - 1;
                int leftIndex = i + 1;
                leftFormula = getLeftPart(formula, i);
                rightFormula = getRightPart(formula, i);
                rightIndex -= rightFormula.length();
                leftIndex += leftFormula.length();
                String leftTrim = formula.substring(0, rightIndex + 1);
                String rightTrim = formula.substring(leftIndex);
                formula = leftTrim + "(¬" + rightFormula + ")" + "⊃" + leftFormula + rightTrim;
            }

        }
        return formula;
    }


    public static String getLeftPart(String formula, int i) {
        int bracketsCount = -1;
        boolean closingBracketExists = false;
        int leftIndex = i + 1;
        if (formula.charAt(leftIndex) != '(') {
            return Character.toString(formula.charAt(leftIndex));
        }
        for (leftIndex = i + 1; leftIndex < formula.length(); leftIndex++) {
            if (formula.charAt(leftIndex) == '(') {
                bracketsCount++;
            } else if (formula.charAt(leftIndex) == ')') {
                bracketsCount--;
                closingBracketExists = true;
            }
            if (closingBracketExists && bracketsCount == -1) {
                leftIndex++;
                return formula.substring(i + 1, leftIndex);
            }
        }
        return "";
    }


    public static String getRightPart(String formula, int i) {
        int bracketsCount = -1;
        boolean closingBracketExists;
        closingBracketExists = false;
        int rightIndex = i - 1;
        if (formula.charAt(rightIndex) != ')') {
            return Character.toString(formula.charAt(rightIndex));
        }
        for (rightIndex = i - 1; rightIndex >= 0; rightIndex--) {
            if (formula.charAt(rightIndex) == ')') {
                bracketsCount++;
            } else if (formula.charAt(rightIndex) == '(') {
                bracketsCount--;
                closingBracketExists = true;
            }
            if (closingBracketExists && bracketsCount == -1) {
                return formula.substring(rightIndex, i);
            }
        }
        return "";

    }
}
