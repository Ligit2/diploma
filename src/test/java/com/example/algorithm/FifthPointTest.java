package com.example.algorithm;

import com.example.algorithm.model.Algorithm;
import com.example.algorithm.model.Goal;
import com.example.algorithm.model.Precondition;
import com.example.algorithm.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FifthPointTest {

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
        Goal currentGoal = new Goal("(¬(A⊃B))", false);
        goals.push(currentGoal);
        Precondition precondition = new Precondition("(¬(¬(A⊃B)))", "parcel");
        preconditions.add(precondition);
        Goal resultGoal = new Goal("⊥", false);
        algorithm.fifthPoint(currentGoal);
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertTrue(goals.contains(resultGoal));
        Assertions.assertEquals(goals.size(), 2);
    }

    @Test
    void testWhenPreconditionIsNotPresent() {
        Goal currentGoal = new Goal("(¬(A⊃B))", false);
        goals.push(currentGoal);
        Precondition result = new Precondition("(¬(¬(A⊃B)))", "parcel");
        Goal resultGoal = new Goal("⊥", false);
        algorithm.fifthPoint(currentGoal);
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertEquals(preconditions.get(0), result);
        Assertions.assertTrue(goals.contains(resultGoal));
        Assertions.assertEquals(goals.size(), 2);
    }

    @Test
    void testWhenPreconditionIsBlocked() {
        Goal currentGoal = new Goal("(¬(A⊃B))", false);
        goals.push(currentGoal);
        Precondition precondition = new Precondition("(¬(¬(A⊃B)))", "parcel");
        precondition.setBlocked();
        preconditions.add(precondition);
        Precondition result = new Precondition("(¬(¬(A⊃B)))", "parcel");
        Goal resultGoal = new Goal("⊥", false);
        algorithm.fifthPoint(currentGoal);
        Assertions.assertEquals(preconditions.size(), 2);
        Assertions.assertEquals(preconditions.get(1), result);
        Assertions.assertTrue(goals.contains(resultGoal));
        Assertions.assertEquals(goals.size(), 2);
    }


}
