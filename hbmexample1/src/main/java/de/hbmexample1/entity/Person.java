package de.hbmexample1.entity;

import javax.persistence.*;

@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = Person.EG_PROFILE_FULL,
                attributeNodes = {
                        @NamedAttributeNode(value = "wallet",
                                subgraph = Wallet.EG_PROFILE_FULL)
                }
        ),
        @NamedEntityGraph(
                name = Person.EG_PROFILE_PARTIAL,
                attributeNodes = {
                        @NamedAttributeNode(value = "wallet",
                                subgraph = Wallet.EG_PROFILE_PARTIAL)
                }
        )
})
public class Person {

    @Transient
    public transient static final String EG_PROFILE_FULL = "PersonFull";

    @Transient
    public transient static final String EG_PROFILE_PARTIAL = "PersonCreditCards";

    @Id
    long id;

    @JoinColumn(name = "wallet_id")
    @OneToOne(cascade = CascadeType.ALL)
    Wallet wallet;

}
