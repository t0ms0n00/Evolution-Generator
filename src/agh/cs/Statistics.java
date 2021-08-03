package agh.cs;

public class Statistics {
    private JungleMap map;
    private StatTexts statTexts;
    private AnimalStatsTexts animalStatsTexts;
    private Animal animal;

    public Statistics(JungleMap map){
        this.map=map;
    }

    public Statistics(Animal animal){this.animal=animal;}

    /* wyznaczenie statystyk mapy */

    public String setText(int i){
        switch (i){
            case 0:return (this.statTexts.values()[i].toString()+this.map.getAmountOfAnimals());
            case 1:return (this.statTexts.values()[i].toString()+this.map.getAmountOfPlants());
            case 2:return (this.statTexts.values()[i].toString()+this.map.getDominantGenotype());
            case 3:return (this.statTexts.values()[i].toString()+this.map.avgEnergy());
            case 4:return (this.statTexts.values()[i].toString()+this.map.avgLiveLength());
            case 5:return (this.statTexts.values()[i].toString()+this.map.avgKids());
            default: return "ERROR";
        }
    }

    /* wyznaczenie statystyk śledzonego zwierzaka */

    public String setAnimalText(int i){
        switch (i){
            case 0:return (this.animalStatsTexts.values()[i].toString()+this.animal.getPosition());
            case 1:return (this.animalStatsTexts.values()[i].toString()+this.animal.getEnergy());
            case 2:return (this.animalStatsTexts.values()[i].toString()+this.animal.getOrient());
            case 3:return (this.animalStatsTexts.values()[i].toString()+this.animal.getGenes());
            case 4:return (this.animalStatsTexts.values()[i].toString()+this.animal.getKids());
            case 5:return (this.animalStatsTexts.values()[i].toString()+this.animal.getAge());
            case 6:
                if (animal.getDeadDay() != -1){
                    return (this.animalStatsTexts.values()[i].toString()+this.animal.getDeadDay());
                }
                else{
                    return ("Ciagle zywy");
                }
            default: return "ERROR";
        }
    }
}
