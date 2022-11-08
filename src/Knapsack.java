import java.util.ArrayList;
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

    private void Selection() {

    }

    private void Crossover() {

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
