package com.ineedhousing.backend.user;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ineedhousing.backend.user.requests.SetUserTypeRequest;

import jakarta.transaction.Transactional;

import java.util.ArrayList;

/**
 * Handles business logic of Users
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * abstracted user of userRepositorySave for other Services which UserService is used
     * such as FavoriteListing
     * @param user
     * @return User
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * get users by their email
     * @param  email
     * @throws UsernameNotFoundException
     * @return User
     */
    @Cacheable("users")
    public User getUserByEmail(String email) {
        return  userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }

    /**
     * update a User, can be used for entire or partial edits
     * @param newUserDetails
     * @param email
     * @throws UsernameNotFoundException
     * @return User
     */
    @Cacheable(value="users", key="$user.id")
    @Transactional
    public User updateUser(User newUserDetails, String email) {
        User user = getUserByEmail(email);
        BeanUtils.copyProperties(newUserDetails, user, "id", "email");
        return userRepository.save(user);
    }

    /**
     * specifically for just updating the user's type
     * whether that be an intern, new-grad (tenant in future)
     * @param request
     * @throws UsernameNotFoundException
     * @return User
     */
    @Transactional
    public User setUserType(SetUserTypeRequest request, String email) {
        User user = getUserByEmail(email);
        user.setUserType(request.getUserType());
        return userRepository.save(user);
    }

    /**
     *  deletes User from db
     * @param email
     * @throws UsernameNotFoundException
     * @return String
     */
    @CacheEvict(value="users", key="#email")
    public String deleteUser(String email) {
        User user = getUserByEmail(email);
        userRepository.delete(user);
        return String.format("User with email: %s, has been successfully deleted.", email);
    }

}
