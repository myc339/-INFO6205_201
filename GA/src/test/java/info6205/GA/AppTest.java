package info6205.GA;

import Maze.MazePreparation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Unit test for simple App.
 */
public class AppTest {
    MazePreparation maze = new MazePreparation(10,10);

    public static int maxGenerations = 1;

    @Test
    public void test0() {
        GeneticAlgorithm ga = new GeneticAlgorithm(10, 0.05, 1, 0.2, 0.8, 100);

        PrivateMethodTester pmt = new PrivateMethodTester(ga);
        Population population = (Population) pmt.invokePrivate("initPopulation", 128);
        assertEquals(10, population.size());
    }

    @Test
    public void test1() {
        GeneticAlgorithm ga = new GeneticAlgorithm(10, 0.05, 1, 0.2, 0.8, 100);

        PrivateMethodTester pmt = new PrivateMethodTester(ga);
        Population population = (Population) pmt.invokePrivate("initPopulation", 128);
        pmt.invokePrivate("evalPopulation", population, maze);
        assertEquals(true, population.getIndividual(0) != null);
    }


    @Test
    public void test2() {
        GeneticAlgorithm ga = new GeneticAlgorithm(10, 0.05, 1, 0.2, 0.8, 100);

        PrivateMethodTester pmt = new PrivateMethodTester(ga);
        Population population = (Population) pmt.invokePrivate("initPopulation", 128);
        pmt.invokePrivate("evalPopulation", population, maze);
        int generation = 1;
        while (! (Boolean) pmt.invokePrivate("isTerminationConditionMet", generation, maxGenerations)) {

            assertEquals(true, population.getFittest(0) != null);
            generation++;
        }

    }

    @Test
    public void testFittness() {
        GeneticAlgorithm ga = new GeneticAlgorithm(10, 0.05, 1, 0.2, 0.8, 100);

        PrivateMethodTester pmt = new PrivateMethodTester(ga);
        Population population = (Population) pmt.invokePrivate("initPopulation", 128);
        pmt.invokePrivate("evalPopulation", population, maze);
        int generation = 1;
        while ( ! (Boolean) pmt.invokePrivate("isTerminationConditionMet", generation, maxGenerations) ) {

            assertEquals( population.getIndividual(0).getFitness(),0.0,10.0);
            generation++;
        }

    }

    @Test
    public void testDead() {
        GeneticAlgorithm ga = new GeneticAlgorithm(10, 0.05, 2, 0, 1, 100);

        PrivateMethodTester pmt = new PrivateMethodTester(ga);
        Population population = (Population) pmt.invokePrivate("initPopulation", 128);
        pmt.invokePrivate("evalPopulation", population, maze);
        population.getIndividual(0).setAge(10);
        population.getIndividual(1).setAge(10);
        assertEquals(true, population.getIndividual(0).getAge() == 10);
        population = (Population) pmt.invokePrivate("deadPopulation", population);
        assertEquals(true, population.getIndividual(0).getisDead() || population.getIndividual(1).getisDead()
        );

    }


    @Test
    public void testCrossOver() {
        GeneticAlgorithm ga = new GeneticAlgorithm(10, 0.05, 2, 0, 1, 100);

        PrivateMethodTester pmt = new PrivateMethodTester(ga);
        Population population = (Population) pmt.invokePrivate("initPopulation", 128);
        pmt.invokePrivate("evalPopulation", population, maze);
        population = (Population) pmt.invokePrivate("crossoverPopulation", population);
        assertEquals(128, population.getIndividual(0).getChromosomeLength());

    }


    @Test
    public void testMutation_Age() {
        GeneticAlgorithm ga = new GeneticAlgorithm(10, 0.05, 2, 0, 1, 100);

        PrivateMethodTester pmt = new PrivateMethodTester(ga);
        Population population = (Population) pmt.invokePrivate("initPopulation", 128);
        pmt.invokePrivate("addAge", population);

        assertEquals(1, population.getIndividual(0).getAge());

    }
}