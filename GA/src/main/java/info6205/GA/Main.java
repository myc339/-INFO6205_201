package info6205.GA;

import Maze.MazePreparation;

import java.io.*;
import java.util.*;

public class Main {
   
    public static int maxGenerations = 1000;
    
    public static void main(String[] args) {
        int len=50;
        int height=50;
        MazePreparation mazePreparation = new MazePreparation(len,height);

        System.out.println( MazePreparation.getMazePathLen()*3);
        GeneticAlgorithm ga = new GeneticAlgorithm(1000, 0.1, 10, 0.2, 0.8, MazePreparation.getMazePathLen()*3);
        //first  generation
        Population population = ga.initPopulation(64*2);
        

         ga.evalPopulation(population, mazePreparation);
        List<String> dataList = new ArrayList<String>();
        int generation =1;

        // evaluation
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
            
            Individual fittest = population.sortIndividualsByFitness(0);

            dataList.add(String.valueOf(fittest.getFitness() / (1.0*MazePreparation.getMazePathLen())));
            System.out.println( fittest.getFitness() / (1.0*MazePreparation.getMazePathLen()) );

            //+ fittest.toString()
            
            //Age +
            ga.addAge(population);
            //dead
            population = ga.deadPopulation(population);

            // crossover mutation rate is 0.2 cross 60 pairs chromosomes
            population = ga.crossoverPopulation(population);

            // mutation
//            population = ga.mutatePopulation(population);

            
            ga.evalPopulation(population, mazePreparation);

            //next generation
            generation++;
        }
       System.out.println("create success?"+ String.valueOf(exportCsv(new File("data.csv"),dataList) ==true));

        System.out.println("Stopped after " + maxGenerations + " generations.");
        Individual optimalOne = population.sortIndividualsByFitness(0);
        System.out.println("Best solution (" + optimalOne.getMaxFitness()/ (1.0*MazePreparation.getMazePathLen()) + "): "
                + optimalOne.toString());

    }
    public static boolean exportCsv(File file, List<String> dataList) {
        boolean isSucess = false;

        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (dataList != null && !dataList.isEmpty()) {
                for (String data : dataList) {
                    bw.append(data).append("\r\n");
                }
            }
            isSucess = true;
        } catch (Exception e) {
            isSucess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isSucess;
    }

}
