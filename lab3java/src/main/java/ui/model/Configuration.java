package ui.model;

public class Configuration {

    public enum Mode {
        TEST, VERBOSE
    }

    public enum Model {
        ID3, RF
    }

    private Mode mode;
    private Model model;
    private int maxDepth = -1;
    private int numTrees = 1;
    private double featureRation = 1.0;
    private double exampleRation = 1.0;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getNumTrees() {
        return numTrees;
    }

    public void setNumTrees(int numTrees) {
        this.numTrees = numTrees;
    }

    public double getFeatureRation() {
        return featureRation;
    }

    public void setFeatureRation(double featureRation) {
        this.featureRation = featureRation;
    }

    public double getExampleRation() {
        return exampleRation;
    }

    public void setExampleRation(double exampleRation) {
        this.exampleRation = exampleRation;
    }

}
