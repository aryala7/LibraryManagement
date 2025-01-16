package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.model.User;
import la.arya.librarymanagement.request.user.CreateUserRequest;
import la.arya.librarymanagement.request.user.UpdateUserRequest;

public interface IUserService {


    User getUserById(Long id);

    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);


}
