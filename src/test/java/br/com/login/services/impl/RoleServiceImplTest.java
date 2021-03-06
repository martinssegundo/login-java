package br.com.login.services.impl;

import br.com.login.entities.Role;
import br.com.login.exceptions.CreateRoleException;
import br.com.login.exceptions.FindRoleException;
import br.com.login.repository.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Before
    public void config(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createRoleWithSucess() throws CreateRoleException {
        Role newRole = Role.builder()
                .name("Admin")
                .technicalName("Admin")
                .startDate(LocalDateTime.now())
                .build();

        Role dbRole = Role.builder()
                .id(1L)
                .name("Admin")
                .technicalName("Admin")
                .startDate(LocalDateTime.now())
                .build();

        when(roleRepository.save(any(Role.class)))
                .thenReturn(dbRole);

        Role resposneRole = roleService.saveRole(newRole);

        assertNotNull(resposneRole.getId());
    }

    @Test(expected = CreateRoleException.class)
    public void createRoleWithErroNameLessFiveCaracters() throws CreateRoleException{
        Role newRole = Role.builder()
                .name("Admi")
                .technicalName("Admin")
                .startDate(LocalDateTime.now())
                .build();


        Role resposneRole = roleService.saveRole(newRole);
    }

    @Test(expected = CreateRoleException.class)
    public void createRoleWithErroNameNull() throws CreateRoleException{
        Role newRole = Role.builder()
                .name(null)
                .technicalName("Admin")
                .startDate(LocalDateTime.now())
                .build();


        Role resposneRole = roleService.saveRole(newRole);
    }


    @Test(expected = CreateRoleException.class)
    public void createRoleWithErroTecnicalLessFiveCaracters() throws CreateRoleException{
        Role newRole = Role.builder()
                .name("Admim")
                .technicalName("Admi")
                .startDate(LocalDateTime.now())
                .build();


        Role resposneRole = roleService.saveRole(newRole);
    }

    @Test(expected = CreateRoleException.class)
    public void createRoleWithErroTecnicalNameNull() throws CreateRoleException{
        Role newRole = Role.builder()
                .name("Admim")
                .technicalName(null)
                .startDate(LocalDateTime.now())
                .build();


        Role resposneRole = roleService.saveRole(newRole);
    }


    @Test(expected = CreateRoleException.class)
    public void createRoleWithErroStartDateNull() throws CreateRoleException{
        Role newRole = Role.builder()
                .name("Admim")
                .technicalName("Admim")
                .startDate(null)
                .build();

        Role resposneRole = roleService.saveRole(newRole);
    }

    @Test
    public void findRoleByTechnicalNameSucess() throws FindRoleException {
        Role role = Role.builder()
                .id(1L)
                .name("Admim")
                .technicalName("Admim")
                .startDate(LocalDateTime.now())
                .build();
        when(roleRepository.findByTechnicalName(anyString()))
                .thenReturn(role);

        Role resposneRole = roleService.findBytechnicalName("ADMIN");

        assertNotNull(resposneRole.getId());
    }

    @Test(expected = FindRoleException.class)
    public void findRoleByTechnicalNameErro() throws FindRoleException {
        when(roleRepository.findByTechnicalName(anyString()))
                .thenThrow(EmptyResultDataAccessException.class);

        Role resposneRole = roleService.findBytechnicalName("XPTO");
    }
}
