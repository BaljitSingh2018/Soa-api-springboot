package be.soa.favorite_coin_list_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteCoinListRepository extends JpaRepository<FavoriteCoinList, Long> {
}
