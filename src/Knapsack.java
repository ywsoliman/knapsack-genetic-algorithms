import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Knapsack {

    Random rand = new Random();
    private int numberOfItems;
    private int knapsackWeight;
    private ArrayList<Item> items;

    public Knapsack(int weight, int numberOfItems, ArrayList<Item> items) {
        this.knapsackWeight = weight;
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

    public int getKnapsackWeight() {
        return knapsackWeight;
    }

    public void setKnapsackWeight(int knapsackWeight) {
        this.knapsackWeight = knapsackWeight;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void clearItems() {
        items.clear();
    }

    public int calculateWeight(ArrayList<Integer> chromosome) {
        int totalWeight = 0;
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i) == 1)
                totalWeight += items.get(i).getWeight();
        }
        return totalWeight;
    }

    public int calculateFitness(ArrayList<Integer> chromosome) {
        int fitness = 0;
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i) == 1) {
                fitness += items.get(i).getValue();
            }
        }
        return fitness;
    }

    private ArrayList<Chromosome> InitializePopulation(int populationSize) {

        ArrayList<Chromosome> totalChromosomes = new ArrayList<>(populationSize);

        for (int i = 0; i < populationSize; i++) {

            ArrayList<Integer> chromosome = new ArrayList<>(numberOfItems);
            double random;

            while (true) {

                for (int gene = 0; gene < numberOfItems; gene++) {
                    random = rand.nextDouble();
                    if (random <= 0.5)
                        chromosome.add(0);
                    else
                        chromosome.add(1);
                }

                if (calculateWeight(chromosome) <= knapsackWeight) {
                    totalChromosomes.add(new Chromosome(chromosome, calculateFitness(chromosome)));
                    break;
                }
                chromosome.clear();
            }

        }
        return totalChromosomes;
    }

    private ArrayList<Integer> calculateCumulative(ArrayList<Chromosome> chromosomes) {

        ArrayList<Integer> cumulativeFitness = new ArrayList<>();

        cumulativeFitness.add(chromosomes.get(0).getFitnessValue());
        for (int i = 1; i < chromosomes.size(); i++)
            cumulativeFitness.add(cumulativeFitness.get(i - 1) + chromosomes.get(i).getFitnessValue());

        return cumulativeFitness;

    }

    //selection and crossover repeated by 1/2 pop size
    private ArrayList<Chromosome> Selection(ArrayList<Chromosome> chromosomes) {

        ArrayList<Chromosome> selectedChromosomes = new ArrayList<>();
        ArrayList<Integer> cumulativeFitness = calculateCumulative(chromosomes);

        double randomNumber;
        int firstCumulate = cumulativeFitness.get(0);
        int lastCumulate = cumulativeFitness.get(cumulativeFitness.size() - 1);

        while (selectedChromosomes.size() != 2) {

            randomNumber = rand.nextInt(lastCumulate - firstCumulate) + firstCumulate;
            for (int i = 0; i < cumulativeFitness.size() - 1; i++) {
                if (cumulativeFitness.get(i) >= randomNumber) {
                    selectedChromosomes.add(chromosomes.get(i));
                    break;
                }
            }

        }

        return selectedChromosomes;

    }

    private ArrayList<Chromosome> Crossover(ArrayList<Chromosome> selectedChromosomes, double Pc) {

        if (selectedChromosomes.get(0) == selectedChromosomes.get(1))
            return selectedChromosomes;

        double Rc = rand.nextDouble();

        ArrayList<Chromosome> afterCrossover = new ArrayList<>();
        afterCrossover.add(selectedChromosomes.get(0));
        afterCrossover.add(selectedChromosomes.get(1));

        if (Rc <= Pc) {

            int Xc = rand.nextInt(numberOfItems - 1) + 1;

            for (int i = Xc; i < items.size(); i++) {
                int temp = afterCrossover.get(0).geneAt(i);
                afterCrossover.get(0).setGeneAt(i, afterCrossover.get(1).geneAt(i));
                afterCrossover.get(1).setGeneAt(i, temp);
            }
        }
        return afterCrossover;
    }

    private void Mutation(ArrayList<Chromosome> parents, ArrayList<Chromosome> totalChromosomes, double Pm) {

        double Rm;

        for (int i = 0; i < totalChromosomes.size(); i++) {

            for (int j = 0; j < totalChromosomes.get(i).getGenes().size(); j++) {

                Rm = rand.nextDouble() / 10;

                if (Rm <= Pm) {
                    if (totalChromosomes.get(i).geneAt(j) == 1)
                        totalChromosomes.get(i).setGeneAt(j, 0);
                    else
                        totalChromosomes.get(i).setGeneAt(j, 1);
                }
            }

            if (calculateWeight(totalChromosomes.get(i).getGenes()) <= knapsackWeight)
                totalChromosomes.get(i).setFitnessValue(calculateFitness(totalChromosomes.get(i).getGenes()));
            else {
                totalChromosomes.set(i, parents.get(i));
            }

        }
    }

    private void Replacement(ArrayList<Chromosome> oldGeneration, ArrayList<Chromosome> newGeneration) {
        oldGeneration = newGeneration;
    }

    public void solve() {

        int populationSize = 20;
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

            Mutation(currentGeneration, newGeneration, Pm);
            Replacement(currentGeneration, newGeneration);

        }

        int bestFitness = Integer.MIN_VALUE;
        for (Chromosome c : currentGeneration) {
            if (bestFitness < c.getFitnessValue()) {
                bestFitness = c.getFitnessValue();
            }
        }
        System.out.println(bestFitness);

        clearItems();

    }

    static class Chromosome {

        private ArrayList<Integer> genes;
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

        public void setGenes(ArrayList<Integer> genes) {
            this.genes = genes;
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

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }

}
