package com.example.algorithm;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Utils {
    static boolean updatePreconditions(Stack<Goal> goals, List<Precondition> preconditions, String part, int parentIndex) {
        Precondition p = new Precondition(part, "&e", "V+0");
        p.setIndexOfV0(parentIndex);
        System.out.println("Adding precondition -> " + p);
        p.addLabel(getLabelsFromMainGoal(goals));
        preconditions.add(p);
        return true;
    }


    static void updatePreconditions(Stack<Goal> goals, List<Precondition> preconditions, int parentIndex, String s) {
        preconditions.get(parentIndex).addLabel("V0");
        System.out.println("Updating label of precondition -> " + preconditions.get(parentIndex));
        Precondition p = new Precondition(preconditions.get(parentIndex).getRightPart(), s, "V+0");
        p.setIndexOfV0(parentIndex);
        System.out.println("Adding precondition -> " + p);
        p.addLabel(getLabelsFromMainGoal(goals));
        preconditions.add(p);
    }


    static boolean isPreconditionPresent(List<Precondition> preconditions, String preconditionFormula) {
        return preconditions.stream().anyMatch(precondition -> !precondition.isBlocked() && precondition.getFormula().equals(preconditionFormula));
    }

    static int findFirstPreconditionToInspect(List<Precondition> preconditions) {
        for (int i = 0; i < preconditions.size(); i++) {
            if (!preconditions.get(i).isBlocked() && preconditions.get(i).getFormula().length() != 1 && !preconditions.get(i).getLabels().contains("V0") && !preconditions.get(i).getLabels().contains("V4")) {
                return i;
            }
        }
        return -1;
    }

    static boolean doesPredicationsHaveContradiction(List<Precondition> preconditions) {
        return preconditions.stream().anyMatch(precondition -> !precondition.isBlocked() && isPreconditionPresent(preconditions, "(¬" + precondition.getFormula() + ")"));
    }

    static int getTheLastParcel(List<Precondition> preconditions) {
        for (int i = preconditions.size() - 1; i >= 0; i--) {
            if (!preconditions.get(i).isBlocked() && preconditions.get(i).getType().equals("parcel")) {
                return i;
            }
        }
        return -1;
    }

    static int indexOf(List<Precondition> preconditions, String formula) {
        for (int i = 0; i < preconditions.size(); i++) {
            if (preconditions.get(i).getFormula().equals(formula) && !preconditions.get(i).isBlocked())
                return i;
        }
        return -1;
    }

    static void blockPreconditionsAndResetLabels(List<Precondition> preconditions, int index) {
        for (int i = index; i < preconditions.size(); i++) {
            int indexOfV0 = preconditions.get(i).getIndexOfV0();
            if (indexOfV0 != -1) {
                System.out.println("Remove V0 label from " + preconditions.get(indexOfV0));
                preconditions.get(indexOfV0).removeLabel("V0");
            }
            preconditions.get(i).setBlocked();
        }
    }

    static Goal getMainGoal(Stack<Goal> goals) {
        return goals.stream().filter(Goal::isMainGoal).findFirst().get();
    }

    static void removePreconditionsWithLabelsV4(List<Precondition> preconditions) {
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

    static void removePreconditionsWithLabelsV3(List<Precondition> preconditions) {
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

    static void removeLabelsFromPreconditions(List<Precondition> preconditions) {
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

}
