package info6205.GA;

import Maze.MazePreparation;

import java.util.ArrayList;

public class Goat {
    private enum Direction{NORTH,EAST,SOUTH,WEST};
    private int xPosition;
    private int yPosition;
    private Direction heading;
    int maxMoves;
    int moves;
    private int sensorVal;
    private final int sensorActions[];
    private MazePreparation maze;
    private ArrayList<int[]> route;
    public Goat(int[] gene, MazePreparation maze, int maxMoves){
        //the parameter gene determine actions
        this.sensorActions = this.calcSensorActions(gene);
        this.maze = maze;
        int startPos[] = this.maze.getStartPosition();
        this.xPosition = startPos[0];
        this.yPosition = startPos[1];
        this.sensorVal = -1;
        this.heading = Direction.EAST;
        this.maxMoves = maxMoves;
        this.moves = 0;
        this.route = new ArrayList<int[]>();
        this.route.add(startPos);
    }
    public void run(){
        while(true){
            this.moves++;
            //when getNextAction()==0 means meeting the end of road. 有问题，一共四种行为1移动2顺时针旋转3逆时针旋转，0直接结束
            if (this.getNextAction() == 0) {
                return;
            }
            //4 is exit
            if (this.maze.getPositionValue(this.xPosition, this.yPosition) == 4) {
                return;
            }
            //keep moving
            if (this.moves > this.maxMoves) {
                return;
            }
            this.makeNextAction();
        }
    }
    private int[] calcSensorActions(int[] gene){
        //2 gene determine 1 action
        int numActions = (int)gene.length/2;
        int sensorActions[] = new int[numActions];

        for (int sensorValue = 0; sensorValue < numActions; sensorValue++){
            int sensorAction = 0;
            if (gene[sensorValue*2] == 1){
                sensorAction += 2;
            }
            if (gene[(sensorValue*2)+1] == 1){
                sensorAction += 1;
            }

            sensorActions[sensorValue] = sensorAction;
        }
        return sensorActions;
    }

    public void makeNextAction(){
        //when 1, move action
        if (this.getNextAction() == 1) {
            int currentX = this.xPosition;
            int currentY = this.yPosition;
            //check direction
            if (Direction.NORTH == this.heading) {
                this.yPosition += -1;
                if (this.yPosition < 0) {
                    this.yPosition = 0;
                }
            }
            else if (Direction.EAST == this.heading) {
                this.xPosition += 1;
                if (this.xPosition > this.maze.getMaxX()) {
                    this.xPosition = this.maze.getMaxX();
                }
            }
            else if (Direction.SOUTH == this.heading) {
                this.yPosition += 1;
                if (this.yPosition > this.maze.getMaxY()) {
                    this.yPosition = this.maze.getMaxY();
                }
            }
            else if (Direction.WEST == this.heading) {
                this.xPosition += -1;
                if (this.xPosition < 0) {
                    this.xPosition = 0;
                }
            }
            //check wall, then confirm move
            if (this.maze.isWall(this.xPosition, this.yPosition) == true) {
                this.xPosition = currentX;
                this.yPosition = currentY;
            }
            else {
                if(currentX != this.xPosition || currentY != this.yPosition) {
                    this.route.add(this.getPosition());
                }
            }
        }
        //clockwise rotation
        else if(this.getNextAction() == 2) {
            if (Direction.NORTH == this.heading) {
                this.heading = Direction.EAST;
            }
            else if (Direction.EAST == this.heading) {
                this.heading = Direction.SOUTH;
            }
            else if (Direction.SOUTH == this.heading) {
                this.heading = Direction.WEST;
            }
            else if (Direction.WEST == this.heading) {
                this.heading = Direction.NORTH;
            }
        }
        //counterclockwise rotation
        else if(this.getNextAction() == 3) {
            if (Direction.NORTH == this.heading) {
                this.heading = Direction.WEST;
            }
            else if (Direction.EAST == this.heading) {
                this.heading = Direction.NORTH;
            }
            else if (Direction.SOUTH == this.heading) {
                this.heading = Direction.EAST;
            }
            else if (Direction.WEST == this.heading) {
                this.heading = Direction.SOUTH;
            }
        }
        //make sensorVal default for next getNextAction()
        this.sensorVal = -1;
    }

