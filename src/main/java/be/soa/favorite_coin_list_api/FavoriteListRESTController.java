package be.soa.favorite_coin_list_api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http://localhost:8080/favorite-list/swagger-ui
 */
@RestController
@RequestMapping("favorite-list/")
public class FavoriteListRESTController {

    @Autowired
    private FavoriteCoinListService favoriteCoinListService;

    /**COIN*/

    @GetMapping("coin/overview")
    @Operation(summary = "Returns list of all crypto coins")
    @ApiResponse(
            responseCode = "200", description = "Found list of crypto coins",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Coin.class))}
    )
    public Iterable<Coin> getAllCoins() {
        return this.favoriteCoinListService.getAllCoins();
    }

    @GetMapping("coin/search/{name}")
    @Operation(summary = "Returns all crypto coins where the name is like searched name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Found list of crypto coins",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Coin.class))}
            ), @ApiResponse(
            responseCode = "400", description = "Invalid input",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Coin.class))}
    )
    })
    public Iterable<Coin> searchCoinName(
            @Parameter(description = "Name of the to be searched coin", required = true)
            @PathVariable(name = "name") String name) {
        try {
            return this.favoriteCoinListService.getCoinsLikeName(name);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coin/search", e);
        }
    }

    @PostMapping("coin/new")
    @Operation(summary = "Creates a new coin",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON body = { \"name\": X,\n" +
                            "    \"alias_currency\": Y } -> NOTE: Replace X and Y with actual name and alias_currency! " +
                            "EX: name: BITCOIN, alias_currency: BTC_EUR", required = true)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Creation successful",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Coin.class))}
            ), @ApiResponse(
            responseCode = "400", description = "Invalid coin name",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Coin.class))}
    )
    })
    public Coin createNewCoin(@RequestBody Map<String, String> payload) {
        try {
            return this.favoriteCoinListService.createNewCoin(payload.get("name"), payload.get("alias_currency"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coin/all-coins", e);
        }
    }


    /**FavoriteCoinList*/

    @GetMapping("/overview")
    @Operation(summary = "Returns of all favorite list(s)")
    @ApiResponse(
            responseCode = "200", description = "Found id and name of favorite list(s)",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
    )
    public Iterable<FavoriteCoinList> getAllFavoriteLists() {
        return this.favoriteCoinListService.getAllFavoriteLists();
    }

    @GetMapping("/overview/{id}")
    @Operation(summary = "Returns all coins inside a specific favorite list")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "All coins inside of favorite list(s) (if any were added)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
            )
    })
    public Map<Long, List<String>> getAllFavoriteListsById(
            @Parameter(description = "Id of the favorite list", required = true)
            @PathVariable(name = "id") long id) {
        try {
            return this.favoriteCoinListService.getCoinsFromList(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "overview-id", e);
        }
    }

    @PostMapping("/new/{name}")
    @Operation(summary = "Create a new Favorite list")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Favorite list creation successful",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid name",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
            )})
    public FavoriteCoinList createNewFavoriteCoinList(
            @Parameter(description = "Name of the new list", required = true)
            @PathVariable String name){
        try {
            return this.favoriteCoinListService.createNewFavoriteCoinList(name);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new", e);
        }
    }

    @PostMapping("/add-coin")
    @Operation(
            summary = "Update a coin inside Favorite list",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON body = { \"coin_id\": X,\n" +
                            "    \"fav_list_id\": Y } !NOTE: Replace X and Y with actual Id's! ", required = true))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Update successful",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid id or coin already exists in list",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
            )})
    public FavoriteCoinList updateFavoriteCoinList(
            @RequestBody Map<String, Integer> payload) {
        try {
            // System.out.println("FavoriteListRESTController.updateFavoriteCoinList" + payload.toString());
            return this.favoriteCoinListService.updateFavoriteCoinList((long) payload.get("coin_id"), (long) payload.get("fav_list_id"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "add-coin", e);
        }
    }


    @DeleteMapping("/remove-coin")
    @Operation(
            summary = "Delete a coin from Favorite list",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON body = { \"coin_id\": X,\n" +
                            "    \"fav_list_id\": Y } -> NOTE: Replace X and Y with actual Id's! ", required = true))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Remove successful",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid coin id or coin not in list or invalid favorite list id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCoinList.class))}
            )})
    public void deleteFavoriteCoinList(@RequestBody Map<String, Integer> payload) {
        try {
            this.favoriteCoinListService.deleteFavoriteCoinList((long) payload.get("coin_id"), (long) payload.get("fav_list_id"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "remove-coin", e);
        }
    }

    /**
     * Swagger-ui for api understanding.
     * localhost:8080/v3/api-docs.yaml
     * localhost:8080/favorite-list/swagger-ui
     */
    @GetMapping("/swagger-ui")
    @Operation(summary = "View the Swagger UI")
    @ApiResponse(responseCode = "200", description = "Swagger found")
    public ResponseEntity<Void> redirectToSwagger() {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/swagger-ui.html"))
                .build();
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