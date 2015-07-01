package de.hbmexample.entity;

import javax.persistence.*;

@Entity
@NamedEntityGraph(
        name = DebitCard.EG_PROFILE_FULL,
        attributeNodes = {
                @NamedAttributeNode(value = "key")
        }
)
public class DebitCard {

    @Transient
    public transient static final String EG_PROFILE_FULL = "DebitCardFull";

    @Id
    long id;

    @Column(name="debitcard_key")
    long key;

}
