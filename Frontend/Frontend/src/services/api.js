import axios from "axios";

const BASE_URL =
  process.env.REACT_APP_API_URL || "http://localhost:8080/Bancoweb1";

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true, // Envía la cookie de sesión JSESSIONID en cada petición
  headers: { "Content-Type": "application/x-www-form-urlencoded" },
});

// Serializa un objeto a application/x-www-form-urlencoded

const toFormData = (data) =>
  Object.keys(data)
    .map((k) => `${encodeURIComponent(k)}=${encodeURIComponent(data[k])}`)
    .join("&");

// ── Auth ──────────────────────────────────────────────────────────────────────
export const authService = {
  login: (email, password) =>
    api.post("/LoginServlet", toFormData({ email, password })),

  //  recibe el objeto form completo 
  registro: (form) =>
    api.post("/registro", toFormData(form)),

  
  
  logout: () => api.get("/logout"),
};

// ── Operaciones bancarias ─────────────────────────────────────────────────────
export const cuentaService = {
  getDashboard: () => api.get("/api/dashboard"),

  depositar: (monto) =>
    api.post("/deposito", toFormData({ monto })),

  transferir: (cuentaDestino, monto) =>
    api.post("/transferencia", toFormData({ cuentaDestino, monto })),
};

export default api;
