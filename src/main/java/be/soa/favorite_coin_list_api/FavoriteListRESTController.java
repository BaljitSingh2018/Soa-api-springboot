package be.soa.favorite_coin_list_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("favorite-list/")
public class FavoriteListRESTController {

    @Autowired
    private FavoriteCoinListService favoriteCoinListService;

    @GetMapping("/all-coins")
    public Iterable<Coin> getAllCoins() {
        return this.favoriteCoinListService.getAllCoins();
    }

    @GetMapping("/search-coin/{name}")
    public Iterable<Coin> searchCoinName(@PathVariable(name = "name") String name) {
        try{
            return this.favoriteCoinListService.getCoinsLikeName(name);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "search-coin", e);
        }
    }

    /**
     * Read all Favorite list
     */
    @GetMapping("/overview")
    public Iterable<FavoriteCoinList> getAllFavoriteLists() {
        return this.favoriteCoinListService.getAllFavoriteLists();
    }

    @GetMapping("/overview/{id}")
    public  Map<Long, List<String>> getAllFavoriteListsById(@PathVariable(name = "id") long id) {
        return this.favoriteCoinListService.getCoinsFromList(id);
    }

    /**
     * Create a new Favorite list
     */
    @PostMapping("/new")
    public FavoriteCoinList createNewFavoriteCoinList(@Valid @RequestBody FavoriteCoinList favoriteCoinList) {
        try {
            this.favoriteCoinListService.createNewFavoriteCoinList(favoriteCoinList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create new favorite list", e);
        }

        // TODO
        return favoriteCoinList;
    }

    /**
     * Update a coin to Favorite list
     */
    @PostMapping("/add-coin/")
    public FavoriteCoinList updateFavoriteCoinList(@RequestBody Map<String, Integer> payload) {
        try{
            // System.out.println("FavoriteListRESTController.updateFavoriteCoinList" + payload.toString());
            return this.favoriteCoinListService.updateFavoriteCoinList( (long) payload.get("coin_id"), (long) payload.get("fav_list_id"));
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Add coin to favorites", e);
        }
    }

    /**
     * Delete a coin from Favorite list
     */
    @DeleteMapping("/remove-coin")
    public void deleteFavoriteCoinList(@RequestBody Map<String, Integer> payload){
        try{
            this.favoriteCoinListService.deleteFavoriteCoinList((long) payload.get("coin_id"), (long) payload.get("fav_list_id"));
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Remove coin", e);
        }
    }


    /**
     * Helper methods
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ResponseStatusException.class})
    private Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        } else {
            errors.put(((ResponseStatusException) ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }
}