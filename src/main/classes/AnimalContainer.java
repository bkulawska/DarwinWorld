package classes;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class AnimalContainer {

    Comparator<Animal> Comp = new AnimalComparator();
    public Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();

    public void add(Animal animal){
        Vector2d v = animal.getPosition();
        TreeSet<Animal> z = animals.get(v);
        if (z==null){
            TreeSet<Animal> x = new TreeSet<>(Comp);
            x.add(animal);
            animals.put(v, x);
        }
        else {
            z.add(animal);
        }
    }

    public void remove(Animal animal){
        Vector2d v = animal.getPosition();
        TreeSet<Animal> z = animals.get(v);
        if (z!=null && !z.isEmpty()){
            animals.remove(v);
            z.remove(animal);
            animals.put(v, z);
        }
    }

}
