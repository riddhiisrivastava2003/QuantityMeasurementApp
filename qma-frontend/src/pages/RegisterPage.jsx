import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Eye, EyeOff, UserPlus, Zap, Loader2, Check } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import toast from 'react-hot-toast'

export default function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()

  const [form,    setForm]    = useState({ username: '', password: '', confirm: '' })
  const [show,    setShow]    = useState(false)
  const [loading, setLoading] = useState(false)

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }))

  const pwStrength = (() => {
    const p = form.password
    if (!p) return 0
    let s = 0
    if (p.length >= 6) s++
    if (p.length >= 10) s++
    if (/[A-Z]/.test(p)) s++
    if (/[0-9]/.test(p)) s++
    if (/[^a-zA-Z0-9]/.test(p)) s++
    return s
  })()
  const strengthLabel = ['', 'Weak', 'Fair', 'Good', 'Strong', 'Very Strong'][pwStrength]
  const strengthColor = ['', 'bg-red-400', 'bg-orange-400', 'bg-yellow-400', 'bg-green-400', 'bg-emerald-500'][pwStrength]

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.username.trim()) return toast.error('Username is required')
    if (form.password.length < 6) return toast.error('Password must be at least 6 characters')
    if (form.password !== form.confirm) return toast.error('Passwords do not match')

    setLoading(true)
    try {
      await register(form.username.trim(), form.password)
      toast.success('Account created! Welcome 🎉')
      navigate('/dashboard')
    } catch (err) {
      const msg = err.response?.data?.error || err.message || 'Registration failed'
      toast.error(msg)
    } finally {
      setLoading(false)
    }
  }

  const perks = [
    'Save unlimited conversion history',
    'Personal dashboard & analytics',
    'Google OAuth login support',
    'Completely free forever',
  ]

  return (
    <div className="min-h-screen flex hero-bg bg-[var(--bg-primary)]">
      {/* Left panel */}
      <div className="hidden lg:flex flex-col justify-between w-1/2 bg-gradient-to-br from-accent-600 to-brand-700 p-12 text-white">
        <Link to="/" className="flex items-center gap-2.5 font-display font-bold text-xl">
          <div className="w-9 h-9 rounded-xl bg-white/20 flex items-center justify-center">
            <Zap size={18} />
          </div>
          QMA
        </Link>
        <div className="space-y-6">
          <h2 className="font-display font-bold text-4xl leading-tight">
            Start measuring.<br />Start tracking.
          </h2>
          <ul className="space-y-3">
            {perks.map(p => (
              <li key={p} className="flex items-center gap-3 text-emerald-100">
                <div className="w-5 h-5 rounded-full bg-white/20 flex items-center justify-center flex-shrink-0">
                  <Check size={12} />
                </div>
                {p}
              </li>
            ))}
          </ul>
        </div>
        <p className="text-emerald-300 text-sm">© 2025 QMA · Spring Boot Microservices</p>
      </div>

      {/* Right panel — form */}
      <div className="flex-1 flex items-center justify-center px-6 py-12">
        <div className="w-full max-w-sm animate-slide-up">
          <Link to="/" className="flex items-center gap-2 font-display font-bold text-lg text-[var(--text-primary)] mb-8 lg:hidden">
            <div className="w-8 h-8 rounded-xl bg-gradient-to-br from-brand-500 to-accent-500 flex items-center justify-center">
              <Zap size={16} className="text-white" />
            </div>
            QMA
          </Link>

          <h1 className="font-display font-bold text-2xl text-[var(--text-primary)] mb-1">Create account</h1>
          <p className="text-[var(--text-secondary)] text-sm mb-7">Join QMA — it's free</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="label">Username</label>
              <input
                type="text"
                name="username"
                value={form.username}
                onChange={handleChange}
                placeholder="Choose a username"
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
                  placeholder="At least 6 characters"
                  className="input-field pr-10"
                  autoComplete="new-password"
                />
                <button type="button" onClick={() => setShow(s => !s)} className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                  {show ? <EyeOff size={17} /> : <Eye size={17} />}
                </button>
              </div>
              {/* Password strength bar */}
              {form.password && (
                <div className="mt-2 space-y-1">
                  <div className="flex gap-1">
                    {[1,2,3,4,5].map(i => (
                      <div key={i} className={`h-1 flex-1 rounded-full transition-colors duration-300 ${i <= pwStrength ? strengthColor : 'bg-gray-200 dark:bg-gray-700'}`} />
                    ))}
                  </div>
                  <p className={`text-xs ${pwStrength <= 1 ? 'text-red-500' : pwStrength <= 3 ? 'text-amber-500' : 'text-green-500'}`}>{strengthLabel}</p>
                </div>
              )}
            </div>

            <div>
              <label className="label">Confirm Password</label>
              <input
                type="password"
                name="confirm"
                value={form.confirm}
                onChange={handleChange}
                placeholder="Repeat your password"
                className={`input-field ${form.confirm && form.confirm !== form.password ? 'ring-2 ring-red-400 border-transparent' : ''}`}
                autoComplete="new-password"
              />
              {form.confirm && form.confirm !== form.password && (
                <p className="text-xs text-red-500 mt-1">Passwords do not match</p>
              )}
            </div>

            <button type="submit" disabled={loading} className="btn-primary w-full py-3 mt-2">
              {loading ? <Loader2 size={18} className="animate-spin" /> : <UserPlus size={18} />}
              {loading ? 'Creating account…' : 'Create Account'}
            </button>
          </form>

          <p className="text-center text-sm text-[var(--text-secondary)] mt-6">
            Already have an account?{' '}
            <Link to="/login" className="text-brand-600 hover:text-brand-700 font-semibold">
              Sign in
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}
