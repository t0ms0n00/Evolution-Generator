package agh.cs;

/* enum opisujący statystyki dla mapy, wykorzystywany w klasie Statistics */

public enum StatTexts {
    COUNT_ANIMALS,
    COUNT_PLANTS,
    GENOTYPE,
    ENERGY,
    LIFE,
    KIDS;

    public String toString(){
        switch(this){
            case LIFE:
                return "Srednia dlugosc zycia: ";
            case KIDS:
                return "Srednia liczba dzieci: ";
            case ENERGY:
                return "Sredni poziom energii: ";
            case COUNT_PLANTS:
                return "Liczba roslin: ";
            case COUNT_ANIMALS:
                return "Liczba zwierzat: ";
            case GENOTYPE:
                return "Dominujacy gen: ";
            default:
                return "ERROR";
        }
    }
}
