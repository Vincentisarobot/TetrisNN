package UI;

import Game.GameObeserver;
import Game.Grid;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vinshit on 4/21/2017.
 */
public class ScoreBar extends JPanel implements GameObeserver{

    Grid grid;
    private int score = 0;
    JLabel scorelabel;
    @Override
    public void stateChanged() {
        score = grid.getScore();
        scorelabel.setText(Integer.toString(score));
        this.repaint();
        this.revalidate();
    }

    public ScoreBar(Grid grid){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(getWidth(),23));
        this.grid = grid;
        grid.addObservers(this);
        scorelabel = new JLabel(Integer.toString(score));
        add(scorelabel,BorderLayout.EAST);
        revalidate();
    }

}
