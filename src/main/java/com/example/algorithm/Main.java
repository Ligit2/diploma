package com.example.algorithm;

public class Main {
    public static void main(String[] args) {

        Algorithm algorithm = new Algorithm();
        algorithm.start("((¬(¬A))⊃(¬(¬((¬A)⊃(¬(¬B))))))","");
        System.out.println(algorithm.goals);
        System.out.println(algorithm.preconditions);
    }
}
