package agh.cs;

import javax.swing.*;
import java.awt.*;

/* osobne okno, w którym po zatrzymaniu symulacji i wybraniu osobnika są wypisane informacje o zwierzęciu
w przypadku dalszego uruchomienia symulacji, zwierzę jest śledzone, a jego statystyki zmieniają się na bieżąco
 */

public class AnimalStatsVisualiser implements IPositionChangeObserver{

    private Animal animal;
    private Statistics statistics;
    private JFrame frame=new JFrame();
    private JPanel statPanel=new JPanel();
    private JPanel picPanel=new JPanel();

    public AnimalStatsVisualiser(Animal animal){
        this.animal=animal;
        this.statistics=new Statistics(this.animal);
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(1,2,10,10));
        frame.add(animalPic());
        frame.add(showAnimalStats());
        frame.setResizable(false);
        frame.pack();
    }

    /* zwraca Panel, który zawiera statystyki dla zwierzęcia, określone w klasie Statistics */

    public JPanel showAnimalStats(){
        statPanel.removeAll();
        statPanel.setLayout(new GridLayout(7,1,0,0));
        for(int i=0;i<7;i++){
            JLabel label=new JLabel();
            label.setText(statistics.setAnimalText(i));
            statPanel.add(label);
        }
        return statPanel;
    }

    /* dodałem kilka zdjęć dla zwierząt, które są wybierane na podstawie jego energii */

    public JPanel animalPic(){
        picPanel.removeAll();
        int energy=this.animal.getEnergy();
        JLabel label = new JLabel();
        if (energy<=0){
            ImageIcon icon = new ImageIcon("src\\images\\dead.png");
            label.setIcon(icon);
        }
        else if (energy<20){
            ImageIcon icon = new ImageIcon("src\\images\\low.png");
            label.setIcon(icon);
        }
        else if (energy>100){
            ImageIcon icon = new ImageIcon("src\\images\\high.png");
            label.setIcon(icon);
        }
        else{
            ImageIcon icon = new ImageIcon("src\\images\\mid.png");
            label.setIcon(icon);
        }
        picPanel.add(label);
        return picPanel;
    }

    /* update statystyk, ewentualnie też obrazka zwierzęcia */

    @Override
    public void positionChanged(Animal animal, Vector2D oldPosition, Vector2D newPosition) {
        if(oldPosition.equals(newPosition)){
            this.animal.setDeadDay();
        }
        this.frame.add(animalPic());
        this.frame.add(showAnimalStats());
        this.frame.repaint();
        this.frame.revalidate();
    }
}
