import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Zap, ArrowRight, Lock, History, BarChart3, Moon, Sun } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import ConverterWidget from '../components/dashboard/ConverterWidget'

const FEATURES = [
  { icon: Zap,      title: 'Instant Conversion',   desc: 'Real-time results as you type — no button needed.' },
  { icon: Lock,     title: 'Secure Auth',           desc: 'JWT-based login with optional Google OAuth.' },
  { icon: History,  title: 'Full History',          desc: 'Every conversion tracked when you\'re signed in.' },
  { icon: BarChart3, title: 'Analytics',            desc: 'Charts and stats to see your conversion patterns.' },
]

export default function LandingPage() {
  const navigate  = useNavigate()
  const { isAuthenticated, darkMode, toggleDark } = useAuth()

  return (
    <div className="min-h-screen bg-[var(--bg-primary)] hero-bg grid-bg">
      {/* Topbar */}
      <nav className="sticky top-0 z-50 backdrop-blur-sm bg-[var(--nav-bg)] border-b border-[var(--border)]">
        <div className="max-w-6xl mx-auto px-4 h-14 flex items-center gap-4">
          <div className="flex items-center gap-2.5 font-display font-bold text-lg text-[var(--text-primary)]">
            <div className="w-8 h-8 rounded-xl bg-gradient-to-br from-brand-500 to-accent-500 flex items-center justify-center">
              <Zap size={16} className="text-white" />
            </div>
            QMA
          </div>
          <div className="flex-1" />
          <button onClick={toggleDark} className="p-2 rounded-lg text-gray-500 hover:text-brand-600 hover:bg-brand-50 dark:hover:bg-surface-800 transition-colors">
            {darkMode ? <Sun size={17} /> : <Moon size={17} />}
          </button>
          {isAuthenticated ? (
            <button onClick={() => navigate('/dashboard')} className="btn-primary text-sm py-2 px-4">
              Dashboard <ArrowRight size={14} />
            </button>
          ) : (
            <div className="flex items-center gap-2">
              <button onClick={() => navigate('/login')} className="btn-secondary text-sm py-2 px-4">Sign In</button>
              <button onClick={() => navigate('/register')} className="btn-primary text-sm py-2 px-4">Get Started</button>
            </div>
          )}
        </div>
      </nav>

      <div className="max-w-6xl mx-auto px-4 py-12">
        {/* Hero */}
        <div className="text-center mb-12 animate-fade-in">
          <div className="inline-flex items-center gap-2 bg-brand-50 dark:bg-brand-900/30 text-brand-600 dark:text-brand-400 text-xs font-semibold px-3 py-1.5 rounded-full mb-5 border border-brand-100 dark:border-brand-800">
            <span className="w-1.5 h-1.5 rounded-full bg-brand-500 animate-pulse-slow" />
            Microservices · Spring Boot · React
          </div>
          <h1 className="font-display font-bold text-4xl sm:text-5xl text-[var(--text-primary)] mb-4 leading-tight">
            Quantity Measurement<br />
            <span className="gradient-text">& Conversion App</span>
          </h1>
          <p className="text-lg text-[var(--text-secondary)] max-w-xl mx-auto leading-relaxed">
            Convert units instantly across Length, Weight, Temperature & Volume.
            Log in to track every conversion in your personal history.
          </p>

          {!isAuthenticated && (
            <div className="flex flex-wrap gap-3 justify-center mt-7">
              <button onClick={() => navigate('/register')} className="btn-primary text-sm px-6 py-3">
                Create Free Account <ArrowRight size={15} />
              </button>
              <button onClick={() => navigate('/login')} className="btn-secondary text-sm px-6 py-3">
                Sign In
              </button>
            </div>
          )}
        </div>

        {/* Converter */}
        <div className="max-w-xl mx-auto mb-16 animate-slide-up">
          {!isAuthenticated && (
            <div className="flex items-center gap-2 bg-amber-50 dark:bg-amber-900/20 border border-amber-100 dark:border-amber-800 rounded-xl px-4 py-2.5 mb-4 text-sm text-amber-700 dark:text-amber-400">
              <Lock size={14} />
              <span>Sign in to save conversion history</span>
              <button onClick={() => navigate('/login')} className="ml-auto font-semibold underline hover:no-underline">Login →</button>
            </div>
          )}
          <ConverterWidget />
        </div>

        {/* Features */}
        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-5 mb-16">
          {FEATURES.map(({ icon: Icon, title, desc }) => (
            <div key={title} className="card p-5 hover:shadow-glow transition-shadow duration-300 animate-fade-in">
              <div className="w-10 h-10 rounded-xl bg-brand-50 dark:bg-brand-900/30 flex items-center justify-center mb-3">
                <Icon size={18} className="text-brand-600 dark:text-brand-400" />
              </div>
              <h3 className="font-display font-semibold text-[var(--text-primary)] mb-1">{title}</h3>
              <p className="text-sm text-[var(--text-secondary)] leading-relaxed">{desc}</p>
            </div>
          ))}
        </div>

        {/* CTA banner */}
        {!isAuthenticated && (
          <div className="rounded-3xl bg-gradient-to-br from-brand-600 to-brand-700 p-8 text-center shadow-glow animate-fade-in">
            <h2 className="font-display font-bold text-2xl text-white mb-2">Ready to track your conversions?</h2>
            <p className="text-brand-200 mb-5">Sign up free — no credit card needed.</p>
            <button onClick={() => navigate('/register')} className="bg-white text-brand-700 font-bold px-8 py-3 rounded-xl hover:bg-brand-50 transition-colors">
              Get Started Free
            </button>
          </div>
        )}
      </div>

      {/* Footer */}
      <footer className="border-t border-[var(--border)] py-6 text-center text-sm text-[var(--text-secondary)]">
        <p>QMA · Built with Spring Boot Microservices + React</p>
      </footer>
    </div>
  )
}
