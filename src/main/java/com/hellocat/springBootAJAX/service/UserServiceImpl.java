package com.hellocat.springBootAJAX.service;

import com.hellocat.springBootAJAX.domen.User;
import com.hellocat.springBootAJAX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findUserByName(String name) {
        User user = userRepository.getUserByName(name);
        return user;
    }

    @Override
    public boolean saveUser(User user) {
        boolean rezult = false;
        User userInDB = userRepository.getUserByName(user.getName());
        if (userInDB == null) {
            String passwordEncod = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(passwordEncod);
            userRepository.save(user);
            rezult = true;
        }
        return rezult;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        if (!user.getPassword().equals(findUserById(user.getId()).getPassword())) {
            String passwordEncod = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(passwordEncod);
        }
        userRepository.save(user);
    }
}
