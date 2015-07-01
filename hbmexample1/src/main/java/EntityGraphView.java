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

public class EntityGraphView {

    protected static final String CONTEXT_LOCATION = "/inmemory-database-test-annotation-context.xml";

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_LOCATION);
        EntityManager em = ((EntityManagerFactory) context.getBean("entityManagerFactory")).createEntityManager();
        EntityGraphImpl<?> entityGraph = (EntityGraphImpl<?>) em.getEntityGraph(Person.EG_PROFILE_FULL);

        StringBuilder sbout = new StringBuilder();
        sbout.append("\nEntityGraph=" + entityGraph.getName() + "\n");

        List<AttributeNode<?>> attributeNodeList = entityGraph.getAttributeNodes();

        sbout.append("AttributeNodes{");

        for (AttributeNode jpaAttributeNode : attributeNodeList) {
            AttributeNodeImpl hbAttributeNode = (AttributeNodeImpl) jpaAttributeNode;
            Attribute jpaAttribute = hbAttributeNode.getAttribute();

            sbout.append("\n\tname=" + jpaAttributeNode.getAttributeName());
            sbout.append(" type=" + jpaAttribute.getJavaType().getName());

            Map<Class, Subgraph> subgraphs = hbAttributeNode.getSubgraphs();
            Map<Class, Subgraph> keySubgraphs = hbAttributeNode.getKeySubgraphs();

            List<Class> genericTypes = new ArrayList<>();
            boolean isCollection = jpaAttribute.isCollection();
            if (isCollection) {
                genericTypes.addAll(subgraphs.keySet());
            }

            boolean isMap = !hbAttributeNode.getKeySubgraphs().isEmpty();
            if (isMap) {
                genericTypes.addAll(keySubgraphs.keySet());
            }
            sbout.append(" generics=" + genericTypes);

            sbout.append("\n\t\tsubgraph{");
            if (!isMap) {
                for (Subgraph subgraph : subgraphs.values()) {
                    SubgraphImpl hbmSubgraph = (SubgraphImpl) subgraph;

                    // print label for empty subnodes
                    if (hbmSubgraph.getAttributeNodes().isEmpty()) {
                        sbout.append("\nempty_subnodes?=" + hbmSubgraph.getAttributeNodes().isEmpty()); // WHY???

                        /*
                         ###############################################################################################
                         #                                                                                             #
                         # need a way to identify the name of a subgraph, to get it by entity graph using entity       #
                         # manager.                                                                                    #
                         #                                                                                             #
                         # needed something like this:                                                                 #
                         #                                                                                             #
                         # String subgraphName = subgraph.getName()                                                    #
                         #                                                                                             #
                         # EntityGraphImpl<?> entityGraph = (EntityGraphImpl<?>) em.getEntityGraph(subgraphName);      #
                         #                                                                                             #
                         ###############################################################################################
                         */

                    }
                }
            }
            sbout.append("}");
        }
        sbout.append("\n}");
        System.out.println(sbout.toString());
    }
}
