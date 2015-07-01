package de.hbmexample1.entity;

import javax.persistence.*;

@Entity
@NamedEntityGraph(
        name = CreditCard.EG_PROFILE_FULL,
        attributeNodes = {
                @NamedAttributeNode(value = "number"),
                @NamedAttributeNode(value = "name")
        }
)
public class CreditCard {

    @Transient
    public transient static final String EG_PROFILE_FULL = "CreditCardFull";

    @Id
    long id;

    @Column(name="creditcard_number")
    int number;

    @Column(name="creditcard_name")
    String name;

}
