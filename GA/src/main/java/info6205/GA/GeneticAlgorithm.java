package info6205.GA;

import Maze.MazePreparation;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    
    private double group1deadRate;
    private double group2deadRate;
    private int tournamentSize;
    private int walk;
    
   
    public GeneticAlgorithm(int populationSize, double mutationRate,
             int tournamentSize, double group1deadRate, double group2deadRate, int walk) {
        super();
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.walk = walk;
        this.group1deadRate = group1deadRate;
        this.group2deadRate = group2deadRate;
        this.tournamentSize = tournamentSize;
    }
    
    public Population initPopulation(int chromosomeLength){
        Population population = new Population(this.populationSize, chromosomeLength);
        return population;
    }

   
    public double calcFitness(Individual individual, MazePreparation mazePreparation) {
    	
        int[] chromosome = individual.getChromosome();
        Goat goat = new Goat(chromosome, mazePreparation, walk);
        goat.FitnessCalculate();
        int fitness = mazePreparation.scoreRoute(goat.getRoute());
        individual.setFitness(fitness);

        return fitness;
    }

    
    public void evalPopulation(Population population, MazePreparation mazePreparation) {
        double populationFitness = 0;
        for (Individual individual : population.getIndividuals())
            populationFitness += this.calcFitness(individual, mazePreparation);
//        population.setPopulationFitness(populationFitness);
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
        	if( !population.getIndividual(i).getisDead()  && population.getIndividual(i).getAge() >= 1){
        		Individual tournamentIndividual = population.getIndividual(i);
        		tournament.setIndividual(x, tournamentIndividual);
        		population.shuffle();
        		x++;
        		
        	}
        }
        
        return tournament.sortIndividualsByFitness(0);
    }

    /**
     * Add Age
     * @param population Goat Populations
     */
    public void addAge(Population population){
        for( Individual individual : population.getIndividuals())
            individual.setAge(individual.getAge()+1);
    }
    
    
    
    public Population crossoverPopulation(Population population) {
       
        Population newPopulation = new Population(population.size());
        

        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
           
            Individual individual = population.getIndividual(populationIndex);
            
            //replace dead one with new individuals
            if (individual.getisDead()) {
                
                Individual offspring = new Individual(individual.getChromosomeLength());
                
                
                Individual parent1 = this.selectParent(population);
                Individual parent2 = this.selectParent(population);

                // get 50 50 from parents
                int parent1chormos,parent2chromos =0;
                for (int geneIndex = 0; geneIndex < offspring.getChromosomeLength(); geneIndex+=2) {

                    // if less than 0.5 get first pair from parent1 2x->2x+2 second pair form parent2 2x+2 ->2x+4
                   if(Math.random() < 0.50)
                   {
                       System.arraycopy(parent1.getChromosome(),geneIndex,offspring.getChromosome(),geneIndex,1);
                       System.arraycopy(parent2.getChromosome(),geneIndex+1,offspring.getChromosome(),geneIndex+1,1);
                   }
                   else{
                       System.arraycopy(parent2.getChromosome(),geneIndex,offspring.getChromosome(),geneIndex,1);
                       System.arraycopy(parent1.getChromosome(),geneIndex+1,offspring.getChromosome(),geneIndex+1,1);
                   }
                    //mutation
                    if(Math.random()<this.mutationRate) {
                        if (offspring.getGene(geneIndex) == 1)
                            offspring.setGene(geneIndex, 0);
                        else offspring.setGene(geneIndex, 1);
                        if (offspring.getGene(geneIndex+1) ==1 )
                            offspring.setGene(geneIndex+1, 0);
                        else offspring.setGene(geneIndex+1, 1);

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
    		Individual individual = population.sortIndividualsByFitness(populationIndex);
    		if(individual.getAge() >= 10){
    			if(Math.random() > 0.5){
    				newPopulation.setIndividual(populationIndex, individual);
    				j--;
    				continue;
    			}
    		}
    		newPopulation.setIndividual(populationIndex, individual);
    		i++;
    		
    	}
    	// make individuals die with different probability
    	for(int front = 0; front < population.size()/2; front++){
    		if(Math.random() < group1deadRate){
    			population.getIndividual(front).setisDead(true);
    		}
    	}
    	for(int back = population.size()/2 ; back <= population.size() - 1; back++){
    		if(Math.random() < group2deadRate){
    			population.getIndividual(back).setisDead(true);
    		}
    	}
    	return newPopulation;
    }
    
    

    
}
