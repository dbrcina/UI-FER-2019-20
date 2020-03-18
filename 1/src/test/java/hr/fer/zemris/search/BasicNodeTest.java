package hr.fer.zemris.search;

import org.junit.Assert;
import org.junit.Test;

public class BasicNodeTest {

    @Test
    public void printPathTowardsNodeTest() {
        BasicNode<String> a = new BasicNode<>("A", null);
        BasicNode<String> b = new BasicNode<>("B", a);
        BasicNode<String> d = new BasicNode<>("D", b);
        Assert.assertEquals("(A) =>\n(B) =>\n(D)", BasicNode.printPathTowardsNode(d));
        Assert.assertEquals("(A)", BasicNode.printPathTowardsNode(a));
        Assert.assertEquals("(A) =>\n(B)", BasicNode.printPathTowardsNode(b));
    }

    @Test
    public void depthTest() {
        BasicNode<String> a = new BasicNode<>("A", null);
        BasicNode<String> b = new BasicNode<>("B", a);
        BasicNode<String> c = new BasicNode<>("C", a);
        BasicNode<String> d = new BasicNode<>("D", b);
        Assert.assertEquals(0, a.depth());
        Assert.assertEquals(1, b.depth());
        Assert.assertEquals(1, c.depth());
        Assert.assertEquals(2, d.depth());
    }

}
