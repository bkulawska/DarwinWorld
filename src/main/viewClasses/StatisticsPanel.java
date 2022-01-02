package viewClasses;

import classes.Simulation;
import classes.WorldMap;

import javax.swing.*;
import java.io.IOException;

//Panel do wyświetlania aktualnych statystyk

public class StatisticsPanel extends JPanel {

    public WorldMap map;
    public Simulation simulation;

    public int numberOfDays;
    public int numberOfAnimals;
    public int numberOfPlants;
    public int dominantGenotype;
    public double averageEnergyLevel;
    public double averageLifeLength;
    public double averageNumberOfChildren;

    public double numberOfAnimals2;
    public double numberOfPlants2;
    public int[] genes;
    public double averageEnergyLevel2;
    public double averageLifeLength2;
    public double averageNumberOfChildren2;

    private JTextField numberOfDaysField;
    private JTextField numberOfAnimalsField;
    private JTextField numberOfPlantsField;
    private JTextField dominantGenotypeField;
    private JTextField averageEnergyLevelField;
    private JTextField averageLifeLengthField;
    private JTextField averageNumberOfChildrenField;

    private JLabel numberOfDaysLabel;
    private JLabel numberOfAnimalsLabel;
    private JLabel numberOfPlantsLabel;
    private JLabel dominantGenotypeLabel;
    private JLabel averageEnergyLevelLabel;
    private JLabel averageLifeLengthLabel;
    private JLabel averageNumberOfChildrenLabel;

    public StatisticsPanel(WorldMap map, Simulation simulation, int n) {

        this.setSize((int) (simulation.frame.getWidth() * 0.4), (int) (simulation.frame.getHeight() * 0.8));
        this.setLocation(0, (int) (0.1 * simulation.frame.getHeight()));

        this.map = map;
        this.simulation = simulation;
        numberOfPlants=0;
        numberOfAnimals=n;
        averageNumberOfChildren=0;
        averageEnergyLevel=map.startEnergy;
        numberOfDays=0;
        genes=new int[8];
        for (int i=0;i<8;i++) genes[i] = 0;

        numberOfDaysLabel = new JLabel("Day:         ");
        numberOfAnimalsLabel = new JLabel("Number of animals:         ");
        numberOfPlantsLabel = new JLabel("Number of plants:         ");
        dominantGenotypeLabel = new JLabel("Dominant gen:         ");
        averageEnergyLevelLabel = new JLabel("Average animal energy level:         ");
        averageLifeLengthLabel = new JLabel("Average animal life length:         ");
        averageNumberOfChildrenLabel = new JLabel("Average animal number of children:         ");

        int a=7;

        numberOfDaysField = new JTextField();
        numberOfDaysField.setColumns(a);
        numberOfDaysField.setText(String.valueOf(numberOfDays));

        numberOfAnimalsField = new JTextField();
        numberOfAnimalsField.setColumns(a);
        numberOfAnimalsField.setText(String.valueOf(numberOfAnimals));

        numberOfPlantsField = new JTextField();
        numberOfPlantsField.setColumns(a);
        numberOfPlantsField.setText(String.valueOf(numberOfPlants));

        dominantGenotypeField = new JTextField();
        dominantGenotypeField.setColumns(a);

        averageEnergyLevelField = new JTextField();
        averageEnergyLevelField.setColumns(a);
        averageEnergyLevelField.setText(String.valueOf(averageEnergyLevel));

        averageLifeLengthField = new JTextField();
        averageLifeLengthField.setColumns(a);

        averageNumberOfChildrenField = new JTextField();
        averageNumberOfChildrenField.setColumns(a);
        averageNumberOfChildrenField.setText(String.valueOf(averageNumberOfChildren));

        numberOfDaysLabel.setLabelFor(numberOfDaysField);
        numberOfAnimalsLabel.setLabelFor(numberOfAnimalsField);
        numberOfPlantsLabel.setLabelFor(numberOfPlantsField);
        dominantGenotypeLabel.setLabelFor(dominantGenotypeField);
        averageEnergyLevelLabel.setLabelFor(averageEnergyLevelField);
        averageLifeLengthLabel.setLabelFor(averageLifeLengthField);
        averageNumberOfChildrenLabel.setLabelFor(averageNumberOfChildrenField);

        JPanel p0 = new JPanel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();
        JPanel p6 = new JPanel();

        p0.add(numberOfDaysLabel);
        p0.add(numberOfDaysField);
        p1.add(numberOfAnimalsLabel);
        p1.add(numberOfAnimalsField);
        p2.add(numberOfPlantsLabel);
        p2.add(numberOfPlantsField);
        p3.add(dominantGenotypeLabel);
        p3.add(dominantGenotypeField);
        p4.add(averageEnergyLevelLabel);
        p4.add(averageEnergyLevelField);
        p5.add(averageLifeLengthLabel);
        p5.add(averageLifeLengthField);
        p6.add(averageNumberOfChildrenLabel);
        p6.add(averageNumberOfChildrenField);

        add(p0);
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(p5);
        add(p6);

    }

    public void update(){

        numberOfDays=map.days;
        numberOfAnimals=map.getNumberOfAnimals();
        numberOfPlants=map.numberOfPlants;
        dominantGenotype=map.getDominantGen();
        averageEnergyLevel=map.getAverageEnergyLevel();
        averageLifeLength=map.getAverageLifeLength();
        averageNumberOfChildren=map.getAverageNumberOfChildren();

        numberOfDaysField.setText(String.valueOf(numberOfDays));
        numberOfAnimalsField.setText(String.valueOf(numberOfAnimals));
        numberOfPlantsField.setText(String.valueOf(numberOfPlants));
        dominantGenotypeField.setText(String.valueOf(dominantGenotype));
        averageEnergyLevelField.setText(String.valueOf(averageEnergyLevel));
        averageLifeLengthField.setText(String.valueOf(averageLifeLength));
        averageNumberOfChildrenField.setText(String.valueOf(averageNumberOfChildren));

        numberOfAnimals2+=numberOfAnimals;
        numberOfPlants2+=numberOfPlants;
        averageEnergyLevel2+=averageEnergyLevel;
        averageLifeLength2+=averageLifeLength;
        averageNumberOfChildren2+=averageNumberOfChildren;
        genes[dominantGenotype]++;

    }

    public void statisticsToFile() throws IOException {
        String s = map.statisticsParser.toJson(map.days, numberOfAnimals2, numberOfPlants2, averageDominantGen(), averageEnergyLevel2, averageLifeLength2, averageNumberOfChildren2);
        map.statisticsParser.toFile(s);
    }

    public int averageDominantGen(){
        int dom=0;
        for (int i=1;i<8;i++) {
            if (genes[i]>dom){
                dom=i;
            }
        }
        return dom;
    }

}
