package danielknowlesprojectjava.geneticAlgorithm;

import java.util.Comparator;

/**
 * The object that encodes the genetic traits.
 */
public class Genotype {
    private double rawFitness;             // The genotype's raw fitness score
    private double normalizedFitness;   // The genotype's normalized fitness
    private boolean[] chromosome;       // The encoded traits
    private static int chromosomeSize;  // The size of the chromosome array

    /**
     * Gets the object's raw fitness score.
     * @return the object's raw fitness score
     */
    public double getRawFitness() { return rawFitness; }

    /**
     * Sets the object's raw fitness score.
     * @param value the value to set as the raw fitness score
     */
    public void setRawFitness(double value) { rawFitness = value; }

    /**
     * Gets the object's normalized fitness score.
     * @return the object's normalized fitness score
     */
    public double getNormalizedFitness() { return normalizedFitness; }

    /**
     * Sets the normalized fitness score.
     * @param value the normalized fitness score
     */
    public void setNormalizedFitness(double value) { normalizedFitness = value;}

    /**
     * Gets the chromosome array. This array is the encoded array of the
     * object's genetic traits.
     * @return the chromosome array
     */
    public boolean[] getChromosome() { return chromosome; }

    /**
     * Sets the chromosome array.
     * @param value the chromosome array to set
     */
    public void setChromosome(boolean[] value) { chromosome = value; }

    /**
     * Compares two genotypes. Used to sort genotypes by raw fitness score.
     */
    static final Comparator<Genotype> FitnessOrder =
            new Comparator<Genotype>() {

        public int compare(Genotype o1, Genotype o2) {
            return (o1.getRawFitness() > o2.getRawFitness() ? -1 :
                (o1.getRawFitness() == o2.getRawFitness() ? 0 : 1));
        }
    };

    /**
     * Creates a new genotype.
     * @param size the size of the chromosome (number of bits)
     */
    public Genotype(int size) {
        chromosomeSize = size;
        chromosome = new boolean [chromosomeSize];
        SetRandomChromosome();
    }

    /**
     * Creates a new genotype.
     * @param genes the chromosome array
     */
    public Genotype(boolean[] genes) {
        chromosome = genes;
        chromosomeSize = genes.length;
    }

    /**
     * Puts random booleans into the chromosome array.
     */
    private void SetRandomChromosome() {
        for (int i = 0; i < chromosomeSize; i++) {
            chromosome[i] = GeneticEngine.rnd.nextBoolean();
        }
    }

    /**
     * Mutates the genotype.
     * @param mutationRate the probability of mutation
     */
    public void Mutate(double mutationRate) {
        // there's a chance for each bit's mutation
        for (int i = 0; i < chromosomeSize; i++) {
            // if the next random double is less than the mutation rate
            if (GeneticEngine.rnd.nextDouble() < mutationRate) {
                // flip the bit
                chromosome[i] = !chromosome[i];
            }
        }
    }

    /**
     * Mates two genotypes to return a third genotype as an offspring.
     * @param g1 the first genotype to mate
     * @param g2 the second genotype to mate
     * @param crossoverRate the probability of crossover
     * @return a genotype as offspring
     */
    public static Genotype Mate(Genotype g1, Genotype g2,
            double crossoverRate) {
        int crossoverPoint = 0; // the index to crossover the chromosomes
        // the offspring's chromosome
        boolean[] offspringChromosome = new boolean[chromosomeSize];
        // if next random double is less than the crossover rate
        if (GeneticEngine.rnd.nextDouble() < crossoverRate) {
            // set crossover point to a random number
            crossoverPoint = GeneticEngine.rnd.nextInt(chromosomeSize);
        }

        // if zero crossover, return the first genotype
        if (crossoverPoint == 0) {
            return g1;
        }
        // otherwise, create offspring using crossover point to mix the genotypes
        else {
            // set the first part of the offspring's genes array to the first
            // part of the first genotype's chromosome
            for (int i = 0; i < crossoverPoint; i++) {
                offspringChromosome[i] = g1.chromosome[i];
            }

            // set the second part of the offspring's genes array to the second
            // part of the second genotype's chromosome
            for (int i = crossoverPoint; i < chromosomeSize; i++) {
                offspringChromosome[i] = g2.chromosome[i];
            }

            // return a new genotype using the offspring chromosome
            return new Genotype(offspringChromosome);
        }
    }
}
