import java.util.ArrayList;


public class Knapsack {

    private int weight;
    private int numberOfItems;
    private ArrayList<Item> items;


    public Knapsack(int weight, int numberOfItems, ArrayList<Item> items) {
        this.weight = weight;
        this.numberOfItems = numberOfItems;
        this.items = items;
    }



    public void solve() {

    }

    private ArrayList<ArrayList<Integer>> InitializePopulation(int populationSize) {

        ArrayList<ArrayList<Integer>> chromosomes = new ArrayList<>(populationSize);
        for(int item=0; item<numberOfItems; item++) {
            ArrayList<Integer> chromosome = new ArrayList<>(numberOfItems);
            for(int gene=0; gene<numberOfItems; gene++) {

                chromosome.add((int) Math.round( Math.random()));
            }
            chromosomes.add(chromosome);
        }
        return chromosomes;
    }

    private void FitnessEvaluation() {

    }

    private void Selection() {

    }

    private void Crossover() {

    }

    private void Mutation() {

    }

    private void Replacement() {

    }

    static class Item {
        int weight;
        int value;
    }

    public static void main(String[] args) {



    }

}
