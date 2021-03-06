package com.example.algorithm;

import com.example.algorithm.model.Algorithm;
import com.example.algorithm.model.Goal;
import com.example.algorithm.model.Precondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ThirteenthPointTest {

    private Algorithm algorithm;
    List<Precondition> preconditions;
    Stack<Goal> goals;

    @BeforeEach
    void initialize() {
        algorithm = Mockito.mock(Algorithm.class, Mockito.CALLS_REAL_METHODS);
        preconditions = new ArrayList<>();
        goals = new Stack<>();
        algorithm.setGoals(goals);
        algorithm.setPreconditions(preconditions);
    }

    @Test
    void testWhenCurrentGoalIsNotAContradiction() {
        Goal currentGoal = new Goal("(A⊃B)", false);
        goals.push(currentGoal);
        Mockito.doNothing().when(algorithm).forthPoint();
        algorithm.eleventhPoint(currentGoal);
        Mockito.verify(algorithm, Mockito.times(1)).forthPoint();
        Mockito.verify(algorithm, Mockito.never()).twelfthPoint();
    }

    @Test
    void testWhenCurrentGoalIsAContradiction() {
        Goal currentGoal = new Goal("⊥", false);
        goals.push(currentGoal);
        Mockito.doNothing().when(algorithm).twelfthPoint();
        algorithm.eleventhPoint(currentGoal);
        Mockito.verify(algorithm, Mockito.times(1)).twelfthPoint();
        Mockito.verify(algorithm, Mockito.never()).forthPoint();
    }
}
