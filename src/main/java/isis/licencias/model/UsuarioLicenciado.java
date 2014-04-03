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
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "dni"))
public class UsuarioLicenciado implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Pattern (regexp = "[A-Za-z ]*]", message = "debe contener solo letras y espacios")
    @Size(min = 1, max = 25)
    @NotNull
    private String name;
    
    @NotNull
    @Size (min = 8, max = 12, message = "Debe ser un número de entre 8 y 12 dígitos")
    @Digits (fraction = 0, integer = 12, message = "Debe ser un número de entre 8 y 12 dígitos")
    @Column (name="dni")
    private String dni;
    
    @NotNull
    @Size (min = 1, max = 25)
    @Pattern (regexp = "[A-Za-z ]*", message = "Debe contener sólo letras y espacios")
    private String title;
    
    @NotNull
    @Size(min = 1, max = 40)
    @Pattern(regexp="[A-Za-z0-9 ]*", message = "Debe contener letras o números y espacios")
    private String ou;
    
    @NotNull
    @Size (min = 1, max = 40)
    @Pattern (regexp = "[A-Za-z0-9 ]*", message = "Debe contener letras o números y espacios")
    private String organization;

    @NotNull
    @NotEmpty
    @Email (message= "Debe colocar una dirección de email bien formada")
    private String email;

    @NotNull
    @Size (min = 1, max = 20)
    @Pattern (regexp = "[A-Za-z ]*", message = "Debe contener sólo letras o espacios")
    private String state;
    
    @NotNull
    @Size (min = 1, max = 2, message = "Debe contener sólo dos letras mayúsculas")
    @Pattern (regexp = "[A-Z]*", message = "Debe contener sólo dos letras mayúsculas")
    private String country;

       
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
}