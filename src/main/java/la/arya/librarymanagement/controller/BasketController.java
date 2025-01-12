package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.model.Basket;
import la.arya.librarymanagement.repository.IBasketService;
import la.arya.librarymanagement.response.ApiResponse;
import la.arya.librarymanagement.util.Hashid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("${api.prefix}/cart")
public class BasketController {

    @Autowired
    private IBasketService basketService;

    private Hashid hashIdService;

    @GetMapping("{hash}")
    public ResponseEntity<ApiResponse> getBasket(
            @PathVariable String hash
    ) {

        Long decodedId = hashIdService.decode(hash);

        Basket basket = basketService.getBasket(decodedId);
        return ResponseEntity.ok(new ApiResponse("",""));
    }
}
