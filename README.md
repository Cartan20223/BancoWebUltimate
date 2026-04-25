🔗 Integración Frontend - Backend

Se implementó la librería Gson en el backend para transformar las respuestas a formato JSON, permitiendo la correcta comunicación con el frontend. Anteriormente, el sistema trabajaba con texto plano, lo que limitaba la interoperabilidad con aplicaciones modernas.

Adicionalmente, se configuró un filtro CORS (Cross-Origin Resource Sharing) que actúa como puente entre el frontend y el backend, permitiendo solicitudes desde distintos orígenes (puertos diferentes). Esto es fundamental para que el cliente desarrollado en React pueda consumir los servicios del backend sin restricciones del navegador.

El sistema utiliza sesiones HTTP para gestionar la autenticación del usuario, por lo que el CORS también está configurado para permitir el envío de credenciales.

⚙️ Ejecución del Proyecto
🔹 Backend (Java + Servlets + Tomcat)
Abrir el proyecto en IntelliJ IDEA
Configurar y ejecutar el servidor Apache Tomcat
El backend estará disponible en:
http://localhost:8080
🔹 Frontend (React)
Abrir el proyecto en Visual Studio Code
Instalar dependencias:
npm install
Ejecutar la aplicación:
npm start
El frontend estará disponible en:
http://localhost:3000
🔄 Funcionalidades
Registro de usuarios
Inicio de sesión con manejo de sesión
Depósitos
Transferencias entre cuentas
Visualización de dashboard con datos en tiempo real
CRUD básico integrado entre frontend y backend
🧠 Notas Técnicas
Uso de Gson para serialización de datos
Implementación de CORS Filter para conexión entre cliente y servidor
Arquitectura en capas (Controller, Service, DAO)
Manejo de sesiones con HttpSession
