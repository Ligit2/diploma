//package com.example.algorithm;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Stack;
//
//public class ForthPointTest {
//
//    private Algorithm algorithm;
//    List<Precondition> preconditions;
//    Stack<Goal> goals;
//
//    @BeforeEach
//    void initialize() {
//        algorithm = Mockito.mock(Algorithm.class, Mockito.CALLS_REAL_METHODS);
//        preconditions = new ArrayList<>();
//        goals = new Stack<>();
//        algorithm.goals = goals;
//        algorithm.preconditions = preconditions;
//    }
//
//    @Test
//    void testWhenCurrentGoalIsSimpleFormula() {
//        Goal goal = new Goal("A", false);
//        goals.push(goal);
//        Mockito.doNothing().when(algorithm).fifthPoint(goal);
//        Mockito.doNothing().when(algorithm).thirdPoint();
//        algorithm.forthPoint();
//        Mockito.verify(algorithm, Mockito.times(1)).fifthPoint(goal);
//        Mockito.verify(algorithm, Mockito.times(1)).thirdPoint();
//    }
//
//    @Test
//    void testWhenCurrentGoalContainsDenial() {
//        Goal goal = new Goal("(¬A)", false);
//        goals.push(goal);
//        Mockito.doNothing().when(algorithm).fifthPoint(goal);
//        Mockito.doNothing().when(algorithm).thirdPoint();
//        algorithm.forthPoint();
//        Mockito.verify(algorithm, Mockito.times(1)).fifthPoint(goal);
//        Mockito.verify(algorithm, Mockito.times(1)).thirdPoint();
//        Assertions.assertEquals(goal.mainSign, "¬");
//    }
//
//    @Test
//    void testWhenCurrentGoalContainsImplication() {
//        Goal goal = new Goal("(A⊃B)", false);
//        goals.push(goal);
//        Mockito.doNothing().when(algorithm).sixthPoint(goal);
//        Mockito.doNothing().when(algorithm).thirdPoint();
//        algorithm.forthPoint();
//        Mockito.verify(algorithm, Mockito.times(1)).sixthPoint(goal);
//        Mockito.verify(algorithm, Mockito.times(1)).thirdPoint();
//        Assertions.assertEquals(goal.mainSign, "⊃");
//    }
//
//    @Test
//    void testWhenCurrentGoalContainsContradiction() {
//        Goal goal = new Goal("⊥", false);
//        goals.push(goal);
//        Mockito.doNothing().when(algorithm).forthPoint();
//        algorithm.forthPoint();
//        Mockito.verify(algorithm, Mockito.times(1)).forthPoint();
//        Assertions.assertNull(goal.mainSign);
//    }
//
//
//}
