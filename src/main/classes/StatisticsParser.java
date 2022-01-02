package classes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Obsługa eksportu uśrednionych statystyk do pliku

public class StatisticsParser {

    public int id;

    public StatisticsParser(int id){
        this.id=id;
    }

    public String toJson(int days, double numberOfAnimals, double numberOfPlants, int dominantGen,
                         double averageEnergyLevel, double averageLifeLength, double averageNumberOfChildren){

        return "{" +
                "\"days\": " + days + ", " +
                "\"average number of animals\": " + numberOfAnimals/days + ", " +
                "\"average number of plants\": " + numberOfPlants/days + ", " +
                "\"dominant gen\": " + dominantGen + ", " +
                "\"average energy level\": " + averageEnergyLevel/days + ", " +
                "\"average life length\": " + averageLifeLength/days + ", " +
                "\"average number of children\": " + averageNumberOfChildren/days +
                "}";

    }

    public void toFile(String s) throws IOException {
        Path outputFile;
        if (id==1){
            outputFile = Paths.get("./StatisticsFromSimulation1.json");
        }
        else {
            outputFile = Paths.get("./StatisticsFromSimulation2.json");
        }
        Files.writeString(outputFile, s);
    }

}