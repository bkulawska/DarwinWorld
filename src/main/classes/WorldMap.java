package classes;

import viewClasses.Painter;

import java.util.*;

public class WorldMap {

    public int w;
    public int h;
    public Vector2d lowerLeft;
    public Vector2d upperRight;

    public int jungleW;
    public int jungleH;
    public Vector2d jungleUpperRight;
    public Vector2d jungleLowerLeft;

    public int startEnergy;
    public int moveEnergy;
    public int plantEnergy;

    public int animalId;

    public int days;
    public int daysForStatistics;

    public int numberOfPlants;
    public int numberOfPlantsInStep;
    public int numberOfPlantsInJungle;

    public int numberOfAliveAnimals;
    public int numberOfDeadAnimals;
    public int numberOfAnimalsInTotal;

    public Animal trackedAnimal=null;
    public int trackingEndDay;
    public int trackingNumOfChildrenBefore;

    public StatisticsParser statisticsParser;

    public AnimalContainer container = new AnimalContainer();
    public Map<Vector2d, Plant> plants = new HashMap<>();

    public ArrayList<Animal> deadAnimals = new ArrayList<>();
    public ArrayList<Animal> aliveAnimals = new ArrayList<>();

    public WorldMap(int w, int h, int jungleRatio, int sE, int mE, int pE, int d, StatisticsParser p){

        startEnergy = sE;
        moveEnergy = mE;
        plantEnergy = pE;
        days=0;
        daysForStatistics=d;
        statisticsParser =p;
        this.w=w;
        this.h=h;
        lowerLeft = new Vector2d(0,0);
        upperRight = new Vector2d(w-1, h-1);
        jungleW = w/jungleRatio;
        jungleH = h/jungleRatio;
        jungleLowerLeft = new Vector2d(w/2-jungleW/2-1, h/2-jungleH/2-1);
        jungleUpperRight = new Vector2d(w/2+jungleW/2-1, h/2+jungleH/2-1);
        jungleW=jungleUpperRight.x-jungleLowerLeft.x+1;
        jungleH=jungleUpperRight.y-jungleLowerLeft.y+1;
        animalId=0;

        numberOfAliveAnimals=0;
        numberOfDeadAnimals=0;
        numberOfAnimalsInTotal=0;
        numberOfPlants=0;
        numberOfPlantsInStep=0;
        numberOfPlantsInJungle=0;

    }

    public void place (Animal animal){
        container.add(animal);
        aliveAnimals.add(animal);
        animalId++;
        numberOfAliveAnimals++;
        numberOfAnimalsInTotal++;
    }

    public void addFirstAnimals(int n){
        for (int i=0; i<n; i++) {

            Random generator = new Random();
            int x;
            int y;
            boolean b;

            //Losowanie pozycji zwierzęcia (x i y)
            do {
                b=false;
                x = generator.nextInt(w);
                y = generator.nextInt(h);
                Vector2d pos = new Vector2d(x,y);
                if (container.animals.get(pos)!=null) {
                    b=true;
                }
            } while (b);

            //Losowanie genów zwierzęcia
            int[] g = new int [32];
            int[] tab = new int [8];

            for (int j=0; j<8; j++) tab[j] = 0;

            for (int j=0; j<32; j++) {
                g[j]=generator.nextInt(8);
                tab[g[j]]++;
            }

            Arrays.sort(g);

            //Zastępowanie genów, jeśli któregoś brakuje
            do {
                b=false;
                for (int j=0; j<8; j++) {
                    if (tab[j]==0) {
                        b = true;
                        replaceGen(j, g, tab);
                    }
                }
            } while (b);

            //Dodanie zwierzęcia do mapy
            Animal animal = new Animal(this, x, y, startEnergy, g);
            place(animal);
        }
    }

    public void replaceGen(int x, int[] g, int[] tab){
        Random generator = new Random();
        while (true){
            int i = generator.nextInt(31);
            if (g[i]==g[i+1]){
                g[i]=x;
                tab[x]++;
                tab[g[i+1]]--;
                Arrays.sort(g);
                return;
            }
        }
    }

    public void decreaseDailyEnergyAndRemoveDead(){
        ArrayList<Animal> temp = new ArrayList<>(aliveAnimals);

        for (Animal animal: temp) {
            container.remove(animal);
            aliveAnimals.remove(animal);
            animal.energy-=moveEnergy;
            if (animal.energy<=0){
                if (!deadAnimals.contains(animal)){
                    animal.dayOfDeath=days-1;
                    deadAnimals.add(animal);
                    numberOfAliveAnimals--;
                    numberOfDeadAnimals++;
                }
            }
            else{
                aliveAnimals.add(animal);
                container.add(animal);
            }
        }
    }

