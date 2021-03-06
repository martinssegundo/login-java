package br.com.login.services.impl;

import br.com.login.entities.Role;
import br.com.login.entities.User;
import br.com.login.entities.UserRole;
import br.com.login.exceptions.FindRoleException;
import br.com.login.exceptions.FindUserException;
import br.com.login.exceptions.RoleAssociateExption;
import br.com.login.repository.UserRoleRepository;
import br.com.login.services.RoleService;
import br.com.login.services.UserRoleService;
import br.com.login.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private UserRoleRepository userRoleRepository;
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository,
                               UserService userService,
                               RoleService roleService){
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public UserRole associateUserToRole(String login, String technicalNameRole) throws RoleAssociateExption {
        User user = null;
        Role role = null;
        try {
            user = userService.findUserByLogin(login);
            role = roleService.findBytechnicalName(technicalNameRole);
        } catch (FindUserException | FindRoleException e) {
            throw new RoleAssociateExption();
        }

        return userRoleRepository.save(UserRole.builder()
                .user(user)
                .role(role)
                .startData(LocalDateTime.now())
                .build());

    }

    @Override
    public void removeRoleToUser(String login, String technicalNameRole) throws RoleAssociateExption {
        try {
            List<UserRole> userRoles = userRoleRepository.findByLogin(login);
            UserRole userRole = userRoles.stream().filter(
                    actualUserRole -> actualUserRole.getRole().getTechnicalName().equals(technicalNameRole)
            ).findFirst().orElseThrow(RoleAssociateExption::new);

            userRole.setEndData(LocalDateTime.now());
            userRoleRepository.save(userRole);
        }catch (EmptyResultDataAccessException ex){
            throw new RoleAssociateExption();
        }
    }

    @Override
    public List<UserRole> findRolesByLogin(String login) throws RoleAssociateExption {
        List<UserRole> roles = null;
        try {
            roles = userRoleRepository.findByLogin(login);
        }catch (EmptyResultDataAccessException ex){
            throw new RoleAssociateExption();
        }

        return roles;
    }


}
