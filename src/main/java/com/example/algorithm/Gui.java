package com.example.algorithm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Stack;

public class Gui implements ActionListener {
    boolean ok = false;

    String preconditions;
    String mainGoal;
    JFrame frame;
    JButton button=new JButton("Click Me");

    Gui(){

    }

    public void prepareGUI(List<Precondition> preconditions, Stack<Goal> goals){
        int max = Math.max(preconditions.size(), goals.size());
        String[][] array = new String[max+1][3];
        for (int i = 0; i < array.length; i++) {
            if(!preconditions.isEmpty() && i<preconditions.size()) {
                array[i][0] = preconditions.get(i).getFormula();
                array[i][1] = preconditions.get(i).getType();

            }
            else{
                array[i][0] = "";
                array[i][1] = "";
            }
            if (!goals.isEmpty() && i< goals.size()) {
                array[i][2] = goals.get(i).formula;
            }
            else {
                array[i][2] = "";
            }
        }

        JFrame frame=new JFrame();
        this.frame = frame;
        String[] column ={"Preconditions","Types","Goals"};
        JButton jButton = new JButton();
        jButton.setBounds(130,200,100,40);
        jButton.setText("Click");
        final JTable jt=new JTable(array,column);
        jt.setBounds(100,100,200,300);
        JScrollPane sp=new JScrollPane(jt);
        frame.add(sp);
        frame.setSize(300,400);
        frame.setVisible(true);
        frame.setTitle("My Window");
        jt.setCellSelectionEnabled(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button.setBounds(130,200,30,40);
        frame.add(button);


    }
    public void buttonProperties(){

        //button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.ok = true;
    }

}
