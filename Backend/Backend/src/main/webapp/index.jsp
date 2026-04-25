<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BancoWeb | Bienvenida</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #eef2f6;
            height: 100vh;
            display: flex;
            align-items: center;
        }
        .hero-card {
            background: white;
            border-radius: 30px;
            padding: 60px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.05);
        }
        .btn-dark {
            border-radius: 15px;
            padding: 12px 30px;
            font-weight: 600;
        }
        .logo-icon {
            background: #1e1e24;
            color: white;
            width: 60px;
            height: 60px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 30px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center text-center">
            <div class="col-md-8 col-lg-6 hero-card">
                <div class="d-flex justify-content-center">
                    <div class="logo-icon">🏦</div>
                </div>
                <h1 class="fw-bold display-5 mb-3">Bienvenido a BancoWeb</h1>
                <p class="text-muted fs-5 mb-5">Gestiona tus finanzas con la tecnología del futuro. Simple, rápido y seguro</p>
                <div class="d-grid gap-3 d-sm-flex justify-content-sm-center">
                    <a href="registro.jsp" class="btn btn-dark btn-lg">Abrir Cuenta</a>
                    <a href="login.jsp" class="btn btn-outline-dark btn-lg">Iniciar Sesión</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>