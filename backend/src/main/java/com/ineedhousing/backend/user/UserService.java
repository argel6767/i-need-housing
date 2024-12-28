package com.ineedhousing.backend.user;

import com.ineedhousing.backend.user.requests.SetUserTypeRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }


    @Transactional
    public User updateUser(User newUserDetails) {
        User user = getUserByEmail(newUserDetails.getEmail());
        BeanUtils.copyProperties(newUserDetails, user, "id", "email");
        return userRepository.save(user);
    }

    @Transactional
    public User setUserType(SetUserTypeRequest request) {
        User user = getUserByEmail(request.getEmail());
        user.setUserType(request.getUserType());
        return userRepository.save(user);
    }
}
