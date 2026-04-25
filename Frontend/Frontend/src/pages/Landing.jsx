import React, { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import './Landing.css';

const FEATURES = [
  {
    icon: '⚡',
    title: 'Transferencias al instante',
    desc: 'Mueve tu dinero en segundos, sin filas ni esperas.',
  },
  {
    icon: '🔒',
    title: 'Seguridad bancaria real',
    desc: 'Cada operación protegida con cifrado de extremo a extremo.',
  },
  {
    icon: '📊',
    title: 'Control total de tu dinero',
    desc: 'Historial detallado con fecha, estado y tipo de cada movimiento.',
  },
];

export default function Landing() {
  const navigate = useNavigate();
  const heroRef = useRef(null);

  useEffect(() => {
    const el = heroRef.current;
    if (!el) return;
    const onMove = (e) => {
      const rect = el.getBoundingClientRect();
      const x = ((e.clientX - rect.left) / rect.width - 0.5) * 20;
      const y = ((e.clientY - rect.top) / rect.height - 0.5) * 20;
      el.style.setProperty('--mx', `${x}px`);
      el.style.setProperty('--my', `${y}px`);
    };
    window.addEventListener('mousemove', onMove);
    return () => window.removeEventListener('mousemove', onMove);
  }, []);

  return (
    <div className="landing">
      <div className="landing-bg">
        <div className="grid-overlay" />
      </div>

      {/* Navbar */}
      <nav className="landing-nav">
        <div className="nav-logo">
          <span className="logo-mark">B</span>
          <span className="logo-text">BancoWeb</span>
        </div>
        <div className="nav-actions">
          <button className="btn-ghost" onClick={() => navigate('/login')}>
            Iniciar sesión
          </button>
          <button className="btn-primary" onClick={() => navigate('/registro')}>
            Abrir cuenta gratis
          </button>
        </div>
      </nav>

      {/* Hero */}
      <main className="hero" ref={heroRef}>
        <div>
          <div className="hero-badge">
            <span className="badge-dot" />
            Sin comisiones · Sin letra pequeña
          </div>
          <h1 className="hero-title">
            El banco que<br />
            <span className="gradient-text">siempre quisiste.</span>
          </h1>
          <p className="hero-sub">
            Abre tu cuenta en minutos. Deposita, transfiere y controla
            tu dinero desde cualquier lugar, en tiempo real.
          </p>
          <div className="hero-ctas">
            <button className="cta-primary" onClick={() => navigate('/registro')}>
              Crear cuenta gratis
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M3 8h10M9 4l4 4-4 4" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </button>
            <button className="cta-secondary" onClick={() => navigate('/login')}>
              Ya tengo cuenta
            </button>
          </div>
        </div>

        {/* Tarjeta decorativa */}
        <div className="hero-card-preview">
          <div className="preview-card">
            <div className="preview-card-top">
              <span className="preview-chip">AHORRO</span>
              <span className="preview-status">● Activa</span>
            </div>
            <div className="preview-balance">
              <span className="preview-label">Saldo disponible</span>
              <span className="preview-amount">$12,540.00</span>
            </div>
            <div className="preview-card-bottom">
              <span className="preview-number mono">AHO-••••-7821</span>
              <span className="preview-logo">BW</span>
            </div>
          </div>
          <div className="preview-pill preview-pill-1">
            <span>+</span> Depósito recibido · $500
          </div>
          <div className="preview-pill preview-pill-2">
            <span>→</span> Transferencia enviada · $200
          </div>
        </div>
      </main>

      {/* Features */}
      <section className="features">
        {FEATURES.map((f) => (
          <div className="feature-card" key={f.title}>
            <div className="feature-icon">{f.icon}</div>
            <h3>{f.title}</h3>
            <p>{f.desc}</p>
          </div>
        ))}
      </section>

      <footer className="landing-footer">
        © 2026 BancoWeb · Todos los derechos reservados
      </footer>
    </div>
  );
}
