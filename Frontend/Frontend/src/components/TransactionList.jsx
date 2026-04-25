import React, { useState } from 'react';
import './TransactionList.css';

const isIngreso = (tipo) =>
  tipo.includes('Consignación') || tipo.includes('Recibida') || tipo.includes('Depósito');

const formatFecha = (iso) => {
  try {
    const d = new Date(iso);
    return (
      d.toLocaleDateString('es-ES', { day: '2-digit', month: 'short', year: 'numeric' }) +
      ' · ' +
      d.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' })
    );
  } catch {
    return iso;
  }
};

export default function TransactionList({ movimientos = [] }) {
  const [filter, setFilter] = useState('todos');

  const filtered = movimientos.filter((t) => {
    if (filter === 'ingresos') return isIngreso(t.tipoTransaccion);
    if (filter === 'egresos') return !isIngreso(t.tipoTransaccion);
    return true;
  });

  return (
    <section className="tx-section">
      <div className="tx-header">
        <h2 className="tx-title">Movimientos</h2>
        <div className="tx-filters">
          {['todos', 'ingresos', 'egresos'].map((f) => (
            <button
              key={f}
              className={`tx-filter-btn${filter === f ? ' active' : ''}`}
              onClick={() => setFilter(f)}
            >
              {f.charAt(0).toUpperCase() + f.slice(1)}
            </button>
          ))}
        </div>
      </div>

      {filtered.length === 0 ? (
        <div className="tx-empty">
          <div className="tx-empty-icon">◎</div>
          <p>No hay movimientos en esta categoría</p>
        </div>
      ) : (
        <div className="tx-list">
          {filtered.map((t) => {
            const ingreso = isIngreso(t.tipoTransaccion);
            return (
              <div className="tx-item" key={t.idTransaccion}>
                <div className={`tx-icon${ingreso ? ' tx-icon--green' : ' tx-icon--red'}`}>
                  {ingreso ? '↓' : '↑'}
                </div>
                <div className="tx-info">
                  <div className="tx-type">{t.tipoTransaccion}</div>
                  <div className="tx-date">{formatFecha(t.fechaTransaccion)}</div>
                </div>
                <div className="tx-right">
                  <div className={`tx-amount${ingreso ? ' tx-amount--green' : ' tx-amount--red'}`}>
                    {ingreso ? '+' : '-'}$
                    {t.monto?.toLocaleString('es-CO', { minimumFractionDigits: 2 })}
                  </div>
                  <div className="tx-status">✓ Completado</div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </section>
  );
}
