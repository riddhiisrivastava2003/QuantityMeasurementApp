import React, { useState, useEffect, useCallback } from 'react'
import { useAuth } from '../context/AuthContext'
import ConverterWidget from '../components/dashboard/ConverterWidget'
import RecentHistory from '../components/dashboard/RecentHistory'
import StatsCards from '../components/dashboard/StatsCards'
import { historyAPI } from '../services/api'

export default function DashboardPage() {
  const { user, isAuthenticated } = useAuth()
  const [history, setHistory] = useState([])
  const [loadingHistory, setLoadingHistory] = useState(false)

  const fetchHistory = useCallback(async () => {
    if (!isAuthenticated) return
    setLoadingHistory(true)
    try {
      const data = await historyAPI.getMyHistory()
      setHistory(Array.isArray(data) ? data : [])
    } catch {
      // silent — maybe history service is down
    } finally {
      setLoadingHistory(false)
    }
  }, [isAuthenticated])

  useEffect(() => { fetchHistory() }, [fetchHistory])

  const greeting = () => {
    const h = new Date().getHours()
    if (h < 12) return 'Good morning'
    if (h < 18) return 'Good afternoon'
    return 'Good evening'
  }

  return (
    <div className="p-6 space-y-6 max-w-6xl mx-auto">
      {/* Header */}
      <div className="animate-fade-in">
        <h1 className="font-display font-bold text-2xl text-[var(--text-primary)]">
          {isAuthenticated && user
            ? `${greeting()}, ${user.username} 👋`
            : 'Quantity Converter'}
        </h1>
        <p className="text-[var(--text-secondary)] text-sm mt-1">
          {isAuthenticated
            ? 'Convert units instantly. All conversions are saved to your history.'
            : 'Convert units instantly. Sign in to track your history.'}
        </p>
      </div>

      {/* Stats — only when logged in with history */}
      {isAuthenticated && history.length > 0 && (
        <StatsCards history={history} />
      )}

      {/* Main grid */}
      <div className={`grid gap-6 ${isAuthenticated ? 'lg:grid-cols-[1fr_360px]' : ''}`}>
        {/* Converter */}
        <div className="animate-slide-up">
          <h2 className="font-display font-semibold text-base text-[var(--text-primary)] mb-3">
            Unit Converter
          </h2>
          <ConverterWidget onConversionDone={fetchHistory} />
        </div>

        {/* Recent history — only when logged in */}
        {isAuthenticated && (
          <div className="animate-slide-up" style={{ animationDelay: '0.1s' }}>
            <h2 className="font-display font-semibold text-base text-[var(--text-primary)] mb-3">
              Recent Activity
            </h2>
            <RecentHistory items={history} loading={loadingHistory} />
          </div>
        )}
      </div>

      {/* Login nudge when not authenticated */}
      {!isAuthenticated && (
        <div className="card p-6 border-dashed border-brand-200 dark:border-brand-800 text-center animate-fade-in">
          <p className="text-2xl mb-2">🔒</p>
          <h3 className="font-display font-semibold text-[var(--text-primary)] mb-1">Track Your Conversions</h3>
          <p className="text-sm text-[var(--text-secondary)] mb-4">Create a free account to save history, view analytics, and more.</p>
          <div className="flex gap-3 justify-center">
            <a href="/register" className="btn-primary text-sm py-2 px-5">Sign Up Free</a>
            <a href="/login" className="btn-secondary text-sm py-2 px-5">Sign In</a>
          </div>
        </div>
      )}
    </div>
  )
}
