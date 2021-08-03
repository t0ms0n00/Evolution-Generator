package agh.cs;

import java.util.*;

/* szerokość i wysokość podane w parameters oznaczają położenie górnego rogu w układzie współrzędnych,
zatem ustawienie tej pary na (x,y) sprawia że punkt (x,y) będzie prawym górnym rogiem mapy
 */

public class JungleMap implements IPositionChangeObserver{

    private int width;
    private int height;
    private double prop;
    public Vector2D jungleLowerLeft;
    public Vector2D jungleUpperRight;
    public int jungleSide;
    private EnergyComparator eComp = new EnergyComparator();
    private Map<Vector2D, SortedSet<Animal>> animals=new HashMap<>();
    private Map<Vector2D,Plant> plantsOnSteppe=new HashMap<>();
    private Map<Vector2D,Plant> plantsInJungle=new HashMap<>();
    private double avgLifeLen=0;
    private double deadAnimals=0;
    private double newDeadsAgeSum=0;
    private double newDeadsCount=0;
    private int day=0;

    public JungleMap(int width,int height,double prop){
        this.width=width;
        this.height=height;
        this.prop=prop;
        int i=0;
        while (i*i<=prop*width*height) i+=1;
        //i-=1;
        this.jungleSide=i;
        this.jungleLowerLeft=new Vector2D(width/2-i/2,height/2-i/2);
        this.jungleUpperRight=new Vector2D(width/2+i/2,height/2+i/2);
    }

    /* getter na szerokość mapy */

    public int getWidth(){
        return this.width;
    }

    /* getter na wysokość mapy */

    public int getHeight(){
        return this.height;
    }

    /* getter na aktualny dzień na mapie */

    public int getDay(){
        return this.day;
    }

    /* getter na pozycje w których są jakiekolwiek zwierzęta */

    public Set<Vector2D> getAnimals(){
        return this.animals.keySet();
    }

    /* ponieważ rośliny nie nakładają się jak zwierzęta getter zwraca pozycje każdej pojedynczej rośliny */

    public Set<Vector2D> getPlants(){
        Set<Vector2D> plants=new HashSet<>();
        plants.addAll(this.plantsInJungle.keySet());
        plants.addAll(this.plantsOnSteppe.keySet());
        return plants;
    }

    /* podliczenie aktualnie żyjących zwierząt*/

    public int getAmountOfAnimals(){
        int amount=0;
        for(SortedSet<Animal> animals: this.animals.values()){
            amount+=animals.size();
        }
        return amount;
    }

    /* podliczenie roślin*/

    public int getAmountOfPlants(){
        return this.getPlants().size();
    }

    /* dominujący gen dla wszystkich zwierząt łącznie, remisy rozwiązywane jak w genotypie pojedynczego zwierzaka */

    public int getDominantGenotype(){
        int[] geneCount=new int[8];
        for(int i=0;i<8;i++) geneCount[i]=0;
        for(SortedSet<Animal> animals: this.animals.values()){
            for(Animal animal: animals){
                int[] animalGenes=animal.getGenes().countTypes();
                for(int j=0;j<8;j++){
                    geneCount[j]+=animalGenes[j];
                }
            }
        }
        int max=geneCount[0];
        int index=0;
        for(int i=1;i<8;i++){
            if(geneCount[i]>max){
                max=geneCount[i];
                index=i;
            }
        }
        return index;
    }

    /* średnia energia dla żywych zwierząt */

    public double avgEnergy(){
        double sum=0;
        double counter=0;
        for(SortedSet<Animal>animals: this.animals.values()){
            for(Animal animal: animals){
                sum+=animal.getEnergy();
                counter+=1;
            }
        }
        return sum/counter;
    }

    /* średnia długość życia dla zmarłych zwierząt, wyliczana dzięki ustawieniu pewnych zmiennych w removeDeadAnimals,
    tam usuwając zwierzęta najpierw zapisujemy informację właśnie o ich długości życia, aktualizowane co epokę
     */

    public double avgLiveLength(){
        double oldAvg=this.avgLifeLen*this.deadAnimals;
        this.deadAnimals+=this.newDeadsCount;
        oldAvg+=this.newDeadsAgeSum;
        if(this.deadAnimals==0) return 0;
        this.avgLifeLen=oldAvg/this.deadAnimals;
        return this.avgLifeLen;
    }

    /* średnia liczba dzieci dla żyjących zwierząt, aktualizowana co epokę */

