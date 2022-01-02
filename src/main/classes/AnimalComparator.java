package classes;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal o1, Animal o2){
        if (o1.energy>o2.energy){
            return 1;
        }
        if (o1.energy<o2.energy){
            return -1;
        }
        else {
            return Integer.compare(o1.id, o2.id);
        }
    }
}
