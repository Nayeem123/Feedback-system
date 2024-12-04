package feedback_system.service;

import feedback_system.dto.UserDto;
import feedback_system.entity.User;
import feedback_system.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepository) {
        super();
        this.userRepo = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User save(UserDto userDto) {
        User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),
                userDto.getFullname());
        return userRepo.save(user);
    }

}