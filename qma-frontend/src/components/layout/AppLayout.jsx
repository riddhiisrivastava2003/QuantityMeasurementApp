import React, { useState } from 'react'
import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import {
  LayoutDashboard, History, User, LogOut,
  Menu, X, Sun, Moon, Zap, ChevronRight
} from 'lucide-react'
import toast from 'react-hot-toast'

const NAV_ITEMS = [
  { to: '/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
  { to: '/history',   icon: History,         label: 'History',  authRequired: true },
  { to: '/profile',   icon: User,            label: 'Profile',  authRequired: true },
]

export default function AppLayout() {
  const { user, logout, darkMode, toggleDark, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [sidebarOpen, setSidebarOpen] = useState(false)

  const handleLogout = async () => {
    await logout()
    toast.success('Logged out successfully')
    navigate('/')
  }

  const visibleNav = NAV_ITEMS.filter(item => !item.authRequired || isAuthenticated)

  return (
    <div className="flex h-screen overflow-hidden bg-[var(--bg-primary)]">
      {/* ── Mobile overlay ── */}
      {sidebarOpen && (
        <div className="fixed inset-0 bg-black/40 z-20 lg:hidden" onClick={() => setSidebarOpen(false)} />
      )}

      {/* ── Sidebar ── */}
      <aside className={`
        fixed lg:static inset-y-0 left-0 z-30
        w-64 flex flex-col
        bg-[var(--card-bg)] border-r border-[var(--border)]
        transform transition-transform duration-300 ease-in-out
        ${sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
      `}>
        {/* Logo */}
        <div className="flex items-center gap-3 px-5 py-5 border-b border-[var(--border)]">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-brand-500 to-accent-500 flex items-center justify-center shadow-glow-sm flex-shrink-0">
            <Zap size={18} className="text-white" />
          </div>
          <div>
            <h1 className="font-display font-bold text-base text-[var(--text-primary)]">QMA</h1>
            <p className="text-xs text-[var(--text-secondary)]">Measurement App</p>
          </div>
          <button onClick={() => setSidebarOpen(false)} className="ml-auto lg:hidden text-gray-400 hover:text-gray-600">
            <X size={18} />
          </button>
        </div>

        {/* Nav */}
        <nav className="flex-1 px-3 py-4 space-y-1 overflow-y-auto">
          {visibleNav.map(({ to, icon: Icon, label }) => (
            <NavLink
              key={to}
              to={to}
              onClick={() => setSidebarOpen(false)}
              className={({ isActive }) =>
                `sidebar-link ${isActive ? 'active' : ''}`
              }
            >
              <Icon size={18} />
              <span>{label}</span>
              <ChevronRight size={14} className="ml-auto opacity-30" />
            </NavLink>
          ))}
        </nav>

        {/* Footer */}
        <div className="px-3 py-4 border-t border-[var(--border)] space-y-2">
          {/* Theme toggle */}
          <button
            onClick={toggleDark}
            className="sidebar-link w-full"
          >
            {darkMode ? <Sun size={18} /> : <Moon size={18} />}
            <span>{darkMode ? 'Light Mode' : 'Dark Mode'}</span>
          </button>

          {/* User info + logout */}
          {isAuthenticated && user && (
            <div className="pt-2 border-t border-[var(--border)]">
              <div className="flex items-center gap-3 px-4 py-2 mb-1">
                <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-brand-400 to-accent-500 flex items-center justify-center flex-shrink-0">
                  <span className="text-white font-bold text-sm">
                    {(user.username || 'U')[0].toUpperCase()}
                  </span>
                </div>
                <div className="min-w-0">
                  <p className="text-sm font-semibold text-[var(--text-primary)] truncate">{user.username}</p>
                  <p className="text-xs text-[var(--text-secondary)]">{user.role || 'User'}</p>
                </div>
              </div>
              <button onClick={handleLogout} className="sidebar-link w-full text-red-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20">
                <LogOut size={18} />
                <span>Logout</span>
              </button>
            </div>
          )}

          {!isAuthenticated && (
            <button onClick={() => navigate('/login')} className="btn-primary w-full text-sm">
              Sign In
            </button>
          )}
        </div>
      </aside>

      {/* ── Main content area ── */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Topbar */}
        <header className="h-14 flex items-center px-4 gap-4 bg-[var(--nav-bg)] backdrop-blur-sm border-b border-[var(--border)] flex-shrink-0 z-10">
          <button
            onClick={() => setSidebarOpen(true)}
            className="lg:hidden text-gray-500 hover:text-gray-700 dark:hover:text-gray-300 p-1.5 rounded-lg hover:bg-gray-100 dark:hover:bg-surface-800"
          >
            <Menu size={20} />
          </button>
          <div className="flex-1" />
          <button onClick={toggleDark} className="p-2 rounded-xl text-gray-500 hover:text-brand-600 hover:bg-brand-50 dark:hover:bg-surface-800 transition-colors">
            {darkMode ? <Sun size={18} /> : <Moon size={18} />}
          </button>
          {isAuthenticated ? (
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-brand-400 to-accent-500 flex items-center justify-center">
                <span className="text-white font-bold text-sm">{(user?.username || 'U')[0].toUpperCase()}</span>
              </div>
              <span className="text-sm font-medium text-[var(--text-primary)] hidden sm:block">{user?.username}</span>
            </div>
          ) : (
            <button onClick={() => navigate('/login')} className="btn-primary text-sm py-2 px-4">Sign In</button>
          )}
        </header>

        {/* Page content */}
        <main className="flex-1 overflow-y-auto">
          <div className="page-enter">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  )
}
