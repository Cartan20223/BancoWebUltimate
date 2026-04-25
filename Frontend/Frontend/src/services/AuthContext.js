import React, { createContext, useContext, useState } from 'react';
// Creación del contexto de autenticación con valor inicial nulo
const AuthContext = createContext(null);
/**
 * Proveedor de Autenticación.
 * Envuelve la aplicación para gestionar el estado de la sesión globalmente.
 */
export function AuthProvider({ children }) {
  const [user, setUser] = useState(
    () => JSON.parse(sessionStorage.getItem('bw_user') || 'null')
  );

  const login = (userData) => {
    setUser(userData);
    sessionStorage.setItem('bw_user', JSON.stringify(userData));
  };

  const logout = () => {
    setUser(null);
    sessionStorage.removeItem('bw_user');
  };
  // Retorna el proveedor con el objeto de usuario y las funciones de control

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
