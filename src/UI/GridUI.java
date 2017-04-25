package UI;

import Game.GameObeserver;
import Game.Grid;
import Game.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.Observer;

/**
 * Created by Vinshit on 4/19/2017.
 */
public class GridUI extends JPanel implements GameObeserver {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;
    private static final int SQUARE_WIDTH = 25;
    private static final int BOARD_WIDTH = 10*SQUARE_WIDTH;
    private static final int BOARD_HEIGHT = 20*SQUARE_WIDTH;

    Grid grid;
    Icon gridIcon;


    private Color[] TetrominoesColor = {Color.lightGray, new Color(204,102,102),
            new Color(102,204,102),new Color(102,102,204),
            new Color(204,204,102),new Color(204, 102, 204),
            new Color(102,204,204),new Color(218, 170, 0) };


    @Override
    public void stateChanged() {
        this.repaint();
        this.revalidate();
    }

    private class GridIcon implements Icon {

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            //
            for(int i = 0;i<WIDTH;i++){
                for(int j = 0;j<HEIGHT;j++){
                    drawSquare(grid.getBoard()[i][j],g2d,i,j);
                }
            }
        }

        public void drawSquare(Piece piece, Graphics2D g2d, int x, int y){
            g2d.setColor(TetrominoesColor[piece.getShape().ordinal()]);
            Rectangle2D square = new Rectangle2D.Double(x*SQUARE_WIDTH,y*SQUARE_WIDTH,SQUARE_WIDTH,SQUARE_WIDTH);
            g2d.fill(square);
            if(piece.getShape().ordinal() >0) {
                g2d.setColor(Color.GRAY);
                g2d.draw(square);
            }
        }

        @Override
        public int getIconWidth() {
            return BOARD_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return BOARD_HEIGHT;
        }
    }

    public GridUI(Grid grid){
        this.grid = grid;
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        this.addKeyListener(new TetrisListener());
        grid.addObservers(this);
        gridIcon = new GridIcon();
        JLabel label = new JLabel(gridIcon);
        add(label);
        label.revalidate();
    }

    public class TetrisListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getExtendedKeyCode()){
                case KeyEvent.VK_A:
                    if(!grid.isValidMove(-1,0)){
                    }
                    break;
                case KeyEvent.VK_D:
                    if(!grid.isValidMove(1,0)){
                    }
                    break;
                case KeyEvent.VK_S:
                    if(!grid.isValidMove(0,1)){
                    }
                    break;
                case KeyEvent.VK_E:
                    grid.rotatePiece(true);
                    break;

                case KeyEvent.VK_Q:
                    grid.rotatePiece(false);
                    break;

                case KeyEvent.VK_W:
                    grid.hardDropPiece();
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

}
