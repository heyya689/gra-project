package com.gra.services;

import com.gra.dao.UserDAO;
import com.gra.dao.RoleDAO;
import com.gra.dao.LokacionDAO;
import com.gra.model.User;
import com.gra.model.Role;
import com.gra.model.Preferenca;
import com.gra.model.Lokacion;
import java.util.List;

public class UserSolver {
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private LokacionDAO lokacionDAO;

    public UserSolver() {
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
        this.lokacionDAO = new LokacionDAO();
    }

    /**
     * Regjistron një përdorues të ri bashkë me preferencat,
     * lokacionin e tij dhe i cakton një rol default (p.sh. "USER").
     */
    public User registerFullUser(User user, Lokacion lokacion) throws Exception {
        // 1. Validimi: Kontrollojmë nëse emaili ekziston
        if (userDAO.emailExists(user.getEmail())) {
            throw new Exception("Ky email është i regjistruar në sistem.");
        }

        // 2. Ruajmë Lokacionin (nëse përdoruesi ka dhënë një të tillë)
        if (lokacion != null) {
            lokacionDAO.save(lokacion);
            user.setLokacion(lokacion);
        }

        // 3. Inicializojmë preferencat default nëse nuk ekzistojnë
        if (user.getPreferenca() == null) {
            user.setPreferenca(new Preferenca());
        }

        // 4. Ruajmë User-in (UserDAO.save automatikisht ruan edhe Preferencat sipas kodit tënd)
        userDAO.save(user);

        // 5. Caktimi i Rolit Default (p.sh. "USER")
        Role defaultRole = roleDAO.findByEmri("USER");
        if (defaultRole != null) {
            roleDAO.assignRoleToUser(user.getUserId(), defaultRole.getRoleId());
        }

        return user;
    }

    /**
     * Autentikon përdoruesin dhe ngarkon të gjitha të dhënat (Rolet, Preferencat).
     */
    public User login(String email, String password) throws Exception {
        User user = userDAO.findByEmail(email);

        if (user != null && user.login(email, password)) {
            // Ngarkojmë rolet sepse UserDAO.findByEmail nuk i ngarkon automatikisht
            List<Role> roles = roleDAO.findRolesByUserId(user.getUserId());
            user.setRoles(roles);
            return user;
        }

        return null;
    }

    /**
     * Përditëson profilin dhe rolet e përdoruesit.
     */
    public void updateCompleteProfile(User user, List<Integer> roleIds) throws Exception {
        // Përditësojmë të dhënat bazë dhe preferencat
        userDAO.update(user);

        // Përditësojmë lokacionin nëse është ndryshuar
        if (user.getLokacion() != null) {
            lokacionDAO.update(user.getLokacion());
        }

        // Përditësojmë rolet (fshijmë të vjetrat dhe shtojmë të rejat)
        if (roleIds != null) {
            // Mund të shtohet logjikë këtu për të sinkronizuar rolet
            for (int roleId : roleIds) {
                roleDAO.assignRoleToUser(user.getUserId(), roleId);
            }
        }
    }

    /**
     * Fshin përdoruesin dhe të gjitha lidhjet e tij (Preferenca, Rolet).
     */
    public void deleteUserCompletely(int userId) throws Exception {
        // RoleDAO fshin lidhjet në user_role, UserDAO fshin Preferencat dhe User-in
        userDAO.delete(userId);
    }
}