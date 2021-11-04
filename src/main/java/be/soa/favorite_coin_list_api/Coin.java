package be.soa.favorite_coin_list_api;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Coin name is mandatory")
    @NotBlank(message = "Coin name cannot be empty!")
    private String name;

    @NotNull(message = "alias is mandatory")
    @NotBlank(message = "alias cannot be empty!")
    private String alias_currency;

    //@ManyToMany(mappedBy = "coins", fetch = FetchType.EAGER) // OG
    //private Set<FavoriteCoinList> favoritedBy;    // OG

    @ManyToMany(mappedBy = "coins", fetch = FetchType.LAZY)
    private Set<FavoriteCoinList> favoritedBy = new HashSet<>();

    // private Set<FavoriteCoinList> getFavoritedBy() { return favoritedBy;   }
    public Set<FavoriteCoinList> getFavoritedBy() { return favoritedBy;   }

    public void setFavoritedBy(Set<FavoriteCoinList> favoriteCoinList) {
        this.favoritedBy = favoriteCoinList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias_currency() {
        return alias_currency;
    }

    public void setAlias_currency(String alias_currency) {
        this.alias_currency = alias_currency;
    }

    public Coin() { }
}