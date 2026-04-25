import React, { useState } from 'react';
import './BalanceCard.css';
/**
 * Tarjeta de saldo principal.
 * Muestra el balance actual, número de cuenta y botones de acción.
 */
export default function BalanceCard({ saldo, numeroCuenta, tipoCuenta, onDepositar, onTransferir }) {
  const [hidden, setHidden] = useState(false);

  const formatted = saldo?.toLocaleString('es-CO', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }) ?? '0.00';

  return (
    <div className="balance-card">
      <div className="bc-circle bc-circle-1" />
      <div className="bc-circle bc-circle-2" />

      <div className="bc-top">
        <div>
          <div className="bc-label">Saldo disponible</div>
          <div className="bc-amount">
            {hidden ? '••••••' : `$${formatted}`}
          </div>
        </div>
        <button
          className="bc-eye"
          onClick={() => setHidden(!hidden)}
          title={hidden ? 'Mostrar saldo' : 'Ocultar saldo'}
        >
          {hidden ? '○' : '●'}
        </button>
      </div>

      <div className="bc-mid">
        <div className="bc-chip">{tipoCuenta?.toUpperCase()}</div>
        <div className="bc-num mono">{numeroCuenta}</div>
      </div>

      <div className="bc-actions">
        <button className="bc-btn bc-btn--primary" onClick={onDepositar}>
          <span>↑</span> Depositar
        </button>
        <button className="bc-btn bc-btn--outline" onClick={onTransferir}>
          <span>→</span> Transferir
        </button>
      </div>
    </div>
  );
}
