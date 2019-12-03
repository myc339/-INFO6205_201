package info6205.GA;

import Maze.MazePreparation;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;

    private double dead1Rate;
    private double dead2Rate;
    private int tournamentSize;
    private int walk;


    public GeneticAlgorithm(int populationSize, double mutationRate,
                            int tournamentSize, double dead1Rate, double dead2Rate, int walk) {
        super();
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.walk = walk;
        this.dead1Rate = dead1Rate;
        this.dead2Rate = dead2Rate;
        this.tournamentSize = tournamentSize;
    }

    public Population initPopulation(int chromosomeLength){
        Population population = new Population(this.populationSize, chromosomeLength);
        return population;
    }


    public double calcFitness(Individual individual, MazePreparation maze) {

        int[] chromosome = individual.getChromosome();
        Goat goat = new Goat(chromosome, maze, walk);
        goat.run();
        int fitness = maze.scoreRoute(goat.getRoute());
        //zebra.printRoute();
        individual.setFitness(fitness);

        return fitness;
    }


    public void evalPopulation(Population population, MazePreparation maze) {
        double populationFitness = 0;
        for (Individual individual : population.getIndividuals())
            populationFitness += this.calcFitness(individual, maze);
        population.setPopulationFitness(populationFitness);
    }


    public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
        return (generationsCount > maxGenerations);
    }


    public Individual selectParent(Population population1) {

        Population tournament = new Population(this.tournamentSize);
        Population population = population1;

        population.shuffle();
        int x = 0;
        for (int i = 0; x < this.tournamentSize;i++ ) {
            if(population.getIndividual(i).getisDead() != true && population.getIndividual(i).getAge() >= 1){
                Individual tournamentIndividual = population.getIndividual(i);
                tournament.setIndividual(x, tournamentIndividual);
                population.shuffle();
                x++;

            }
        }

        return tournament.getFittest(0);
    }

    public void addAge(Population population){
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getIndividual(populationIndex);
            individual.setAge(individual.getAge() + 1);
        }

    }



    public Population crossoverPopulation(Population population) {

        Population newPopulation = new Population(population.size());


        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {

            Individual individual = population.getIndividual(populationIndex);


            if (individual.getisDead() == true) {

                Individual offspring = new Individual(individual.getChromosomeLength());


                Individual parent1 = this.selectParent(population);
                Individual parent2 = this.selectParent(population);


                int swapPoint = (int) (Math.random() * (parent1.getChromosomeLength() + 1));

                for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex ++) {

                    if (geneIndex < swapPoint) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }


                }


                newPopulation.setIndividual(populationIndex, offspring);
            } else {

                newPopulation.setIndividual(populationIndex, individual);
            }
        }

        return newPopulation;
    }



    public Population deadPopulation(Population population){
        Population newPopulation = new Population(this.populationSize);
        int i = 0, j = population.size() - 1;
        for(int populationIndex = 0; populationIndex < population.size() ; populationIndex++){
            if(i > j) break;
            Individual individual = population.getFittest(populationIndex);
            if(individual.getAge() >= 10){
                if(Math.random() > 0.5){
                    newPopulation.setIndividual(j, individual);
                    j--;
                    continue;
                }
            }
            newPopulation.setIndividual(i, individual);
            i++;

        }
        for(int front = 0; front < population.size()/2; front++){
            if(Math.random() < dead1Rate){
                population.getIndividual(front).setisDead(true);
            }
        }
        for(int back = population.size()/2 ; back <= population.size() - 1; back++){
            if(Math.random() < dead2Rate){
                population.getIndividual(back).setisDead(true);
            }
        }
        return newPopulation;
    }


    public Population mutatePopulation(Population population){
        Population newPopulation = new Population(this.populationSize);

        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {

            Individual individual = population.getFittest(populationIndex);

            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                if(Math.random() > 0.5){
                    if(this.mutationRate>Math.random()){
                        int newGene = 1;
                        if(individual.getGene(geneIndex)==1){
                            newGene = 0;
                        }
                        individual.setGene(geneIndex, newGene);
                    }
                }
            }

            newPopulation.setIndividual(populationIndex, individual);
        }
        return newPopulation;
    }

}