    public int getNextAction() {
        return this.sensorActions[this.getSensorValue()];
    }

    public int getSensorValue(){
        if (this.sensorVal > -1) {
            return this.sensorVal;
        }

        boolean frontSensor, frontLeftSensor, frontRightSensor, leftSensor, rightSensor, backSensor;
        frontSensor = frontLeftSensor = frontRightSensor = leftSensor = rightSensor = backSensor = false;

        if (this.getHeading() == Direction.NORTH) {
            frontSensor = this.maze.isWall(this.xPosition, this.yPosition-1);
            frontLeftSensor = this.maze.isWall(this.xPosition-1, this.yPosition-1);
            frontRightSensor = this.maze.isWall(this.xPosition+1, this.yPosition-1);
            leftSensor = this.maze.isWall(this.xPosition-1, this.yPosition);
            rightSensor = this.maze.isWall(this.xPosition+1, this.yPosition);
            backSensor = this.maze.isWall(this.xPosition, this.yPosition+1);
        }
        else if (this.getHeading() == Direction.EAST) {
            frontSensor = this.maze.isWall(this.xPosition+1, this.yPosition);
            frontLeftSensor = this.maze.isWall(this.xPosition+1, this.yPosition-1);
            frontRightSensor = this.maze.isWall(this.xPosition+1, this.yPosition+1);
            leftSensor = this.maze.isWall(this.xPosition, this.yPosition-1);
            rightSensor = this.maze.isWall(this.xPosition, this.yPosition+1);
            backSensor = this.maze.isWall(this.xPosition-1, this.yPosition);
        }
        else if (this.getHeading() == Direction.SOUTH) {
            frontSensor = this.maze.isWall(this.xPosition, this.yPosition+1);
            frontLeftSensor = this.maze.isWall(this.xPosition+1, this.yPosition+1);
            frontRightSensor = this.maze.isWall(this.xPosition-1, this.yPosition+1);
            leftSensor = this.maze.isWall(this.xPosition+1, this.yPosition);
            rightSensor = this.maze.isWall(this.xPosition-1, this.yPosition);
            backSensor = this.maze.isWall(this.xPosition, this.yPosition-1);
        }
        else {
            frontSensor = this.maze.isWall(this.xPosition-1, this.yPosition);
            frontLeftSensor = this.maze.isWall(this.xPosition-1, this.yPosition+1);
            frontRightSensor = this.maze.isWall(this.xPosition-1, this.yPosition-1);
            leftSensor = this.maze.isWall(this.xPosition, this.yPosition+1);
            rightSensor = this.maze.isWall(this.xPosition, this.yPosition-1);
            backSensor = this.maze.isWall(this.xPosition+1, this.yPosition);
        }

        int sensorVal = 0;

        if (frontSensor == true) {
            sensorVal += 1;
        }
        if (frontLeftSensor == true) {
            sensorVal += 2;
        }
        if (frontRightSensor == true) {
            sensorVal += 4;
        }
        if (leftSensor == true) {
            sensorVal += 8;
        }
        if (rightSensor == true) {
            sensorVal += 16;
        }
        if (backSensor == true) {
            sensorVal += 32;
        }

        this.sensorVal = sensorVal;

        return sensorVal;
    }

    public ArrayList<int[]> getRoute(){
        return this.route;
    }
    public int[] getPosition(){
        return new int[]{this.xPosition, this.yPosition};
    }
    private Direction getHeading(){
        return this.heading;
    }
    public String printRoute() {
        String route = "";

        for (Object routeStep : this.route) {
            int step[] = (int[]) routeStep;
            route += "{" + step[0] + "," + step[1] + "}";
        }
        System.out.println(route);
        return route;
    }
}