package de.hbmexample.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = Wallet.EG_PROFILE_FULL,
                attributeNodes = {
                        @NamedAttributeNode(value = "creditcards",
                                subgraph = CreditCard.EG_PROFILE_FULL),
                        @NamedAttributeNode(value = "debitCards",
                                subgraph = DebitCard.EG_PROFILE_FULL)
                }
        ),
        @NamedEntityGraph(
                name = Wallet.EG_PROFILE_PARTIAL,
                attributeNodes = {
                        @NamedAttributeNode(value = "creditcards",
                                subgraph = CreditCard.EG_PROFILE_FULL)
                }
        )
})
public class Wallet {

    @Transient
    public transient static final String EG_PROFILE_FULL = "WalletFull";

    @Transient
    public transient static final String EG_PROFILE_PARTIAL = "WalletCredit";

    @Id
    long id;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn(name = "idx")
    @JoinColumn(name = "wallet_id")
    List<CreditCard> creditcards;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn(name = "idx")
    @JoinColumn(name = "wallet_id")
    List<DebitCard> debitCards;
}
