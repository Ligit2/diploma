package com.example.algorithm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Algorithm algorithm = new Algorithm();
        new Gui(algorithm);

    }

// 1.((¬(¬A))⊃(¬(¬((¬A)⊃(¬(¬B))))))
// 2.(B&A)goal  (¬(A⊃(¬B)))precondition
// 3.(A⊃(B⊃(A&B)))  ->   (A⊃(B⊃(¬(A⊃(¬B)))))
// 4.(B⊃(A∨B))
// 5.(A⊃(A∨B))
// 6.(((¬(¬A))⊃B)⊃((¬((¬A)&(¬B)))⊃B))
// 7.(((¬(¬A))⊃B)⊃((¬((¬A)⊃(¬(¬B))))∨B))- ->   (((¬(¬A))⊃B)⊃((¬(¬((¬A)⊃(¬(¬B)))))⊃B))+
// 8.(((¬A)⊃(¬B))⊃((¬(A&B))⊃(¬B)))
// 9.(((¬(A&(¬B)))⊃B)⊃((¬A)⊃B))
// 10. (((A&(¬B))∨B)⊃(A∨B)) +  ->  (((¬(A&(¬B)))⊃B)⊃((¬A)⊃B))+
// 11. ((A∨(¬B))⊃((A&B)∨(¬B))) - ->  (((¬A)⊃(¬B))⊃((¬(A&B))⊃(¬B)))+
// 12. (((¬A)∨B)⊃(((¬A)&(¬B))∨B)) - -> (((¬(¬A))⊃B)⊃((¬((¬A)&(¬B)))⊃B))+
//13 .(((¬A)∨B)⊃((¬((¬A)⊃(¬(¬B))))∨B)) - -> (((¬(¬A))⊃B)⊃((¬(¬((¬A)⊃(¬(¬B)))))⊃B))+
//14 .((A⊃B)∨(B⊃A))  ->  ((¬(A⊃B))⊃(B⊃A))
//15 .((¬(¬((A⊃B)⊃(¬(¬A)))))⊃((¬A)⊃B))
//16.((¬((A⊃B)&(¬A)))⊃((¬A)⊃B))
//17. ((A⊃B)∨A)
//18 .((A⊃B)∨(¬(A&B)))

}

