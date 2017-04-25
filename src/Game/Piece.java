package Game;

import java.awt.*;
import java.util.Random;
import java.util.Timer;

/**
 * Created by Vinshit on 4/18/2017.
 */
public class Piece {
    public enum Tetrominoes {None, ZShape, SShape, IShape, TShape, SquareShape, LShape, MirLShape};

    private Timer timer;
    private Tetrominoes pieceShape;
    private int coords[][];
    static private int [][][] coordsTable = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
    };

    static Random rand = new Random();


    public Piece(){
        coords = new int[4][2];
        setShape(Tetrominoes.None);
    }

    public void setShape(Tetrominoes piece){
        for(int i =0; i<4; i++){
            for(int j = 0; j<2; j++){
                this.coords[i][j] = coordsTable[piece.ordinal()][i][j];
            }
        }
        this.pieceShape = piece;
    }

    private void setX(int index, int x){coords[index][0] = x;}
    private void setY(int index, int y){coords[index][1] = y;}
    public int x(int index) { return coords[index][0];}
    public int y(int index) { return coords[index][1];}
    public Tetrominoes getShape()  { return pieceShape; }

    public void setRandomShape(){
        int x = Math.abs(rand.nextInt()) % 7 + 1;

        Tetrominoes[] values = Tetrominoes.values();
        setShape(values[x]);
    }

    public int maxX() {
        int m = coords[0][0];
        for (int i=0; i < 4; i++) {
            m = Math.max(m, coords[i][0]);
        }
        return m;
    }

    public int minX() {
        int m = coords[0][0];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }

    public int maxY() {
        int m = coords[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.max(m, coords[i][1]);
        }
        return m;
    }

    public int minY() {
        int m = coords[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public Piece rotateLeft() {
        if(this.pieceShape == Tetrominoes.SquareShape){
            return this;
        }
        for(int i = 0; i<4; i++) {
            this.coords[i][0] += this.coords[i][1];
            this.coords[i][1] -= this.coords[i][0];
            this.coords[i][0] += this.coords[i][1];
        }
        return this;
    }

    public Piece rotateRight() {
        if(this.pieceShape == Tetrominoes.SquareShape){
            return this;
        }
        for(int i = 0; i<4; i++) {
            this.coords[i][1] += this.coords[i][0];
            this.coords[i][0] -= this.coords[i][1];
            this.coords[i][1] += this.coords[i][0];
        }
        return this;
    }

    public void seedRand(long seed){
        rand.setSeed(seed);
    }

}
