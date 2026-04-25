import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { authService } from "../services/api"; // Servicio centralizado para peticiones HTTP

/**
 * Vista de Registro.
 * Permite a los nuevos usuarios crear una cuenta en la plataforma.
 */
export default function Registro() {
  const navigate = useNavigate();
  
  // Estado inicial del formulario con todos los campos necesarios
  const [form, setForm] = useState({
    nombre: "",
    apellido: "",
    documento: "",
    email: "",
    password: "",
  });

  const [error, setError] = useState(""); // Almacena mensajes de error para la UI
  const [loading, setLoading] = useState(false); // Controla el estado de envío

  /**
   * Actualiza dinámicamente el estado del formulario según el input modificado.
   */
  const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  /**
   * Procesa el envío del formulario.
   */
  const submit = async (e) => {
    e.preventDefault(); // Previene la recarga de la página
    setError("");

    // Validación básica del lado del cliente
    if (form.password.length < 6) {
      setError("La contraseña debe tener al menos 6 caracteres.");
      return;
    }

    setLoading(true);
    try {
      // Llamada asíncrona al backend (Servlet de Registro)
      const res = await authService.registro(form);
      const data = res.data;

      if (data.ok) {
        // Redirección exitosa con flag de éxito
        navigate("/login?registro=ok");
      } else {
        setError(data.error || "Error al registrar. Intenta nuevamente.");
      }
    } catch (err) {
      // Captura de errores de Axios (4xx, 5xx o red)
      const errorMsg =
        err.response?.data?.error ||
        "Error de conexión con el servidor.";
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Efecto para capturar errores provenientes de redirecciones en la URL.
   */
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    if (params.get("error"))
      setError("Ocurrió un error al registrar. Intenta nuevamente.");
  }, []);

  return (
    <AuthLayout
      title="Abre tu cuenta"
      subtitle="Completa tus datos para comenzar. Es gratis."
      altText="¿Ya tienes cuenta?"
      altLink="/login"
      altLabel="Inicia sesión"
    >
      {/* Alerta de error si existe alguno */}
      {error && <div className="auth-alert danger">⚠ {error}</div>}

      <form onSubmit={submit}>
        <div className="auth-row">
          <div className="auth-field">
            <label className="auth-label">Nombre</label>
            <input
              className="auth-input"
              type="text"
              name="nombre"
              placeholder="Tu nombre"
              value={form.nombre}
              onChange={handle}
              required
              autoFocus
            />
          </div>
          <div className="auth-field">
            <label className="auth-label">Apellido</label>
            <input
              className="auth-input"
              type="text"
              name="apellido"
              placeholder="Tu apellido"
              value={form.apellido}
              onChange={handle}
              required
            />
          </div>
        </div>

        <div className="auth-field">
          <label className="auth-label">Documento</label>
          <input
            className="auth-input"
            type="text"
            name="documento"
            placeholder="Número de documento"
            value={form.documento}
            onChange={handle}
            required
          />
        </div>

        <div className="auth-field">
          <label className="auth-label">Email</label>
          <input
            className="auth-input"
            type="email"
            name="email"
            placeholder="correo@ejemplo.com"
            value={form.email}
            onChange={handle}
            required
          />
        </div>

        <div className="auth-field">
          <label className="auth-label">Contraseña</label>
          <input
            className="auth-input"
            type="password"
            name="password"
            placeholder="Mínimo 6 caracteres"
            value={form.password}
            onChange={handle}
            required
          />
        </div>

        <button className="auth-submit" type="submit" disabled={loading}>
          {loading ? <span className="spinner" /> : "Crear cuenta y continuar"}
        </button>
      </form>
    </AuthLayout>
  );
}