import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './services/AuthContext';
import Landing from './pages/Landing';
import Login from './pages/Login';
import Registro from './pages/Registro';
import Dashboard from './pages/Dashboard';
/**
 * Componente de orden superior para proteger rutas privadas.
 * Verifica si hay un usuario en el contexto; si no, redirige al login.
 */
function ProtectedRoute({ children }) {
  const { user } = useAuth();
  // Si no hay usuario, usamos Navigate para mandarlo al login con 'replace' 
  // para que no pueda volver atrás en el historial a la ruta protegida.
  return user ? children : <Navigate to="/login" replace />;
}
/**
 * Definición de la estructura de navegación de la aplicación.
 */
function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route path="/login" element={<Login />} />
      <Route path="/registro" element={<Registro />} /> 
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        }
      />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
/**
 * Componente principal de la aplicación.
 * Configura el proveedor de autenticación y el enrutador del navegador.
 */
export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </AuthProvider>
  );
}
