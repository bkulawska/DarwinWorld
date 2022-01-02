package viewClasses;

import classes.Simulation;
import classes.StatisticsParser;
import classes.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantLock;

//Panel początkowy do wprowadzenia parametrów symulacji i uruchomienia jej

public class SettingsPanel extends JPanel implements ActionListener {

    protected ReentrantLock locker = new ReentrantLock();

    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;

    public int width;
    public int height;
    public int jungleRatio;
    public int startEnergy;
    public int moveEnergy;
    public int plantEnergy;
    public int numberOfFirstAnimals;
    public int daysForStatistics;

    private JTextField widthField;
    private JTextField heightField;
    private JTextField jungleRatioField;
    private JTextField startEnergyField;
    private JTextField moveEnergyField;
    private JTextField plantEnergyField;
    private JTextField numberOfFirstAnimalsField;
    private JTextField daysForStatisticsField;

    private JLabel widthLabel;
    private JLabel heightLabel;
    private JLabel jungleRatioLabel;
    private JLabel startEnergyLabel;
    private JLabel moveEnergyLabel;
    private JLabel plantEnergyLabel;
    private JLabel numberOfFirstAnimalsLabel;
    private JLabel daysForStatisticsLabel;

    private JLabel header;
    private JButton startButton;

    public SettingsPanel() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        startButton = new JButton("Start");
        startButton.addActionListener(this);

        width=30;
        height=30;
        jungleRatio=3;
        startEnergy=500;
        moveEnergy=1;
        plantEnergy=80;
        numberOfFirstAnimals=20;
        daysForStatistics=100;

        header = new JLabel("Wprowadź parametry dla symulacji:");
        widthLabel = new JLabel("Map width:    ");
        heightLabel = new JLabel("Map height:    ");
        jungleRatioLabel = new JLabel("Jungle ratio:    ");
        startEnergyLabel = new JLabel("Animal start energy:    ");
        moveEnergyLabel = new JLabel("Move energy cost:    ");
        plantEnergyLabel = new JLabel("Plant energy:     ");
        numberOfFirstAnimalsLabel = new JLabel("Number of first animals:     ");
        daysForStatisticsLabel = new JLabel("Po ilu dniach wygenerować statystyki do pliku?");

        int a = 10;

        widthField = new JTextField();
        widthField.setColumns(a);
        widthField.setText(String.valueOf(width));

        heightField = new JTextField();
        heightField.setColumns(a);
        heightField.setText(String.valueOf(height));

        jungleRatioField = new JTextField();
        jungleRatioField.setColumns(a);
        jungleRatioField.setText(String.valueOf(jungleRatio));

        startEnergyField = new JTextField();
        startEnergyField.setColumns(a);
        startEnergyField.setText(String.valueOf(startEnergy));

        moveEnergyField = new JTextField();
        moveEnergyField.setColumns(a);
        moveEnergyField.setText(String.valueOf(moveEnergy));

        plantEnergyField = new JTextField();
        plantEnergyField.setColumns(a);
        plantEnergyField.setText(String.valueOf(plantEnergy));

        numberOfFirstAnimalsField = new JTextField();
        numberOfFirstAnimalsField.setColumns(a);
        numberOfFirstAnimalsField.setText(String.valueOf(numberOfFirstAnimals));

        daysForStatisticsField = new JTextField();
        daysForStatisticsField.setColumns(a);
        daysForStatisticsField.setText(String.valueOf(daysForStatistics));

        widthLabel.setLabelFor(widthField);
        heightLabel.setLabelFor(heightField);
        jungleRatioLabel.setLabelFor(jungleRatioField);
        startEnergyLabel.setLabelFor(startEnergyField);
        moveEnergyLabel.setLabelFor(moveEnergyField);
        plantEnergyLabel.setLabelFor(plantEnergyField);
        numberOfFirstAnimalsLabel.setLabelFor(numberOfFirstAnimalsField);
        daysForStatisticsLabel.setLabelFor(daysForStatisticsField);

        JPanel p0 = new JPanel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();
        JPanel p6 = new JPanel();
        JPanel p7 = new JPanel();
        JPanel p8 = new JPanel();

        p0.add(header);
        p1.add(widthLabel);
        p1.add(widthField);
        p2.add(heightLabel);
        p2.add(heightField);
        p3.add(jungleRatioLabel);
        p3.add(jungleRatioField);
        p4.add(startEnergyLabel);
        p4.add(startEnergyField);
        p5.add(moveEnergyLabel);
        p5.add(moveEnergyField);
        p6.add(plantEnergyLabel);
        p6.add(plantEnergyField);
        p7.add(numberOfFirstAnimalsLabel);
        p7.add(numberOfFirstAnimalsField);
        p8.add(daysForStatisticsLabel);
        p8.add(daysForStatisticsField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        add(p0);
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(p5);
        add(p6);
        add(p7);
        add(p8);
        add(buttonPanel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        int jungleRatio = Integer.parseInt(jungleRatioField.getText());
        int startEnergy = Integer.parseInt(startEnergyField.getText());
        int moveEnergy = Integer.parseInt(moveEnergyField.getText());
        int plantEnergy = Integer.parseInt(plantEnergyField.getText());
        int numberOfFirstAnimals = Integer.parseInt(numberOfFirstAnimalsField.getText());
        int daysForStatistics = Integer.parseInt(daysForStatisticsField.getText());

        StatisticsParser parser1 = new StatisticsParser(1);
        WorldMap map1 = new WorldMap(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, daysForStatistics, parser1);
        Simulation simulation1 = new Simulation(map1, numberOfFirstAnimals, locker, 1);

        StatisticsParser parser2 = new StatisticsParser(2);
        WorldMap map2 = new WorldMap(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, daysForStatistics, parser2);
        Simulation simulation2 = new Simulation(map2, numberOfFirstAnimals, locker, 2);

        simulation1.startSimulation();
        simulation2.startSimulation();

    }

}