    public double avgKids(){
        double sum=0;
        double counter=0;
        for(SortedSet<Animal> animals:this.animals.values()){
            for(Animal animal:animals){
                sum+=animal.getKids();
                counter+=1;
            }
        }
        return sum/counter;
    }

    /* dodanie zwierzęcia do mapy */

    public void addAnimal(Animal animal,Vector2D position){
        if(this.animals.containsKey(position)){
            this.animals.get(position).add(animal);
        }
        else{
            SortedSet<Animal> animals=new TreeSet<>(this.eComp);
            animals.add(animal);
            this.animals.put(position, animals);
        }
    }

    /* usunięcie zwierząt o energii mniejszej lub równej 0 */

    public void removeDeadAnimals(){
        this.newDeadsAgeSum=0;
        this.newDeadsCount=0;
        for(SortedSet<Animal> animals: this.animals.values()){
            while(!animals.isEmpty() && animals.last().getEnergy()<=0){
                this.newDeadsCount+=1;
                this.newDeadsAgeSum+=animals.last().getAge();
                animals.last().positionChanged(animals.last().getPosition(),animals.last().getPosition());
                animals.removeIf(animal -> animal==animals.last());
            }
        }
        List<Vector2D> posToFree=new ArrayList<>();
        for(Vector2D position:this.animals.keySet()){
            if(this.animals.get(position).isEmpty()) posToFree.add(position);
        }
        for(Vector2D position: posToFree){
            this.animals.remove(position);
        }
    }

    /* ruch wszystkich zwierząt */

    public void moveAnimals(int energyLost){
        List<Animal> animalsToMove=new ArrayList<>();
        for(SortedSet<Animal> animals: this.animals.values()){
            animalsToMove.addAll(animals);
        }
        for(Animal animal: animalsToMove){
            animal.move();
            animal.subtractEnergy(energyLost);
        }
    }

    /* sprawdzenie czy pozycja jest na stepie czy w dzungli, korzystając z pary wektorów, którą reprezentuje dzungle */

    public boolean onSteppe(Vector2D position) {
        if (position.lowerLeft(this.jungleLowerLeft).equals(this.jungleLowerLeft) && position.upperRight(this.jungleUpperRight).equals(this.jungleUpperRight)) {
            return false;
        }
        return true;
    }

    /* dodanie roślin na mapie, losuje z dostępnych pól, chyba że takich nie ma to roślina się nie pojawia */

    public void addPlants(int unitEnergy){
        Random random=new Random();
        List <Vector2D> allowedOnSteppe = new ArrayList<>();
        List <Vector2D> allowedInJungle = new ArrayList<>();
        for(int w=0;w<=this.width;w++){
            for(int h=0;h<=this.height;h++){
                Vector2D position=new Vector2D(w,h);
                if(onSteppe(position)){
                    if(!this.plantsOnSteppe.containsKey(position)){
                        allowedOnSteppe.add(position);
                    }
                }
                else{
                    if(!this.plantsInJungle.containsKey(position)){
                        allowedInJungle.add(position);
                    }
                }
            }
        }
        if(allowedOnSteppe.size()>0){
            Vector2D newPlantPos=allowedOnSteppe.get(random.nextInt(allowedOnSteppe.size()));
            Plant newPlant=new Plant(newPlantPos,unitEnergy);
            this.plantsOnSteppe.put(newPlantPos,newPlant);
        }
        if(allowedInJungle.size()>0){
            Vector2D newPlantPos=allowedInJungle.get(random.nextInt(allowedInJungle.size()));
            Plant newPlant=new Plant(newPlantPos,unitEnergy);
            this.plantsInJungle.put(newPlantPos,newPlant);
        }
    }

    /* zlicza ile zwierząt ma maksymalną i jednocześnie równą energię z grupy zwierząt */

    public int countEqualMaxEnergy(SortedSet<Animal> animals){
        int c=0;
        int maxEnergy=animals.first().getEnergy();
        for(Animal animal: animals){
            if (animal.getEnergy()==maxEnergy){
                c+=1;
            }
            else{
                break;
            }
        }
        return c;
    }

    /* dwie funkcje do karmienia zwierząt, dla każdej rośliny sprawdzają czy jest na niej przynajmniej jedno zwierze,
    jeśli jest to najsilniejsze zjadają roślinę */

