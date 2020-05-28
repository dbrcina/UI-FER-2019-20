package ui.ml;

import ui.model.Sample;

import java.util.Collection;

public interface MLAlgorithm {

    void fit(Collection<Sample> dataset);

    Collection<String> predict(Collection<Sample> dataset);

}
