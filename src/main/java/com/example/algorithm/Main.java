package com.example.algorithm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Algorithm algorithm = new Algorithm();
        //algorithm.start("(((¬(¬A))⊃B)⊃((¬((¬A)⊃(¬(¬B))))∨B))", "");
       // algorithm.anotherPrint();

        algorithm.start("((A⊃B)∨(B⊃A))", "");
        //System.out.println(algorithm.goals);
        //System.out.println(algorithm.preconditions);
    }

// 1.((¬(¬A))⊃(¬(¬((¬A)⊃(¬(¬B))))))
// 2.(B&A)  (¬(A⊃(¬B)))
// 3.(A⊃(B⊃(A&B)))    (A⊃(B⊃(¬(A⊃(¬B)))))  (¬(¬(¬(A⊃(¬B))))) ¬¬
// 4.(B⊃(A∨B))
// 5.(A⊃(A∨B))
// 6.(((¬(¬A))⊃B)⊃((¬((¬A)&(¬B)))⊃B))
// 7.(((¬(¬A))⊃B)⊃((¬((¬A)⊃(¬(¬B))))∨B))????????????? -   (((¬(¬A))⊃B)⊃((¬(¬((¬A)⊃(¬(¬B)))))⊃B))+
// 8.(((¬A)⊃(¬B))⊃((¬(A&B))⊃(¬B)))
// 9.(((¬(A&(¬B)))⊃B)⊃((¬A)⊃B))
// 10. (((A&(¬B))∨B)⊃(A∨B)) +    (((¬(A&(¬B)))⊃B)⊃((¬A)⊃B))+
// 11. ((A∨(¬B))⊃((A&B)∨(¬B))) -  (((¬A)⊃(¬B))⊃((¬(A&B))⊃(¬B)))+
// 12. (((¬A)∨B)⊃(((¬A)&(¬B))∨B)) - (((¬(¬A))⊃B)⊃((¬((¬A)&(¬B)))⊃B))+
//13 .(((¬A)∨B)⊃((¬((¬A)⊃(¬(¬B))))∨B)) - (((¬(¬A))⊃B)⊃((¬(¬((¬A)⊃(¬(¬B)))))⊃B))+
//14 .((A⊃B)∨(B⊃A))
//15 .((¬(¬((A⊃B)⊃(¬(¬A)))))⊃((¬A)⊃B))
//16.((¬((A⊃B)&(¬A)))⊃((¬A)⊃B))
//17. ((A⊃B)∨A)
//18 .((A⊃B)∨(¬(A&B)))

}

