<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ page import="com.sena.bancoweb.entity.Usuario" %>
<%@ page import="com.sena.bancoweb.entity.Cuenta" %>
<%@ page import="com.sena.bancoweb.entity.Transaccion" %>
<%@ page import="com.sena.bancoweb.repository.CuentaDAO" %>
<%@ page import="com.sena.bancoweb.repository.TransaccionDAO" %>
<%@ page import="java.util.List" %>

<%
    // 1. Validar sesión
    Usuario user = (Usuario) session.getAttribute("usuario");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 2. Cargar datos de la cuenta
    CuentaDAO cuentaDAO = new CuentaDAO();
    Cuenta cuenta = cuentaDAO.obtenerCuentaPorUsuario(user.getIdUsuario());
    request.setAttribute("cuenta", cuenta);

    // 3. Cargar historial
    TransaccionDAO transDAO = new TransaccionDAO();
    List<Transaccion> movimientos = transDAO.obtenerPorCuenta(cuenta.getIdCuenta());
    request.setAttribute("listaMovimientos", movimientos);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Resumen - BancoWeb</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #eef2f6; /* Fondo azul clarito de la imagen */
            color: #1e1e24;
        }

        /* Contenedor principal para el layout */
        .app-container {
            display: flex;
            min-height: 100vh;
            padding: 20px;
        }

        /* Sidebar Oscuro */
        .sidebar {
            width: 80px;
            background-color: #1e1e24;
            border-radius: 20px;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 30px 0;
            position: fixed;
            height: calc(100vh - 40px);
        }

        .sidebar-logo {
            background-color: #fdf4e3;
            color: #1e1e24;
            width: 45px;
            height: 45px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            margin-bottom: 40px;
            font-weight: bold;
        }

        .nav-icon {
            color: #8b8b93;
            font-size: 22px;
            margin-bottom: 30px;
            cursor: pointer;
            transition: 0.3s;
        }

        .nav-icon:hover, .nav-icon.active {
            color: #ffffff;
        }

        /* Contenido Principal */
        .main-content {
            margin-left: 100px; /* Deja espacio para el sidebar */
            background-color: #ffffff;
            border-radius: 30px;
            padding: 40px;
            width: calc(100% - 100px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.03);
        }

        /* Tarjetas de Diseño (Colores Pastel) */
        .card-custom {
            border-radius: 20px;
            border: none;
            padding: 25px;
        }

        .card-balance {
            background-color: #eaf4fc; /* Azul claro */
        }

        .card-asset-1 {
            background-color: #ede7f6; /* Morado claro */
        }

        .card-asset-2 {
            background-color: #e8f5e9; /* Verde claro */
        }

        /* Tipografía específica */
        .text-huge {
            font-size: 2.2rem;
            font-weight: 700;
            letter-spacing: -1px;
        }

        .btn-action {
            border-radius: 12px;
            font-weight: 600;
            padding: 10px 20px;
        }

        /* Tabla estilizada */
        .table-custom th {
            border-bottom: 1px solid #eee;
            color: #8b8b93;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 12px;
        }

        .table-custom td {
            vertical-align: middle;
            border-bottom: 1px solid #f8f9fa;
        }

        .icon-circle {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 18px;
        }
    </style>
</head>
<body>

