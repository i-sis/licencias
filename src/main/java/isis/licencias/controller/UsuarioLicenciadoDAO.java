package isis.licencias.controller;

import javax.ejb.Local;
import isis.licencias.model.UsuarioLicenciado;;

/**
 * Operaciones b�sicas para la manipulaci�n de UsuarioLicenciado
 */
@Local
public interface UsuarioLicenciadoDAO  {
	void createUsuario(UsuarioLicenciado usuario);
}