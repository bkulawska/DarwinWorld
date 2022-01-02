package classes;

import viewClasses.DominantGenButton;
import viewClasses.Painter;
import viewClasses.StartStopButton;
import viewClasses.StatisticsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class Simulation extends Throwable implements ActionListener {

    protected ReentrantLock locker;

    public WorldMap map;
    public int startNumOfAnimals;
    public JFrame frame;

    public viewClasses.Painter visualization;
    public StatisticsPanel statistics;
    public StartStopButton startStopButton;
    public DominantGenButton dominantGenButton;
    public boolean dominantGenButtonActive;

    public Timer timer;
    public boolean timerActive;

    public Simulation (WorldMap thisMap, int n, ReentrantLock lock, int id){

        map=thisMap;
        startNumOfAnimals=n;
        timer = new Timer(200, this);
        timerActive=false;
        locker=lock;
        dominantGenButtonActive=false;

        frame = new JFrame();
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (id==1){
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);

        visualization = new Painter(map, this);
        visualization.setSize(new Dimension(1, 1));
        statistics=new StatisticsPanel(map,this, n);
        startStopButton = new StartStopButton(this);
        dominantGenButton = new DominantGenButton(this);

        statistics.add(startStopButton);
        statistics.add(dominantGenButton);
        frame.add(statistics);
        frame.add(visualization);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            locker.lockInterruptibly();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } finally {

            runOneDay();
            statistics.update();
            visualization.repaint();

            map.checkOnTracked(visualization);

            if (map.days==map.daysForStatistics){
                try {
                    statistics.statisticsToFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            locker.unlock();
        }
    }

    public void startSimulation() {
        map.addFirstAnimals(startNumOfAnimals);
        timerActive=true;
        timer.start();
    }

    public void runOneDay() {

        map.days++;

        //Zmniejszenie energii zwierząt o dzienną ilość i usuwanie martwych zwierząt z mapy
        map.decreaseDailyEnergyAndRemoveDead();

        //Skręt i przemieszczenie każdego zwierzęcia
        map.moveAnimals();

        // Jedzenie
        map.eatPlants();

        //Rozmnażanie
        map.reproduce();

        //Dodanie nowych roślin do mapy
        map.addPlants();

    }

}

