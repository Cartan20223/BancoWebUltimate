import React from 'react';
import './Sidebar.css';

const NAV = [
  { icon: '▦', label: 'Resumen', active: true },
  { icon: '◈', label: 'Cuenta' },
  { icon: '↗', label: 'Transferir' },
  { icon: '⊕', label: 'Depósito' },
  { icon: '◉', label: 'Perfil' },
];

export default function Sidebar({ onLogout }) {
  return (
    <aside className="sidebar">
      <div className="sb-logo">B</div>
      <nav className="sb-nav">
        {NAV.map((item) => (
          <button
            key={item.label}
            className={`sb-nav-btn${item.active ? ' active' : ''}`}
            title={item.label}
          >
            <span className="sb-icon">{item.icon}</span>
          </button>
        ))}
      </nav>
      <button className="sb-logout" onClick={onLogout} title="Cerrar sesión">
        <span>⎋</span>
      </button>
    </aside>
  );
}