    public void moveAnimals(){
        ArrayList<Animal> temp = new ArrayList<>(aliveAnimals);

        for (Animal animal: temp) {
            container.remove(animal);
            aliveAnimals.remove(animal);
            animal.move();
            aliveAnimals.add(animal);
            container.add(animal);
        }
    }

    public void addPlants(){
        addPlantInJungle();
        addPlantInStep();
    }

    public void addPlantInJungle(){
        Random generator = new Random();
        int x;
        int y;
        boolean b;
        int counter=0;
        //Losowanie pozycji rośliny
        do {
            b=false;
            x = generator.nextInt(jungleUpperRight.x+1);
            y = generator.nextInt(jungleUpperRight.y+1);
            Vector2d pos = new Vector2d(x,y);

            if (!(pos.follows(jungleLowerLeft) && pos.precedes(jungleUpperRight))){
                b=true;
            }
            else if (plants.get(pos)!=null){
                b=true;
            }
            else if (container.animals.get(pos)!=null) {
                if (!container.animals.get(pos).isEmpty()){
                    b=true;
                }
            }
            counter++;

        } while (b && counter<10);

        if (!b){
            Plant plant1 = new Plant(x,y,this);
            plants.put(plant1.getPosition(), plant1);
            numberOfPlants++;
            numberOfPlantsInJungle++;
        }

        //Jeśli nie udało się wylosować wolnej pozycji 10 razy, to przechodzimy
        //do iteracji, w której znajdziemy pierwsze wolne miejsce na mapie

        else {
            for (int i=jungleLowerLeft.x; i<jungleUpperRight.x+1; i++){
                for (int j=jungleLowerLeft.y; j<jungleUpperRight.y+1; j++){
                    Vector2d pos = new Vector2d(i,j);
                    if (plants.get(pos)==null){
                        if (container.animals.get(pos)==null || container.animals.get(pos).isEmpty()) {
                            Plant plant1 = new Plant(x,y,this);
                            plants.put(plant1.getPosition(), plant1);
                            numberOfPlants++;
                            numberOfPlantsInJungle++;
                            return;
                        }
                    }
                }
            }
        }
    }

