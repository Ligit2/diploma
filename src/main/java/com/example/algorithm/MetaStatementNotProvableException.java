package com.example.algorithm;

public class MetaStatementNotProvableException extends RuntimeException{
    MetaStatementNotProvableException(String reason, int point){
        super(reason + " in point "+ point);
    }
}
