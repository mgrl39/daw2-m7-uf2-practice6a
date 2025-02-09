<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Date"%>
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
	<h1>Información de su Sesión</h1>
	<table border="1">
		<tr>
			<td>Atributo</td>
			<td>Valor</td>
		</tr>
		<tr>
			<td>Identificador</td>
			<td><%=session.getId()%></td>
		</tr>
		<tr>
			<td>Fecha/hora creación</td>
			<td><%=new Date(session.getCreationTime())%></td>
		</tr>
		<tr>
			<td>Hora último acceso</td>
			<td><%=new Date(session.getLastAccessedTime())%></td>
		</tr>
		<tr>
			<td>Número previo de accesos</td>
			<td><%=session.getAttribute("contadorAccessos") != null ? session.getAttribute("contadorAccessos") : 0%></td>
		</tr>
		<tr>
			<td>Usuario</td>
			<td><%=session.getAttribute("usuario") != null ? session.getAttribute("usuario") : "No validado"%></td>
		</tr>
		<tr>
			<td>Número de usuarios conectados</td>
			<td><%=application.getAttribute("usuariosConectados") != null ? application.getAttribute("usuariosConectados") : 0%></td>
		</tr>
		<tr>
			<td>Número de usuarios validados</td>
			<td><%=application.getAttribute("usuariosValidados") != null ? application.getAttribute("usuariosValidados") : 0%></td>
		</tr>
	</table>
	<a href="index.html">Ir a la pantalla inicial</a>
</body>
</html>
