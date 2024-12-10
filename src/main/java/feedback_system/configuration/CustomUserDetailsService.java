package feedback_system.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import feedback_system.dto.UserDto;
import feedback_system.entity.Role;
import feedback_system.entity.User;
import feedback_system.repository.UserRepo;
import feedback_system.utility.ApiResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        super();
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username or Password not found");
        }

//        ErrorCode.error = AppConstants.FALSE;
//        ErrorCode.errorMsg = AppConstants.SUCCESS;
        return new CustomUserDetails(user.getUsername(), user.getPassword(), authorities(), user.getFullname());
    }

    public Collection<? extends GrantedAuthority> authorities() {
        List<GrantedAuthority> authorities = Arrays.asList(
                (GrantedAuthority) () -> "ROLE_ADMIN",
                (GrantedAuthority) () -> "ROLE_USER"
        );
        return authorities;
    }

}