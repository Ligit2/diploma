package com.example.algorithm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class EleventhPointTest {

    private Algorithm algorithm;
    List<Precondition> preconditions;
    Stack<Goal> goals;

    @BeforeEach
    void initialize() {
        algorithm = Mockito.mock(Algorithm.class, Mockito.CALLS_REAL_METHODS);
        preconditions = new ArrayList<>();
        goals = new Stack<>();
        algorithm.goals = goals;
        algorithm.preconditions = preconditions;
    }

    @Test
    void testWhenCurrentGoalIsReached() {
        Goal currentGoal = new Goal("A", false);
        goals.add(currentGoal);
        Precondition precondition = new Precondition("A", "parcel");
        preconditions.add(precondition);
        Mockito.doNothing().when(algorithm).twelfthPoint();
        algorithm.eleventhPoint();
        Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(1)).twelfthPoint();
    }

    @Test
    void testWhenCurrentGoalIsNotReachedAsPreconditionIsBlocked() {
        Goal currentGoal = new Goal("A", false);
        goals.add(currentGoal);
        Precondition precondition = new Precondition("A", "parcel");
        precondition.setBlocked();
        preconditions.add(precondition);
        Mockito.doNothing().when(algorithm).thirteenthPoint(currentGoal);
        algorithm.eleventhPoint();
        Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(0)).twelfthPoint();
        Mockito.verify(algorithm, Mockito.times(1)).thirteenthPoint(currentGoal);
    }

    @Test
    void testWhenCurrentGoalIsNotReachedAsPreconditionIsNotPresent() {
        Goal currentGoal = new Goal("A", false);
        goals.add(currentGoal);
        Mockito.doNothing().when(algorithm).thirteenthPoint(currentGoal);
        algorithm.eleventhPoint();
        Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(0)).twelfthPoint();
        Mockito.verify(algorithm, Mockito.times(1)).thirteenthPoint(currentGoal);
    }

    @Test
    void testWhenCurrentGoalisHakasutyunIsNotReachedAsPreconditionIsNotPresent() {
        Goal currentGoal = new Goal("⊥", false);
        goals.add(currentGoal);
        Mockito.doNothing().when(algorithm).thirteenthPoint(currentGoal);
        algorithm.eleventhPoint();
        Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(0)).twelfthPoint();
        Mockito.verify(algorithm, Mockito.times(1)).thirteenthPoint(currentGoal);
    }

    @Test
    void testWhenCurrentGoalIshakasutyunButIsNotReachedAsOneOfPreconditionsIsBlocked() {
        Goal currentGoal = new Goal("⊥", false);
        goals.add(currentGoal);
        Precondition precondition1 = new Precondition("A", "parcel");
        Precondition precondition2 = new Precondition("(¬A)", "parcel");
        precondition1.setBlocked();
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        Mockito.doNothing().when(algorithm).thirteenthPoint(currentGoal);
        algorithm.eleventhPoint();
        Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(1)).doesPredicationsHaveContradiction();
        Mockito.verify(algorithm, Mockito.times(0)).twelfthPoint();
        Mockito.verify(algorithm, Mockito.times(1)).thirteenthPoint(currentGoal);
        Assertions.assertFalse(algorithm.doesPredicationsHaveContradiction());
    }

    @Test
    void testWhenCurrentGoalIshakasutyunIsReached() {
        Goal currentGoal = new Goal("⊥", false);
        goals.add(currentGoal);
        Precondition precondition1 = new Precondition("A", "parcel");
        Precondition precondition2 = new Precondition("(¬A)", "parcel");
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        Mockito.doNothing().when(algorithm).twelfthPoint();
        algorithm.eleventhPoint();
        Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(1)).doesPredicationsHaveContradiction();
        Mockito.verify(algorithm, Mockito.times(1)).twelfthPoint();
        Assertions.assertTrue(algorithm.doesPredicationsHaveContradiction());
    }

}