<div class="app-container">

    <div class="sidebar shadow-lg">
        <div class="sidebar-logo">
            <i class="bi bi-bank"></i>
        </div>
        <i class="bi bi-grid-1x2-fill nav-icon active"></i>
        <i class="bi bi-wallet2 nav-icon"></i>
        <i class="bi bi-bar-chart nav-icon"></i>
        <i class="bi bi-box nav-icon"></i>
        <i class="bi bi-person nav-icon"></i>
        <div style="margin-top: auto;">
            <a href="${pageContext.request.contextPath}/logout" class="nav-icon text-danger" title="Cerrar Sesión">
                <i class="bi bi-box-arrow-left"></i>
            </a>
        </div>
    </div>

    <div class="main-content">

        <div class="d-flex justify-content-between align-items-center mb-5">
            <h2 class="fw-bold mb-0">Overview</h2>
            <div class="d-flex align-items-center gap-3">
                <button class="btn btn-light rounded-circle"><i class="bi bi-search"></i></button>
                <button class="btn btn-light rounded-circle"><i class="bi bi-bell"></i></button>
                <div class="d-flex align-items-center bg-light rounded-pill px-3 py-1 border">
                    <span class="fw-bold me-2">${usuario.nombre}</span>
                    <i class="bi bi-person-circle fs-4 text-secondary"></i>
                </div>
            </div>
        </div>

        <% if("transfer_ok".equals(request.getParameter("mensaje")) || "ok".equals(request.getParameter("mensaje"))) { %>
            <div class="alert alert-success border-0 rounded-4 shadow-sm"><i class="bi bi-check-circle-fill me-2"></i> Operación exitosa.</div>
        <% } %>
        <% if("transfer_fail".equals(request.getParameter("error")) || "transaccion".equals(request.getParameter("error"))) { %>
            <div class="alert alert-danger border-0 rounded-4 shadow-sm"><i class="bi bi-exclamation-triangle-fill me-2"></i> Hubo un problema con la operación. Verifica tus fondos.</div>
        <% } %>

        <div class="row g-4 mb-5">

            <div class="col-md-6">
                <div class="d-flex justify-content-between mb-3">
                    <h5 class="fw-bold">Portfolio</h5>
                </div>
                <div class="card card-custom card-balance h-100">
                    <p class="text-muted mb-1 fw-bold">Saldo Disponible</p>
                    <h1 class="text-huge text-dark">$ ${cuenta.saldo}</h1>
                    <div class="mt-4 pt-3 border-top border-light d-flex gap-2">
                        <button class="btn btn-dark btn-action w-50" data-bs-toggle="modal" data-bs-target="#modalDeposito">Depositar</button>
                        <button class="btn btn-outline-dark btn-action w-50" data-bs-toggle="modal" data-bs-target="#modalTransferir">Transferir</button>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="d-flex justify-content-between mb-3">
                    <h5 class="fw-bold">Tus Productos</h5>
                </div>
                <div class="row g-3">
                    <div class="col-6">
                        <div class="card card-custom card-asset-1 h-100">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div>
                                    <h4 class="fw-bold mb-0">ACTIVA</h4>
                                    <small class="text-muted">${cuenta.tipoCuenta}</small>
                                </div>
                                <i class="bi bi-three-dots-vertical text-muted"></i>
                            </div>
                            <div class="mt-3">
                                <span class="badge bg-white text-dark rounded-pill px-3 py-2 fw-bold border">
                                    <i class="bi bi-hash"></i> ${cuenta.numeroCuenta}
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="card card-custom bg-dark text-white h-100 d-flex flex-column justify-content-center">
                            <h5 class="fw-bold">BancoWeb Security</h5>
                            <p class="small text-light opacity-75">Tus transacciones están protegidas con cifrado de extremo a extremo.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h5 class="fw-bold mb-0">Últimos Movimientos</h5>
            </div>

            <table class="table table-custom table-hover">
                <thead>
                    <tr>
                        <th>Transacción</th>
                        <th>Fecha</th>
                        <th>Estado</th>
                        <th class="text-end">Monto</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Transaccion> lista = (List<Transaccion>) request.getAttribute("listaMovimientos");
                        if (lista != null && !lista.isEmpty()) {
                            for (Transaccion t : lista) {
                                boolean esIngreso = t.getTipoTransaccion().contains("Consignación") || t.getTipoTransaccion().contains("Recibida");
                                String colorIcono = esIngreso ? "bg-success" : "bg-dark";
                                String icono = esIngreso ? "bi-arrow-down-left" : "bi-arrow-up-right";
                                String colorTexto = esIngreso ? "text-success" : "text-dark";
                    %>
                        <tr>
                            <td>
                                <div class="d-flex align-items-center gap-3">
                                    <div class="icon-circle <%= colorIcono %>">
                                        <i class="bi <%= icono %>"></i>
                                    </div>
                                    <div>
                                        <h6 class="mb-0 fw-bold text-dark"><%= t.getTipoTransaccion() %></h6>
                                        <small class="text-muted">ID: #<%= t.getIdCuentaOrigen() %>0<%= t.getMonto() %></small>
                                    </div>
                                </div>
                            </td>
                            <td class="text-muted fw-semibold">
                                <%= t.getFechaTransaccion().toString().replace("T", " a las ").substring(0, 16) %>
                            </td>
                            <td>
                                <i class="bi bi-check-circle-fill text-success"></i> Completado
                            </td>
                            <td class="text-end fw-bold <%= colorTexto %> fs-5">
                                <%= esIngreso ? "+" : "-" %> $<%= String.format("%,.2f", t.getMonto()) %>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="4" class="text-center py-5 text-muted">
                                <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                Aún no tienes movimientos registrados.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
</div>

<div class="modal fade" id="modalDeposito" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold">Realizar Depósito</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/deposito" method="POST">
                <div class="modal-body p-4">
                    <div class="input-group input-group-lg mb-3 shadow-sm rounded-3 overflow-hidden">
                        <span class="input-group-text bg-light border-0 text-dark fw-bold">$</span>
                        <input type="number" name="monto" class="form-control border-0 bg-light" placeholder="0.00" min="1" step="0.01" required>
                    </div>
                </div>
                <div class="modal-footer border-0 pt-0">
                    <button type="submit" class="btn btn-dark w-100 btn-action rounded-pill">Confirmar Depósito</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="modalTransferir" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold">Enviar Dinero</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/transferencia" method="POST">
                <div class="modal-body p-4">
                    <div class="mb-4">
                        <label class="form-label fw-bold text-muted small text-uppercase">Cuenta Destino</label>
                        <input type="text" name="cuentaDestino" class="form-control form-control-lg border-0 bg-light shadow-sm" placeholder="Ej: AHO-123456" required>
                    </div>
                    <div>
                        <label class="form-label fw-bold text-muted small text-uppercase">Monto a Enviar</label>
                        <div class="input-group input-group-lg shadow-sm rounded-3 overflow-hidden">
                            <span class="input-group-text bg-light border-0 text-dark fw-bold">$</span>
                            <input type="number" name="monto" class="form-control border-0 bg-light" placeholder="0.00" min="1" step="0.01" required>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0 pt-0">
                    <button type="submit" class="btn btn-dark w-100 btn-action rounded-pill">Transferir Fondos</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>