package agh.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal {
    private Vector2D position;
    private int energy;
    private MapDirection orient=MapDirection.NORTH;
    private Genotype genes;
    private JungleMap map;
    public final List<IPositionChangeObserver> observers = new ArrayList<>();
    private int kids=0;
    private int age=0;
    private int deadDay=-1;

    /*konstruktor używany tylko do wygenerowania początkowych zwierząt*/

    public Animal(Vector2D position, int energy, JungleMap map) {
        this.position=position;
        this.energy=energy;
        this.genes=new Genotype();
        this.map=map;
        this.addObserver(this.map);
        this.changeOrient();
    }

    /* konstruktor tworzący nowe zwierzęta na podstawie genotypu rodziców*/

    public Animal(Vector2D position,int orient, int energy, Animal parent1, Animal parent2) {
        this.position=position;
        for(int i=0;i<orient;i++){
            this.orient.next();
        }
        this.energy=energy;
        this.genes=new Genotype(parent1.genes,parent2.genes);
        this.map= parent1.map;
        this.addObserver(this.map);
        this.changeOrient();
    }

    /* getter na energię zwierzaka*/

    public int getEnergy(){
        return this.energy;
    }

    /* procedura zwiększająca energię, np przy zjadaniu trawy*/

    public void addEnergy(int energy){
        this.energy+=energy;
    }

    /* procedura obniżająca energię, głównie przy ruchu, ale również przy rozmnażaniu*/

    public void subtractEnergy(int energy){
        this.energy-=energy;
    }

    /* procedura która zlicza dzieci dla danego osobnika, stosowana przy rozmnażaniu */

    public void addKid(){
        this.kids+=1;
    }

    /* funkcja zwracająca liczbę dzieci */

    public int getKids(){
        return this.kids;
    }

    /* funkcja zwracająca położenie zwierzaka */

    public Vector2D getPosition(){
        return this.position;
    }

    /* funkcja zwracająca zwrot zwierzaka */

    public MapDirection getOrient(){
        return this.orient;
    }

    /* funkcja zwracająca informacje o genotypie zwierzaka */

    public Genotype getGenes(){
        return this.genes;
    }

    /* funkcja zwracająca wiek zwierzaka */

    public int getAge(){
        return this.age;
    }

    /* procedura notująca dzień śmierci zwierzaka */

    public void setDeadDay(){
        this.deadDay=this.map.getDay();
    }

    /* funkcja zwracająca datę śmierci zwierzaka */

    public int getDeadDay(){
        return this.deadDay;
    }

    /* procedura odpowiadająca za ruch zwierzaka, uwzględnia zawijanie mapy */

    public void move(){
        this.age+=1;
        Vector2D oldPosition=this.getPosition();
        this.position=this.position.add(this.orient.toUnitVector());
        if (this.position.x>this.map.getWidth()){
            this.position=new Vector2D(0,this.position.y);
        }
        if (this.position.y>this.map.getHeight()){
            this.position=new Vector2D(this.position.x,0);
        }
        if (this.position.x<0){
            this.position=new Vector2D(this.map.getWidth(),this.position.y);
        }
        if (this.position.y<0){
            this.position=new Vector2D(this.position.x,this.map.getHeight());
        }
        this.positionChanged(oldPosition,this.position);
        this.changeOrient();
    }

    /* procedura która losuje i zmienia kierunek ruchu zwierzaka */

    public void changeOrient(){
        Random random = new Random();
        int rand=random.nextInt(this.genes.getAmountOfGenes());
        int n=this.genes.getNthGene(rand);
        for(int i=0;i<n;i++){
            this.orient=this.orient.next();
        }
    }

    /* rejestracja obserwatora, który będzie wykonywał określone zadanie po zmianie pozycji zwierzaka */

    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    /* usunięcie obserwatora */

    public void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    /* wywołanie dla obserwatorów ich zadań związanych z ruchem zwierzaka */

    public void positionChanged(Vector2D oldPosition,Vector2D newPosition){
        for(IPositionChangeObserver observer:this.observers){
            observer.positionChanged(this,oldPosition,newPosition);
        }
    }
}
