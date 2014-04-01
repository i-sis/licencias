package isis.licencias.util;


import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans. As it is a stateful bean, it
 * can produce extended persistence contexts.
 *
 * Example injection on a managed bean field:
 *
 * &#064;Inject private EntityManager em;
 *
 * @author Pete Muir
 * @author Lukas Fryc
 *
 */

@Stateful
@RequestScoped
public class Resources {
	

   @PersistenceContext
   private EntityManager em;
   
   
   @Produces
   public EntityManager getEm() {
       return em;
   }
   
   
   @Produces
   public Logger produceLog(InjectionPoint injectionPoint) {
      return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
   }
}