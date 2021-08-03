package agh.cs;

import java.util.Comparator;

/* grupy zwierząt na mapie przechowywane są w SortedSecie, aby mieć szybki dostęp do zwierząt o największej i
najmniejszej energii w danym polu
 */

public class EnergyComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal o1, Animal o2) {
        if (o1.getEnergy()>o2.getEnergy()) return -1;
        return 1;
    }
}
