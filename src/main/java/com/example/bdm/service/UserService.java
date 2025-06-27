package com.example.bdm.service;

import com.example.bdm.dto.AppUserDto;
import com.example.bdm.model.AppUser;
import com.example.bdm.repository.AppUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class UserService {
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(AppUserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<AppUserDto> getAllUserDTOSortedByName() {
        List<AppUser> foundUsers = userRepository.findAll(Sort.by("lastName"));
        return foundUsers.stream()
                .map(AppUserDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppUserDto getUserDTOById(Long id) throws NoSuchElementException {
        AppUser foundUser = userRepository.findById(id).orElseThrow();
        return new AppUserDto(foundUser);
    }

    @Transactional
    public void deleteUserById(Long id) throws NoSuchElementException {
        AppUser userToDelete = userRepository.findById(id).orElseThrow();
        userRepository.delete(userToDelete);
    }
}
