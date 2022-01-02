package viewClasses;

import classes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TreeSet;

//Panel do rysowania animacji

public class Painter extends JPanel implements MouseListener {

    public WorldMap map;
    public Simulation simulation;
    int width;
    int height;
    int wScale;
    int hScale;

    public TrackAnimalPanel trackPanel;

    public Painter(WorldMap map, Simulation simulation) {
        this.map = map;
        this.simulation = simulation;
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize((int) (simulation.frame.getWidth() * 0.5), (int) (simulation.frame.getHeight() * 0.8));
        this.setLocation((int) (0.45 * simulation.frame.getWidth()), (int) (0.1 * simulation.frame.getHeight()));
        width = this.getWidth();
        height = this.getHeight();
        wScale = Math.round(width / map.w);
        hScale = Math.round(height / map.h);

        //Rysowanie stepu
        g.setColor(new Color(246, 241, 137));
        g.fillRect(0, 0, width, height);

        //Rysowanie dżungli
        g.setColor(new Color(22, 226, 27));
        g.fillRect(map.jungleLowerLeft.x * wScale,map.jungleLowerLeft.y * hScale,map.jungleW * wScale,map.jungleH * hScale);

        //Rysowanie roślin
        for (Plant plant : map.plants.values()) {
            g.setColor(new Color(2, 99, 2));
            int y = plant.getPosition().y * hScale;
            int x = plant.getPosition().x * wScale;
            g.fillRect(x, y, wScale, hScale);
        }

        //Rysowanie zwierząt

        //a) Rysowanie przy zatrzymanej symulacji, z zaznaczeniem zwierząt z dominującym genem
        if (simulation.dominantGenButtonActive){
            for (TreeSet<Animal> s: map.container.animals.values()) {
                for (Animal animal: s) {
                    if (animal.domGen==map.getDominantGen()){
                        if (animal.energy>0 && !map.deadAnimals.contains(animal)){
                            g.setColor(new Color(234, 9, 9));
                            int y = animal.getPosition().y * hScale;
                            int x = animal.getPosition().x * wScale;
                            g.fillOval(x, y, wScale, hScale);
                            break;
                        }
                    }
                    else{
                        if (animal.energy>0 && !map.deadAnimals.contains(animal)){
                            g.setColor(animal.getColor());
                            int y = animal.getPosition().y * hScale;
                            int x = animal.getPosition().x * wScale;
                            g.fillOval(x, y, wScale, hScale);
                        }
                    }
                }
            }
        }
        //b) Zwykłe rysowanie:
        else{
            for (TreeSet<Animal> s: map.container.animals.values()) {
                int i=0;
                for (Animal animal: s) {
                    if (i>0){
                        break;
                    }
                    if (animal.energy>0 && !map.deadAnimals.contains(animal)){
                        g.setColor(animal.getColor());
                        int y = animal.getPosition().y * hScale;
                        int x = animal.getPosition().x * wScale;
                        g.fillOval(x, y, wScale, hScale);

                        i++;
                    }
                }
            }
        }

    }

    //Obsługa kliknięcia w zwierzę na mapie
    @Override
    public void mouseClicked(MouseEvent e) {

        if (!simulation.timerActive){
            int x = Math.round(e.getX() / wScale);
            int y = Math.round(e.getY() / hScale);

            Vector2d v = new Vector2d(x,y);
            TreeSet<Animal> z = map.container.animals.get(v);
            Animal animal = null;
            if (z!=null){
                if (!z.isEmpty()){
                    animal = z.first();
                }
            }

            if (animal!=null){
                JFrame frame = new JFrame();
                frame.setSize(600, 300);

                trackPanel = new TrackAnimalPanel(map, animal);

                frame.add(trackPanel);
                frame.setVisible(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}