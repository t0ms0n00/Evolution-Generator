package agh.cs;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MapVisualiser implements ActionListener {

    private JungleMap map;
    private JPanel mapPanel=new JPanel();
    private JPanel statPanel=new JPanel();
    private JPanel superPanel=new JPanel();
    private Statistics statistics;
    private HashMap<Vector2D,JPanel> panels=new HashMap<>();
    private int width;
    private int height;
    private Map<Vector2D, Pair<JButton,Vector2D>> buttonMap=new HashMap();
    private Map<Vector2D,JPanel> animalPanels = new HashMap<>();

    public MapVisualiser(JungleMap map){
        this.map=map;
        this.width=this.map.getWidth();
        this.height=this.map.getHeight();
        this.statistics=new Statistics(this.map);
        this.mapPanel.setLayout(new GridLayout(this.height+1,this.width+1,1,1));
        for(int h=this.height;h>=0;h--){
            for(int w=0;w<=this.width;w++){
                JPanel panel=new JPanel();
                panel.setBounds(w,h,1,1);
                this.mapPanel.add(panel);
                this.panels.put(new Vector2D(w,h),panel);
            }
        }
        this.statPanel.setLayout(new GridLayout(6,1,0,1));
        this.superPanel.add(this.mapPanel);
        this.superPanel.add(this.statPanel);
        this.superPanel.setLayout(new GridLayout(2,1,10,0));
    }

    /* mapa jest panelem składającym się z dwóch paneli: samej mapy oraz drugiego panelu statystyk mapy*/

    public JPanel getMainPanel(){
        return this.superPanel;
    }

    /* update statystyk, wykonywany po każdym dniu symulacji*/

    public void updateStats(){
        this.statPanel.removeAll();
        for(int i=0;i<6;i++){
            JPanel panel=new JPanel();
            JLabel label=new JLabel();
            label.setText(this.statistics.setText(i));
            panel.add(label);
            this.statPanel.add(panel);
        }
        this.statPanel.repaint();
        this.statPanel.revalidate();
    }

    /* wyciągnięcie tekstu zawierającego wszystkie statystyki */

    public String getStats(){
        String stats = new String();
        for(int i=0;i<6;i++){
            stats=stats+this.statistics.setText(i)+"\n";
        }
        return stats;
    }

    /* rysowanie mapy, obiekty rysowane w kolejności: zwierzęta, rośliny, wolne pola
    energia zwierzęcia została oddana jako kolor kwadracika, gdy ma wysoką energię jest widocznie bardziej czerwone,
    w przypadku energii niskiej staje się coraz bardziej czarne (czerwony kolor jako nawiązanie, że ma dużo czerwonych
    krwinek => jest zdrowe => ma dużo energii
    --każde zwierze ma na sobie przycisk, odpowiadający za wybranie go do śledzenia
    --po zatrzymaniu symulacji można wybrać takie zwierzę klikając, spowoduje to wyświetlenie statystyk zwierzaka
    nie zamykając okna jest możliwość wznowienia symulacji, wtedy okno ze zwierzakiem będzie na bieżąco updateować
    informacje o nim - zwierzę jest śledzone
     */

    public void draw(){
        this.animalPanels.clear();
        Set<Vector2D> plants=this.map.getPlants();
        Set<Vector2D> animals=this.map.getAnimals();
        Vector2D v1=this.map.jungleLowerLeft;
        Vector2D v2=this.map.jungleUpperRight;
        this.buttonMap.clear();
        for(int h=0;h<=this.height;h++){
            for(int w=0;w<=this.width;w++){
                JPanel panel=this.panels.get(new Vector2D(w,h));
                panel.removeAll();
                if(animals.contains(new Vector2D(w,h))){
                    int lifeLvl=this.map.getStrongest(new Vector2D(w,h)).getEnergy();
                    if(lifeLvl<0) lifeLvl=0;
                    if(lifeLvl>255) lifeLvl=255;
                    panel.setBackground(new Color(lifeLvl,0,0));
                    JButton button=new JButton();
                    button.setPreferredSize(new Dimension(panel.getWidth()/2,panel.getHeight()/2));
                    button.addActionListener(this);
                    this.buttonMap.put(new Vector2D(w,h),new Pair<>(button,new Vector2D(w,h)));
                    panel.add(button);
                    animalPanels.put(new Vector2D(w,h),panel);
                }
                else if(plants.contains(new Vector2D(w,h))){
                    panel.setBackground(new Color(10,100,10));
                }
                else if(v1.x<=w && v1.y<=h && v2.x>=w && v2.y>=h){
                    panel.setBackground(Color.GREEN);
                }
                else{
                    panel.setBackground(new Color(160,180,0));
                }
            }
        }
        this.updateStats();
    }

    /* podświetla na niebiesko zwierzęta, które mają dominujący gen taki jak jest ustalony dla całej populacji na mapie*/

    public void showDominators(){
        this.draw();
        int gene=this.map.getDominantGenotype();
        for (Vector2D position: this.animalPanels.keySet()){
            if (gene==this.map.getStrongest(position).getGenes().getDominant()){
                this.animalPanels.get(position).setBackground(new Color(0,100,255));
            }
        }
    }

    /* obsługa wyboru zwierzęcia do wskazania statystyk/ śledzenia */

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Pair button:this.buttonMap.values()){
            if (button.getKey()==e.getSource()){

                Animal animal = this.map.getStrongest((Vector2D) button.getValue());
                AnimalStatsVisualiser aSV=new AnimalStatsVisualiser(animal);
                animal.addObserver(aSV);
            }
        }
    }
}
