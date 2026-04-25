<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro - BancoWeb</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #eef2f6;
            height: 100vh;
            display: flex;
            align-items: center;
        }
        .reg-card {
            background: white;
            border-radius: 30px;
            padding: 40px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.05);
        }
        .form-control {
            border: none;
            background-color: #f4f7fa;
            border-radius: 12px;
            padding: 12px 20px;
        }
        .form-control:focus {
            background-color: #ebf0f5;
            box-shadow: none;
        }
        .btn-reg {
            background-color: #1e1e24;
            color: white;
            border-radius: 12px;
            padding: 12px;
            font-weight: 600;
            width: 100%;
            transition: 0.3s;
        }
        .btn-reg:hover {
            background-color: #33333c;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5 col-lg-4 reg-card">
                <h3 class="fw-bold mb-2">Abrir Nueva Cuenta</h3>
                <p class="text-muted mb-4 small">Completa tus datos para crear tu cuenta</p>

                <form action="registro" method="POST">
                    <div class="mb-3">
                        <label class="form-label small fw-bold text-muted text-uppercase">Nombre</label>
                        <input type="text" name="nombre" class="form-control" placeholder="Tu nombre" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold text-muted text-uppercase">Apellido</label>
                        <input type="text" name="apellido" class="form-control" placeholder="Tu apellido" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold text-muted text-uppercase">Documento</label>
                        <input type="text" name="documento" class="form-control" placeholder="Número de documento" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold text-muted text-uppercase">Email</label>
                        <input type="email" name="email" class="form-control" placeholder="correo@ejemplo.com" required>
                    </div>
                    <div class="mb-4">
                        <label class="form-label small fw-bold text-muted text-uppercase">Contraseña</label>
                        <input type="password" name="password" class="form-control" placeholder="Mínimo 6 caracteres" required>
                    </div>

                    <button type="submit" class="btn btn-reg mb-3">Registrarme y Crear Cuenta</button>
                </form>

                <p class="text-center small mb-0">
                    ¿Ya eres miembro?
                    <a href="login.jsp" class="text-dark fw-bold text-decoration-none">Inicia sesión</a>
                </p>
            </div>
        </div>
    </div>
</body>
</html>