import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Eye, EyeOff, LogIn, Zap, Loader2 } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import toast from 'react-hot-toast'

export default function LoginPage() {
  const { login } = useAuth()
  const navigate  = useNavigate()

  const [form,    setForm]    = useState({ username: '', password: '' })
  const [show,    setShow]    = useState(false)
  const [loading, setLoading] = useState(false)

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }))

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.username.trim() || !form.password) {
      toast.error('Please fill in all fields')
      return
    }
    setLoading(true)
    try {
      await login(form.username.trim(), form.password)
      toast.success('Welcome back! 👋')
      navigate('/dashboard')
    } catch (err) {
      const msg = err.response?.data?.error || err.message || 'Login failed'
      toast.error(msg)
    } finally {
      setLoading(false)
    }
  }

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google'
  }

  return (
    <div className="min-h-screen flex hero-bg bg-[var(--bg-primary)]">
      {/* Left panel — branding */}
      <div className="hidden lg:flex flex-col justify-between w-1/2 bg-gradient-to-br from-brand-600 to-brand-800 p-12 text-white">
        <Link to="/" className="flex items-center gap-2.5 font-display font-bold text-xl">
          <div className="w-9 h-9 rounded-xl bg-white/20 flex items-center justify-center">
            <Zap size={18} />
          </div>
          QMA
        </Link>
        <div className="space-y-4">
          <h2 className="font-display font-bold text-4xl leading-tight">
            Convert anything.<br />Track everything.
          </h2>
          <p className="text-brand-200 text-lg leading-relaxed">
            Sign in to access your personal dashboard, history, and analytics.
          </p>
        </div>
        <p className="text-brand-300 text-sm">© 2025 QMA · Spring Boot Microservices</p>
      </div>

      {/* Right panel — form */}
      <div className="flex-1 flex items-center justify-center px-6 py-12">
        <div className="w-full max-w-sm animate-slide-up">
          {/* Mobile logo */}
          <Link to="/" className="flex items-center gap-2 font-display font-bold text-lg text-[var(--text-primary)] mb-8 lg:hidden">
            <div className="w-8 h-8 rounded-xl bg-gradient-to-br from-brand-500 to-accent-500 flex items-center justify-center">
              <Zap size={16} className="text-white" />
            </div>
            QMA
          </Link>

          <h1 className="font-display font-bold text-2xl text-[var(--text-primary)] mb-1">Welcome back</h1>
          <p className="text-[var(--text-secondary)] text-sm mb-7">Sign in to your account</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="label">Username</label>
              <input
                type="text"
                name="username"
                value={form.username}
                onChange={handleChange}
                placeholder="Enter your username"
                className="input-field"
                autoComplete="username"
              />
            </div>

            <div>
              <label className="label">Password</label>
              <div className="relative">
                <input
                  type={show ? 'text' : 'password'}
                  name="password"
                  value={form.password}
                  onChange={handleChange}
                  placeholder="Enter your password"
                  className="input-field pr-10"
                  autoComplete="current-password"
                />
                <button
                  type="button"
                  onClick={() => setShow(s => !s)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                >
                  {show ? <EyeOff size={17} /> : <Eye size={17} />}
                </button>
              </div>
            </div>

            <button type="submit" disabled={loading} className="btn-primary w-full py-3 mt-2">
              {loading ? <Loader2 size={18} className="animate-spin" /> : <LogIn size={18} />}
              {loading ? 'Signing in…' : 'Sign In'}
            </button>
          </form>

          {/* Divider */}
          <div className="flex items-center gap-3 my-5">
            <div className="flex-1 h-px bg-[var(--border)]" />
            <span className="text-xs text-[var(--text-secondary)]">or</span>
            <div className="flex-1 h-px bg-[var(--border)]" />
          </div>

          {/* Google OAuth */}
          <button
            onClick={handleGoogleLogin}
            className="btn-secondary w-full py-3 gap-3"
          >
            <svg width="18" height="18" viewBox="0 0 18 18">
              <path fill="#4285F4" d="M16.51 8H8.98v3h4.3c-.18 1-.74 1.48-1.6 2.04v2.01h2.6a7.8 7.8 0 002.38-5.88c0-.57-.05-.66-.15-1.18z"/>
              <path fill="#34A853" d="M8.98 17c2.16 0 3.97-.72 5.3-1.94l-2.6-2a4.8 4.8 0 01-7.18-2.54H1.83v2.07A8 8 0 008.98 17z"/>
              <path fill="#FBBC05" d="M4.5 10.52a4.8 4.8 0 010-3.04V5.41H1.83a8 8 0 000 7.18l2.67-2.07z"/>
              <path fill="#EA4335" d="M8.98 4.18c1.17 0 2.23.4 3.06 1.2l2.3-2.3A8 8 0 001.83 5.4L4.5 7.49a4.77 4.77 0 014.48-3.3z"/>
            </svg>
            Continue with Google
          </button>

          <p className="text-center text-sm text-[var(--text-secondary)] mt-6">
            Don't have an account?{' '}
            <Link to="/register" className="text-brand-600 hover:text-brand-700 font-semibold">
              Sign up free
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}
