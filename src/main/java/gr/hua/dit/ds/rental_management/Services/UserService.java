package gr.hua.dit.ds.rental_management.Services;

import gr.hua.dit.ds.rental_management.Entities.Roles;
import gr.hua.dit.ds.rental_management.Entities.User;
import gr.hua.dit.ds.rental_management.Repositories.RoleRepository;
import gr.hua.dit.ds.rental_management.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Integer saveUser(User user) {
        //debugging
        System.out.println("User before save: " + user);

        user = userRepository.save(user);

        System.out.println("User after save: " + user);

        return user.getId();
    }

    //Μέθοδος για να ελέγξεις αν υπάρχει το username
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public Integer updateUser(User user) {
        user = userRepository.save(user);
        return user.getId();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     //   log.info("Trying to authenticate user: " + username); //debugging
        Optional<User> opt = userRepository.findByUsername(username);

        if(opt.isEmpty()) {
            throw new UsernameNotFoundException("User: " + username + " not found !");
        }

        User user = opt.get();
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(role-> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .collect(Collectors.toSet())
            );

    }

    @Transactional
    public Object getUsers() {
        return userRepository.findAll();
    }

    public Object getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional
    public void updateOrInsertRole(Roles role) {
        roleRepository.updateOrInsert(role);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

}