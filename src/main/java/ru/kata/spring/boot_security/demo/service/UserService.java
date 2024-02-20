package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        User user = (User) authentication.getPrincipal();
        return user;
    }


    public User getOne(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NoSuchUserException("there is no USER with ID = " + id + " found in DB");
        }
        return user;
    }

    public void update(User updatedUser, Integer id) {
        userRepository.findById(id).ifPresentOrElse(
                user -> {
                    userRepository.save(user);
                },
                () -> {
                    throw new IllegalArgumentException("User with this ID does not exist");
                }
        );
    }

}
