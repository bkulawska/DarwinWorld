package viewClasses;

import classes.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Przycisk do pokazywania zwierząt z dominującym genomem

public class DominantGenButton extends JPanel implements ActionListener {

    public Simulation simulation;
    JButton button;

    public DominantGenButton(Simulation simulation){
        this.simulation=simulation;
        button = new JButton("Pokaż wszystkie zwierzęta z dominującym genomem");
        this.add(button);
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!simulation.timerActive){
            simulation.dominantGenButtonActive=true;
            simulation.visualization.repaint();
        }
    }
}
