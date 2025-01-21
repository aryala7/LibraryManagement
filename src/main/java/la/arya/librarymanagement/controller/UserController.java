package la.arya.librarymanagement.controller;

import la.arya.librarymanagement.dto.UserResponse;
import la.arya.librarymanagement.model.User;
import la.arya.librarymanagement.repository.IUserService;
import la.arya.librarymanagement.request.user.CreateUserRequest;
import la.arya.librarymanagement.request.user.UpdateUserRequest;
import la.arya.librarymanagement.response.ApiResponse;
import la.arya.librarymanagement.util.Hashid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {


    @Autowired
    protected  IUserService userService;

    @Autowired
    protected Hashid hashIdService;

    @GetMapping("/{hashId}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable("hashId") String hashId) {
        try {
            Long id = hashIdService.decode(hashId);
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse("Success",  userService.mapToUserResponse(user)));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{hashId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("hashId") String hashId, @RequestBody UpdateUserRequest request ) {
        try {
            Long id = hashIdService.decode(hashId);
            User user = userService.updateUser(request,id);
            return ResponseEntity.ok(new ApiResponse("Success", userService.mapToUserResponse(user)));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(new ApiResponse("Success", userService.mapToUserResponse(user)));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }



}
