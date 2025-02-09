package net.elpuig.Practica5.m7.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.elpuig.Practica5.m7.beans.Usuario;

import java.io.IOException;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String usuarioIntro = request.getParameter("txtUsuario");

		if ("daniel".equals(usuarioIntro) && "colomer".equals(request.getParameter("txtContrasenya"))) {
			request.getSession().setAttribute("usuario", new Usuario(usuarioIntro));
			response.sendRedirect("infosesion.jsp");
		} else {
			request.setAttribute("error", "Usuario o contraseña incorrecta");
			request.getRequestDispatcher("acceso.jsp").forward(request, response);
		}
	}

	// Nos devuelve el nombre del usuario si la pareja
	// nombre-passw es correcta
	// Tendriamos que comparar con valores de la BD!
	private String validarUsuario(String usuarioIntro, String passwIntro) {
		String retorno = null;

		if (usuarioIntro.equals("daniel") && passwIntro.equals("daniel")) {
			retorno = usuarioIntro;
		}
		return retorno;
	}

}
