package classes;

public class Plant {

    public Vector2d position;
    public WorldMap thisMap;

    public Plant (int x, int y, WorldMap map){
        position = new Vector2d(x, y);
        thisMap = map;
    }

    public Vector2d getPosition(){
        return position;
    }

}
