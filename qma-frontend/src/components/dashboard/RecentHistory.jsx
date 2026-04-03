import React from 'react'
import { useNavigate } from 'react-router-dom'
import { ArrowRight, History } from 'lucide-react'
import { getUnitLabel } from '../../utils/units'

export default function RecentHistory({ items = [], loading }) {
  const navigate = useNavigate()

  if (loading) {
    return (
      <div className="card p-5 space-y-3">
        <div className="h-5 w-32 shimmer rounded" />
        {[1,2,3].map(i => <div key={i} className="h-14 shimmer rounded-xl" />)}
      </div>
    )
  }

  return (
    <div className="card p-5">
      <div className="flex items-center justify-between mb-4">
        <h3 className="font-display font-semibold text-[var(--text-primary)] flex items-center gap-2">
          <History size={17} className="text-brand-500" />
          Recent Conversions
        </h3>
        <button
          onClick={() => navigate('/history')}
          className="text-xs text-brand-600 hover:text-brand-700 dark:text-brand-400 font-medium flex items-center gap-1"
        >
          View all <ArrowRight size={12} />
        </button>
      </div>

      {items.length === 0 ? (
        <div className="text-center py-8 text-[var(--text-secondary)]">
          <div className="text-3xl mb-2">📭</div>
          <p className="text-sm">No conversions yet.</p>
          <p className="text-xs mt-1">Your history will appear here.</p>
        </div>
      ) : (
        <div className="space-y-2">
          {items.slice(0, 5).map((item) => (
            <div
              key={item.id}
              className="flex items-center gap-3 p-3 rounded-xl bg-surface-50 dark:bg-surface-800
                         hover:bg-brand-50 dark:hover:bg-surface-700 transition-colors group"
            >
              <div className="w-8 h-8 rounded-lg bg-brand-100 dark:bg-brand-900/30 flex items-center justify-center flex-shrink-0">
                <span className="text-sm">🔄</span>
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-[var(--text-primary)] truncate">
                  {item.inputValue} {getUnitLabel(item.fromUnit)} → {item.result} {getUnitLabel(item.toUnit)}
                </p>
                <p className="text-xs text-[var(--text-secondary)]">
                  {item.measurementType} · {new Date(item.createdAt).toLocaleString()}
                </p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
