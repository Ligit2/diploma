package com.example.algorithm;

import com.example.algorithm.model.Algorithm;
import com.example.algorithm.model.Goal;
import com.example.algorithm.model.Precondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SixthPointTest {

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
    void testWhenPreconditionIsPresent() {
        Goal currentGoal = new Goal("(A⊃B)", false);
        Goal resultGoal = new Goal("B", false);
        goals.push(currentGoal);
        Precondition precondition = new Precondition("A", "parcel");
        preconditions.add(precondition);
        algorithm.sixthPoint(currentGoal);
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertEquals(goals.size(), 2);
        Assertions.assertTrue(goals.contains(resultGoal));
    }

    @Test
    void testWhenPreconditionIsNotPresent() {
        Goal currentGoal = new Goal("(A⊃B)", false);
        Goal resultGoal = new Goal("B", false);
        goals.push(currentGoal);
        Precondition result = new Precondition("A", "parcel");
        algorithm.sixthPoint(currentGoal);
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertTrue(preconditions.contains(result));
        Assertions.assertEquals(goals.size(), 2);
        Assertions.assertTrue(goals.contains(resultGoal));
    }

    @Test
    void testWhenPreconditionIsBlocked() {
        Goal currentGoal = new Goal("(A⊃B)", false);
        Goal resultGoal = new Goal("B", false);
        goals.push(currentGoal);
        Precondition precondition = new Precondition("A", "parcel");
        precondition.setBlocked();
        Precondition result = new Precondition("A", "parcel");
        preconditions.add(precondition);
        algorithm.sixthPoint(currentGoal);
        Assertions.assertEquals(preconditions.size(), 2);
        Assertions.assertTrue(preconditions.contains(result));
        Assertions.assertEquals(goals.size(), 2);
        Assertions.assertTrue(goals.contains(resultGoal));
    }


}
