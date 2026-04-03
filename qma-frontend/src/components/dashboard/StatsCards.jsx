import React from 'react'
import { TrendingUp, Zap, Clock, BarChart3 } from 'lucide-react'

export default function StatsCards({ history = [] }) {
  const totalConversions = history.length

  // Most used category
  const categoryCounts = history.reduce((acc, item) => {
    const k = item.measurementType || 'UNKNOWN'
    acc[k] = (acc[k] || 0) + 1
    return acc
  }, {})
  const topCategory = Object.entries(categoryCounts).sort((a,b) => b[1]-a[1])[0]?.[0] || '—'

  // Today's conversions
  const today = new Date().toDateString()
  const todayCount = history.filter(h => new Date(h.createdAt).toDateString() === today).length

  // Last used
  const lastUsed = history[0]
    ? new Date(history[0].createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    : '—'

  const cards = [
    { icon: Zap,        label: 'Total Conversions', value: totalConversions, color: 'from-brand-500 to-brand-600',   bg: 'bg-brand-50 dark:bg-brand-900/20'   },
    { icon: TrendingUp, label: 'Top Category',       value: topCategory,     color: 'from-amber-500 to-orange-500',  bg: 'bg-amber-50 dark:bg-amber-900/20'   },
    { icon: BarChart3,  label: "Today's Count",      value: todayCount,      color: 'from-teal-500 to-cyan-500',     bg: 'bg-teal-50 dark:bg-teal-900/20'     },
    { icon: Clock,      label: 'Last Active',        value: lastUsed,        color: 'from-rose-500 to-pink-500',     bg: 'bg-rose-50 dark:bg-rose-900/20'     },
  ]

  return (
    <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
      {cards.map(({ icon: Icon, label, value, color, bg }) => (
        <div key={label} className="card p-4 flex items-center gap-3 animate-fade-in">
          <div className={`w-10 h-10 rounded-xl ${bg} flex items-center justify-center flex-shrink-0`}>
            <div className={`bg-gradient-to-br ${color} rounded-lg w-7 h-7 flex items-center justify-center`}>
              <Icon size={14} className="text-white" />
            </div>
          </div>
          <div className="min-w-0">
            <p className="text-xs text-[var(--text-secondary)] font-medium">{label}</p>
            <p className="font-display font-bold text-lg text-[var(--text-primary)] truncate">{value}</p>
          </div>
        </div>
      ))}
    </div>
  )
}
