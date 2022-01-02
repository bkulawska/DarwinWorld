package viewClasses;

import classes.Animal;
import classes.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

//Panel do śledzenia zwierzaka

public class TrackAnimalPanel extends JPanel implements ActionListener {

    public static final int HEIGHT = 300;
    public static final int WIDTH = 500;

    public WorldMap map;
    public Animal animal;

    private JTextField genomeField;
    private JTextField dayOfDeathField;
    private JTextField numberOfChildrenField;
    private JTextField nField;

    private JLabel genomeLabel;
    private JLabel dayOfDeathLabel;
    private JLabel numberOfChildrenLabel;
    private JLabel nLabel;

    private JLabel header;
    private JButton startButton;

    public TrackAnimalPanel (WorldMap map, Animal animal) {

        this.animal=animal;
        this.map=map;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        startButton = new JButton("Track");
        startButton.addActionListener(this);

        header = new JLabel("Wybrane zwierzę:");

        genomeLabel = new JLabel("Genom:");
        nLabel = new JLabel("Podaj liczbę epok (n):");


        genomeField = new JTextField();
        genomeField.setColumns(50);
        genomeField.setText(Arrays.toString(animal.genes));

        nField = new JTextField();
        nField.setColumns(10);

        genomeLabel.setLabelFor(genomeField);
        nLabel.setLabelFor(nField);

        JPanel p0 = new JPanel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();

        JPanel buttonPanel = new JPanel();

        p0.add(header);
        p1.add(genomeLabel);
        p1.add(genomeField);
        p2.add(nLabel);
        p2.add(nField);
        buttonPanel.add(startButton);

        add(p0);
        add(p1);
        add(p2);
        add(buttonPanel);

        dayOfDeathLabel = new JLabel("Dzień śmierci:");
        numberOfChildrenLabel = new JLabel("Liczba dzieci po n epokach:");

        int a=10;

        dayOfDeathField = new JTextField();
        dayOfDeathField.setColumns(a);

        numberOfChildrenField = new JTextField();
        numberOfChildrenField.setColumns(a);

        dayOfDeathLabel.setLabelFor(dayOfDeathField);
        numberOfChildrenLabel.setLabelFor(numberOfChildrenField);

        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();

        p3.add(dayOfDeathLabel);
        p3.add(dayOfDeathField);
        p4.add(numberOfChildrenLabel);
        p4.add(numberOfChildrenField);

        add(p3);
        add(p4);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int n = Integer.parseInt(nField.getText());
        map.track(animal,n);
    }

    public void endTracking(){
        numberOfChildrenField.setText(String.valueOf(animal.numberOfChildren-map.trackingNumOfChildrenBefore));
    }

    public void trackedDied(){
        dayOfDeathField.setText(String.valueOf(animal.dayOfDeath));
    }

}
