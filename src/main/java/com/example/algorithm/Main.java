package com.example.algorithm;

import com.example.algorithm.model.Algorithm;
import com.example.algorithm.utils.GUI;

public class Main {
    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm();
        new GUI(algorithm);
    }
}
//1.((¬(¬A))⊃(¬(¬((¬A)⊃(¬(¬B))))))
//2.(B&A)goal  (¬(A⊃(¬B)))precondition
//3.(A⊃(B⊃(A&B)))  |   (A⊃(B⊃(¬(A⊃(¬B)))))
//4.(B⊃(A∨B))
//5.(A⊃(A∨B))
//6.(((¬(¬A))⊃B)⊃((¬((¬A)&(¬B)))⊃B)) ---->
//7.(((¬(¬A))⊃B)⊃((¬((¬A)⊃(¬(¬B))))∨B)) ||   (((¬(¬A))⊃B)⊃((¬(¬((¬A)⊃(¬(¬B)))))⊃B))
//8.(((¬A)⊃(¬B))⊃((¬(A&B))⊃(¬B)))
//9.(((¬(A&(¬B)))⊃B)⊃((¬A)⊃B))
//10. (((A&(¬B))∨B)⊃(A∨B))   |  (((¬(A&(¬B)))⊃B)⊃((¬A)⊃B))
//11. ((A∨(¬B))⊃((A&B)∨(¬B)))  ||  (((¬A)⊃(¬B))⊃((¬(A&B))⊃(¬B)))
//12. (((¬A)∨B)⊃(((¬A)&(¬B))∨B))  || (((¬(¬A))⊃B)⊃((¬((¬A)&(¬B)))⊃B))
//13 .(((¬A)∨B)⊃((¬((¬A)⊃(¬(¬B))))∨B)) || (((¬(¬A))⊃B)⊃((¬(¬((¬A)⊃(¬(¬B)))))⊃B))
//14 .((A⊃B)∨(B⊃A))  |  ((¬(A⊃B))⊃(B⊃A))
//15 .((¬(¬((A⊃B)⊃(¬(¬A)))))⊃((¬A)⊃B))
//16.((¬((A⊃B)&(¬A)))⊃((¬A)⊃B))
//17. ((A⊃B)∨A)
//18. ((A⊃B)∨(¬(A&B)))
//19. B,(A⊃(B⊃C)),A  C
//20. ((¬(¬A))⊃A)
//21. (A⊃(¬(¬A)))
//22. ((¬A)⊃(A⊃B))
//23. (A⊃((¬A)⊃B))
//24. ((A⊃B)⊃((¬B)⊃(¬A)))
//25. (((¬B)⊃(¬A))⊃(A⊃B))
//26. (A⊃((¬B)⊃(¬(A⊃B))))
//27. ((A⊃B)⊃(((¬A)⊃B)⊃B))
//28. (B⊃(A∨(((¬B)⊃(A&B))∨A)))
//29. ((¬(A⊃B))⊃((¬A)⊃(A&(¬B))))

