package be.soa.favorite_coin_list_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {

    Coin findAllByName(String name);

    Iterable<Coin> findAllByNameContaining(String name);

    @Query(value="SELECT * FROM FAV_LIST_COIN fc LEFT OUTER JOIN Coin c on c.id = fc.coin_id AND fc.fav_list_id = :searchId", nativeQuery=true)
    Iterable<Coin> getCoinsByFavoriteListId(long searchId);
}