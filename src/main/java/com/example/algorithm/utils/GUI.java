package com.example.algorithm.utils;

import com.example.algorithm.exception.MetaStatementNotProvableException;
import com.example.algorithm.model.Algorithm;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {
    JFrame frameToStart;
    JFrame frameToFinish;
    JTextField jTextFieldForPreconditions;
    JTextField jTextFieldForMainGoal;
    JButton button;
    Algorithm algorithm;

    public void resultGuiWithSuccess(String name) {
        int max = Math.max(algorithm.getPreconditions().size(), algorithm.getGoalsToTrack().size());
        Object[][] array = new Object[max][4];
        for (int i = 0; i < array.length; i++) {
            if (!algorithm.getPreconditions().isEmpty() && i < algorithm.getPreconditions().size()) {
                array[i][0] = " "+ algorithm.getPreconditions().get(i).getFormula();
                array[i][1] =" " +  algorithm.getPreconditions().get(i).getLabels();
                array[i][2] = " "+algorithm.getPreconditions().get(i).getType();

            } else {
                array[i][0] = "";
                array[i][1] = "";
                array[i][2] = "";
            }
            if (!algorithm.getGoalsToTrack().isEmpty() && i < algorithm.getGoalsToTrack().size()) {
                array[i][3] = " " + algorithm.getGoalsToTrack().get(i).formula;
            } else {
                array[i][3] = "";
            }
        }

        JFrame frameToStart = new JFrame();
        String[] column = {"Preconditions","Label", "Types", "Goals"};
        JTable jt = new JTable(array, column);
        jt.setShowHorizontalLines(true);
        jt.setShowVerticalLines(true);
        jt.getTableHeader().setBackground(Color.lightGray);
        jt.setGridColor(Color.BLACK);
        jt.setFont(new Font("Serif",Font.PLAIN, 20));
        jt.setRowHeight(jt.getRowHeight()+20);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        jt.setDefaultRenderer(String.class, centerRenderer);
        jt.getColumn("Preconditions").setPreferredWidth(jt.getColumn("Preconditions").getWidth()+220);
        jt.getColumn("Goals").setPreferredWidth(jt.getColumn("Goals").getWidth()+220);
        jt.getColumn("Label").setPreferredWidth(jt.getColumn("Label").getWidth()-250);
        jt.getColumn("Types").setPreferredWidth(jt.getColumn("Types").getWidth()-100);
        jt.getTableHeader().setFont(new Font("Serif",Font.PLAIN, 20));
        JScrollPane jScrollPane = new JScrollPane(jt);
        this.frameToFinish = frameToStart;
        this.frameToFinish.setTitle(name);
        this.frameToFinish.add(jScrollPane);
        this.frameToFinish.setSize(1000, 600);
        this.frameToFinish.setVisible(true);
        this.frameToFinish.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public GUI(Algorithm algorithm){
        this.algorithm = algorithm;
        JFrame f= new JFrame("LET THE ALGORITHM SOLVE");
        f.setBackground(Color.lightGray);
        this.frameToStart = f;
        JTextField t1,t2;
        t1=new JTextField("Insert Preconditions");
        t1.setFont(new Font("Serif", Font.PLAIN, 20));
        this.jTextFieldForPreconditions = t1;
        t1.setBounds(50,100, 500,50);
        t2=new JTextField("Insert Main Goal");
        t2.setFont(new Font("Serif", Font.PLAIN, 20));
        this.jTextFieldForMainGoal = t2;
        t2.setBounds(50,170, 500,50);
        this.button = new JButton("solve");
        this.button.setFont(new Font("Serif", Font.PLAIN, 20));
        button.doClick();
        this.button.setBounds(250,300,100,40);
        this.button.addActionListener(this);
        f.add(t1); f.add(t2); f.add(this.button);
        f.setSize(600,400);
        f.setLayout(null);
        f.setVisible(true);
        frameToStart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String preconditions = jTextFieldForPreconditions.getText();
        String mainGoal = jTextFieldForMainGoal.getText();
        if(preconditions.equalsIgnoreCase("empty")){
            try {
                long start = System.currentTimeMillis();
                algorithm.start(mainGoal,"");
                long finish = System.currentTimeMillis();
                System.out.println(finish - start);
                System.out.println(algorithm.countOfSteps);
                resultGuiWithSuccess("SUCCESS");
            } catch (InterruptedException | MetaStatementNotProvableException ex) {
                resultGuiWithSuccess("FAILURE");
            }
        }
        else{
            try {
                long start = System.currentTimeMillis();
                algorithm.start(mainGoal,preconditions);
                long finish = System.currentTimeMillis();
                System.out.println(finish - start);
                System.out.println(algorithm.countOfSteps);
                resultGuiWithSuccess("SUCCESS");
            } catch (InterruptedException | MetaStatementNotProvableException ex) {
                resultGuiWithSuccess("FAILURE");
            }
        }
        this.frameToStart.dispose();
    }
}
