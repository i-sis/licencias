package isis.licencias.controller;

import javax.ejb.Local;
import isis.licencias.model.UsuarioLicenciado;;

/**
 * Operaciones básicas para la manipulación de UsuarioLicenciado
 */
@Local
public interface UsuarioLicenciadoDAO  {
	public UsuarioLicenciado getUsuarioPorDNI(String dni);
	void createUsuario(UsuarioLicenciado usuario);
}