import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Knapsack {

    private final int numberOfItems;
    Random rand = new Random();
    private int weight;
    private ArrayList<Item> items;

    public Knapsack(int weight, int numberOfItems, ArrayList<Item> items) {
        this.weight = weight;
        this.numberOfItems = numberOfItems;
        this.items = items;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int testCases = sc.nextInt();

        int sizeOfKnapsack, numberOfItems, itemWeight, itemValue;
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < testCases; i++) {

            sizeOfKnapsack = sc.nextInt();
            numberOfItems = sc.nextInt();
            for (int j = 0; j < numberOfItems; j++) {
                itemWeight = sc.nextInt();
                itemValue = sc.nextInt();
                items.add(new Item(itemWeight, itemValue));
            }

            Knapsack ks = new Knapsack(sizeOfKnapsack, numberOfItems, items);
            ks.solve();

        }

    }

    private int calculateFitness(ArrayList<Integer> chromosome) {
        int fitness = 0;
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i) == 1) {
                fitness += items.get(i).value;
            }
        }
        return fitness;
    }

    private ArrayList<Chromosome> InitializePopulation(int populationSize) {

        ArrayList<Chromosome> totalChromosomes = new ArrayList<>(populationSize);

        for (int i = 0; i < populationSize; i++) {

            ArrayList<Integer> chromosome = new ArrayList<>(numberOfItems);

            while (true) {

                for (int gene = 0; gene < numberOfItems; gene++) {
                    chromosome.add((int) Math.round(Math.random()));
                }
                if (calculateFitness(chromosome) <= weight) {
                    totalChromosomes.add(new Chromosome(chromosome, calculateFitness(chromosome)));
                    break;
                }
                chromosome.clear();
            }

        }
        return totalChromosomes;
    }

    //selection and crossover repeated by 1/2 pop size
    private ArrayList<Chromosome> Selection(ArrayList<Chromosome> chromosomes) {

        ArrayList<Chromosome> selectedChromosomes = new ArrayList<>();
        ArrayList<Integer> cumulativeFitness = new ArrayList<>();

        cumulativeFitness.add(0);
        for (int i = 1; i <= chromosomes.size(); i++) {
            cumulativeFitness.add(cumulativeFitness.get(i - 1) + chromosomes.get(i - 1).getFitnessValue());
        }

        while (selectedChromosomes.size() != 2) {
            int randomNumber = rand.nextInt(cumulativeFitness.get(cumulativeFitness.size() - 1) + 1);
            for (int k = 0; k < cumulativeFitness.size() - 1; k++) {
                if (randomNumber > cumulativeFitness.get(k) && randomNumber <= cumulativeFitness.get(k + 1)) {
                    Chromosome c = chromosomes.get(k);
                    if (!selectedChromosomes.contains(c)) {
                        selectedChromosomes.add(c);
                    }
                }
            }
        }
        return selectedChromosomes;
    }

    private ArrayList<Chromosome> Crossover(ArrayList<Chromosome> selectedChromosomes, double Pc) {
        double Rc = rand.nextDouble();
        if (Rc <= Pc) {
            int Xc = rand.nextInt(numberOfItems - 1) + 1;

            Chromosome firstChromosome = selectedChromosomes.get(0);
            Chromosome secondChromosome = selectedChromosomes.get(1);

            for (int i = Xc; i < items.size(); i++) {
                int temp = firstChromosome.geneAt(i);
                firstChromosome.setGeneAt(i, secondChromosome.geneAt(i));
                secondChromosome.setGeneAt(i, temp);
            }
        }
        return selectedChromosomes;
    }

    private void Mutation(ArrayList<Chromosome> totalChromosomes, double Pm) {


        for (Chromosome chromosome : totalChromosomes) {

            for (int i = 0; i < chromosome.getGenes().size(); i++) {

                double Rm = rand.nextDouble();

                if (Rm <= Pm) {
                    if (chromosome.geneAt(i) == 1)
                        chromosome.setGeneAt(i, 0);
                    else
                        chromosome.setGeneAt(i, 1);
                }
            }

            chromosome.setFitnessValue(calculateFitness(chromosome.getGenes()));

        }


    }

    private void Replacement(ArrayList<Chromosome> oldGeneration, ArrayList<Chromosome> newGeneration) {

        oldGeneration = new ArrayList<>(newGeneration);

    }

    public void solve() {

        int populationSize = numberOfItems * 2;
        int maxGeneration = 10;
        double Pc = 0.6;
        double Pm = 0.05;
        ArrayList<Chromosome> currentGeneration = InitializePopulation(populationSize);

        // INSIDE A FOR LOOP FOR MAX GENERATIONS
        for (int i = 0; i < maxGeneration; i++) {
            ArrayList<Chromosome> newGeneration = new ArrayList<>();

            for (int j = 0; j < populationSize / 2; j++) {
                ArrayList<Chromosome> selectedParents = Selection(currentGeneration);
                ArrayList<Chromosome> parentsCrossover = Crossover(selectedParents, Pc);
                newGeneration.add(parentsCrossover.get(0));
                newGeneration.add(parentsCrossover.get(1));
            }
            Mutation(newGeneration, Pm);
            Replacement(currentGeneration, newGeneration);
        }

        for (Chromosome c : currentGeneration) {
            System.out.println(c.getFitnessValue());
        }

    }

    static class Chromosome {

        private final ArrayList<Integer> genes;
        private int fitnessValue;

        public Chromosome(ArrayList<Integer> genes, int fitnessValue) {
            this.genes = genes;
            this.fitnessValue = fitnessValue;
        }

        public int getFitnessValue() {
            return fitnessValue;
        }

        public void setFitnessValue(int fitnessValue) {
            this.fitnessValue = fitnessValue;
        }

        public ArrayList<Integer> getGenes() {
            return genes;
        }

        public int geneAt(int pos) {
            return genes.get(pos);
        }

        public void setGeneAt(int pos, int value) {
            genes.set(pos, value);
        }
    }

    static class Item {

        private int weight;
        private int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }

    }

}
