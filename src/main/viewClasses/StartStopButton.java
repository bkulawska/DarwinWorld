package viewClasses;

import classes.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Przycisk zatrzymujący i wznawiający symulację

public class StartStopButton extends JPanel implements ActionListener {

    public Simulation simulation;
    JButton button;

    public StartStopButton(Simulation simulation){
        this.simulation=simulation;
        button = new JButton("Stop");
        this.add(button);
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (simulation.timerActive) {
            simulation.timerActive=false;
            button.setText("Start");
            simulation.timer.stop();
        }
        else {
            simulation.dominantGenButtonActive=false;
            simulation.timerActive=true;
            button.setText("Stop");
            simulation.timer.start();
        }
    }
}
