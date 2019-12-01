package Maze;
import java.util.Random;

public class Maze {
    private int map[][];
    private Random rand = new Random();
    //纵横
    private int x, y, start, exit ;

    public Maze(int len,int height, int start,int exit) {
//        rand.setSeed(System.currentTimeMillis());
        map = new int[len][height];
        x = (len - 2) / 2 -1 ;
        y = (height - 2)/ 2 -1;
        this.start=start;
        this.exit=exit;
    }

    public  int getX() {
        return x;
    }

    public  int getY() {
        return y;
    }
        // 0 path , 1 wall
    public void printMaze() {
        int z1, z2;
        for (z2 = 1; z2 <= x * 2 + 1; z2++) {
            for (z1 = 1; z1 <= y * 2 + 1; z1++) {
                if (map[z2][z1] == 0) {
                    System.out.print(" ○ ");
                } else {
                    System.out.print(" ■ ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void makeMaze() {
        rand.setSeed(System.currentTimeMillis());
        int z1, z2;
        for (int i = 0; i <= x * 2 + 2; ++i)
            for (int j = 0; j <= y * 2 + 2; ++j)
                map[i][j] = 1;
        // 2 * x +2 =len ,2*y+2 = height
        for (z1 = 0, z2 = 2 * y + 2; z1 <= 2 * x + 2; ++z1) {
            map[z1][0] = 0;  // first row and last row as 0
            map[z1][z2] = 0;
        }
        for (z1 = 0, z2 = 2 * x + 2; z1 <= 2 * y + 2; ++z1) {
            map[0][z1] = 0;  // first column and last column as 0
            map[z2][z1] = 0;
        }
        // set default start and exit point
        map[1][start] = 0;
        map[2 * x + 1][exit] = 0;

        generatePath(rand() % x + 1, rand() % y + 1);
    }

    private int generatePath(int x, int y) {
        int dir[][] = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
        int zx = x * 2;
        int zy = y * 2;
        int next, turn, i;
        map[zx][zy] = 0;
        turn = 3;
        if (rand() % 2 == 1)
            turn = 1;
        for (i = 0, next = rand() % 4; i < 4; ++i, next = (next + turn) % 4) {
            if (map[zx + 2 * dir[next][0]][zy + 2 * dir[next][1]] == 1) {
                map[zx + dir[next][0]][zy + dir[next][1]] = 0;
                generatePath(x + dir[next][0], y + dir[next][1]);
            }
        }
        return 0;
    }

    private int rand() {
        return Math.abs(rand.nextInt());
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

}