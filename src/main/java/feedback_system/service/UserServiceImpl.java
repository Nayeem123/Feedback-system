package feedback_system.service;

import feedback_system.constants.AppConstants;
import feedback_system.dto.RoleDto;
import feedback_system.dto.UserDto;
import feedback_system.entity.Feedback;
import feedback_system.entity.Role;
import feedback_system.entity.User;
import feedback_system.helper.PrepairResponse;
import feedback_system.repository.RoleRepo;
import feedback_system.repository.UserRepo;
import feedback_system.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;


    public UserServiceImpl(UserRepo userRepository,RoleRepo roleRepo,PasswordEncoder passwordEncoder) {
        super();
        this.userRepo = userRepository;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User save(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFullname(userDto.getFullname());
//        Role role = roleRepo.findByName("ROLE_ADMIN");
//        if(role == null){
//            role = checkRoleExist();
//        }
        //user.setRoles(Arrays.asList(role));
        return userRepo.save(user);
    }

    private Role checkRoleExist() {
        Role role = new Role();
        //role.setName("ROLE_ADMIN");
        return roleRepo.save(role);
    }

    @Override
    public ApiResponse findAllUsers() {
        PrepairResponse prepairResponse = new PrepairResponse();
        List<User> users = userRepo.findAll();

        return prepairResponse.setSuccessForFindAllUsers(users);
    }

    @Override
    public ApiResponse login(UserDto userDto){

        PrepairResponse prepairResponse = new PrepairResponse();
        User dbUser = userRepo.findByUsername(userDto.getUsername());
        if(dbUser == null){
            return prepairResponse.setFailResponse(AppConstants.USER_NOT_FOUND);
        }
        boolean isValidUser = validateCredentials(userDto,dbUser);
        System.out.println("isValidUser " +isValidUser);
        if (isValidUser){
            return prepairResponse.setLoginSuccessResponse(UserDto.getUserDto(dbUser));
        }

        return prepairResponse.setFailResponse(AppConstants.INVALID_PASSWORD);
    }

    @Override
    public ApiResponse createUserByAdmin(UserDto userDto) {

        PrepairResponse prepairResponse = new PrepairResponse();
        User dbUser = userRepo.findByUsername(userDto.getUsername());
        if(dbUser != null){
            return prepairResponse.setFailResponse(AppConstants.USER_EXISTS);
        }

        User savedUser = saveUser(userDto);
        assignRole(savedUser);
        return prepairResponse.setUserCreateSuccessResponse(UserDto.getUserDto(savedUser));
    }

    @Override
    public ApiResponse addRoleToUser(RoleDto roleDto) {

        PrepairResponse prepairResponse = new PrepairResponse();
        User dbUser = userRepo.findByUsername(roleDto.getUsername());
        if(dbUser == null){
            return prepairResponse.setFailResponseForRoleAssign(roleDto.getUsername());
        }
        User updatedRoleUser = updateRole(dbUser,roleDto.getRoleName());
        Optional<Role> role = roleRepo.findById(updatedRoleUser.getId());
        if (role.isPresent()) {
            role.get().setRoles(updatedRoleUser.getRoles());
            roleRepo.save(role.get());
        }
        return prepairResponse.setSuccessForRoleAssign(updatedRoleUser.getUsername());
    }

    @Override
    public ApiResponse deleteUser(Long id) {

        PrepairResponse prepairResponse = new PrepairResponse();
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()){
            userRepo.deleteById(id);
            return prepairResponse.deleteUserSuccess(user.get().getUsername());
        }

        return prepairResponse.deleteUserFailed(id);
    }

    @Override
    public ApiResponse updateUser(UserDto userDto) {
        System.out.println(userDto.toString());
        PrepairResponse prepairResponse = new PrepairResponse();
        User dbUser = userRepo.findByUsername(userDto.getUsername());
        if (dbUser == null){
            return prepairResponse.updateUserFailed(userDto.getUsername());
        }

        User updatedUser = updateUser(dbUser, userDto);
        return prepairResponse.updateUserSuccess(updatedUser.getUsername());
    }

    private User updateUser(User dbUser, UserDto userDto) {
        if(userDto.getFullname() != null)
            dbUser.setFullname(userDto.getFullname());
        if(userDto.getPassword() != null)
            dbUser.setPassword(userDto.getPassword());
        if(userDto.getMobileNumber() != null)
            dbUser.setMobileNumber(userDto.getMobileNumber());
        if (!userDto.getRoles().isEmpty()){
            List<String> roles = dbUser.getRoles();
            for(String role : userDto.getRoles()){
                if(!roles.contains(role)){
                    roles.add(role);
                }
            }
            dbUser.setRoles(roles);
        }
        return userRepo.save(dbUser);
    }

    private User updateRole(User dbUser, String newRole) {
        List<String> existingRole = dbUser.getRoles();
        existingRole.add(newRole);
        dbUser.setRoles(existingRole);
        return userRepo.save(dbUser);
    }

    private void assignRole(User savedUser) {
        Role role = new Role();
        role.setUserId(savedUser.getId());
        role.setRoles(savedUser.getRoles());
        roleRepo.save(role);
    }

    private User saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFullname(userDto.getFullname());
        user.setGender(userDto.getGender());
        user.setMobileNumber(userDto.getMobileNumber());
        //user.setRoles(Arrays.asList(AppConstants.ROLE_SUPPORT));
        user.setRoles(Arrays.asList(AppConstants.ROLE_USER));
        return userRepo.save(user);

    }

    private boolean validateCredentials(UserDto userDto,User dbUser) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        System.out.println("username " +dbUser.getUsername().equals(username));
        System.out.println("password " +password);
        if(dbUser.getUsername().equals(username) &&
                dbUser.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public ApiResponse changePassword(UserDto userDto) {
        ApiResponse apiResponse = new ApiResponse();
        PrepairResponse prepairResponse = new PrepairResponse();
        if(userDto.getUsername() != null){

            User feedback = userRepo.findByUsername(userDto.getUsername());
            if(feedback == null){
                apiResponse.setMessage(AppConstants.USER_NOT_FOUND);
                return prepairResponse.setApiResponseFail(apiResponse);
            }
            if (feedback.getPassword().equals(userDto.getOldPassword())){
                feedback.setPassword(userDto.getPassword());
                userRepo.save(feedback);
                apiResponse.setMessage(AppConstants.PASSWORD_UPDATED);
                return prepairResponse.setSuccessResponse(apiResponse);
            }
            else {
                apiResponse.setMessage(AppConstants.OLD_PASSWORD_NOT_MATCHED);
                return prepairResponse.setApiResponseFail(apiResponse);
            }
        }
        apiResponse.setMessage(AppConstants.USER_NOT_FOUND);
        return prepairResponse.setApiResponseFail(apiResponse);
    }
}