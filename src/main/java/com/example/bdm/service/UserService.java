package com.example.bdm.service;

import com.example.bdm.dto.AppUserDto;
import com.example.bdm.dto.ResponseUserGdpr;
import com.example.bdm.model.AppUser;
import com.example.bdm.repository.AppUserRepository;
import com.example.bdm.repository.RoleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Service dedicated to actions on User entity especially when it comes to interacting with the database
 */
@Service
public class UserService {
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserService(AppUserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieve all users sorted by name from the database and returns a list of the DTO made from the User instances
     * @return list of UserDto
     */
    @Transactional
    public List<AppUserDto> getAllUserDTOSortedByName() {
        List<AppUser> foundUsers = userRepository.findAll(Sort.by("lastName"));
        return foundUsers.stream()
                .map(AppUserDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a user from the database using a given ID and returns the DTO made from the User, throws NoSuchElementException if no user found for the given id
     * @return UserDto
     */
    @Transactional
    public AppUserDto getUserDTOById(Long id) throws NoSuchElementException {
        AppUser foundUser = userRepository.findById(id).orElseThrow();
        return new AppUserDto(foundUser);
    }

    /**
     * delete a user from the database using a given ID, throws NoSuchElementException if no user found for the given id
     * @return UserDto
     */
    @Transactional
    public void deleteUserById(Long id) throws NoSuchElementException {
        AppUser userToDelete = userRepository.findById(id).orElseThrow();
        userRepository.delete(userToDelete);
    }

    @Transactional
    public ResponseUserGdpr getUserGdpr(Long id) throws NoSuchElementException {
        AppUser user = userRepository.findById(id).orElseThrow();
        return new ResponseUserGdpr(user);
    }

    @Transactional
    public boolean updateUserGdpr(Long id, boolean isGdprAccepted) throws NoSuchElementException {
        AppUser user = userRepository.findById(id).orElseThrow();
        System.out.println(">>> stored user gdpr:"+user.getGdpr());
        System.out.println(">>> given gdpr:"+isGdprAccepted);
        System.out.println(">>> result:"+(user.getGdpr() == isGdprAccepted));
        if(user.getGdpr() == isGdprAccepted){
            return false;
        }
        user.setGdpr(isGdprAccepted);
        return true;
    }
}
