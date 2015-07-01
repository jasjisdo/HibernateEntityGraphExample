import de.hbmexample.entity.Person;
import org.hibernate.jpa.graph.internal.AttributeNodeImpl;
import org.hibernate.jpa.graph.internal.EntityGraphImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.AttributeNode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.List;

public class EntityGraphView {

    protected static final String CONTEXT_LOCATION = "/inmemory-database-test-annotation-context.xml";

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_LOCATION);
        EntityManager em = ((EntityManagerFactory) context.getBean("entityManagerFactory")).createEntityManager();
        EntityGraphImpl<Person> entityGraph = (EntityGraphImpl<Person>) em.getEntityGraph(Person.EG_PROFILE_FULL);

        StringBuilder sbout = new StringBuilder();
        sbout.append("\nEntityGraph="+entityGraph.getName()+"\n");

        List<AttributeNode<?>> attributeNodeList = entityGraph.getAttributeNodes();

        sbout.append("Attribute Nodes{");

        for (AttributeNode jpaAttributeNode : attributeNodeList){
            AttributeNodeImpl hbAttributeNode = (AttributeNodeImpl) jpaAttributeNode;
            Attribute jpaAttribute = hbAttributeNode.getAttribute();

            sbout.append("\n\tname="+jpaAttributeNode.getAttributeName());
            sbout.append(" type="+jpaAttribute.getJavaType().getName());

            List<String> genericTypes = new ArrayList<>();
            boolean isCollection = jpaAttribute.isCollection();
            if(isCollection) {
                genericTypes.addAll(hbAttributeNode.getSubgraphs().keySet());
            }

            boolean isMap = !hbAttributeNode.getKeySubgraphs().isEmpty();
            if(isMap) {
                genericTypes.addAll(hbAttributeNode.getKeySubgraphs().keySet());
            }

            sbout.append(" generics="+genericTypes);
        }

        sbout.append("\n}");

        System.out.println(sbout.toString());

    }
}
