package com.gra.services;

import com.gra.dao.UserDAO;
import com.gra.model.User;

public class UserSolver {
    private UserDAO userDAO;

    public UserSolver() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String email, String password) throws Exception {
        User user = userDAO.findByEmail(email);
        if (user != null && user.login(email, password)) {
            return user;
        }
        return null;
    }

    public User registerUser(User user) throws Exception {
        if (userDAO.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email already registered");
        }
        userDAO.save(user);
        return user;
    }

    public void updateProfile(User user) throws Exception {
        userDAO.update(user);
    }
}