package Game;

import UI.GridUI;
import UI.ScoreBar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vinshit on 4/19/2017.
 */
public class Game {
    public static void main(String[] args){
        JFrame jFrame = new JFrame();
        JPanel windowcontent = new JPanel(new BorderLayout());
        Grid grid = new Grid();
        GridUI gridUI = new GridUI(grid);
        ScoreBar scoreBar = new ScoreBar(grid);
        windowcontent.add(gridUI,BorderLayout.CENTER);
        windowcontent.add(scoreBar,BorderLayout.SOUTH);
        jFrame.add(windowcontent);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
