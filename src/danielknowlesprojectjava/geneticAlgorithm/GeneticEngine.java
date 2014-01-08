package danielknowlesprojectjava.geneticAlgorithm;

import danielknowlesprojectjava.collections.FeederCollection;
import java.util.*;

/**
 * Holds a population of genotypes and runs the genetic algorithm. This
 * implementation is meant to keep the genetic algorithm generic enough to be
 * used for multiple applications.
 */
public class GeneticEngine {
    // Random object used for random number generation in the genetic engine
    public static Random rnd = new Random();
    // the population of genotypes being evaluated
    private ArrayList<Genotype> population;
    // the size of the population
    private int populationSize;
    // the probability of crossover when mating genotypes
    private static double crossoverProbability = 0.7;
    // the probability of mutation
    private static double mutationProbability = 0.001;
    // the maximum number of generations for each run
    private int maxGenerations;
    // the length of the genotype's chromosome array
    private int chromosomeSize;
    // the population's total fitness score
    private double totalFitness;
    // lists that hold the average fitness scores
    private ArrayList<Double> avgFitnesses;
    // list that holds the highest fitness scores
    private ArrayList<Double> highFitnesses;


    /**
     * Gets the list of average fitness scores for each generation. The index
     * of each entry corresponds to the generation number minus one.
     * @return list of average fitness scores
     */
    public ArrayList<Double> getAverageFitnesses() { return avgFitnesses; }

    /**
     * Gets the list of highest fitness scores found for each generation. The
     * index of each entry corresponds to the generation number minus one.
     * @return the list of highest fitness scores
     */
    public ArrayList<Double> getHighestFitnesses() { return highFitnesses; }

    /**
     * Gets the population.
     * @return the population
     */
    public ArrayList<Genotype> getPopulation() { return population; }
    /**
     * Sets the population.
     * @param value the population of genotypes
     */
    public void setPopulation(ArrayList<Genotype> value) { population = value; }

    /**
     * Gets the probability of crossover.
     * @return the probability of crossover
     */
    public static double getCrossoverProbability() { return crossoverProbability; }
    /**
     * Sets the crossover probability.
     * @param value the crossover probability
     */
    public static void setCrossoverProbability(double value) { crossoverProbability = value; }

    /**
     * Gets the probability of mutation.
     * @return the probability of mutation
     */
    public static double getMutationProbability() { return mutationProbability; }
    /**
     * Sets the mutation probability.
     * @param value the mutation probability
     */
    public static void setMutationProbability(double value) { mutationProbability = value; }

    /**
     * Gets the number of maximum generations in each run.
     * @return
     */
    public int getMaxGenerations() { return maxGenerations; }
    /**
     * Sets the maximum number of generations per run.
     * @param value the maximum number of generations
     */
    public void setMaxGenerations(int value) { maxGenerations = value; }

    /**
     * Creates a new genetic engine with default values and no population.
     */
    public GeneticEngine() {
        avgFitnesses = new ArrayList<Double>();
        highFitnesses = new ArrayList<Double>();
        population = null;
        crossoverProbability = 0.07;
        mutationProbability = 0.001;
        maxGenerations = Integer.MAX_VALUE;
    }
    /**
     * Creates a new genetic engine.
     * @param popSize the size of the population
     * @param crossoverRate the probability of crossover
     * @param mutationRate the probability of mutation
     * @param maxGen the maximum number of generations for each run
     * @param chromosomeLength the length of the chromosome array
     */
    public GeneticEngine(int popSize, double crossoverRate,
            double mutationRate, int maxGen, int chromosomeLength) {

        avgFitnesses = new ArrayList<Double>();
        highFitnesses = new ArrayList<Double>();
        populationSize = popSize;
        crossoverProbability = crossoverRate;
        mutationProbability = mutationRate;
        maxGenerations = maxGen;
        chromosomeSize = chromosomeLength;
        initializePopulation();
    }

    /**
     * Creates a new genetic engine.
     * @param popSize the size of the population
     * @param maxGen the maximum number of generations for each run
     * @param chromosomeLength the length of the chromosome array
     */
    public GeneticEngine(int popSize, int maxGen, int chromosomeLength) {
        avgFitnesses = new ArrayList<Double>();
        highFitnesses = new ArrayList<Double>();
        populationSize = popSize;
        maxGenerations = maxGen;
        chromosomeSize = chromosomeLength;
        initializePopulation();
    }

    /**
     * Sets a random, starting population.
     */
    private void initializePopulation() {
        // reset the population
        population = new ArrayList<Genotype>();

        // set the population to an array of random genotypes
        for (int i = 0; i < populationSize; i++) {
            population.add(new Genotype(chromosomeSize));
        }

        // evaluate the population
        // evaluatePopulation();
    }

    /**
     * Sets the fitness scores for each member of the population.
     */
    public void evaluatePopulation() {
        // reset the total fitness score
        totalFitness = 0;

        for (int i = 0; i < populationSize; i++) {
            population.get(i).setRawFitness(
                    FeederCollection.getFeeders().get(i).GetFitness());
            totalFitness += population.get(i).getRawFitness();
        }

        // set the normalized fitness for each population member
        for (Genotype g : population) {
            // normalized fitness = raw fitness / total fitness
            g.setNormalizedFitness((g.getRawFitness() / totalFitness));
        }

        // sort the popoulation
        Collections.sort(population, Genotype.FitnessOrder);

        highFitnesses.add(population.get(0).getRawFitness());
        avgFitnesses.add(totalFitness / population.size());


    }

    /**
     * Creates the next generation.
     */
    public void setNextGeneration() {
        // create a temporary population
        ArrayList<Genotype> tempPopulation = new ArrayList<Genotype>();

        // select mates
        while (tempPopulation.size() < populationSize) {
            tempPopulation.add(Genotype.Mate(rouletteSelection(),
                    rouletteSelection(), crossoverProbability));
        }
    }

    /**
     * Selects a genotype to mate.
     * @return a genotype to mate
     */
    public Genotype rouletteSelection() {
        // get a number between 0 and 1 to compare against
        double slice = rnd.nextDouble();
        // keep track of the cumulative fitness
        double cumulativeFitness = 0;

        // loop through the population to pick a mate
        for (Genotype g : population) {
            // increment the cumulative fitness with the member's normalized fitness
            cumulativeFitness += g.getNormalizedFitness();
            // if the cumulative fitness is greater than the random number,
            if (cumulativeFitness > slice) {
                // select the member for mating
                return g;
            }
        }

        // if no members are chosen, pick the one with the highest fitness score
        return population.get(0);
    }
}
