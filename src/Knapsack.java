import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;


public class Knapsack {

    private final int numberOfItems;
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

    public void solve() {

    }

    private ArrayList<Chromosome> InitializePopulation(int populationSize) {

        ArrayList<Chromosome> totalChromosomes = new ArrayList<>(populationSize);

        for (int item = 0; item < numberOfItems; item++) {

            ArrayList<Integer> chromosome = new ArrayList<>(numberOfItems);

            int fitnessValue = 0;

            while (true) {

                for (int gene = 0; gene < numberOfItems; gene++) {
                    chromosome.add((int) Math.round(Math.random()));
                    if (chromosome.get(gene) == 1) {
                        fitnessValue += items.get(gene).value;
                    }
                }
                if (fitnessValue <= weight) {
                    totalChromosomes.add(new Chromosome(chromosome, fitnessValue));
                    break;
                }
                fitnessValue = 0;
                chromosome.clear();
            }


        }
        return totalChromosomes;
    }

    Random rand = new Random();

    //selection and crossover repeated by 1/2 pop size
    private ArrayList<Chromosome> Selection(ArrayList<Chromosome> chromosomes) {
        ArrayList<Chromosome> selectedChromosomes = new ArrayList<>();
        ArrayList<Integer> cumulativeFitness = new ArrayList<>();

        cumulativeFitness.add(0);
        for (int i = 1; i <= chromosomes.size(); i++) {
            cumulativeFitness.add(cumulativeFitness.get(i - 1) + chromosomes.get(i-1).getFitnessValue());
        }

        while (selectedChromosomes.size() != 2) {
            int randomNumber = rand.nextInt(cumulativeFitness.get(cumulativeFitness.size() - 1) + 1);
            for (int k = 0; k < cumulativeFitness.size() - 1; k++) {
                if (randomNumber > cumulativeFitness.get(k) && randomNumber <= cumulativeFitness.get(k + 1)) {
                    Chromosome c = chromosomes.get(k + 1);
                    if (!selectedChromosomes.contains(c)) {
                        selectedChromosomes.add(c);
                    }
                }

            }
        }
        return selectedChromosomes;
    }

    private ArrayList<Chromosome> Crossover(ArrayList<Chromosome> selectedChromosomes, int populationSize, double Pc) {
        float Rc = rand.nextFloat();
        if (Rc <= Pc) {
            int Xc = rand.nextInt(numberOfItems-1) + 1;

            Chromosome firstChromosome = selectedChromosomes.get(0);
            Chromosome secondChromosome = selectedChromosomes.get(1);

            for(int i = Xc; i<items.size(); i++) {
                int temp = firstChromosome.geneAt(i);

                firstChromosome.setGeneAt(i, secondChromosome.geneAt(i));

                secondChromosome.setGeneAt(i, temp);
            }
        }
        return selectedChromosomes;
    }

    private void Mutation() {

    }

    private void Replacement() {

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
