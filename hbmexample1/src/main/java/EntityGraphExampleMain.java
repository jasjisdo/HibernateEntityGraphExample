import de.hbmexample1.entity.Person;
import org.hibernate.jpa.graph.internal.AttributeNodeImpl;
import org.hibernate.jpa.graph.internal.EntityGraphImpl;
import org.hibernate.jpa.graph.internal.SubgraphImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.AttributeNode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Subgraph;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityGraphExampleMain {

    protected static final String CONTEXT_LOCATION = "/inmemory-database-test-annotation-context.xml";

    public static void main(String[] args) {

        // get entity graph by name.
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_LOCATION);
        EntityManager em = ((EntityManagerFactory) context.getBean("entityManagerFactory")).createEntityManager();
        EntityGraphImpl<?> entityGraph = (EntityGraphImpl<?>) em.getEntityGraph(Person.EG_PROFILE_FULL);

        // print entity graph name
        System.out.println("EntityGraph = '" + entityGraph.getName() + "'");

        // print entity graph attribute node list size
        System.out.println("EntityGraph.attributeNodes.size = '" + entityGraph.getAttributeNodes().size() + "'");

        // get wallet attribute node.
        AttributeNode<?> attributeNode = entityGraph.getAttributeNodes().get(0);

        // convert and receive needed objects
        AttributeNodeImpl hbAttributeNode = (AttributeNodeImpl) attributeNode;
        Attribute jpaAttribute = hbAttributeNode.getAttribute();

        // print attribute node name
        System.out.println("AttributeNode = '" + attributeNode.getAttributeName() + "'");

        // get subgraph of wallet
        Map<Class, Subgraph> subgraphs = hbAttributeNode.getSubgraphs();

        // subgraph map should have only one entry
        System.out.println("Subgraphs.size = '" + subgraphs.entrySet().size() + "'");

        // subgraph map should contain Wallet class key.
        System.out.println("Subgraphs.keys = '" + subgraphs.keySet() + "'");

        Subgraph subgraph = subgraphs.get(subgraphs.keySet().iterator().next());
        SubgraphImpl hbSubgraph = (SubgraphImpl) subgraph;

        // print subgraph type
        System.out.println("Subgraph.type = '" + subgraph.getClassType().getName() + "'");

        // try to get subgraph attribute nodes

        // subgraph attribute node list should contian one or two elements...but it is zero!
        System.out.println("(Sub)AttributeNode.size = '" + subgraph.getAttributeNodes().size() + "'");

        /*
+        ###############################################################################################
+        #                                                                                             #
+        # need a way to identify the name of a subgraph, to get it by entity graph using entity       #
+        # manager.                                                                                    #
+        #                                                                                             #
+        # needed something like this:                                                                 #
+        #                                                                                             #
+        # String subgraphName = subgraph.getName()                                                    #
+        #                                                                                             #
+        # EntityGraphImpl<?> entityGraph = (EntityGraphImpl<?>) em.getEntityGraph(subgraphName);      #
+        #                                                                                             #
+        ###############################################################################################
+        */

    }
}