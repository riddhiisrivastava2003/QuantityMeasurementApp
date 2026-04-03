import React from 'react'
import { useNavigate } from 'react-router-dom'
import { ArrowLeft, Zap } from 'lucide-react'

export default function NotFoundPage() {
  const navigate = useNavigate()
  return (
    <div className="min-h-screen flex items-center justify-center bg-[var(--bg-primary)] hero-bg">
      <div className="text-center animate-slide-up px-4">
        <div className="w-20 h-20 rounded-3xl bg-gradient-to-br from-brand-500 to-accent-500 flex items-center justify-center mx-auto mb-6 shadow-glow">
          <Zap size={32} className="text-white" />
        </div>
        <h1 className="font-display font-bold text-6xl text-[var(--text-primary)] mb-2">404</h1>
        <p className="text-xl text-[var(--text-secondary)] mb-6">Page not found</p>
        <button onClick={() => navigate('/')} className="btn-primary mx-auto">
          <ArrowLeft size={16} /> Back to Home
        </button>
      </div>
    </div>
  )
}
