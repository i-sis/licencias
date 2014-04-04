package isis.licencias.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.security.auth.x500.X500Principal;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import isis.licencias.controller.UsuarioLicenciadoDAO;
import isis.licencias.model.UsuarioLicenciado;
import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;



@Path ("/")
public class GestorLicencias_service {
	
	
	@Inject
	private Logger log;
	
	@Inject
	private UsuarioLicenciadoDAO usuario;
	
	private UsuarioLicenciado newUsuario;
	private KeyStoreParam privateKeyStoreParam;
	private CipherParam cipherParam;
	private LicenseParam licenseParam; 

	
	@POST
    @Produces("application/octet-stream")
	@ValidateRequest
    public Response getLicencia(@FormParam("CN") String CN,
			  					@FormParam("dni") String dni,
			  					@FormParam("title") String title, 
			  					@FormParam("OU") String OU,
			  					@FormParam("O") String O,
			  					@FormParam("email") String email,
			  					@FormParam("ST") String ST,
			  					@FormParam("C") String C)    {

		ResponseBuilder response = null;
		newUsuario = new UsuarioLicenciado();
		newUsuario.setName(CN);
		newUsuario.setDni(dni);
		newUsuario.setTitle(title);
		newUsuario.setOu(OU);
		newUsuario.setOrganization(O);
		newUsuario.setEmail(email);
		newUsuario.setState(ST);
		newUsuario.setCountry(C);
		
		try {
			usuario.createUsuario(newUsuario);
		
			System.out.println("PASE POR AQUI");	
			/* Creo archivo temporal con la licencia */ 
			File licencia_file = crearLicencia();
			response = Response.ok((Object) licencia_file);
			
			/* Devuelvo un arreglo de bytes con el contenido del archivo Licencia al cliente */ 
			if (licencia_file != null){
		        response = Response.ok((Object) licencia_file);
		        return response.build();
		 	}
			else {
	            response = Response.status(Status.BAD_REQUEST);
	            return response.build();
			}
		}
		catch (RollbackException ex){
			ex.printStackTrace();
			System.out.println("ENCONTRE UNA ROLLBACKEXCEPTIONs");
			System.out.println(ex.getMessage());
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		}
		
		catch (ConstraintViolationException ex) {
			System.out.println("NO, FUE POR AQUI");
			ex.printStackTrace();
			//Handle bean validation issues
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		} 
		catch (ValidationException ex) {
			System.out.println("ENTRE POR AQUI");
			ex.printStackTrace();
			//Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email","Email taken");
			response = Response.status(Response.Status.CONFLICT).entity(responseObj);
			return response.build();
		}
		catch (PersistenceException ex){
			System.out.println("ENCONTRO UNA PERSISTENCE EXCEPTION");
			ex.printStackTrace();
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		}
		catch (Exception ex){
			System.out.println("ENCONTRE UNA EXCEPCION	");
			ex.printStackTrace();
			response = Response.status(Status.BAD_REQUEST).entity(ex.getMessage()); 
			return response.build();
		} 
    }
	
	
	private File crearLicencia() {
		       
		       /*Implemento la interface KeyStoreParam*/
		   	   privateKeyStoreParam = new KeyStoreParam() {
		           public InputStream getStream() throws IOException {
		        	   final String resourceName = "/privateKeys.store";
		               final InputStream in = getClass().getResourceAsStream(resourceName);
		               
		               if (in == null)
		                   throw new FileNotFoundException(resourceName);
		               return in;
		       }
		       
		       public String getAlias() {
		               return "privatekey";
		       }
		       
		       public String getStorePwd() {
		               return "1q2w3e4r";
		       }
		       public String getKeyPwd() {
		               return "1q2w3e4r";
		       }
		   };


		   /* Implemento la interface CipherParam */
		   cipherParam = new CipherParam() {
		       public String getKeyPwd() {
		           return "1q2w3e4r";
		       }
		   };
		       
		       
		   /* Implemento la interface LicenseParam */
		   licenseParam = new LicenseParam() {
		   		public String getSubject() {
		               return "Firmador Digital v2.0";
		           }
		        public KeyStoreParam getKeyStoreParam() {
		                return privateKeyStoreParam;
		           }
		           
		        public CipherParam getCipherParam() {
		                return cipherParam;
		           }
				@Override
				public Preferences getPreferences() {
					// TODO Auto-generated method stub
					return null;
				}
		       };    
		       
		       /* Creo el archivo con la licencia */
		       File file=null;
		       try {
		    	   file = File.createTempFile("lic", ".tmp");
		    	   LicenseManager lm = new LicenseManager(licenseParam);
		           lm.store(createLicenseContent(), file);
		       } catch (Exception f) {
		           f.printStackTrace();
		       }
		    
		       return file;
	  	   }
		   
		   public LicenseContent createLicenseContent() {
		       LicenseContent result = new LicenseContent();
		       X500Principal holder = new X500Principal("CN=" + newUsuario.getName() + "UID="+newUsuario.getDni());
		       result.setHolder(holder);
		       X500Principal issuer = new X500Principal(
		           "CN=isis Consultores, L=Mendoza, O=isis Consultores,"
		         +" C=Argentina,"
		         +" DC=AR");
		       result.setIssuer(issuer);
		       result.setConsumerAmount(1);
		       result.setConsumerType("User");
		       result.setInfo("Limita el número de usuarios que pueden utilizar esta aplicación");
		       Date now = new Date();
		       result.setIssued(now);
		       now.setYear(now.getYear() + 1);
		       result.setNotAfter(now);
		       result.setSubject(licenseParam.getSubject());
		       return result;
		   }	   
}
