package de.hbmexample1.egraph;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.jpa.graph.internal.AttributeNodeImpl;

import javax.persistence.AttributeNode;
import javax.persistence.Subgraph;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by domann on 21.06.15.
 */
public class HibernateAttributeNode {

    private static final Logger log = Logger.getLogger(HibernateAttributeNode.class);
    static {
        log.setLevel(Level.DEBUG);
    }
    private String name;

    private Class type;

    private List<String> genericTypes;

    private boolean isCollection;

    private boolean isMap;

    private List<HibernateAttributeNode> subgraph;

    public HibernateAttributeNode(AttributeNodeImpl hbmAttributeNode) {
        Attribute jpaAttribute = hbmAttributeNode.getAttribute();
        this.name              = jpaAttribute.getName();
        this.type              = jpaAttribute.getJavaType();
        this.genericTypes      = new ArrayList<>();

        this.isCollection      = jpaAttribute.isCollection();
        log.debug("isCollection=" + isCollection);
        if(jpaAttribute.isCollection()) this.genericTypes.addAll(hbmAttributeNode.getSubgraphs().keySet());



        this.isMap = !hbmAttributeNode.getKeySubgraphs().isEmpty();
        log.debug("isMap=" + isMap);
        if(isMap) {
            genericTypes.addAll(hbmAttributeNode.getKeySubgraphs().keySet());

            this.subgraph = getAllAttributes(new ArrayList<>(hbmAttributeNode.getKeySubgraphs().values()));

        }

        if(!isMap)
            this.subgraph = getAllAttributes(new ArrayList<>(hbmAttributeNode.getSubgraphs().values()));
        log.debug("subgraph=" + subgraph);
    }

    private List<HibernateAttributeNode> getAllAttributes(List<Subgraph> subgraphs) {
        log.debug("getAllAttributes(subgraph.size=" + subgraphs.size() + ")");
        // get stream of all subgraphs
        Stream<Subgraph> subgraphStream = subgraphs.stream();
        log.debug(subgraphs.get(0).getAttributeNodes().size());
        // map all subgraphs to (their) list of attributeNodes stream.
        if (log.isDebugEnabled()) {
            subgraphStream.flatMap(g -> g.getAttributeNodes().stream()).forEach(l -> log.debug("converted list=" + l));
            subgraphStream = subgraphs.stream();
        }
        Stream<List<AttributeNode>> streamOfAttributeNodeLists = subgraphStream.flatMap(g -> g.getAttributeNodes().stream());
        // define converting function to convert all AttributeNodes to de.hbmexample1.egraph.HibernateAttributeDescription
        Function<AttributeNode, HibernateAttributeNode> convertToHbmAttr =
                a -> new HibernateAttributeNode((AttributeNodeImpl) a);
        // flat all lists into one by mapping with the toList() collector, and by converting each element with
        // the above defined function
        return streamOfAttributeNodeLists
                .flatMap(
                        l -> {
                            log.info(l.getClass());
                            return l.stream()
                                .map(convertToHbmAttr);
                        }).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "HibernateAttributeNode{" +
                "name='" + name + '\'' +
                ", type='" + type.getName() + '\'' +
                ", genericTypes=" + genericTypes +
                ", isCollection=" + isCollection +
                ", isMap=" + isMap +
                '}';
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public List<String> getGenericTypes() {
        return genericTypes;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public List<HibernateAttributeNode> getSubgraph() {
        return subgraph;
    }
}
