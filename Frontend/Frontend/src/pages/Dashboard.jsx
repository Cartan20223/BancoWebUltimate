import React, { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext";
import { authService, cuentaService } from "../services/api"; // 
import Sidebar from "../components/Sidebar";
import BalanceCard from "../components/BalanceCard";
import TransactionList from "../components/TransactionList";
import Modal from "../components/Modal";
import "./Dashboard.css";

// Datos de demo para cuando el backend no tenga el endpoint /api/dashboard aún
const DEMO_DATA = {
  usuario: {
    nombre: "Usuario",
    apellido: "Fallido",
    email: "nosirveesteuser",
  },
  cuenta: {
    numeroCuenta: "AHO-482917",
    tipoCuenta: "Cuenta de Ahorro",
    saldo: -1000,
  },
  movimientos: [
    {
      idTransaccion: 1,
      tipoTransaccion: "Consignación",
      monto: 5000,
      fechaTransaccion: "2024-06-10T09:30:00",
      idCuentaOrigen: 1,
    },
    {
      idTransaccion: 2,
      tipoTransaccion: "Transferencia Enviada",
      monto: 800,
      fechaTransaccion: "2024-06-09T14:15:00",
      idCuentaOrigen: 1,
    },
    {
      idTransaccion: 3,
      tipoTransaccion: "Consignación",
      monto: 2000,
      fechaTransaccion: "2024-06-08T11:00:00",
      idCuentaOrigen: 1,
    },
    {
      idTransaccion: 4,
      tipoTransaccion: "Transferencia Recibida",
      monto: 350,
      fechaTransaccion: "2024-06-07T16:45:00",
      idCuentaOrigen: 2,
    },
    {
      idTransaccion: 5,
      tipoTransaccion: "Transferencia Enviada",
      monto: 1200,
      fechaTransaccion: "2024-06-06T08:20:00",
      idCuentaOrigen: 1,
    },
  ],
};

export default function Dashboard() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState(null);
  const [modal, setModal] = useState(null); // 'deposito' | 'transferencia'

  const [montoDeposito, setMontoDeposito] = useState("");
  const [formTransferencia, setFormTransferencia] = useState({
    cuentaDestino: "",
    monto: "",
  });
  const [opLoading, setOpLoading] = useState(false);

  const showToast = useCallback((msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 4000);
  }, []);

  // ── Carga inicial del dashboard ─────────────────────────────────────────────
  const fetchDashboard = useCallback(async () => {
    setLoading(true);
    try {
      const res = await cuentaService.getDashboard();
      setData(res.data);
    } catch (err) {
      const status = err.response?.status;

      if (status === 401 || status === 403) {
        // Sesión Java expirada → limpiar sesión React y al login
        logout();
        navigate("/login");
      } else if (!status) {
        // Sin status = backend caído o sin CORS → mostrar demo
        // Solo en desarrollo, nunca con sesión activa
        if (!user) {
          setData(DEMO_DATA);
        } else {
          // Hay usuario en React pero el backend no responde
          showToast("No se pudo conectar con el servidor.", "error");
          logout();
          navigate("/login");
        }
      } else {
        // Cualquier otro error (500, etc.) → al login
        logout();
        navigate("/login");
      }
    } finally {
      setLoading(false);
    }
  }, [navigate, logout, user, showToast]);

  useEffect(() => {
    fetchDashboard();
    const params = new URLSearchParams(window.location.search);
    if (params.get("mensaje")) showToast("Operación realizada con éxito. ✓");
    if (params.get("error"))
      showToast("Hubo un problema con la operación.", "error");
  }, [fetchDashboard, showToast]);

  // ── Depósito ────────────────────────────────────────────────────────────────
  const handleDeposito = async (e) => {
    e.preventDefault();
    setOpLoading(true);
    try {
      // CORREGIDO: usa cuentaService y lee la respuesta real del servidor
      const res = await cuentaService.depositar(montoDeposito);
      const data = res.data;

      if (data.ok) {
        // Solo actualizamos el estado local si el servidor confirmó el éxito
        const m = parseFloat(montoDeposito);
        setData((prev) => ({
          ...prev,
          cuenta: { ...prev.cuenta, saldo: prev.cuenta.saldo + m },
          movimientos: [
            {
              idTransaccion: Date.now(),
              tipoTransaccion: "Consignación",
              monto: m,
              fechaTransaccion: new Date().toISOString(),
              idCuentaOrigen: 1,
            },
            ...prev.movimientos,
          ],
        }));
        setModal(null);
        setMontoDeposito("");
        showToast(
          `Depósito de $${parseFloat(montoDeposito).toLocaleString("es-CO")} realizado. ✓`,
        );
      } else {
        // El servidor respondió {ok:false, error:"..."}
        showToast(data.error || "No se pudo completar el depósito.", "error");
      }
    } catch (err) {
      const errorMsg =
        err.response?.data?.error || "Error de conexión con el servidor.";
      showToast(errorMsg, "error");
    } finally {
      setOpLoading(false);
    }
  };

  // ── Transferencia ───────────────────────────────────────────────────────────
  const handleTransferencia = async (e) => {
    e.preventDefault();
    setOpLoading(true);
    try {
      // CORREGIDO: usa cuentaService y lee la respuesta real del servidor
      const res = await cuentaService.transferir(
        formTransferencia.cuentaDestino,
        formTransferencia.monto,
      );
      const data = res.data;

      if (data.ok) {
        // Solo descontamos el saldo si el servidor confirmó la transferencia
        const m = parseFloat(formTransferencia.monto);
        setData((prev) => ({
          ...prev,
          cuenta: { ...prev.cuenta, saldo: prev.cuenta.saldo - m },
          movimientos: [
            {
              idTransaccion: Date.now(),
              tipoTransaccion: "Transferencia Enviada",
              monto: m,
              fechaTransaccion: new Date().toISOString(),
              idCuentaOrigen: 1,
            },
            ...prev.movimientos,
          ],
        }));
        setModal(null);
        setFormTransferencia({ cuentaDestino: "", monto: "" });
        showToast(
          `Transferencia de $${parseFloat(formTransferencia.monto).toLocaleString("es-CO")} enviada. ✓`,
        );
      } else {
        // Fondos insuficientes, cuenta no existe, etc.
        showToast(
          data.error || "Fondos insuficientes o cuenta no encontrada.",
          "error",
        );
      }
    } catch (err) {
      const errorMsg =
        err.response?.data?.error || "Error de conexión con el servidor.";
      showToast(errorMsg, "error");
    } finally {
      setOpLoading(false);
    }
  };

  // ── Logout ──────────────────────────────────────────────────────────────────
  const handleLogout = async () => {
    try {
      // CORREGIDO: usa authService.logout (axios) — cuando LogoutServlet
      // responda JSON en lugar de sendRedirect, esto funcionará limpiamente
      await authService.logout();
    } catch {
      // Si falla la petición al servidor, igual cerramos sesión en el cliente
    }
    logout();
    navigate("/");
  };

  // ── Loading screen ──────────────────────────────────────────────────────────
  if (loading) {
    return (
      <div className="db-loading">
        <div className="db-loading-logo">B</div>
        <div className="db-loading-bar">
          <div className="db-loading-fill" />
        </div>
      </div>
    );
  }

  const { usuario, cuenta, movimientos } = data || DEMO_DATA;

  const totalIngresos = movimientos
    .filter(
      (t) =>
        t.tipoTransaccion.includes("Consignación") ||
        t.tipoTransaccion.includes("Recibida"),
    )
    .reduce((a, t) => a + t.monto, 0);

  const totalEgresos = movimientos
    .filter((t) => t.tipoTransaccion.includes("Enviada"))
    .reduce((a, t) => a + t.monto, 0);

  return (
    <div className="dashboard">
      <Sidebar onLogout={handleLogout} />

      <div className="db-main">
        {/* Header */}
        <header className="db-header">
          <div>
            <h1 className="db-header-title">Resumen</h1>
            <p className="db-header-sub">
              {new Date().toLocaleDateString("es-ES", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric",
              })}
            </p>
          </div>
          <div className="db-user-chip">
            <div className="db-avatar">
              {usuario.nombre?.[0]?.toUpperCase()}
            </div>
            <div>
              <div className="db-user-name">
                {usuario.nombre} {usuario.apellido}
              </div>
              <div className="db-user-email">{usuario.email}</div>
            </div>
          </div>
        </header>

        {/* Toast */}
        {toast && (
          <div className={`db-toast ${toast.type}`}>
            {toast.type === "success" ? "✓" : "⚠"} {toast.msg}
          </div>
        )}

        {/* Cards */}
        <div className="db-cards">
          <BalanceCard
            saldo={cuenta.saldo}
            numeroCuenta={cuenta.numeroCuenta}
            tipoCuenta={cuenta.tipoCuenta}
            onDepositar={() => setModal("deposito")}
            onTransferir={() => setModal("transferencia")}
          />
          <div className="db-stats">
            <div className="db-stat-card">
              <span className="db-stat-icon db-stat-icon--green">↑</span>
              <div>
                <div className="db-stat-label">Total ingresos</div>
                <div className="db-stat-value">
                  $
                  {totalIngresos.toLocaleString("es-CO", {
                    minimumFractionDigits: 2,
                  })}
                </div>
              </div>
            </div>
            <div className="db-stat-card">
              <span className="db-stat-icon db-stat-icon--red">↓</span>
              <div>
                <div className="db-stat-label">Total egresos</div>
                <div className="db-stat-value">
                  $
                  {totalEgresos.toLocaleString("es-CO", {
                    minimumFractionDigits: 2,
                  })}
                </div>
              </div>
            </div>
            <div className="db-stat-card">
              <span className="db-stat-icon db-stat-icon--blue">#</span>
              <div>
                <div className="db-stat-label">Movimientos</div>
                <div className="db-stat-value">{movimientos.length}</div>
              </div>
            </div>
            <div className="db-account-card">
              <div className="db-account-label">Tipo de cuenta</div>
              <div className="db-account-type">{cuenta.tipoCuenta}</div>
              <div className="db-account-num mono">{cuenta.numeroCuenta}</div>
              <div className="db-account-badge">● ACTIVA</div>
            </div>
          </div>
        </div>

        {/* Transactions */}
        <TransactionList movimientos={movimientos} />
      </div>

      {/* Modal Depósito */}
      {modal === "deposito" && (
        <Modal title="Realizar depósito" onClose={() => setModal(null)}>
          <form onSubmit={handleDeposito}>
            <div className="modal-field">
              <label className="modal-label">Monto a depositar</label>
              <div className="modal-input-prefix">
                <span>$</span>
                <input
                  type="number"
                  min="1"
                  step="0.01"
                  placeholder="0.00"
                  value={montoDeposito}
                  onChange={(e) => setMontoDeposito(e.target.value)}
                  required
                  autoFocus
                />
              </div>
            </div>
            <button className="modal-submit" type="submit" disabled={opLoading}>
              {opLoading ? <span className="spinner" /> : "Confirmar depósito"}
            </button>
          </form>
        </Modal>
      )}

      {/* Modal Transferencia */}
      {modal === "transferencia" && (
        <Modal title="Enviar dinero" onClose={() => setModal(null)}>
          <form onSubmit={handleTransferencia}>
            <div className="modal-field">
              <label className="modal-label">Cuenta destino</label>
              <input
                className="modal-input"
                type="text"
                placeholder="Ej: AHO-482917"
                value={formTransferencia.cuentaDestino}
                onChange={(e) =>
                  setFormTransferencia({
                    ...formTransferencia,
                    cuentaDestino: e.target.value,
                  })
                }
                required
                autoFocus
              />
            </div>
            <div className="modal-field">
              <label className="modal-label">Monto a enviar</label>
              <div className="modal-input-prefix">
                <span>$</span>
                <input
                  type="number"
                  min="1"
                  step="0.01"
                  placeholder="0.00"
                  value={formTransferencia.monto}
                  onChange={(e) =>
                    setFormTransferencia({
                      ...formTransferencia,
                      monto: e.target.value,
                    })
                  }
                  required
                />
              </div>
            </div>
            <button className="modal-submit" type="submit" disabled={opLoading}>
              {opLoading ? <span className="spinner" /> : "Transferir fondos"}
            </button>
          </form>
        </Modal>
      )}
    </div>
  );
}
