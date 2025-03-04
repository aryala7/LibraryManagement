package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.BasketResponse;
import la.arya.librarymanagement.model.Basket;
import la.arya.librarymanagement.repository.IBasketService;
import la.arya.librarymanagement.request.basket.AddBasketRawRequest;
import la.arya.librarymanagement.request.basket.AddBasketRequest;
import la.arya.librarymanagement.request.basket.RemoveItemRequest;
import la.arya.librarymanagement.request.basket.UpdateBasketRequest;
import la.arya.librarymanagement.response.ApiResponse;
import la.arya.librarymanagement.service.BasketService;
import la.arya.librarymanagement.util.Hashid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("${api.prefix}/cart")
public class BasketController {

    @Autowired
    private IBasketService basketService;

    @Autowired
    private Hashid hashIdService;

    @GetMapping("{hash}")
    public ResponseEntity<ApiResponse> getBasket(
            @PathVariable String hash
    ) {

        try {
            Long decodedId = hashIdService.decode(hash);

            Basket basket = basketService.getBasket(decodedId);
            BasketResponse response = basketService.mapToBasketResponse(basket);

            return ResponseEntity.ok(new ApiResponse("",response));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(),e));
        }
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> addBasket(@RequestBody AddBasketRawRequest rawRequest) {

        try {
            Long decodedProductId = hashIdService.decode(rawRequest.getProductHashId());

            AddBasketRequest request = new AddBasketRequest();

            BeanUtils.copyProperties(rawRequest,request);

            request.setProductId(decodedProductId);

            BasketResponse response =  basketService.createBasket(request);

            return ResponseEntity.ok(new ApiResponse("Success",response));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(),e));
        }

    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> addItemToBasket(@RequestBody UpdateBasketRequest request) {

        try {
            Long decodedBasketId = hashIdService.decode(request.getBasketId());
            Long decodedProductId = hashIdService.decode(request.getProductId());
            BasketResponse response = basketService.addItemToBasket(decodedBasketId,decodedProductId,request.getQuantity());
            return ResponseEntity.ok(new ApiResponse("Success",response));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(),e));
        }
    }

    @PostMapping("/remove-item")
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestBody RemoveItemRequest request) {
           try {
               Long decodedId = hashIdService.decode(request.getBasketId());
               Long decodedProductId = hashIdService.decode(request.getProductId());
               BasketResponse response = basketService.removeItemFromBasket(decodedId,decodedProductId);
               return ResponseEntity.ok(new ApiResponse("Success",response));
           }catch (Exception e) {
               return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(),e));
           }

    }
}
