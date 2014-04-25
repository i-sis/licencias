package isis.licencias.controller;

import java.util.List;

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
    public UsuarioLicenciado getUsuarioPorDNI(String dni) {
        List<UsuarioLicenciado> result = em.createQuery("select usuario from UsuarioLicenciado usuario where usuario.dni = ?", UsuarioLicenciado.class).setParameter(1, dni).getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
    

    @Override
    public void createUsuario (UsuarioLicenciado usuario) {
    	System.out.println("ESTOY PERSISTIENDO UN USUARIO");
   		em.persist(usuario);
    }
}