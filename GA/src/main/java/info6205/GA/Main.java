package info6205.GA;

import Maze.MazePreparation;

import java.io.*;
import java.util.*;

public class Main {
   
    public static int maxGenerations = 1000;
    
    public static void main(String[] args) {
        int len=30;
        int height=30;
        MazePreparation mazePreparation = new MazePreparation();
        int  TotalLen =mazePreparation.getMazePathLen();

        GeneticAlgorithm ga = new GeneticAlgorithm(1000, 0.05, 10, 0.25, 0.8, 300);
        Population population = ga.initPopulation(128);


        ga.evalPopulation(population, mazePreparation);

        int generation =1;

        // evaluation
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {

            Individual fittest = population.getFittest(0);

            System.out.println( fittest.getMaxFitness()/(TotalLen*1.0)  );

            //+ fittest.toString()

            //Age +
            ga.addAge(population);
            //dead
            population = ga.deadPopulation(population);

            // crossover
            population = ga.crossoverPopulation(population);

            // mutation
            population = ga.mutatePopulation(population);


            ga.evalPopulation(population, mazePreparation);

            //next generation
            generation++;
        }

        System.out.println("Stopped after " + maxGenerations + " generations.");
        Individual fittest = population.getFittest(0);
        System.out.println("Best solution (" + fittest.getMaxFitness()/(TotalLen*1.0) + "): "
                + fittest.toString());

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
