import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { useAuth } from './context/AuthContext'

// Layouts
import AppLayout from './components/layout/AppLayout'

// Pages
import LandingPage    from './pages/LandingPage'
import LoginPage      from './pages/LoginPage'
import RegisterPage   from './pages/RegisterPage'
import DashboardPage  from './pages/DashboardPage'
import HistoryPage    from './pages/HistoryPage'
import ProfilePage    from './pages/ProfilePage'
import OAuthCallback  from './pages/OAuthCallback'
import NotFoundPage   from './pages/NotFoundPage'

// ─── Route Guards ────────────────────────────────────────────────────────────
function PrivateRoute({ children }) {
  const { isAuthenticated, loading } = useAuth()
  if (loading) return <LoadingScreen />
  return isAuthenticated ? children : <Navigate to="/login" replace />
}

function PublicOnlyRoute({ children }) {
  const { isAuthenticated, loading } = useAuth()
  if (loading) return <LoadingScreen />
  return !isAuthenticated ? children : <Navigate to="/dashboard" replace />
}

function LoadingScreen() {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-surface-50 dark:bg-surface-950">
      <div className="flex flex-col items-center gap-4">
        <div className="w-12 h-12 rounded-2xl bg-gradient-to-br from-brand-500 to-accent-500 flex items-center justify-center shadow-glow animate-pulse-slow">
          <span className="text-2xl">⚡</span>
        </div>
        <p className="text-gray-500 dark:text-gray-400 text-sm font-medium animate-pulse">Loading QMA…</p>
      </div>
    </div>
  )
}

// ─── Routing ─────────────────────────────────────────────────────────────────
function AppRoutes() {
  return (
    <Routes>
      {/* Public */}
      <Route path="/"             element={<LandingPage />} />
      <Route path="/oauth-callback" element={<OAuthCallback />} />

      {/* Public-only (redirect to dashboard if logged in) */}
      <Route path="/login"    element={<PublicOnlyRoute><LoginPage /></PublicOnlyRoute>} />
      <Route path="/register" element={<PublicOnlyRoute><RegisterPage /></PublicOnlyRoute>} />

      {/* Protected — wrapped in AppLayout (sidebar + navbar) */}
      <Route element={<AppLayout />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/history"   element={<PrivateRoute><HistoryPage /></PrivateRoute>} />
        <Route path="/profile"   element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
      </Route>

      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}

export default function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  )
}
