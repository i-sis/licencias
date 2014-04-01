package isis.licencias.controller;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import isis.licencias.model.UsuarioLicenciado;;


/**
 * Provee funcionalidades para la manipulación de Members usando el contexto de persistencia desde {@link Resources}.
 */
@Stateful
public class UsuarioLicenciadoDAOImplementacion implements UsuarioLicenciadoDAO {

    @Inject
    private EntityManager em;

    @Override
    public void createUsuario (UsuarioLicenciado usuario) {
    	System.out.println("VOY A INTENTAR PERSISTIR");
    	em.persist(usuario);
    }
}