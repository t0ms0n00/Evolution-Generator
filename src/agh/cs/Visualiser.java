package agh.cs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visualiser implements ActionListener {
    private MapVisualiser mapV1;
    private MapVisualiser mapV2;
    private JFrame frame = new JFrame();
    private MyButton button=new MyButton(0,"STOP");
    private MyButton button1=new MyButton(1,"TYLKO LEWA");
    private MyButton button2=new MyButton(2,"TYLKO PRAWA");
    private MyButton button12=new MyButton(3,"START");
    private MyButton dominantGene=new MyButton(0,"GEN DOMINUJACY");
    private MyButton writeToFile=new MyButton(0,"ZAPISZ DO PLIKU");
    private Simulator simulator;

    public Visualiser(MapVisualiser v1,MapVisualiser v2,Simulator simulator){
        this.mapV1=v1;
        this.mapV2=v2;
        this.simulator=simulator;
        this.frame.setBounds(50,50,1400,1200);
        this.frame.setTitle("Symulacja");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
        this.frame.setLayout(new GridLayout(1,3,20,0));
        this.frame.add(this.mapV1.getMainPanel());
        this.frame.add(this.mapV2.getMainPanel());
        addButtons();
        this.frame.pack();
    }

    /* konfiguracja przycisków */

    public void addButtons(){
        this.button.addActionListener(this);
        this.button1.addActionListener(this);
        this.button2.addActionListener(this);
        this.button12.addActionListener(this);
        this.dominantGene.addActionListener(this);
        this.writeToFile.addActionListener(this);
        JPanel buttonPanel=new JPanel();
        buttonPanel.add(button);
        buttonPanel.add(button12);
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(dominantGene);
        buttonPanel.add(writeToFile);
        buttonPanel.setLayout(new GridLayout(3,2,5,5));
        this.frame.add(buttonPanel);
    }

    /* rysowanie stanu mapy (jeśli jest taka konieczność) */

    public void draw(){
        if(this.simulator.getState()!=2) this.mapV1.draw();
        if(this.simulator.getState()!=1) this.mapV2.draw();
    }

    /* obsługa przycisków, znajdujących się po prawej stronie okna */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (button.equals(e.getSource())) {
            this.simulator.setState(0);
        }
        else if (button1.equals(e.getSource())) {
            this.simulator.setState(1);
        }
        else if (button2.equals(e.getSource())) {
            this.simulator.setState(2);
        }
        else if (button12.equals(e.getSource())) {
            this.simulator.setState(3);
        }
        else if (dominantGene.equals(e.getSource())){
            this.simulator.setState(0);
            this.mapV1.showDominators();
            this.mapV2.showDominators();
        }
        else if (writeToFile.equals(e.getSource())){
            this.simulator.setState(0);
            WriteToFile writer=new WriteToFile(this.mapV1,this.mapV2);
        }
    }
}
