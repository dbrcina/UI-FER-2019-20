package ui.model;

import java.util.Collection;

public class Sample {

    private final Collection<Feature> features;
    private final ClassLabel classLabel;

    public Sample(Collection<Feature> features, ClassLabel classLabel) {
        this.features = features;
        this.classLabel = classLabel;
    }

    public Collection<Feature> getFeatures() {
        return features;
    }

    public ClassLabel getClassLabel() {
        return classLabel;
    }

}
