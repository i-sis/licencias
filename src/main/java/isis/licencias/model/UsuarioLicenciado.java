package isis.licencias.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@Table
public class UsuarioLicenciado implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

	@NotNull
	@Pattern(regexp = "[A-Za-zñáéíóúÑÁÉÍÓÚ\\s]{2,50}$", message = "debe contener sólo letras y espacios")
    @Size (min = 5, max = 50)
    private String name;

	@Pattern(regexp ="(DU\\s)?\\d*", message = "debe ajustarse al formato numérico o a la cadena DU 8 dígitos")
    private String dni;
    
    private String title;
    
    private String ou;
    
	@NotNull
	@Pattern(regexp = "[A-Za-zñáéíóúÑ&#?¿!_0-9\\s\\.\\-\\*]{1,255}$", message = "debe contener sólo letras y espacios")
	@Size (min = 1, max = 50)
    private String organization;

    @NotNull
    @NotEmpty
    @Email (message= "Debe colocar una dirección de email bien formada")
    private String email;

    private String state;
    
	@Pattern(regexp = "[A-Z][A-Z]", message = "debe contener un código de país válido")
    private String country;

    
    /* Tipos de licencia 1 - DEMO  2 - LITE 3- FULL*/
    @NotNull
    private Integer tipo_licencia;
    
    @NotNull
    private Date fecha;

        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public Integer getTipo_Licencia() {
        return tipo_licencia;
    }

    public void setTipo_Licencia(Integer tipo) {
        this.tipo_licencia = tipo;
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha_registroLicencia) {
    	this.fecha = fecha_registroLicencia;
    }
}