package info6205.GA;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Population {
	
    public Individual[] population;
//    public double populationFitness=-1;
   
    public Population(int populationSize){
        this.population = new Individual[populationSize];
    }
    
   /**
    * @Param populationSize
    * @Param chromosomeLength
    * **/
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

    /**
     * get best individuals in groups
     * @param index individuals serial number
     * @return return best individuals when index is 0
     */
    public Individual sortIndividualsByFitness(int index){
        
    	PriorityQueue<Individual> pq = new PriorityQueue<Individual>(population.length,new Comparator<Individual>() {

			@Override
			public int compare(Individual o1, Individual o2) {
				return (int) (o2.getFitness()-o1.getFitness());
			}});

           
         Individual[] Sorted_population = new Individual[index+1];

         for(int i = 0; i < population.length; i++){
             pq.add(population[i]);
         }
         
    	 for(int j = 0; j <= index; j++){
    		 Sorted_population[j] = pq.poll();
    	 }
         
         return Sorted_population[index];
     }
    
//    public void setPopulationFitness(double populationFitness) {
//        this.populationFitness = populationFitness;
//    }
    
    public int size(){
        return this.population.length;
    }
    
    public Individual setIndividual(int serialNumber,Individual individual){
        return population[serialNumber] = individual;
    }
    
    public Individual getIndividual(int serialNumber){
        return population[serialNumber];
    }

    /**
     * shuffle groups
     */
    public void shuffle() {
        Random rand = new Random();
        for (int i = population.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            Individual individual = population[index];
            population[index] = population[i];
            population[i] = individual;
        }
    }
}
