package feedback_system.helper;

import feedback_system.constants.AppConstants;
import feedback_system.dto.UserDto;
import feedback_system.entity.User;
import feedback_system.utility.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class PrepairResponse {

    public ApiResponse setLoginSuccessResponse(UserDto userDto){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(AppConstants.FALSE);
        apiResponse.setMessage(AppConstants.LOGIN_SUCCESS);
        apiResponse.setUser(userDto);
        return apiResponse;
    }

    public ApiResponse setFailResponse(String errorMessage){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(AppConstants.TRUE);
        apiResponse.setMessage(AppConstants.LOGIN_FAIL + errorMessage);
        return apiResponse;
    }

    public ApiResponse setUserCreateSuccessResponse(UserDto userDto) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(AppConstants.FALSE);
        apiResponse.setMessage(AppConstants.USER_CREATE_SUCCESS);
        apiResponse.setUser(userDto);
        return apiResponse;
    }

    public ApiResponse setFailResponseForRoleAssign(String userId) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(AppConstants.TRUE);
        apiResponse.setMessage(AppConstants.INVALID_USER_ID + userId);
        return apiResponse;
    }

    public ApiResponse setSuccessForRoleAssign(String username) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(AppConstants.FALSE);
        apiResponse.setMessage(AppConstants.ROLE_ADDED + " for " + username);
        return apiResponse;
    }

    public ApiResponse setSuccessForFindAllUsers(List<User> users) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(AppConstants.FALSE);
        apiResponse.setMessage(AppConstants.ALL_USER_FETCHED);
        List<UserDto> userDtoList = new ArrayList<>();
        users.stream().forEach(user -> userDtoList.add(UserDto.getUserDto(user)));
        apiResponse.setUserList(userDtoList);
        return apiResponse;
    }

    public ApiResponse deleteUserSuccess(String username) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(AppConstants.FALSE);
        apiResponse.setMessage(AppConstants.USER_DELETED + username);
        return apiResponse;
    }

    public ApiResponse deleteUserFailed(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(AppConstants.FALSE);
        apiResponse.setMessage(AppConstants.USER_NOT_FOUND_TO_DELETE + id);
        return apiResponse;
    }
}