    public void addPlantInStep(){
        Random generator = new Random();
        int x;
        int y;
        boolean b;
        int counter=0;

        if (canPlantsGrowInStep()){
            //Losowanie pozycji rośliny
            do {
                b=false;
                x = generator.nextInt(w);
                y = generator.nextInt(h);
                Vector2d pos = new Vector2d(x,y);

                if (pos.follows(jungleLowerLeft) && pos.precedes(jungleUpperRight)){
                    b=true;
                }
                else if (plants.get(pos)!=null){
                    b=true;
                }
                else if (container.animals.get(pos)!=null) {
                    if (!container.animals.get(pos).isEmpty()){
                        b=true;
                    }
                }
                counter++;

            } while (b && counter<10);

            if (!b){
                Plant plant1 = new Plant(x,y,this);
                plants.put(plant1.getPosition(), plant1);
                numberOfPlants++;
                numberOfPlantsInJungle++;
            }

            //Jeśli nie udało się wylosować wolnej pozycji 10 razy, to przechodzimy
            //do iteracji, w której znajdziemy pierwsze wolne miejsce na mapie
            else {
                for (int i=0; i<w; i++){
                    for (int j=0; j<h; j++){
                        Vector2d pos = new Vector2d(i,j);
                        if (!(pos.follows(jungleLowerLeft) && pos.precedes(jungleUpperRight))){
                            if (plants.get(pos)==null){
                                if (container.animals.get(pos)==null || container.animals.get(pos).isEmpty()) {
                                    Plant plant1 = new Plant(x,y,this);
                                    plants.put(plant1.getPosition(), plant1);
                                    numberOfPlants++;
                                    numberOfPlantsInJungle++;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean canPlantsGrowInStep(){
        return numberOfPlantsInStep < w * h - jungleW * jungleH;
    }

    public boolean isInJungle(Vector2d v){
        return v.follows(jungleLowerLeft) && v.precedes(jungleUpperRight);
    }

    public void eatPlants(){
        Plant[] tab = new Plant[plants.size()];
        int j=0;
        //Dla każdej rośliny na mapie:
        for (Plant plant: plants.values()) {
            TreeSet<Animal> temp = container.animals.get(plant.getPosition());
            if (temp!=null && !temp.isEmpty()) {
                int thisPlantEnergy=plantEnergy;
                Animal[] t = new Animal[temp.size()];
                int k=0;
                Animal animal = temp.first();

                //Znajdowanie wszystkich zwierząt z max energią, które stoją na polu,
                //na którym znajduje się roślina
                while (true){
                    t[k]=temp.first();
                    temp.remove(t[k]);
                    if (temp.isEmpty()){
                        break;
                    }
                    k++;
                }

                //Dzielenie energii rośliny pomiędzy zwierzęta z max energią
                for (int i=0; i<k; i++){
                    Animal anim = t[i];
                    container.remove(anim);
                    aliveAnimals.remove(animal);
                    if (i==k-1){
                        anim.energy += thisPlantEnergy;
                    }
                    else {
                        anim.energy += plantEnergy/k;
                        thisPlantEnergy -= plantEnergy/k;
                    }
                    aliveAnimals.add(animal);
                    container.add(animal);
                }
                tab[j]=plant;
                j++;
            }
        }

        //Usuwanie zjedzonych roślin z mapy
        for (int i=0; i<j; i++){
            if (isInJungle(tab[i].getPosition())){
                numberOfPlantsInJungle--;
            }
            else {
                numberOfPlantsInStep--;
            }
            plants.remove(tab[i].getPosition());
            numberOfPlants--;
        }
    }

    public void reproduce(){

        Map<Vector2d, TreeSet<Animal>> tempMap = new HashMap<>();

        //Przechodzimy po każdym zajętym przez zwierzę polu na mapie
        for (TreeSet<Animal> s: container.animals.values()) {
            TreeSet<Animal> r = new TreeSet<>(container.Comp);
            //Przechodzimy po zwierzętach stojących na tym samym polu
            for (Animal animal: s) {
                if (animal.energy>= startEnergy/2){
                    r.add(animal);
                }
            }
            if (!r.isEmpty() && r.size()>1){
                tempMap.put(r.first().getPosition(), r);
            }
        }

        for (TreeSet<Animal> r: tempMap.values()){

            //Wybieranie rodziców

            Animal[] t = new Animal[r.size()];
            int k=0;
            Animal animal = r.first();
            int e = animal.energy;

            //Znajdujemy wszystkie zwierzęta z max energią, które stoją na tym samym polu
            while (e==animal.energy) {
                t[k] = r.first();
                r.remove(t[k]);
                k++;
                if (!r.isEmpty()) {
                    e = r.first().energy;
                } else {
                    break;
                }
            }

            Animal animal1;
            Animal animal2=null;

            if (k>1){
                //Jeśli zwierząt z max energią jest więcej niż 1, losujemy spośród nich oboje rodziców
                Random generator = new Random();
                int i = generator.nextInt(k);
                animal1 = t[i];

                int j=i;
                while (j==i) {
                    j = generator.nextInt(k);
                    animal2 = t[j];
                }
            }


            else {
                //Jeśli było tylko jedno zwierzę z max energią, to zostaje ono rodzicem 1
                animal1=t[0];
                Animal[] t1 = new Animal[r.size()];
                int k1=0;
                Animal anim = r.first();
                int e1 = anim.energy;

                //Następnie z pozostałych zwierząt znajdujemy wszystkie zwierzęta,
                //które teraz mają max energię
                while (e1==anim.energy) {
                    t1[k1] = r.first();
                    r.remove(t1[k1]);
                    k1++;
                    if (!r.isEmpty()) {
                        e1 = r.first().energy;
                    } else {
                        break;
                    }
                }

                //Losujemy spośród zwierząt z max energią rodzica 2
                Random generator = new Random();
                int j = generator.nextInt(k1);
                animal2 = t1[j];
            }

            container.remove(animal1);
            aliveAnimals.remove(animal1);
            container.remove(animal2);
            aliveAnimals.remove(animal2);

            //W tym momencie mamy już wybranych oboje rodziców i następuje rozmnażanie/rodzi się dziecko:
            giveBirth(animal1, animal2);
        }
    }

    public void giveBirth(Animal animal1, Animal animal2){
        //Rodzice oddają po 1/4 swojej energii na rzecz dziecka
        int energy;
        energy = animal1.energy / 4 + animal2.energy / 4;
        animal1.energy = animal1.energy * 3 / 4;
        animal2.energy = animal2.energy * 3 / 4;

        Random generator = new Random();

        //Znajdujemy wolny sąsiedni kwadracik, na którym może wylądować urodzone zwierzątko
        Vector2d pos = animal1.getPosition();
        int x=pos.x;
        int y=pos.y;
        for (int i=-1; i<2;i++){
            for (int j=-1; j<2;j++){
                Vector2d temp = new Vector2d(i,j);
                if (container.animals.get(temp)==null){
                    if (plants.get(temp)==null){
                        x=x+i;
                        y=y+j;
                    }
                }
            }
        }

        //Jeśli nie ma wolnego sąsiedniego pola, to wybieramy losowe zajęte sąsiednie pole
        if (x==pos.x && y==pos.y){
            int[] tab = new int[3];
            tab[0]=-1;
            tab[2]=1;
            int a = generator.nextInt(3);
            int b = generator.nextInt(3);
            x=x+tab[a];
            y=y+tab[b];
        }

        //Dobieranie genów dziecka na podstawie genów rodziców
        int[] g = new int[32];
        int[] tab = new int[8];

        for (int j = 0; j < 8; j++) tab[j] = 0;

        int in1 = generator.nextInt(30);
        int in2 = generator.nextInt(31 - in1) + in1;

        System.arraycopy(animal1.genes, 0, g, 0, in1);
        if (in2 - in1 >= 0) System.arraycopy(animal2.genes, in1, g, in1, in2 - in1);
        if (32 - in2 >= 0) System.arraycopy(animal1.genes, in2, g, in2, 32 - in2);

        Arrays.sort(g);

        //Zastępowanie genów, jeśli któregoś brakuje
        boolean c;
        do {
            c = false;
            for (int j = 0; j < 8; j++) {
                if (tab[j] == 0) {
                    c = true;
                    replaceGen(j, g, tab);
                }
            }

        } while (c);

        animal1.numberOfChildren++;
        animal2.numberOfChildren++;

        //Dodanie rodziców z powrotem na mapę
        container.add(animal1);
        aliveAnimals.add(animal1);
        container.add(animal2);
        aliveAnimals.add(animal2);

        //Dodanie nowego zwierzęcia na mapę
        Animal anim = new Animal(this, x, y, energy, g);
        place(anim);

    }

    //Funkcje do tworzenia statystyk:

    public int getNumberOfAnimals() {
        int numAnimals=0;

        for (Animal animal: aliveAnimals) {
            if (animal.energy>0){
                numAnimals++;
            }
        }
        return numAnimals;
    }

    public int getDominantGen() {

        int[] g = new int[8];

        for (Animal animal: aliveAnimals) {
            if (animal.energy>0) {
                for (int i = 0; i < 32; i++) {
                    g[animal.genes[i]]++;
                }
            }
        }

        int dom=0;

        for (int i=1;i<8;i++) {
            if (g[i]>g[dom]){
                dom=i;
            }
        }

        return dom;
    }

    public double getAverageEnergyLevel() {
        int numEnergy=0;
        int numAnimals=0;

        for (Animal animal: aliveAnimals) {
            if (animal.energy>0){
                numEnergy+=animal.energy;
                numAnimals++;
            }
        }

        if (numAnimals!=0){
            double x = ((double)numEnergy)/numAnimals;
            x *= 100;
            x = Math.round(x);
            x /= 100;
            return x;
        }
        else {
            return 0;
        }
    }

    public double getAverageLifeLength() {
        int numLife=0;
        int numAnimals=0;

        for (Animal animal: deadAnimals) {
            numLife+=(animal.dayOfDeath-animal.dayOfBirth);
            numAnimals++;
        }

        if (numAnimals!=0){
            double x = ((double)numLife)/numAnimals;
            x *= 100;
            x = Math.round(x);
            x /= 100;
            return x;
        }
        else {
            return 0;
        }
    }

    public double getAverageNumberOfChildren() {
        int numChildren=0;
        int numAnimals=0;

        for (Animal animal: aliveAnimals) {
            if (animal.energy>0){
                numChildren+=animal.numberOfChildren;
                numAnimals++;
            }
        }

        if (numAnimals!=0){
            double x = ((double)numChildren)/numAnimals;
            x *= 100;
            x = Math.round(x);
            x /= 100;
            return x;
        }
        else {
            return 0;
        }
    }

    //Funkcje do śledzenia wybranego zwierzaka:

    public void track(Animal animal, int n){
        trackedAnimal=animal;
        trackingEndDay=days+n;
        trackingNumOfChildrenBefore=animal.numberOfChildren;
    }

    public void checkOnTracked(Painter visualization){
        if (trackedAnimal!=null){
            if (days==trackingEndDay){
                visualization.trackPanel.endTracking();
            }
            if (trackedAnimal.energy<=0){
                visualization.trackPanel.trackedDied();
                if (days<trackingEndDay){
                    visualization.trackPanel.endTracking();
                }
                trackedAnimal=null;
            }
        }
    }

}
