package Maze;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MazePath {
    static String path = "";
    static String shortestPath = "";
    private static int pathLen ;

    public void dfsMaze(int x, int y, int[][] maze, int exit) {
        /* * 获得矩阵的大小 * */
        int m=maze.length;
        int n=maze[0].length;
        //设置结束条件
        if (x < 0 || y < 0)
            return;
        // 如果坐标越界，或者 maze[x][y]==1 表示遇到障碍
        if (x > m - 1 || y > n - 1 || maze[x][y] ==1)
            return;
        //表示遇到障碍
        if (maze[x][y] == 1)
            return; // 判断是否通路和越界
        if (x == m - 1 && y == exit) { // 判断是否抵达出口
            path = path + "(" + x + "," + y + ")";
            if (shortestPath.length() == 0 || shortestPath.length() > shortestPath.length())
                shortestPath = path;
//            System.out.println("find path:");
            return;
        }
//        System.out.println(x+" y:"+y);

        String temp = path;
        path = path + "(" + x + "," + y + ")" + "-"; // 记录路线
//        System.out.println(path);
        maze[x][y] = 1; // 将走过的路标记
        // 向四个方向搜索

        dfsMaze(x + 1, y, maze,exit);  //向右搜索
        dfsMaze(x, y + 1, maze,exit);  //向下搜索
        dfsMaze(x, y - 1, maze,exit);  //向上搜索
        dfsMaze(x - 1, y, maze,exit);  //向左搜索
        // 将路线和标记恢复成上一次的状态
        maze[x][y] = 0;
        //清除
        path = temp;
    }
    public int [][] findMazePath(Maze maze,int start, int exit) {

        int xupbound=2*maze.getX()+1;
        int yupbound=2*maze.getY()+1;
        int [][] map =new int[xupbound][yupbound];
//        System.out.println(xupbound);
        for( int i =1;i<=xupbound;i++)
            for(int j =1;j<=yupbound;j++)
                map[i-1][j-1]=maze.getMap()[i][j];
//        for( int i =0;i<xupbound;i++)
//        {
//            for(int j =0;j<yupbound;j++)
//                System.out.print(map[i][j]+" ");
//            System.out.println();
//        }
        dfsMaze(0, start , map,exit );

        if (shortestPath.length() != 0)
            System.out.println("find path " );
        else
            System.out.println("not found path！");
        Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
        Matcher matcher = pattern.matcher(shortestPath);
        List<String> list=new ArrayList<String>();
        while(matcher.find()){
            list.add(matcher.group());
        }
        System.out.println(shortestPath);
        pathLen =list.size();

        if(list.isEmpty()) return null;
        list.remove(0);
        list.remove(list.size()-1);
        System.out.println("path length:"+list.size());
        map[0][start]=2;
        map[xupbound-1][exit]=4;
        for( String index:list)
        {
            int x =Integer.valueOf( index.split(",")[0]);
            int y =Integer.valueOf( index.split(",")[1]);
            map[x][y]=3;
        }
        for( int i =0;i<xupbound;i++)
        {
            for(int j=0;j<yupbound;j++)
                System.out.print(map[i][j]+" ");
            System.out.println();
        }

        return map;
    }

    public static int getPathLen() {
        return pathLen;
    }

    public static void setPathLen(int pathLen) {
        MazePath.pathLen = pathLen;
    }
}
