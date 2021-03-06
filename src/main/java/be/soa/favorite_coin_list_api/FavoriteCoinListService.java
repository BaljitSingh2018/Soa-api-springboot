package be.soa.favorite_coin_list_api;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FavoriteCoinListService {

    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    private FavoriteCoinListRepository favoriteCoinListRepository;


    /***Coins*/

    public Iterable<Coin> getAllCoins() {
        return this.coinRepository.findAll();
    }

    public Iterable<Coin> getCoinsLikeName(String name) throws ServiceException{
        if (isEmpty(name)){
            throw new ServiceException("Coin name cannot be empty!");
        }
        return this.coinRepository.findAllByNameContaining(name.toUpperCase());
    }

    public Coin createNewCoin(String name, String alias_currency) throws ServiceException{
        Coin byName = this.coinRepository.findAllByName(name);
        if (byName != null){
            throw new ServiceException("Coin already exists");
        }
        return this.coinRepository.save(new Coin(name, alias_currency));
    }

    /***Favorite List*/
    public List<FavoriteCoinList> getAllFavoriteLists() throws ServiceException {
        return this.favoriteCoinListRepository.findAll();
    }

    public FavoriteCoinList createNewFavoriteCoinList(String name) throws ServiceException {
        if (isEmpty(name)){
            throw new ServiceException("Name cannot be empty!");
        }
        FavoriteCoinList f = new FavoriteCoinList();
        f.setName(name);
        return this.favoriteCoinListRepository.save(f);
    }


    public FavoriteCoinList updateFavoriteCoinList(long coinId, long favListId) throws ServiceException {

        if (!this.favoriteCoinListRepository.findById(favListId).isPresent() || !this.coinRepository.findById(coinId).isPresent()) {
            throw new ServiceException("Invalid Favorite List id or Coin id");
        }

        FavoriteCoinList favoriteCoinList = this.favoriteCoinListRepository.findById(favListId).get();
        Coin coin = this.coinRepository.findById(coinId).get();

        if (favoriteCoinList.getCoins().contains(coin)){
            throw new ServiceException("This crypto currency is already added to favorites");
        }

        favoriteCoinList.addCoinToFavoriteCoinList(coin);
        return this.favoriteCoinListRepository.save(favoriteCoinList);
    }

    public void deleteFavoriteCoinList(long coinId, long favListId) throws ServiceException{

        if (!this.favoriteCoinListRepository.findById(favListId).isPresent() || !this.coinRepository.findById(coinId).isPresent()) {
            throw new ServiceException("Invalid Favorite List id or Coin id");
        }
        FavoriteCoinList favoriteCoinList = this.favoriteCoinListRepository.findById(favListId).get();
        Coin coin = this.coinRepository.findById(coinId).get();

        if (!favoriteCoinList.getCoins().contains(coin)){
            throw new ServiceException("Invalid coin id");
        }
        favoriteCoinList.removeCoinToFavoriteCoinList(coin);
        this.favoriteCoinListRepository.save(favoriteCoinList);
    }

    public  Map<Long, List<String>> getCoinsFromList(long id) throws ServiceException {

        if (!this.coinRepository.findById(id).isPresent()){
            throw new ServiceException("Invalid Id");
        }

        Iterable<Coin> coinsList = this.coinRepository.getCoinsByFavoriteListId(id);
        Map<Long, List<String>> res = new HashMap<>();

        for (Coin coin: coinsList) {
            if (coin != null){
                List<String> value = new ArrayList<>();
                value.add(coin.getName());
                value.add(coin.getAlias_currency());

                res.put(coin.getId(), value);
                //System.out.println("FavoriteCoinListService.getAllCoins " +  str + "\n");
            }
        }
        return res;
    }

    private boolean isEmpty(String s){ return s == null || s.trim().isEmpty();}
}















