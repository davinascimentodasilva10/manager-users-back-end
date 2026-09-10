package br.com.managerusers.services;

import br.com.managerusers.dtos.UserDTO;
import br.com.managerusers.entities.User;
import br.com.managerusers.exceptions.UserNotFoundException;
import br.com.managerusers.mappers.UserMapper;
import br.com.managerusers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                String.format("User with Id %d Not Found", id)
                        )
                );
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toDTO(userRepository.save(user));
    }

}
