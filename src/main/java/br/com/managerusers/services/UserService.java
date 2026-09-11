package br.com.managerusers.services;

import br.com.managerusers.dtos.LoginDTO;
import br.com.managerusers.dtos.UserDTO;
import br.com.managerusers.exceptions.UserNotFoundException;
import br.com.managerusers.mappers.UserMapper;
import br.com.managerusers.repositories.UserRepository;
import br.com.managerusers.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Transactional(readOnly = true)
    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO getUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                String.format("User with Id %d Not Found", id)
                        )
                );
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        var user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toDTO(userRepository.save(user));
    }

    @Transactional
    public LoginDTO login(UserDTO userDTO) throws UnsupportedEncodingException {
        var user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found"
                        ));
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return LoginDTO
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .token(jwtService.generateToken(user.getEmail()))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found"
                        ));
    }
}
