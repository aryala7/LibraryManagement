package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.dto.UserResponse;
import la.arya.librarymanagement.model.User;
import la.arya.librarymanagement.request.user.CreateUserRequest;
import la.arya.librarymanagement.request.user.UpdateUserRequest;

import java.util.List;

public interface IUserService {


    User getUserById(Long id);

    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);


    UserResponse mapToUserResponse(User user);

    List<UserResponse> convertToUserResponse(List<User> users);
}
