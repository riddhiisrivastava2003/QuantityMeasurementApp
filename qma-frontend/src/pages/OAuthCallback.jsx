// OAuthCallback.jsx — handles Google OAuth redirect with token in URL
import React, { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Loader2, Zap } from 'lucide-react'
import toast from 'react-hot-toast'

export function OAuthCallback() {
  const navigate = useNavigate()
  const [params] = useSearchParams()

  useEffect(() => {
    const token = params.get('token')
    if (token) {
      localStorage.setItem('qma_token', token)
      toast.success('Signed in with Google!')
      // Reload so AuthContext picks up the token
      window.location.href = '/dashboard'
    } else {
      toast.error('OAuth login failed')
      navigate('/login')
    }
  }, [params, navigate])

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-[var(--bg-primary)]">
      <div className="flex flex-col items-center gap-4 animate-fade-in">
        <div className="w-14 h-14 rounded-2xl bg-gradient-to-br from-brand-500 to-accent-500 flex items-center justify-center shadow-glow">
          <Zap size={24} className="text-white" />
        </div>
        <div className="flex items-center gap-2 text-[var(--text-secondary)]">
          <Loader2 size={18} className="animate-spin" />
          <span className="font-medium">Completing sign in…</span>
        </div>
      </div>
    </div>
  )
}

export default OAuthCallback
