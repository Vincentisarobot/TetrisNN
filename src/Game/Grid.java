package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by Vinshit on 4/18/2017.
 */


public class Grid implements ActionListener{
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;

    private boolean started = false;
    private boolean paused = false;

    Timer timer;

    Point cursor = new Point(0,0);
    Piece cursorpiece = new Piece();

    NextPieces pieces = new NextPieces();

    private static ArrayList<GameObeserver> observers = new ArrayList<GameObeserver>();

    private Piece[][] Board;

    //Game Stats
    private int highestY = HEIGHT;
    private int numLinesCleared = 0;
    private int numLastLineClear= 0;
    private int score = 0;

    public Grid(){

        Board = new Piece[WIDTH][HEIGHT];
        clearBoard();


        timer = new Timer(300,this);
        start();
    }

    private void clearBoard( ){
        for(int i = 0;i<WIDTH;i++){
            for(int j = 0;j<HEIGHT;j++){
                Board[i][j] = new Piece();
            }
        }
    }

    private void start(){
        timer.start();
        newPiece();
    }

    private void newPiece(){
        cursorpiece.setShape(pieces.getNext().getShape());
        cursor = new Point(WIDTH/2,cursorpiece.maxY());
        for(int i = 0; i<4;i++){
            Board[cursor.x+cursorpiece.x(i)][cursor.y+cursorpiece.y(i)]=cursorpiece;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dropPiece();
    }

    private void dropPiece() {
        if (!isValidMove(0,1)){
            if(cursor.y >0) {
                for (int i = 0; i < 4; i++) {
                    Board[cursor.x + cursorpiece.x(i)][cursor.y + cursorpiece.y(i)] = new Piece();
                    Board[cursor.x + cursorpiece.x(i)][cursor.y + cursorpiece.y(i)].setShape(cursorpiece.getShape());
                }
                highestY = Math.min(highestY,cursor.y+cursorpiece.minY());
                clearLines();
                newPiece();

            }
            else{
                //Game over
            }
        }
        notifyObservers();
    }

    public void hardDropPiece() {
        while(isValidMove(0,1)) {
        }
        for (int i = 0; i < 4; i++) {
            Board[cursor.x + cursorpiece.x(i)][cursor.y + cursorpiece.y(i)] = new Piece();
            Board[cursor.x + cursorpiece.x(i)][cursor.y + cursorpiece.y(i)].setShape(cursorpiece.getShape());
        }
        highestY = Math.min(highestY,cursor.y+cursorpiece.minY());
        clearLines();
        newPiece();
        notifyObservers();
    }

    public void rotatePiece(boolean isClockwise){
            for (int i = 0; i < 4; i++) {
                Board[cursor.x + cursorpiece.x(i)][cursor.y + cursorpiece.y(i)] = new Piece();
            }
            cursorpiece = (isClockwise)?cursorpiece.rotateRight():cursorpiece.rotateLeft();
            if(cursor.y+cursorpiece.maxY() >= HEIGHT){
                cursor.y = HEIGHT-cursorpiece.maxY()-1;
            }
            if(cursor.x+cursorpiece.maxX() >= WIDTH){
                cursor.x = WIDTH-cursorpiece.maxX()-1;
            }
            if(cursor.y+cursorpiece.minY() <= 0){
                cursor.y -= cursor.y+cursorpiece.minY();
            }
            if(cursor.x+cursorpiece.minX() <= 0){
                cursor.x -= cursor.x+cursorpiece.minX();
            }
            for (int i = 0; i < 4; i++) {
                Board[cursor.x + cursorpiece.x(i)][cursor.y + cursorpiece.y(i)].setShape(cursorpiece.getShape());
            }



        notifyObservers();
    }

    public Boolean isValidMove(int deltax, int deltay){
        for (int i = 0; i < 4; i++) {
            int x = cursor.x + cursorpiece.x(i) + deltax;
            int y = cursor.y + cursorpiece.y(i) + deltay;
            if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
                return false;
            if (Board[x][y].getShape() != Piece.Tetrominoes.None) {
                Boolean selfcollision = false;
                for(int i2= 0; i2<4;i2++){
                    if(x-cursor.x== cursorpiece.x(i2) && y-cursor.y== cursorpiece.y(i2)){
                        selfcollision = true;
                    }
                }
                if(!selfcollision){
                    return false;
                }
            }
        }
        cursor.x +=deltax;
        cursor.y +=deltay;
        for (int i = 0; i < 4; i++) {
            Board[cursor.x + cursorpiece.x(i)-deltax][cursor.y-deltay + cursorpiece.y(i)] = new Piece();
        }
        for (int i = 0; i < 4; i++) {
            Board[cursor.x + cursorpiece.x(i)][cursor.y + cursorpiece.y(i)].setShape(cursorpiece.getShape());
        }

        notifyObservers();
        return true;
    }

    private void clearLines(){
        int linescleared = 0;
        System.out.println(highestY);
        for(int i = highestY; i< HEIGHT;i++){
            Boolean clearthis = true;
            for (int i2 = 0; i2 < WIDTH; i2++) {
                if (Board[i2][i].getShape().ordinal()== Piece.Tetrominoes.None.ordinal()) {
                    clearthis = false;
                    break;
                }
            }
            if (clearthis) {
                for (int i3 = i; i3 > 0; i3--) {
                    for (int i2 = 0; i2 < WIDTH; i2++) {
                        Board[i2][i3].setShape(Board[i2][i3 - 1].getShape());
                    }
                }
                linescleared++;
            }
        }
        if(linescleared != 0){
            recalculateScore(linescleared);
        }
    }

    private void recalculateScore(int newlines){
        switch(newlines) {
            case 4:
                if(numLastLineClear == 4){
                    score+=1200;
                }
                else{
                    score+=800;
                }
            default:
                score+= newlines*100;
        }
    }

    public Piece[][] getBoard() {
        return Board;
    }

    public int getScore() {
        return score;
    }

    public Boolean getPaused() {return paused;}

    public void addObservers(GameObeserver gameObeserver){
        observers.add(gameObeserver);
    }

    public void notifyObservers(){
        for (GameObeserver obeserver:observers){
            obeserver.stateChanged();
        }
    }

    public class NextPieces{
        ArrayList<Piece> pieces;
        int length = 4;
        NextPieces(){
            pieces = new ArrayList<Piece>();
            for(int i = 0; i<length; i++){
                pieces.add(new Piece());
                pieces.get(i).setRandomShape();
            }
        }

        public Piece getNext(){
            Piece result = pieces.get(0);
            pieces.remove(0);
            Piece newone = new Piece();
            newone.setRandomShape();
            pieces.add(newone);
            return result;
        }
    }
}
