package com.example.algorithm;

import com.example.algorithm.model.Algorithm;
import com.example.algorithm.model.Goal;
import com.example.algorithm.model.Precondition;
import com.example.algorithm.utils.Utils;
import jdk.jshell.execution.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NinthPointTest {

    private Algorithm algorithm;
    private Utils utils;
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
    void testWhenCurrentGoalIsReached() {
        Goal currentGoal = new Goal("A", false);
        goals.add(currentGoal);
        Precondition precondition = new Precondition("A", "parcel");
        preconditions.add(precondition);
        Mockito.doNothing().when(algorithm).tenthPoint();
        algorithm.ninthPoint();
        //Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(1)).tenthPoint();
    }

    @Test
    void testWhenCurrentGoalIsNotReachedAsPreconditionIsBlocked() {
        Goal currentGoal = new Goal("A", false);
        goals.add(currentGoal);
        Precondition precondition = new Precondition("A", "parcel");
        precondition.setBlocked();
        preconditions.add(precondition);
        Mockito.doNothing().when(algorithm).eleventhPoint(currentGoal);
        algorithm.ninthPoint();
        Mockito.verify(algorithm, Mockito.times(0)).tenthPoint();
        Mockito.verify(algorithm, Mockito.times(1)).eleventhPoint(currentGoal);
    }

    @Test
    void testWhenCurrentGoalIsNotReachedAsPreconditionIsNotPresent() {
        Goal currentGoal = new Goal("A", false);
        goals.add(currentGoal);
        Mockito.doNothing().when(algorithm).eleventhPoint(currentGoal);
        algorithm.ninthPoint();
       // Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(0)).tenthPoint();
        Mockito.verify(algorithm, Mockito.times(1)).eleventhPoint(currentGoal);
    }

    @Test
    void testWhenCurrentGoalIsContradictionIsNotReachedAsPreconditionIsNotPresent() {
        Goal currentGoal = new Goal("⊥", false);
        goals.add(currentGoal);
        Mockito.doNothing().when(algorithm).eleventhPoint(currentGoal);
        algorithm.ninthPoint();
       // Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
        Mockito.verify(algorithm, Mockito.times(0)).tenthPoint();
        Mockito.verify(algorithm, Mockito.times(1)).eleventhPoint(currentGoal);
    }

    @Test
    void testWhenCurrentGoalIsContradictionButIsNotReachedAsOneOfPreconditionsIsBlocked() {
        Goal currentGoal = new Goal("⊥", false);
        goals.add(currentGoal);
        Precondition precondition1 = new Precondition("A", "parcel");
        Precondition precondition2 = new Precondition("(¬A)", "parcel");
        precondition1.setBlocked();
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        Mockito.doNothing().when(algorithm).eleventhPoint(currentGoal);
        algorithm.ninthPoint();
        //Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
       // Mockito.verify(algorithm, Mockito.times(1)).doesPredicationsHaveContradiction();
        Mockito.verify(algorithm, Mockito.times(0)).tenthPoint();
        Mockito.verify(algorithm, Mockito.times(1)).eleventhPoint(currentGoal);
        //Assertions.assertFalse(algorithm.doesPredicationsHaveContradiction());
    }

    @Test
    void testWhenCurrentGoalContradictionIsReached() {
        Goal currentGoal = new Goal("⊥", false);
        goals.add(currentGoal);
        Precondition precondition1 = new Precondition("A", "parcel");
        Precondition precondition2 = new Precondition("(¬A)", "parcel");
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        Mockito.doNothing().when(algorithm).tenthPoint();
        algorithm.ninthPoint();
       // Mockito.verify(algorithm, Mockito.times(1)).isPreconditionPresent(currentGoal.getFormula());
       // Mockito.verify(algorithm, Mockito.times(1)).doesPredicationsHaveContradiction();
        //Mockito.verify(algorithm, Mockito.times(1)).tenthPoint();
        //Assertions.assertTrue(algorithm.doesPredicationsHaveContradiction());
    }

}
