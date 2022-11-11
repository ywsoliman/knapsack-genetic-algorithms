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

    //selection and crossover repeated by 1/2 pop size
    private ArrayList<Chromosome> Selection(ArrayList<Chromosome> chromosomes) {

        ArrayList<Chromosome> selectedChromosomes = new ArrayList<>();
        ArrayList<Integer> cumulativeFitness = new ArrayList<>();

        cumulativeFitness.add(0);
        for (int i = 1; i <= chromosomes.size(); i++) {
            cumulativeFitness.add(cumulativeFitness.get(i - 1) + chromosomes.get(i - 1).getFitnessValue());
        }

        double randomNumber;

        while (selectedChromosomes.size() != 2) {
            randomNumber = rand.nextInt(cumulativeFitness.get(cumulativeFitness.size() - 1) + 1);
            for (int k = 0; k < cumulativeFitness.size() - 1; k++) {
                if (randomNumber > cumulativeFitness.get(k) && randomNumber <= cumulativeFitness.get(k + 1)) {
                    Chromosome c = chromosomes.get(k);
                    selectedChromosomes.add(c);
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

    private void Mutation(ArrayList<Chromosome> totalChromosomes, double Pm) {

        double Rm;

        for (Chromosome chromosome : totalChromosomes) {

            for (int i = 0; i < chromosome.getGenes().size(); i++) {

                Rm = rand.nextDouble() / 10;

                if (Rm <= Pm) {
                    if (chromosome.geneAt(i) == 1)
                        chromosome.setGeneAt(i, 0);
                    else
                        chromosome.setGeneAt(i, 1);
                }
            }
        }

        Chromosome c = totalChromosomes.get(0);
        for (Chromosome chromosome : totalChromosomes) {
            if (c.getFitnessValue() < chromosome.getFitnessValue() && calculateWeight(chromosome.getGenes()) < knapsackWeight)
                c = chromosome;
        }

        // INFEASIBLE SOLUTION
        for (Chromosome chromosome : totalChromosomes) {
            if (calculateWeight(chromosome.getGenes()) > knapsackWeight) {
                chromosome = c;
            }
        }
    }

    private void Replacement(ArrayList<Chromosome> oldGeneration, ArrayList<Chromosome> newGeneration) {
        oldGeneration = newGeneration;
    }

    public void solve() {

        int populationSize = 32;
        int maxGeneration = 100;
        double Pc = 0.6;
        double Pm = 0.05;
        ArrayList<Chromosome> currentGeneration = InitializePopulation(populationSize);

        // INSIDE A FOR LOOP FOR MAX GENERATIONS
        for (int i = 0; i < maxGeneration; i++) {
            ArrayList<Chromosome> newGeneration = new ArrayList<>();

            while (newGeneration.size() != currentGeneration.size()) {

                ArrayList<Chromosome> selectedParents = Selection(currentGeneration);
                ArrayList<Chromosome> parentsCrossover = Crossover(selectedParents, Pc);
                newGeneration.add(parentsCrossover.get(0));
                newGeneration.add(parentsCrossover.get(1));

            }
            Mutation(newGeneration, Pm);
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
