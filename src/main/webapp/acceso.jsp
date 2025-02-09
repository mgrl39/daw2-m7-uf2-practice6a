<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>Acceso</title>
</head>
<body>
	<h1>Acceso</h1>
	<c:if test="${not empty error}">
		<p style="color: red;">${error}</p>
	</c:if>
	<form action=AlumnoServlet method="post">
		<input type="hidden" name="order" value="validar">
		Usuario: <input type="text" name="txtUsuario" required><br>	
		Contraseña: <input type="password" name="txtContrasenya" required><br>
		<input type="submit" value="validar">
	</form>
	<a href="index.html">Volver</a>
</body>
</html>	