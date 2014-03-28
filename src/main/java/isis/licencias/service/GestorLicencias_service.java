package isis.licencias.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;



@Path ("/")
public class GestorLicencias_service {
	
	@POST
    @Produces("application/octet-stream")
	@ValidateRequest
    public Response getLicencia(@FormParam("CN")
    							@NotNull
    							@Pattern (regexp = "[A-Za-z]*", message = "debe contener solo letritas y espacios")
    							String CN,
			  					@FormParam("dni") String dni,
			  					@FormParam("title") String title, 
			  					@FormParam("OU") String OU,
			  					@FormParam("O") String O,
			  					@FormParam("email") String email,
			  					@FormParam("ST") String ST,
			  					@FormParam("C") String C)    {

		Response response = Response.ok("HOLA NUEVO MUNDO").build();
        return response;
	}
}
