import React from 'react';
import { useNavigate } from 'react-router-dom';
import './AuthLayout.css';

export default function AuthLayout({ children, title, subtitle, altText, altLink, altLabel }) {
  const navigate = useNavigate();
  return (
    <div className="auth-root">
      <div className="auth-left">
        <div className="auth-left-inner">
          <div className="auth-logo" onClick={() => navigate('/')}>
            <span className="auth-logo-mark">B</span>
            <span className="auth-logo-text">BancoWeb</span>
          </div>
        </div>
        <div className="auth-quote">
          <blockquote>
            Gestiona tu dinero con total control.
          </blockquote>
          <cite>— BancoWeb, banca sin límites</cite>
        </div>
        <div className="auth-left-orb" />
      </div>
      <div className="auth-right">
        <div className="auth-form-wrapper">
          <h2 className="auth-title">{title}</h2>
          <p className="auth-subtitle">{subtitle}</p>
          {children}
          <p className="auth-alt">
            {altText}{' '}
            <button className="auth-alt-btn" onClick={() => navigate(altLink)}>
              {altLabel}
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}
