package agh.cs;

/* określa statystyki dla zwierzęcia, enum wykorzystywany w klasie Statistics */

public enum AnimalStatsTexts {
    POSITION,
    ENERGY,
    ORIENT,
    GENOTYPE,
    KIDS,
    AGE,
    DEAD_DAY;

    public String toString(){
        switch (this){
            case AGE:
                return "Wiek: ";
            case KIDS:
                return "Liczba dzieci: ";
            case ENERGY:
                return "Energia: ";
            case ORIENT:
                return "Ostatni Ruch: ";
            case GENOTYPE:
                return "Genotyp: ";
            case POSITION:
                return "Polozenie: ";
            case DEAD_DAY:
                return "Dzien smierci: ";
            default:
                return "ERROR";
        }
    }
}
