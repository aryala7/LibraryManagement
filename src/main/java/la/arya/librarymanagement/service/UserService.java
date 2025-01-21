package la.arya.librarymanagement.service;


import jakarta.persistence.EntityNotFoundException;
import la.arya.librarymanagement.dto.UserResponse;
import la.arya.librarymanagement.exception.ResourceNotFoundException;
import la.arya.librarymanagement.model.User;
import la.arya.librarymanagement.repository.IUserService;
import la.arya.librarymanagement.repository.UserRepository;
import la.arya.librarymanagement.request.user.CreateUserRequest;
import la.arya.librarymanagement.request.user.UpdateUserRequest;
import la.arya.librarymanagement.util.Hashid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    @Autowired
    protected final UserRepository userRepository;

    @Autowired
    protected final ModelMapper modelMapper;


    protected Hashid hashIdService;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {


        return Optional.of(request)
                        .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                        .map(req -> {
                            User user = new User();
                            user.setFirstName(req.getFirstName());
                            user.setLastName(req.getLastName());
                            user.setEmail(req.getEmail());
                            user.setPassword(req.getPassword());
                            return userRepository.save(user);
                        }).orElseThrow(() -> new RuntimeException("User Already Exists with the given Email"));

    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository
                .findById(userId)
                .ifPresentOrElse(userRepository::delete, () -> System.out.println("User with id " + userId + " not found"));
    }

    @Override
    public UserResponse mapToUserResponse(User user) {
            UserResponse response =  modelMapper.map(user, UserResponse.class);
            response.setHashId(hashIdService.encode(user.getId()));
            return response;
    }

    @Override
    public List<UserResponse> convertToUserResponse(List<User> users) {
        return users.stream().map(this::mapToUserResponse).toList();
    }
}
