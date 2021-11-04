package be.soa.favorite_coin_list_api;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FavoriteCoinList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Favorite list name is mandatory")
    @NotBlank(message = "Favorite list name cannot be empty!")
    private String name;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "favList_coin",
            // FK: owner-side
            joinColumns = @JoinColumn(name = "fav_list_id", nullable = false),

            // non-owner side
            inverseJoinColumns  = @JoinColumn(name = "coin_id")
    )
    private Set<Coin> coins = new HashSet<>();

    public void setCoins(Set<Coin> coins) {
        this.coins = coins;
    }

    public Set<Coin> getCoins() { return coins; }

    public void addCoinToFavoriteCoinList(Coin coin){
        this.coins.add(coin);
        coin.getFavoritedBy().add(this); // NEW
    }
    public void removeCoinToFavoriteCoinList(Coin coin) {
        this.coins.remove(coin);
        coin.getFavoritedBy().remove(this); // NEW
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

    public FavoriteCoinList() {
    }
}
