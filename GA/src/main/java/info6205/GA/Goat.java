package info6205.GA;

import Maze.MazePreparation;

import java.util.ArrayList;

public class Goat {
    // ( 0 ,-1) (+1, 0) (0,+1) (-1,0)
    private enum Direction{north, east, south, west};
    private int xPosition;
    private int yPosition;
    private Direction heading;
    int maxMoves;
    int moves;
    private int sensorVal;
    private final int sensorActions[];
    private MazePreparation mazePreparation;
    private ArrayList<int[]> route;

    public Goat(int[] gene, MazePreparation mazePreparation, int maxMoves){
        //the parameter gene determine actions
        this.sensorActions = this.calcSensorActions(gene);
        this.mazePreparation = mazePreparation;
        int startPos[] = this.mazePreparation.getStartPosition();
        this.xPosition = startPos[0];
        this.yPosition = startPos[1];
        this.sensorVal = -1;
        this.heading = Direction.east;
        this.maxMoves = maxMoves;
        this.moves = 0;
        this.route = new ArrayList<int[]>();
        this.route.add(startPos);
    }
    public void FitnessCalculate(){
        while(true){

            //when getNextAction()==0 means meeting the end of road. 有问题，一共四种行为1移动2顺时针旋转3逆时针旋转，0直接结束
             if (this.getNextAction() == 0) {
                return;
            }
             //4 represent reach exit
            if (this.mazePreparation.getPositionValue(this.xPosition, this.yPosition) == 4) {
                return;
            }
            //keep moving
            if (this.moves > this.maxMoves) {
                return;
            }
            this.moves++;
            this.makeNextAction();    
        }
    }
    private int[] calcSensorActions(int[] gene){
        //2 bit as one gene 60 pairs
        int numActions = (int)gene.length/2;
//        System.out.println;
        int sensorActions[] = new int[numActions];
        /// 00 -> 0  01 -> 1 10 -> 2 11-> 3
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
//        for( int action :sensorActions) {
            if (this.getNextAction() == 1) {
                int currentX = this.xPosition;
                int currentY = this.yPosition;
                //check direction
                if (Direction.north == this.heading) {
                    this.yPosition += -1;
                    if (this.yPosition < 0) {
                        this.yPosition = 0;
                    }
                } else if (Direction.east == this.heading) {
                    this.xPosition += 1;
                    if (this.xPosition > this.mazePreparation.getMaxX()) {
                        this.xPosition = this.mazePreparation.getMaxX();
                    }
                } else if (Direction.south == this.heading) {
                    this.yPosition += 1;
                    if (this.yPosition > this.mazePreparation.getMaxY()) {
                        this.yPosition = this.mazePreparation.getMaxY();
                    }
                } else if (Direction.west == this.heading) {
                    this.xPosition += -1;
                    if (this.xPosition < 0) {
                        this.xPosition = 0;
                    }
                }
                //check wall, then confirm move
                if (this.mazePreparation.isWall(this.xPosition, this.yPosition)) {
                    this.xPosition = currentX;
                    this.yPosition = currentY;
                } else {
                    if (currentX != this.xPosition || currentY != this.yPosition) {
                        this.route.add(this.getPosition());
                    }
                }
            }
            //clockwise rotation
            else if (this.getNextAction() == 2) {
                if (Direction.north == this.heading) {
                    this.heading = Direction.east;
                } else if (Direction.east == this.heading) {
                    this.heading = Direction.south;
                } else if (Direction.south == this.heading) {
                    this.heading = Direction.west;
                } else if (Direction.west == this.heading) {
                    this.heading = Direction.north;
                }
            }
            //counterclockwise rotation
            else if (this.getNextAction() == 3) {
                if (Direction.north == this.heading) {
                    this.heading = Direction.west;
                } else if (Direction.east == this.heading) {
                    this.heading = Direction.north;
                } else if (Direction.south == this.heading) {
                    this.heading = Direction.east;
                } else if (Direction.west == this.heading) {
                    this.heading = Direction.south;
                }
            }
            //make sensorVal default for next getNextAction()
            this.sensorVal = -1;
//        }
    }
    
    public int getNextAction() {

        return this.sensorActions[getSensorValue()];
    }
    
    public int getSensorValue(){
        if (this.sensorVal > -1) {
            return this.sensorVal;
        }
                
        boolean frontSensor, frontLeftSensor, frontRightSensor, leftSensor, rightSensor, backSensor;
        if (this.getHeading() == Direction.north) {
            frontSensor = this.mazePreparation.isWall(this.xPosition, this.yPosition-1);
            frontLeftSensor = this.mazePreparation.isWall(this.xPosition-1, this.yPosition-1);
            frontRightSensor = this.mazePreparation.isWall(this.xPosition+1, this.yPosition-1);
            leftSensor = this.mazePreparation.isWall(this.xPosition-1, this.yPosition);
            rightSensor = this.mazePreparation.isWall(this.xPosition+1, this.yPosition);
            backSensor = this.mazePreparation.isWall(this.xPosition, this.yPosition+1);
        }
        else if (this.getHeading() == Direction.east) {
            frontSensor = this.mazePreparation.isWall(this.xPosition+1, this.yPosition);
            frontLeftSensor = this.mazePreparation.isWall(this.xPosition+1, this.yPosition-1);
            frontRightSensor = this.mazePreparation.isWall(this.xPosition+1, this.yPosition+1);
            leftSensor = this.mazePreparation.isWall(this.xPosition, this.yPosition-1);
            rightSensor = this.mazePreparation.isWall(this.xPosition, this.yPosition+1);
            backSensor = this.mazePreparation.isWall(this.xPosition-1, this.yPosition);
        }
        else if (this.getHeading() == Direction.south) {
            frontSensor = this.mazePreparation.isWall(this.xPosition, this.yPosition+1);
            frontLeftSensor = this.mazePreparation.isWall(this.xPosition+1, this.yPosition+1);
            frontRightSensor = this.mazePreparation.isWall(this.xPosition-1, this.yPosition+1);
            leftSensor = this.mazePreparation.isWall(this.xPosition+1, this.yPosition);
            rightSensor = this.mazePreparation.isWall(this.xPosition-1, this.yPosition);
            backSensor = this.mazePreparation.isWall(this.xPosition, this.yPosition-1);
        }
        else {
            frontSensor = this.mazePreparation.isWall(this.xPosition-1, this.yPosition); //left
            frontLeftSensor = this.mazePreparation.isWall(this.xPosition-1, this.yPosition+1); // left-down
            frontRightSensor = this.mazePreparation.isWall(this.xPosition-1, this.yPosition-1); // left-up
            leftSensor = this.mazePreparation.isWall(this.xPosition, this.yPosition+1); // down
            rightSensor = this.mazePreparation.isWall(this.xPosition, this.yPosition-1); // up
            backSensor = this.mazePreparation.isWall(this.xPosition+1, this.yPosition);  // right
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
//    public String printRoute() {
//        String route = "";
//
//        for (Object routeStep : this.route) {
//            int step[] = (int[]) routeStep;
//            route += "{" + step[0] + "," + step[1] + "}";
//        }
//        System.out.println(route);
//        return route;
//    }
}