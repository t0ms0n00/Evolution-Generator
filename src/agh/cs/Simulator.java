package agh.cs;

import java.util.Random;

public class Simulator {

    private JungleMap map;
    private JungleMap map2;
    private Visualiser vis;
    private int startEnergy;
    private int energyLost;
    private int plantUnitEnergy;
    private int state=0; /// 0 - stop all, 1 - run 1, 2 - run 2, 3 - run 1 && 2

    /* konstruktor, od razu generuje podaną w pliku json liczbę początkowych zwierząt */

    public Simulator(JungleMap map,JungleMap map2,int animalsOnStart,int startEnergy,int energyLost, int plantEnergy){
        this.map=map;
        this.map2=map2;
        this.vis=new Visualiser(new MapVisualiser(this.map),new MapVisualiser(this.map2),this);
        this.startEnergy=startEnergy;
        this.energyLost=energyLost;
        this.plantUnitEnergy=plantEnergy;
        genXAnimals(animalsOnStart,startEnergy);
        this.vis.draw();
    }

    /* generowanie początkowej partii zwierząt */

    public void genXAnimals(int x, int startEnergy){
        Random random=new Random();
        int width=this.map.getWidth();
        int height=this.map.getHeight();
        if(x>(1+width)*(1+height)){
            throw new IllegalArgumentException("Zbyt duzo zwierzat, nie mozna umiescic az tylu");
        }
        for(int i=0;i<x;){
            Vector2D position=new Vector2D(Math.abs(random.nextInt(1+width)),
                    Math.abs(random.nextInt(1+height)));
            if(this.map.getAnimals().contains(position)) continue;
            Animal animal=new Animal(position,startEnergy,this.map);
            Animal animal2=new Animal(position,startEnergy,this.map2);
            this.map.addAnimal(animal,position);
            this.map2.addAnimal(animal2,position);
            i+=1;
        }
    }

    /* ustawienie stanu symulacji
       0 - pauza na obu mapach
       1 - działa lewa
       2 - działa prawa
       3 - działają obie
    */

    public void setState(int state){
        this.state=state;
    }

    /* zwraca stan symulacji */

    public int getState(){
        return this.state;
    }

    /* procedura wykonująca symulację pojedynczego dnia na mapie */

    public void step(JungleMap map){
        map.nextDay();
        map.removeDeadAnimals();
        map.moveAnimals(this.energyLost);
        map.feedAnimalsInSteppe();
        map.feedAnimalsInJungle();
        map.spawnNewAnimals(this.startEnergy);
        map.addPlants(this.plantUnitEnergy);
    }

    /* procedura odpowiedzialna za pełną symulację */

    public void run() throws InterruptedException {
        while(true){
            if(this.state==1 || this.state==3){
                this.step(this.map);
            }
            if(this.state==2 || this.state==3){
                this.step(this.map2);
            }
            if(this.state!=0) this.vis.draw();
            Thread.sleep(70);
        }
    }
}