    public void feedAnimalsInJungle(){
        List <Vector2D> plantsEaten=new ArrayList<>();
        for (Plant plant: this.plantsInJungle.values()){
            if(this.animals.containsKey(plant.getPosition())){
                plantsEaten.add(plant.getPosition());
                SortedSet<Animal> animalsOnSamePos=this.animals.get(plant.getPosition());
                int s=animalsOnSamePos.size();
                if(s>0){
                    int eq=countEqualMaxEnergy(animalsOnSamePos);
                    int energyToAdd=plant.getUnitEnergy()/eq;
                    int i=0;
                    for (Animal animal: animalsOnSamePos){
                        if(i==eq){
                            break;
                        }
                        else{
                            animal.addEnergy(energyToAdd);
                            i+=1;
                        }
                    }
                }
            }
        }
        for (Vector2D position: plantsEaten){
            this.plantsInJungle.remove(position);
        }
    }

    public void feedAnimalsInSteppe(){
        List <Vector2D> plantsEaten=new ArrayList<>();
        for (Plant plant: this.plantsOnSteppe.values()){
            if(this.animals.containsKey(plant.getPosition())){
                plantsEaten.add(plant.getPosition());
                SortedSet<Animal> animalsOnSamePos=this.animals.get(plant.getPosition());
                int s=animalsOnSamePos.size();
                if(s>0){
                    int eq=countEqualMaxEnergy(animalsOnSamePos);
                    int energyToAdd=plant.getUnitEnergy()/eq;
                    int i=0;
                    for (Animal animal: animalsOnSamePos){
                        if(i==eq){
                            break;
                        }
                        else{
                            animal.addEnergy(energyToAdd);
                            i+=1;
                        }
                    }
                }
            }
        }
        for (Vector2D position: plantsEaten){
            this.plantsOnSteppe.remove(position);
        }
    }

    /* proces rozmnażania zwierząt, taki jak opisany w treści zadania i faq */

    public void spawnNewAnimals(int startEnergy){
        List <Animal> newborns=new ArrayList<>();
        for(int w=0;w<=this.width;w++){
            for(int h=0;h<=this.height;h++){
                Vector2D position=new Vector2D(w,h);
                SortedSet <Animal> currentAnimals=this.animals.get(position);
                if(this.animals.containsKey(position) && currentAnimals.size()>1){
                    Animal parent1=currentAnimals.first();
                    if(parent1.getEnergy()*2<startEnergy) continue;
                    currentAnimals.removeIf(animal -> animal==parent1);
                    Animal parent2=currentAnimals.first();
                    currentAnimals.add(parent1);
                    if(parent2.getEnergy()*2<startEnergy) continue;
                    List <Vector2D> freePositions=new ArrayList<>();
                    List <Vector2D> occupiedPositions=new ArrayList<>();
                    for(int i=-1;i<=1;i++){
                        for(int j=-1;j<=1;j++){
                            Vector2D posToCheck= new Vector2D((position.x+i)%this.width,(position.y+j)%this.height);
                            if(!this.animals.containsKey(posToCheck)){
                                freePositions.add(posToCheck);
                            }
                            else if(i!=0 && j!=0){
                                occupiedPositions.add(posToCheck);
                            }
                        }
                    }
                    Vector2D newbornPos;
                    Random random = new Random();
                    if(freePositions.size()>0){
                        newbornPos=freePositions.get(random.nextInt(freePositions.size()));
                    }
                    else{
                        newbornPos=occupiedPositions.get(random.nextInt(occupiedPositions.size()));
                    }
                    int e1= parent1.getEnergy()/4;
                    int e2= parent2.getEnergy()/4;
                    parent1.subtractEnergy(e1);
                    parent2.subtractEnergy(e2);
                    parent1.addKid();
                    parent2.addKid();
                    Animal newborn=new Animal(newbornPos, random.nextInt(8), e1+e2,parent1,parent2);
                    newborns.add(newborn);
                }
            }
        }
        for(Animal animal: newborns){
            this.addAnimal(animal, animal.getPosition());
        }
    }

    /* zwraca najsilniejsze zwierzę z zadanego pola */

    public Animal getStrongest(Vector2D position){
        Animal animal=this.animals.get(position).first();
        return animal;
    }

    /* przeskok dnia na mapie */

    public void nextDay(){
        this.day=this.day+1;
    }

    /* zmienia dla każdego zwierzaka po ruchu jego pozycję na mapie, usuwajac go i dodając z innym kluczem do hashmapy zwierząt */

    @Override
    public void positionChanged(Animal animal,Vector2D oldPosition, Vector2D newPosition) {
        if(!oldPosition.equals(newPosition)){
            SortedSet<Animal> current=this.animals.get(oldPosition);
            current.removeIf(animal1 -> animal1==animal);
            if(current.size()==0) this.animals.remove(oldPosition);
            addAnimal(animal,newPosition);
        }
    }
}
