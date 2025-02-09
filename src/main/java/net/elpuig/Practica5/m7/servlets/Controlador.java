package net.elpuig.Practica5.m7.servlets;

import net.elpuig.Practica5.m7.enums.Protocol;
import net.elpuig.Practica5.m7.servlets.Login;
import net.elpuig.Practica5.m7.beans.Alumno;
import net.elpuig.Practica5.m7.beans.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

// 25 de novembre de 2024
@WebServlet("/AlumnoServlet")
public class Controlador extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		incrementarContadorSesion(session);

		boolean condition;

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String order = request.getParameter("order");
		if (order != null)
			order = order.trim();
		System.out.printf("Valor recibido de 'order': [ %s ]%n", order);
		condition = (!"Ejecutar".equalsIgnoreCase(order) && !"info".equalsIgnoreCase(order))
				&& !"desconectar".equalsIgnoreCase(order);
		if (order == null || order.isEmpty() || condition) {
			out.println("INVALID VALUE 2");
			return;
		}
		if ("info".equalsIgnoreCase(order)) {
			try {
				request.getRequestDispatcher("/infosesion.jsp").forward(request, response);
			} catch (Exception e) {
				out.println("Error al redirigir: " + e.getMessage());
			}
			return;
		} else if ("desconectar".equalsIgnoreCase(order)) {
			session.invalidate();
			try {
				request.getRequestDispatcher("/desconectado.jsp").forward(request, response);
			} catch (Exception e) {
				out.println("Error al redirigir: " + e.getMessage());
			}
		}

		String sql = request.getParameter("sql");
		if (isNullOrEmpty(sql)) {
			out.println("<p style=\"color: red\">Error: No se proporcionó ninguna consulta SQL</p>");
			return;
		}

		try {
			List<Map<String, String>> data = Alumno.executeQuery(sql);

			if ("true".equals(request.getParameter("jstl"))) {
				request.setAttribute("data", data);
				request.getRequestDispatcher("index.jsp").forward(request, response);
			} else
				printSql(request, response);
		} catch (RuntimeException e) {
			out.println("<p style=\"color: red\">Error al ejecutar la consulta: " + e.getMessage() + "</p>");
		}
	}

	private void printSql(HttpServletRequest request, HttpServletResponse response) throws IOException {
		out = response.getWriter();
		String sql = request.getParameter("sql");
		if (isNullOrEmpty(sql)) {
			out.println(webFormatter(
					"<p style=\"color: #FF0000\">Error: <strong style=\"color: #FF0000\">No se proporcionó ninguna consulta SQL</strong></p>",
					Protocol.GET));
			return;
		}
		try {
			List<Map<String, String>> data = Alumno.executeQuery(sql);
			String htmlTable = "";
			if (data.isEmpty()) {
				htmlTable += "<p style=\"color:#00007e\">No se encontraron resultados.</p>\n";
				out.println(webFormatter(htmlTable, Protocol.GET));
				return;
			}
			htmlTable += "<table style=\"color:#00007e\" border='1'><tr>";
			for (String columnName : data.get(0).keySet()) {
				htmlTable += "<th>" + columnName + "</th>";
			}
			System.out.printf(htmlTable + '\n');
			htmlTable += "</tr>";
			for (Map<String, String> row : data) {
				htmlTable += "<tr>";
				for (String value : row.values()) {
					htmlTable += "<td>" + value + "</td>";
					System.out.printf("testing " + value + "\n");
				}
				htmlTable += "</tr>";
			}

			htmlTable += "</table>";
			System.out.printf(htmlTable + '\n');
			out.println(webFormatter(htmlTable, Protocol.GET));
		} catch (RuntimeException e) {
			out.println(webFormatter(
					"<p style=\"color:#00007e\">Error al ejecutar la consulta: <strong style=\"color: #FF0000\">"
							+ e.getMessage() + "</strong></p>",
					Protocol.GET));
		}
	}

	private static String webFormatter(String msg, Protocol p) {
		String style;
		style = "<html><body style=\"background-color:#ffff9d\"><center><h1 style=\"color:#00007e\">";
		style += p == Protocol.GET ? "Usa JDBC para recuperar registros de una tabla"
				: "Usa JDBC para grabar un registro en una tabla";
		style += "</h1></center><hr style=\"color:#00007e\">";
		style += "<p style=\"color:#00007e\">" + msg + "</p></body></html>";
		return (style);
	}

	private boolean isNullOrEmpty(String value) {
		return (value == null || value.trim().isEmpty());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession(true);
		out = response.getWriter();

		if (request.getParameter("order").equals("alta")) {
			if (session.getAttribute("usuario") != null)
				postWithSession(request, response);
			else {
				session.setAttribute("sesAlumnoID", request.getParameter("id"));
				session.setAttribute("sesAlumnoNombre", request.getParameter("nombre"));
				session.setAttribute("sesAlumnoCurso", request.getParameter("curso"));
				request.getRequestDispatcher("/acceso.jsp").forward(request, response);
			}
		} else if (request.getParameter("order").equals("validar")) {
			String user = validarUsuario(request.getParameter("txtUsuario"), request.getParameter("txtContrasenya"));
			if (user == null) {
				request.setAttribute("error", "Usuario o contraseña incorrecta");
				request.getRequestDispatcher("/acceso.jsp").forward(request, response);
			} else {
				Usuario usuario = new Usuario(user);
				session.setAttribute("usuario", usuario);
				String sesAlumnoID = (String) session.getAttribute("sesAlumnoID");
				String sesAlumnoNombre = (String) session.getAttribute("sesAlumnoNombre");
				String sesAlumnoCurso = (String) session.getAttribute("sesAlumnoCurso");
				if (sesAlumnoID != null && sesAlumnoNombre != null && sesAlumnoCurso != null) {
					Alumno alumno = new Alumno(Integer.parseInt(sesAlumnoID), sesAlumnoNombre, sesAlumnoCurso);
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println("Alumno añadido con éxito. Filas afectadas: 1");
					out.println("<br><a href='index.html'>Ir a la pantalla inicial</a>");

					// Limpiar los datos de la sesión después del alta
					session.removeAttribute("sesAlumnoID");
					session.removeAttribute("sesAlumnoNombre");
					session.removeAttribute("sesAlumnoCurso");
				} else {
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println("Error: No se encontraron datos del alumno en la sesión.");
				}
			}
		}

	}

	private void postWithSession(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idStr = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String curso = request.getParameter("curso");
		try {
			if ((isNullOrEmpty(idStr) || isNullOrEmpty(curso)) || isNullOrEmpty(nombre)) {
				out.println(webFormatter("Todos los campos son obligatorios", Protocol.POST));
				return;
			}
			Alumno nuevoAlumno = new Alumno(Integer.parseInt(idStr), curso, nombre);
			out.println(webFormatter(nuevoAlumno.save() ? "Alumno añadido con éxito" : "Error al añadir el alumno",
					Protocol.POST));
		} catch (NumberFormatException e) {
			out.println(webFormatter("Error: El ID debe ser un número válido", Protocol.POST));
		}
	}

	/*
	 * Metodo para llevar el control del numero de veces que accede al servlet un
	 * usuario
	 */
	private void incrementarContadorSesion(HttpSession sesion) {

		// Variable para guardar las veces que accede el usuario
		Integer contadorAccesos = new Integer(0);

		// Si ya existe la sesion...
		if (!sesion.isNew()) {
			// Recuperar el contador
			Integer contadorActual = (Integer) sesion.getAttribute("contadorAccesos");
			// Incrementarlo
			if (contadorActual != null) {
				contadorAccesos = new Integer(contadorActual.intValue() + 1);
			}
		}
		// Guardar el nuevo valor del contador
		sesion.setAttribute("contadorAccesos", contadorAccesos);
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
