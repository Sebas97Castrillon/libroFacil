package com.desarrollo.librofacil.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo.librofacil.model.dao.UserDAO;
import com.desarrollo.librofacil.model.entity.Rol;
import com.desarrollo.librofacil.model.entity.RolUsuario;
@Service
public class UsuarioDetailService implements UserDetailsService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        RolUsuario rolUsuario = userDAO.findByNombre(username);

        if (rolUsuario == null) {
            logger.error("*** Error de autenticación, el usuario '" + username + "' no existe");
            throw new UsernameNotFoundException("*** Error de autenticación, el usuario '" + username + "' no existe");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        for (Rol rol : rolUsuario.getRoles()) {
            logger.info("Rol: " + rol.getNombre());
            roles.add(new SimpleGrantedAuthority(rol.getNombre()));
        }

        if (roles.isEmpty()) {
            logger.warn("El usuario " + rolUsuario.getNombre() + " no tiene roles asignados");
            throw new UsernameNotFoundException("El usuario " + rolUsuario.getNombre() + " no tiene roles asignados");
        }

        return new User(rolUsuario.getNombre(), rolUsuario.getClave(), rolUsuario.isActivo(), true, true, true, roles);
    }
}
