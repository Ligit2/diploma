package com.example.algorithm;

import com.example.algorithm.model.Algorithm;
import com.example.algorithm.model.Goal;
import com.example.algorithm.model.Precondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ThirdPointTest {

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
    void testWhenPreconditionIsASimpleFormula() {
        Precondition precondition = new Precondition("A", "parcel");
        preconditions.add(precondition);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertTrue(preconditions.contains(precondition));
    }

    @Test
    void testWhenPreconditionContainsContainsLabelV0() {
        Precondition precondition = new Precondition("(A⊃B)", "parcel");
        precondition.addLabel("V0");
        preconditions.add(precondition);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertTrue(preconditions.contains(precondition));
    }

    @Test
    void testWhenPreconditionContainsImplicationButInsertionRuleCantBeApplied() {
        Precondition precondition = new Precondition("(A⊃B)", "parcel");
        preconditions.add(precondition);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertTrue(preconditions.contains(precondition));
    }


    @Test
    void testWhenPreconditionContainsImplication() {
        Precondition precondition1 = new Precondition("(A⊃B)", "parcel");
        Precondition precondition2 = new Precondition("A", "parcel");
        Precondition result = new Precondition("B", "⊃e", "V+0");
        result.setIndexOfV0(0);
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 3);
        Assertions.assertTrue(preconditions.contains(precondition1));
        Assertions.assertTrue(preconditions.contains(precondition2));
        Assertions.assertTrue(preconditions.contains(result));
        Assertions.assertEquals(preconditions.get(2), result);
        Assertions.assertTrue(preconditions.get(0).getLabels().contains("V0"));
    }

    @Test
    void testWhenPreconditionContainsImplicationWhenResultAlreadyExists() {
        Precondition precondition1 = new Precondition("(A⊃B)", "parcel");
        Precondition precondition2 = new Precondition("A", "parcel");
        Precondition precondition3 = new Precondition("B", "parcel");
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        preconditions.add(precondition3);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 3);
        Assertions.assertTrue(preconditions.contains(precondition1));
        Assertions.assertTrue(preconditions.contains(precondition2));
        Assertions.assertTrue(preconditions.contains(precondition3));
    }

    @Test
    void testWhenPreconditionContainsImplicationWhenFirstPreconditionIsBlocked() {
        Precondition precondition1 = new Precondition("(A⊃B)", "parcel");
        Precondition precondition2 = new Precondition("A", "parcel");
        precondition2.setBlocked();
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 2);
        Assertions.assertTrue(preconditions.contains(precondition1));
        Assertions.assertTrue(preconditions.contains(precondition2));
    }

    @Test
    void testWhenPreconditionContainsImplicationWhenMainPreconditionIsBlocked() {
        Precondition precondition1 = new Precondition("(A⊃B)", "parcel");
        precondition1.setBlocked();
        preconditions.add(precondition1);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertTrue(preconditions.contains(precondition1));
    }

    @Test
    void testWhenPreconditionContainsImplicationWhenSecondPreconditionIsBlocked() {
        Precondition precondition1 = new Precondition("(A⊃B)", "parcel");
        Precondition precondition2 = new Precondition("A", "parcel");
        Precondition precondition3 = new Precondition("B", "parcel");
        Precondition result = new Precondition("B", "⊃e", "V+0");
        result.setIndexOfV0(0);
        precondition3.setBlocked();
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        preconditions.add(precondition3);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 4);
        Assertions.assertTrue(preconditions.contains(precondition1));
        Assertions.assertTrue(preconditions.contains(precondition2));
        Assertions.assertTrue(preconditions.contains(precondition3));
        Assertions.assertTrue(preconditions.contains(result));
        Assertions.assertEquals(preconditions.get(3), result);
        Assertions.assertTrue(preconditions.get(0).getLabels().contains("V0"));
    }


    @Test
    void testWhenPreconditionContainsOnlyOneConductionButInsertionRuleCantBeApplied() {
        Precondition precondition = new Precondition("(¬(A⊃B))", "parcel");
        preconditions.add(precondition);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 1);
        Assertions.assertTrue(preconditions.contains(precondition));
    }

    @Test
    void testWhenPreconditionContainsConductionButInsertionRuleCantBeApplied() {
        Precondition precondition1 = new Precondition("(¬(¬A))", "parcel");
        Precondition precondition2 = new Precondition("A", "parcel");
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 2);
        Assertions.assertTrue(preconditions.contains(precondition1));
        Assertions.assertTrue(preconditions.contains(precondition2));
    }

    @Test
    void testWhenPreconditionContainsConductionButInsertionRuleAppliedAsPreconditionBlocked() {
        Precondition precondition1 = new Precondition("(¬(¬A))", "parcel");
        Precondition precondition2 = new Precondition("A", "parcel");
        precondition2.setBlocked();
        Precondition result = new Precondition("A", "¬¬e", "V+0");
        result.setIndexOfV0(0);
        preconditions.add(precondition1);
        preconditions.add(precondition2);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 3);
        Assertions.assertTrue(preconditions.contains(precondition1));
        Assertions.assertTrue(preconditions.contains(precondition2));
        Assertions.assertEquals(preconditions.get(2), result);
        Assertions.assertTrue(preconditions.get(0).getLabels().contains("V0"));
    }

    @Test
    void testWhenPreconditionContainsConductionAndInsertionRuleApplied() {
        Precondition precondition1 = new Precondition("(¬(¬A))", "parcel");
        Precondition result = new Precondition("A", "¬¬e", "V+0");
        preconditions.add(precondition1);
        result.setIndexOfV0(0);
        Mockito.doNothing().when(algorithm).ninthPoint();
        algorithm.thirdPoint();
        Assertions.assertEquals(preconditions.size(), 2);
        Assertions.assertTrue(preconditions.contains(precondition1));
        Assertions.assertEquals(preconditions.get(1), result);
        Assertions.assertTrue(preconditions.get(0).getLabels().contains("V0"));
    }
}
