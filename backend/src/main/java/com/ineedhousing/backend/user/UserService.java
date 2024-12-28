package com.ineedhousing.backend.user;

import com.ineedhousing.backend.user.requests.SetUserTypeRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Handles business logic of Users
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * get users by their email
     */
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    /*
     * update a User, can be used for entire or partial edits
     */
    @Transactional
    public User updateUser(User newUserDetails) {
        User user = getUserByEmail(newUserDetails.getEmail());
        BeanUtils.copyProperties(newUserDetails, user, "id", "email");
        return userRepository.save(user);
    }

    /*
     * specifically for just updating the user's type
     * whether that be an intern, new-grad (tenant in future)
     */
    @Transactional
    public User setUserType(SetUserTypeRequest request) {
        User user = getUserByEmail(request.getEmail());
        user.setUserType(request.getUserType());
        return userRepository.save(user);
    }

}
