package isis.licencias.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
public class UsuarioLicenciado implements Serializable {

    @Id
    @GeneratedValue
    private Long id;


    @NotNull
    @Size (min = 1, max = 50)
    private String name;
    
    //@Size (min = 8, max = 12, message = "Debe ser un n�mero de entre 8 y 12 d�gitos")
    //@Digits (fraction = 0, integer = 12, message = "Debe ser un n�mero de entre 8 y 12 d�gitos")
    @Column (name="dni")
    private String dni;
    
    //@Size (min = 1, max = 40)
    //@Pattern (regexp = "[A-Za-z ]*", message = "Debe contener s�lo letras y espacios")
    private String title;
    
    //@Size(min = 1, max = 40)
    //@Pattern(regexp="[A-Za-z0-9 ]*", message = "Debe contener letras o n�meros y espacios")
    private String ou;
    
    @NotNull
    //@Size (min = 1, max = 40)
    //@Pattern (regexp = "[A-Za-z0-9 ]*", message = "Debe contener letras o n�meros y espacios")
    private String organization;

    @NotNull
    @NotEmpty
    @Email (message= "Debe colocar una direcci�n de email bien formada")
    private String email;

    //@Size (min = 1, max = 20)
    //@Pattern (regexp = "[A-Za-z ]*", message = "Debe contener s�lo letras o espacios")
    private String state;
    
    @Size (min = 1, max = 2, message = "Debe contener s�lo dos letras may�sculas")
    @Pattern (regexp = "[A-Z]*", message = "Debe contener s�lo dos letras may�sculas")
    private String country;

    
    /* Tipos de licencia 1 - DEMO  2 - LITE 3- FULL*/
    @NotNull
    private Integer tipo_licencia;

        
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
}