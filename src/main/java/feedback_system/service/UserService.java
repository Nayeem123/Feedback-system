package feedback_system.service;


import feedback_system.dto.RoleDto;
import feedback_system.dto.UserDto;
import feedback_system.entity.User;
import feedback_system.utility.ApiResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findByUsername(String username);

    User save(UserDto userDto);

    ApiResponse findAllUsers();
    ApiResponse login(UserDto userDto);

    ApiResponse createUserByAdmin(UserDto userDto);

    ApiResponse addRoleToUser(RoleDto roleDto);


    ApiResponse deleteUser(Long id);

    ApiResponse updateUser(UserDto userDto);
}