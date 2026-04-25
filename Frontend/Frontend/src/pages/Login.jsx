import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { useAuth } from "../services/AuthContext";
import { authService } from "../services/api";

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const res = await authService.login(form.email, form.password);
      const data = res.data;

      if (data.ok) {
        // Guardamos nombre y email en el contexto global para usar en el Dashboard
        login({ nombre: data.nombre, email: data.email });
        navigate("/dashboard");
      } else {
        setError(data.error || "Correo o contraseña incorrectos.");
      }
    } catch (err) {
      // Axios lanza error en respuestas 4xx/5xx — leemos el mensaje del servidor
      const errorMsg =
        err.response?.data?.error || "Error de conexión con el servidor.";
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  // Leer parámetros de URL: ?registro=ok viene de Registro.jsx tras éxito
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    if (params.get("registro") === "ok")
      setSuccess("¡Cuenta creada! Ya puedes ingresar.");
  }, []);

  return (
    <AuthLayout
      title="Bienvenido de vuelta"
      subtitle="Ingresa tus credenciales para acceder a tu cuenta."
      altText="¿No tienes cuenta?"
      altLink="/registro"
      altLabel="Regístrate aquí"
    >
      {error && <div className="auth-alert danger">⚠ {error}</div>}
      {success && <div className="auth-alert success">✓ {success}</div>}

      <form onSubmit={submit}>
        <div className="auth-field">
          <label className="auth-label">Email</label>
          <input
            className="auth-input"
            type="email"
            name="email"
            placeholder="nombre@correo.com"
            value={form.email}
            onChange={handle}
            required
            autoFocus
          />
        </div>
        <div className="auth-field">
          <label className="auth-label">Contraseña</label>
          <input
            className="auth-input"
            type="password"
            name="password"
            placeholder="••••••••"
            value={form.password}
            onChange={handle}
            required
          />
        </div>
        <button className="auth-submit" type="submit" disabled={loading}>
          {loading ? <span className="spinner" /> : "Entrar a mi banco"}
        </button>
      </form>
    </AuthLayout>
  );
}
