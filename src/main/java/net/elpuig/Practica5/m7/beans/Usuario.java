package net.elpuig.Practica5.m7.beans;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

public class Usuario implements HttpSessionBindingListener {

	private String nombre;

	public Usuario(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	// Sobrescribir toString para que no aparezca como "Usuario@xxxx"
	@Override
	public String toString() {
		return nombre;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		System.out.println("Usuario añadido a la sesión. ID: " + event.getSession().getId());
		ServletContext context = event.getSession().getServletContext();
		synchronized (context) {
			Integer usuariosValidados = (Integer) context.getAttribute("usuariosValidados");
			context.setAttribute("usuariosValidados", (usuariosValidados == null) ? 1 : ++usuariosValidados);
		}
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		System.out.println("Usuario eliminado de la sesión. ID: " + event.getSession().getId());
		ServletContext context = event.getSession().getServletContext();
		synchronized (context) {
			Integer usuariosValidados = (Integer) context.getAttribute("usuariosValidados");
			context.setAttribute("usuariosValidados",
					(usuariosValidados == null || usuariosValidados <= 0) ? 0 : --usuariosValidados);
		}
	}
}