package isis.licencias.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import isis.licencias.controller.UsuarioLicenciadoDAO;
import isis.licencias.model.UsuarioLicenciado;

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
	private int tipo_licencia;
	
	@GET
    @Path("{dni}")
	@Produces("text/*")
    public String getUsuarioLicenciado(@PathParam("dni") String dni) {
		if (usuario.getUsuarioPorDNI(dni)!=null){
			return usuario.getUsuarioPorDNI(dni).getName();
		}
		else {
			return "No existe";
		}
    }
	
	
	@POST
	@Path("tipo/DEMO")
    @Produces("application/octet-stream")
	@ValidateRequest
    public Response getLicencia_DEMO(@FormParam("CN")
    								 @NotNull
    								 String CN,
	
									 @FormParam("dni") 
    								 @Pattern(regexp ="((DU|CUIT|CUIL)\\s)?\\d*", message = "debe ajustarse al formato numérico o a la cadena DU 8 dígitos o a la cadena CUIT o CUIL y 13 dígitos")
									 String dni,
	
									 @FormParam("title") String title, 
									 @FormParam("OU") String OU,
		
									 @FormParam("O") 
									 String O,
		
									 @FormParam("email")
									 @NotNull
									 @NotEmpty
									 @Email (message= "Debe colocar una dirección de email bien formada")
									 String email,
	
									 @FormParam("ST") String ST,
		
									 @FormParam("C")
									 @Pattern(regexp = "[A-Z][A-Z]", message = "debe contener un código de país válido")
									 String C) {

		ResponseBuilder response = null;
		
		try {
			newUsuario = new UsuarioLicenciado();
			newUsuario.setName(CN);
			newUsuario.setDni(dni);
			newUsuario.setTitle(title);
			newUsuario.setOu(OU);
			newUsuario.setOrganization(O);
			newUsuario.setEmail(email);
			newUsuario.setState(ST);
			newUsuario.setCountry(C);
			tipo_licencia = 1; //1 - Licencia tipo DEMO
			newUsuario.setTipo_Licencia(tipo_licencia); 
			newUsuario.setFecha(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
			
			usuario.createUsuario(newUsuario);
			
			/* Creo archivo temporal con la licencia */ 
			File licencia_file = crearLicencia("Firmador Digital v2.0 - DEMO");
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
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		}
		
		catch (ConstraintViolationException ex) {
			ex.printStackTrace();
			//Handle bean validation issues
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		} 
		catch (ValidationException ex) {
			ex.printStackTrace();
			//Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email","Email taken");
			response = Response.status(Response.Status.CONFLICT).entity(responseObj);
			return response.build();
		}
		catch (PersistenceException ex){
			ex.printStackTrace();
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		}
		catch (Exception ex){
			ex.printStackTrace();
			response = Response.status(Status.BAD_REQUEST).entity(ex.getMessage()); 
			return response.build();
		} 
    }
	
	@POST
	@Path("tipo/Comercial")
    @Produces("application/octet-stream")
	@ValidateRequest
    public Response getLicencia_FULL(@FormParam("CN")
    							@NotNull
    							String CN,
    							
			  					@FormParam("dni") 
    							@NotNull
    							@Pattern(regexp ="((DU|CUIT|CUIL)\\s)?\\d*", message = "debe ajustarse al formato numérico o a la cadena DU 8 dígitos o a la cadena CUIL o CUIT Y 11 dígitos")
    							String dni,
    							
			  					@FormParam("title") String title, 
			  					@FormParam("OU") String OU,
			  					
			  					@FormParam("O") 
    							@Pattern(regexp = "[A-Za-zñáéíóúÑ.&-_0-9\\s]{2,50}$", message = "debe contener sólo letras y espacios")
    							String O,
			  					
    							@FormParam("email")
    							@NotNull
    							@NotEmpty
    							@Email (message= "Debe colocar una dirección de email bien formada")
    							String email,
    							
			  					@FormParam("ST") String ST,
			  					
			  					@FormParam("C")
    							@Pattern(regexp = "[A-Z][A-Z]", message = "debe contener un código de país válido")
    							String C,
    							
    							@FormParam("tipo")
    							@NotNull
    							@NotEmpty
    							String tipo) {

		ResponseBuilder response = null;
		
		try {
			System.out.println("AHORA SI");
			newUsuario = new UsuarioLicenciado();
			newUsuario.setName(CN);
			newUsuario.setDni(dni);
			newUsuario.setTitle(title);
			newUsuario.setOu(OU);
			newUsuario.setOrganization(O);
			newUsuario.setEmail(email);
			newUsuario.setState(ST);
			newUsuario.setCountry(C);
			
			/* Tipos Licencia 2-Base_anual, 3-Base_perpetu, 4-Full_anual, 5-Full_perpetua */
			System.out.println("ESTOY");
			this.tipo_licencia = Integer.parseInt("2");
			newUsuario.setTipo_Licencia(new Integer(tipo_licencia));
			newUsuario.setFecha(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
			
			usuario.createUsuario(newUsuario);
			
			/* Creo archivo temporal con la licencia */ 
			File licencia_file = crearLicencia("Firmador Digital v2.0 - FULL");
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
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		}
		
		catch (ConstraintViolationException ex) {
			ex.printStackTrace();
			//Handle bean validation issues
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		} 
		catch (ValidationException ex) {
			ex.printStackTrace();
			//Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email","Email taken");
			response = Response.status(Response.Status.CONFLICT).entity(responseObj);
			return response.build();
		}
		catch (PersistenceException ex){
			ex.printStackTrace();
			response = Response.status(Status.BAD_REQUEST);
			return response.build();
		}
		catch (Exception ex){
			ex.printStackTrace();
			response = Response.status(Status.BAD_REQUEST).entity(ex.getMessage()); 
			return response.build();
		} 
    }

	
	private File crearLicencia(final String version) {
		       
		/*Implemento la interface KeyStoreParam*/
		privateKeyStoreParam = new KeyStoreParam() {
			public InputStream getStream() throws IOException {
		        final String resourceName = "/privateKeys.store";
		         final InputStream in = getClass().getResourceAsStream(resourceName);
		         if (in == null) throw new FileNotFoundException(resourceName);
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
		        return version;
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
		 } 
		 catch (Exception f) {
		     f.printStackTrace();
		 }
		 
		 return file;
	}
		   
	public LicenseContent createLicenseContent() {
		 LicenseContent result = new LicenseContent();
		 X500Principal holder = new X500Principal("CN=" + newUsuario.getName() + ", UID="+newUsuario.getDni() + ", O="+newUsuario.getOrganization());
		 result.setHolder(holder);
		 X500Principal issuer = new X500Principal("CN=ISIS Consultores, L=Mendoza, O=ISIS Consultores,"
		         								+" C=Argentina,"
		         								+" DC=AR");
		 result.setIssuer(issuer);
		 result.setConsumerAmount(1);
		 result.setConsumerType("User");
		 result.setInfo("Limita el número de usuarios/firmantes que pueden utilizar esta aplicación");
		 Date now = new Date();
		 result.setIssued(now);
		 
		  /* Evaluo según el tipo de licencia, si debo fijar una licencia a término */
		 if (this.tipo_licencia == 2 || this.tipo_licencia == 4){
			 /* si la licencia es de tipo Base_anual o Full_anual */
			 now.setYear(now.getYear() + 1);
			 result.setNotAfter(now);
		 }
		 System.out.println("SUBJECT: " + licenseParam.getSubject() );
		 result.setSubject(licenseParam.getSubject());
		 return result;
	}	   
}