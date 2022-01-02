package classes;

import java.awt.*;
import java.util.Random;

public class Animal {

    public Vector2d position;
    public int orientation;
    public WorldMap thisMap;
    public int energy;
    public int[] genes;
    public int domGen;
    public int id;

    public int numberOfChildren;
    public int dayOfBirth;
    public int dayOfDeath;

    public Animal (WorldMap map, int x, int y, int startEnergy, int[] g){
        thisMap = map;
        position = new Vector2d(x, y);
        energy=startEnergy;
        genes=g;
        id=map.animalId;
        Random generator = new Random();
        int i = generator.nextInt(32);
        orientation = genes[i];
        numberOfChildren=0;
        dayOfBirth=map.days;
        domGen=getDomGen();
    }

    public Vector2d getPosition(){
        return position;
    }

    public int getDomGen(){
        int[] g = new int[8];
        for (int i=0; i < 32; i++) {
            g[genes[i]]++;
        }
        int dom=0;
        for (int i=1;i<8;i++) {
            if (g[i]>g[dom]){
                dom=i;
            }
        }
        return dom;
    }

    public Color getColor() {
        if (energy<=0) return new Color(255, 255, 255);
        if (energy < 0.2 * thisMap.startEnergy) return new Color(217, 176, 176);
        if (energy < 0.6 * thisMap.startEnergy) return new Color(203, 126, 114);
        if (energy < thisMap.startEnergy) return new Color(146, 87, 78);
        if (energy < 2 * thisMap.startEnergy) return new Color(130, 68, 59);
        if (energy < 6 * thisMap.startEnergy) return new Color(95, 52, 47);
        if (energy < 10 * thisMap.startEnergy) return new Color(62, 35, 31);
        return new Color(52, 29, 21);
    }

    public void move(){

        Random generator = new Random();
        int i = generator.nextInt(32);
        int o = genes[i];

        orientation = orientation + o;
        orientation = orientation % 8;

        switch (this.orientation) {
            case 0 -> this.position.y = this.position.y + 1;
            case 1 -> {
                this.position.y = this.position.y + 1;
                this.position.x = this.position.x + 1;
            }
            case 2 -> this.position.x = this.position.x + 1;
            case 3 -> {
                this.position.y = this.position.y - 1;
                this.position.x = this.position.x + 1;
            }
            case 4 -> this.position.y = this.position.y - 1;
            case 5 -> {
                this.position.y = this.position.y - 1;
                this.position.x = this.position.x - 1;
            }
            case 6 -> this.position.x = this.position.x - 1;
            case 7 -> {
                this.position.y = this.position.y + 1;
                this.position.x = this.position.x - 1;
            }
        }

        //Jeśli zwierzak wyszedł poza mapę, pojawia się po jej drugiej stronie,
        //zgodnie z wymaganiem zawijanych rogów:

        if (this.position.x>thisMap.w-1){
            this.position.x=0;
        }
        else if (this.position.x<0){
            this.position.x=thisMap.w-1;
        }

        if (this.position.y>thisMap.h-1){
            this.position.y=0;
        }
        else if (this.position.y<0){
            this.position.y=thisMap.h-1;
        }

    }

}