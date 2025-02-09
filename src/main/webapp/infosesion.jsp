<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>Información de Sesión</title>
<style>
body {
	background-color: #934993; /* Morado oscuro */
	color: white;
	font-family: Arial, sans-serif;
	text-align: center;
}

h1 {
	color: #ffff99; /* Dorado */
}

table {
	margin: auto;
	border-collapse: collapse;
	width: 60%;
	background-color: #ffff99; /* Amarillo claro */
	color: black;
}

th, td {
	border: 1px solid black;
	padding: 10px;
	text-align: left;
}

th {
	background-color: #FFD700; /* Dorado */
}

a {
	color: #ADD8E6; /* Azul claro */
	text-decoration: none;
	font-weight: bold;
	display: block;
	margin-top: 20px;
}

a:hover {
	text-decoration: underline;
}
</style>
</head>
<body>
	<h1>Información de Sesión</h1>
	<table border="1">
		<tr>
			<td>Identificador de sesión:</td>
			<td>${sessionScope.idSesion}</td>
		</tr>
		<tr>
			<td>Fecha de creación:</td>
			<td>${sessionScope.fechaCreacion}</td>
		</tr>
		<tr>
			<td>Último acceso:</td>
			<td>${sessionScope.ultimoAcceso}</td>
		</tr>
		<tr>
			<td>Número de accesos:</td>
			<td>${sessionScope.contadorAccessos}</td>
		</tr>
		<tr>
			<td>Usuario:</td>
			<td>${not empty sessionScope.usuario ? sessionScope.usuario.nombre : "No validado"}</td>
		</tr>
		<tr>
			<td>Usuarios conectados:</td>
			<td>${applicationScope.usuariosConectados}</td>
		</tr>
		<tr>
			<td>Usuarios validados:</td>
			<td>${applicationScope.usuariosValidados}</td>
		</tr>
	</table>
	<a href="index.html">Ir a la pantalla inicial</a>
</body>
</html>