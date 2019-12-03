package info6205.GA;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Population {

    public Individual[] population;
    public double populationFitness=-1;

    public Population(int populationSize){
        this.population = new Individual[populationSize];
    }

    /*
     * （1）population size，（2）chromosome Length
     * */
    public Population(int populationSize,int chromosomeLength){
        this.population = new Individual[populationSize];
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            Individual individual = new Individual(chromosomeLength);
            this.population[individualCount] = individual;
        }
    }

    public Individual[] getIndividuals(){
        return this.population;
    }
    /*
     *  sort population by fitness with help of priorityQueue
     * */
    public Individual getFittest(int index){

        PriorityQueue<Individual> qi = new PriorityQueue<Individual>(population.length,new Comparator<Individual>(){

            @Override
            public int compare(Individual o1, Individual o2) {
                return (int) (o2.getFitness()-o1.getFitness());
            }});


        Individual[] population_Sort = new Individual[index+1];

        for(int i = 0; i < population.length; i++){
            qi.add(population[i]);
        }

        for(int j = 0; j <= index; j++){
            population_Sort[j] = qi.poll();
        }

        return population_Sort[index];
    }

    public double getPopulationFitness() {
        return populationFitness;
    }


    public void setPopulationFitness(double populationFitness) {
        this.populationFitness = populationFitness;
    }

    public int size(){
        return this.population.length;
    }

    public Individual setIndividual(int offset,Individual individual){
        return population[offset] = individual;
    }

    public Individual getIndividual(int offset){
        return population[offset];
    }

    public void shuffle() {
        Random rnd = new Random();
        for (int i = population.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individual a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }
}