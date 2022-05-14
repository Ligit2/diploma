package com.example.algorithm;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Stack;

public class Gui implements ActionListener {
    JFrame frame;
    JFrame frame1;
    JTextField jTextField1;
    JTextField jTextField2;
    JButton button;
    Algorithm algorithm;

    public void resultGuiWithSuccess(String name) {
        int max = Math.max(algorithm.preconditions.size(), algorithm.goalsToTrack.size());
        Object[][] array = new Object[max][3];
        for (int i = 0; i < array.length; i++) {
            if (!algorithm.preconditions.isEmpty() && i < algorithm.preconditions.size()) {
                array[i][0] = algorithm.preconditions.get(i).getFormula();
                array[i][1] = algorithm.preconditions.get(i).getType();

            } else {
                array[i][0] = "";
                array[i][1] = "";
            }
            if (!algorithm.goalsToTrack.isEmpty() && i < algorithm.goalsToTrack.size()) {
                array[i][2] = algorithm.goalsToTrack.get(i).formula;
            } else {
                array[i][2] = "";
            }
        }

        JFrame frame = new JFrame();
        String[] column = {"Preconditions", "Types", "Goals"};
        JTable jt = new JTable(array, column);
        //jt.setBounds(30, 40, 300, 400);
        jt.setShowHorizontalLines(true);
        jt.setShowVerticalLines(true);
        jt.getTableHeader().setBackground(Color.lightGray);
        jt.setGridColor(Color.BLACK);
        JScrollPane jScrollPane = new JScrollPane(jt);
        this.frame1 = frame;
        this.frame1.setTitle(name);
        this.frame1.add(jScrollPane);
        this.frame1.setSize(600, 400);
        this.frame1.setVisible(true);
        this.frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    Gui(Algorithm algorithm){
        this.algorithm = algorithm;
        JFrame f= new JFrame("Let the algorithm solve");
        this.frame = f;
        JTextField t1,t2;
        t1=new JTextField("Insert Preconditions");
        this.jTextField1 = t1;
        t1.setBounds(50,100, 300,40);
        t2=new JTextField("Insert Main Goal");
        this.jTextField2 = t2;
        t2.setBounds(50,150, 300,40);
        this.button = new JButton("solve");
        button.doClick();
        this.button.setBounds(250,300,100,20);
        this.button.addActionListener(this);
        f.add(t1); f.add(t2); f.add(this.button);
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String preconditions = jTextField1.getText();
        String mainGoal = jTextField2.getText();
        if(preconditions.equalsIgnoreCase("empty")){
            try {
                long start = System.currentTimeMillis();
                algorithm.start(mainGoal,"");
                long finish = System.currentTimeMillis();
                System.out.println(finish - start);
                resultGuiWithSuccess("SUCCESS");
                System.out.println(algorithm.preconditions);
            } catch (InterruptedException | MetaStatementNotProvableException ex) {
                resultGuiWithSuccess("FAILURE");
            }
        }
        else{
            try {
                algorithm.start(mainGoal,preconditions);
                resultGuiWithSuccess("SUCCESS");
            } catch (InterruptedException | MetaStatementNotProvableException ex) {
                resultGuiWithSuccess("FAILURE");
            }
        }
        this.frame.dispose();
    }
}
