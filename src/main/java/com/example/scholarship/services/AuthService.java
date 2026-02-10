package com.example.scholarship.services;

import com.example.scholarship.models.User;
import com.example.scholarship.models.Admin;
import com.example.scholarship.repo.UserRepository;
import com.example.scholarship.repo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find user
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            return user.get();
        }
        
        // Try to find admin
        Optional<Admin> admin = adminRepository.findByEmail(username);
        if (admin.isPresent()) {
            return admin.get();
        }
        
        throw new UsernameNotFoundException("User not found with email: " + username);
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole("USER");
        user.setIsActive(true);
        
        return userRepository.save(user);
    }

    @Transactional
    public Admin registerAdmin(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Admin email already registered");
        }
        
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setIsActive(true);
        
        return adminRepository.save(admin);
    }

    public User updateLastLogin(User user) {
        // User doesn't have lastLogin field, so just return
        return user;
    }

    public Admin updateLastLogin(Admin admin) {
        admin.setLastLogin(LocalDateTime.now());
        return adminRepository.save(admin);
    }

    // Helper method to get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Helper method to get admin by email
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }
}