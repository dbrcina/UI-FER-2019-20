package hr.fer.zemris.search;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class SearchAlgorithmTest {

    @Test
    public void BFSTest() {
        BasicNode<String> a1 = new BasicNode<>("A", null);
        BasicNode<String> a2 = new BasicNode<>("A", a1);
        BasicNode<String> b1 = new BasicNode<>("B", a1);
        BasicNode<String> a3 = new BasicNode<>("A", a2);
        BasicNode<String> b2 = new BasicNode<>("B", a2);
        BasicNode<String> c1 = new BasicNode<>("C", b1);
        BasicNode<String> d1 = new BasicNode<>("D", b1);
        BasicNode<String> a4 = new BasicNode<>("A", a3);
        BasicNode<String> b3 = new BasicNode<>("B", a3);
        BasicNode<String> c2 = new BasicNode<>("C", b2);
        BasicNode<String> d2 = new BasicNode<>("D", b2);
        BasicNode<String> e = new BasicNode<>("E", c1);
        BasicNode<String> f = new BasicNode<>("F", c1);
        BasicNode<String> g = new BasicNode<>("G", d1);
        BasicNode<String> h = new BasicNode<>("H", d1);
        Map<String, Set<String>> transitions = Map.of(
                "A", new LinkedHashSet<>(List.of("A", "B")),
                "B", new LinkedHashSet<>(List.of("C", "D")),
                "C", new LinkedHashSet<>(List.of("E", "F")),
                "D", new LinkedHashSet<>(List.of("G", "H")),
                "E", Set.of(""),
                "F", Set.of(""),
                "G", Set.of(""),
                "H", Set.of("")
        );
        Function<String, Set<String>> succ = transitions::get;
        Predicate<String> goal = s -> s.equals("G");
        SearchAlgorithm<String> searchAlg = new BFS<>();
        BasicNode<String> result = searchAlg.search("A", succ, goal).get();
        Assert.assertEquals("(A) =>\n(B) =>\n(D) =>\n(G)", BasicNode.printPathTowardsNode(result));
        Assert.assertEquals(4, result.depth() + 1);
        Assert.assertEquals(4, searchAlg.getStatesVisited());
    }

}
