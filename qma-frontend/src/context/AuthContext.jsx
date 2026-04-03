import React, { createContext, useContext, useState, useEffect, useCallback } from 'react'
import { authAPI } from '../services/api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser]       = useState(null)
  const [token, setToken]     = useState(() => localStorage.getItem('qma_token'))
  const [loading, setLoading] = useState(true)
  const [darkMode, setDarkMode] = useState(() => localStorage.getItem('qma_dark') === 'true')

  // Apply dark mode class
  useEffect(() => {
    document.documentElement.classList.toggle('dark', darkMode)
    localStorage.setItem('qma_dark', darkMode)
  }, [darkMode])

  // Fetch profile when token is available
  const fetchProfile = useCallback(async (tok) => {
    if (!tok) { setLoading(false); return }
    try {
      const data = await authAPI.getProfile(tok)
      setUser(data)
    } catch {
      // Token invalid or expired — clear it
      localStorage.removeItem('qma_token')
      setToken(null)
      setUser(null)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchProfile(token)
  }, [token, fetchProfile])

  const login = useCallback(async (username, password) => {
    const data = await authAPI.login(username, password)
    const tok = data.token
    localStorage.setItem('qma_token', tok)
    setToken(tok)
    setUser({ username: data.username || username, role: 'USER' })
    return data
  }, [])

  const register = useCallback(async (username, password) => {
    const data = await authAPI.register(username, password)
    const tok = data.token
    localStorage.setItem('qma_token', tok)
    setToken(tok)
    setUser({ username: data.username || username, role: 'USER' })
    return data
  }, [])

  const logout = useCallback(async () => {
    try { await authAPI.logout(token) } catch { /* ignore */ }
    localStorage.removeItem('qma_token')
    setToken(null)
    setUser(null)
  }, [token])

  const toggleDark = useCallback(() => setDarkMode(d => !d), [])

  const isAuthenticated = !!token && !!user

  return (
    <AuthContext.Provider value={{
      user, token, loading, isAuthenticated,
      login, register, logout,
      darkMode, toggleDark
    }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider')
  return ctx
}
