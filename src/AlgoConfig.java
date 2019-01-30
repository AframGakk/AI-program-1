public class AlgoConfig {
    private int expanded;
    private int frontierSize;
    private int cost;

    public AlgoConfig() {
        expanded = 0;
        frontierSize = 0;
        cost = 0;
    }

    public int getExpanded() {
        return expanded;
    }

    public void setExpanded(int expanded) {
        this.expanded = expanded;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getFrontierSize() {
        return frontierSize;
    }

    public void setFrontierSize(int frontierSize) {
        this.frontierSize = frontierSize;
    }

    public void incrExp() {
        this.expanded++;
    }

    public void printTotals() {
        System.out.println("=========== Final Counts ===========");
        System.out.println("    Nodes expanded: " + this.expanded);
        System.out.println("    Frontier size: " + this.frontierSize);
        System.out.println("    Final cost: " + this.cost);
        System.out.println("====================================");
    }
}
