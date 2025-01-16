package la.arya.librarymanagement.service;


import jakarta.persistence.EntityNotFoundException;
import la.arya.librarymanagement.excpetion.ResourceNotFoundException;
import la.arya.librarymanagement.model.User;
import la.arya.librarymanagement.repository.IUserService;
import la.arya.librarymanagement.repository.UserRepository;
import la.arya.librarymanagement.request.user.CreateUserRequest;
import la.arya.librarymanagement.request.user.UpdateUserRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    @Autowired
    protected final UserRepository userRepository;

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
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> System.out.println("User with id " + userId + " not found"));
    }
}
