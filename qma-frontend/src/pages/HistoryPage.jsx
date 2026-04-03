import React, { useState, useEffect, useCallback, useMemo } from 'react'
import { Search, Trash2, Download, RefreshCw, Loader2, Filter } from 'lucide-react'
import { historyAPI } from '../services/api'
import { useAuth } from '../context/AuthContext'
import { getUnitLabel, UNIT_CATEGORIES } from '../utils/units'
import toast from 'react-hot-toast'
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend
} from 'recharts'

const CATEGORY_COLORS = {
  LENGTH:      '#6366f1',
  WEIGHT:      '#f59e0b',
  TEMPERATURE: '#ef4444',
  VOLUME:      '#14b8a6',
  UNKNOWN:     '#94a3b8',
}

export default function HistoryPage() {
  const { user } = useAuth()
  const [history,  setHistory]  = useState([])
  const [loading,  setLoading]  = useState(true)
  const [search,   setSearch]   = useState('')
  const [filter,   setFilter]   = useState('ALL')
  const [clearing, setClearing] = useState(false)
  const [view,     setView]     = useState('table') // 'table' | 'chart'

  const fetchHistory = useCallback(async () => {
    setLoading(true)
    try {
      const data = await historyAPI.getMyHistory()
      setHistory(Array.isArray(data) ? data : [])
    } catch (err) {
      toast.error('Failed to load history')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetchHistory() }, [fetchHistory])

  // Filtered + searched list
  const filtered = useMemo(() => {
    return history.filter(item => {
      const matchCat = filter === 'ALL' || item.measurementType === filter
      const q = search.toLowerCase()
      const matchSearch = !q
        || (item.fromUnit || '').toLowerCase().includes(q)
        || (item.toUnit   || '').toLowerCase().includes(q)
        || String(item.inputValue).includes(q)
        || String(item.result).includes(q)
        || (item.measurementType || '').toLowerCase().includes(q)
      return matchCat && matchSearch
    })
  }, [history, filter, search])

  // Chart data
  const barData = useMemo(() => {
    const byDay = {}
    history.forEach(h => {
      const day = new Date(h.createdAt).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })
      byDay[day] = (byDay[day] || 0) + 1
    })
    return Object.entries(byDay).slice(-7).map(([day, count]) => ({ day, count }))
  }, [history])

  const pieData = useMemo(() => {
    const counts = {}
    history.forEach(h => { counts[h.measurementType || 'UNKNOWN'] = (counts[h.measurementType || 'UNKNOWN'] || 0) + 1 })
    return Object.entries(counts).map(([name, value]) => ({ name, value }))
  }, [history])

  const handleClear = async () => {
    if (!window.confirm('Delete ALL your conversion history? This cannot be undone.')) return
    setClearing(true)
    try {
      await historyAPI.clearHistory()
      setHistory([])
      toast.success('History cleared')
    } catch {
      toast.error('Failed to clear history')
    } finally {
      setClearing(false)
    }
  }

  const exportCSV = () => {
    if (!filtered.length) return toast.error('No data to export')
    const headers = ['ID','Username','Operation','From Unit','To Unit','Input Value','Result','Category','Date']
    const rows = filtered.map(h => [
      h.id, h.username, h.operation, h.fromUnit, h.toUnit,
      h.inputValue, h.result, h.measurementType,
      new Date(h.createdAt).toLocaleString()
    ])
    const csv = [headers, ...rows].map(r => r.join(',')).join('\n')
    const blob = new Blob([csv], { type: 'text/csv' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url; a.download = `qma-history-${user?.username}.csv`; a.click()
    URL.revokeObjectURL(url)
    toast.success('CSV exported!')
  }

  const categories = ['ALL', ...UNIT_CATEGORIES.map(c => c.label.toUpperCase())]

  return (
    <div className="p-6 space-y-6 max-w-6xl mx-auto">
      {/* Header */}
      <div className="flex flex-wrap items-center gap-3 justify-between">
        <div>
          <h1 className="font-display font-bold text-2xl text-[var(--text-primary)]">Conversion History</h1>
          <p className="text-[var(--text-secondary)] text-sm mt-0.5">
            {history.length} total conversion{history.length !== 1 ? 's' : ''}
          </p>
        </div>
        <div className="flex flex-wrap items-center gap-2">
          {/* View toggle */}
          <div className="flex rounded-xl overflow-hidden border border-[var(--border)]">
            {['table', 'chart'].map(v => (
              <button
                key={v}
                onClick={() => setView(v)}
                className={`px-4 py-2 text-sm font-medium transition-colors capitalize
                  ${view === v
                    ? 'bg-brand-600 text-white'
                    : 'bg-[var(--card-bg)] text-[var(--text-secondary)] hover:text-brand-600'
                  }`}
              >
                {v === 'table' ? '📋 Table' : '📊 Charts'}
              </button>
            ))}
          </div>
          <button onClick={exportCSV} className="btn-secondary text-sm py-2 px-3">
            <Download size={15} /> Export CSV
          </button>
          <button onClick={fetchHistory} className="btn-secondary text-sm py-2 px-3">
            <RefreshCw size={15} />
          </button>
          <button onClick={handleClear} disabled={clearing || !history.length} className="btn-danger text-sm py-2 px-3">
            {clearing ? <Loader2 size={15} className="animate-spin" /> : <Trash2 size={15} />}
            Clear
          </button>
        </div>
      </div>

      {/* Charts View */}
      {view === 'chart' && (
        <div className="grid lg:grid-cols-2 gap-6 animate-fade-in">
          <div className="card p-5">
            <h3 className="font-display font-semibold text-[var(--text-primary)] mb-4">Last 7 Days Activity</h3>
            {barData.length === 0 ? (
              <p className="text-center text-[var(--text-secondary)] py-10 text-sm">No data yet</p>
            ) : (
              <ResponsiveContainer width="100%" height={220}>
                <BarChart data={barData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="var(--border)" />
                  <XAxis dataKey="day" tick={{ fontSize: 12 }} />
                  <YAxis tick={{ fontSize: 12 }} allowDecimals={false} />
                  <Tooltip />
                  <Bar dataKey="count" fill="#6366f1" radius={[6,6,0,0]} name="Conversions" />
                </BarChart>
              </ResponsiveContainer>
            )}
          </div>
          <div className="card p-5">
            <h3 className="font-display font-semibold text-[var(--text-primary)] mb-4">By Category</h3>
            {pieData.length === 0 ? (
              <p className="text-center text-[var(--text-secondary)] py-10 text-sm">No data yet</p>
            ) : (
              <ResponsiveContainer width="100%" height={220}>
                <PieChart>
                  <Pie data={pieData} cx="50%" cy="50%" outerRadius={80} dataKey="value" label={({ name, percent }) => `${name} ${(percent*100).toFixed(0)}%`} labelLine={false}>
                    {pieData.map((entry) => (
                      <Cell key={entry.name} fill={CATEGORY_COLORS[entry.name] || '#94a3b8'} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            )}
          </div>
        </div>
      )}

      {/* Table View */}
      {view === 'table' && (
        <>
          {/* Search + Filter */}
          <div className="flex flex-wrap gap-3">
            <div className="relative flex-1 min-w-48">
              <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
              <input
                type="text"
                placeholder="Search conversions…"
                value={search}
                onChange={e => setSearch(e.target.value)}
                className="input-field pl-9 py-2.5"
              />
            </div>
            <div className="flex items-center gap-2 flex-wrap">
              <Filter size={15} className="text-gray-400" />
              {categories.map(cat => (
                <button
                  key={cat}
                  onClick={() => setFilter(cat)}
                  className={`px-3 py-1.5 rounded-lg text-xs font-medium transition-colors
                    ${filter === cat
                      ? 'bg-brand-600 text-white'
                      : 'bg-surface-100 dark:bg-surface-800 text-[var(--text-secondary)] hover:text-brand-600'
                    }`}
                >
                  {cat}
                </button>
              ))}
            </div>
          </div>

          {/* Table */}
          <div className="card overflow-hidden">
            {loading ? (
              <div className="p-8 space-y-3">
                {[1,2,3,4,5].map(i => <div key={i} className="h-12 shimmer rounded-xl" />)}
              </div>
            ) : filtered.length === 0 ? (
              <div className="text-center py-16 text-[var(--text-secondary)]">
                <div className="text-5xl mb-3">📭</div>
                <p className="font-semibold text-[var(--text-primary)]">No conversions found</p>
                <p className="text-sm mt-1">{search || filter !== 'ALL' ? 'Try adjusting your search or filter' : 'Start converting to build your history'}</p>
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="bg-surface-50 dark:bg-surface-800 border-b border-[var(--border)]">
                      {['Category', 'From', 'To', 'Result', 'Date'].map(h => (
                        <th key={h} className="text-left px-4 py-3 font-semibold text-[var(--text-secondary)] text-xs uppercase tracking-wide">{h}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-[var(--border)]">
                    {filtered.map((item) => (
                      <tr key={item.id} className="hover:bg-surface-50 dark:hover:bg-surface-800 transition-colors group">
                        <td className="px-4 py-3">
                          <span
                            className="badge text-white text-xs px-2 py-0.5 rounded-md"
                            style={{ background: CATEGORY_COLORS[item.measurementType] || '#94a3b8' }}
                          >
                            {item.measurementType || 'UNKNOWN'}
                          </span>
                        </td>
                        <td className="px-4 py-3 font-mono text-[var(--text-primary)]">
                          {item.inputValue} <span className="text-brand-500">{getUnitLabel(item.fromUnit)}</span>
                        </td>
                        <td className="px-4 py-3 font-mono text-[var(--text-secondary)]">
                          {getUnitLabel(item.toUnit)}
                        </td>
                        <td className="px-4 py-3 font-mono font-semibold text-[var(--text-primary)]">
                          {typeof item.result === 'number'
                            ? Number(item.result).toLocaleString(undefined, { maximumFractionDigits: 6 })
                            : item.result}
                        </td>
                        <td className="px-4 py-3 text-[var(--text-secondary)] text-xs whitespace-nowrap">
                          {new Date(item.createdAt).toLocaleString()}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                <div className="px-4 py-3 border-t border-[var(--border)] text-xs text-[var(--text-secondary)] bg-surface-50 dark:bg-surface-800">
                  Showing {filtered.length} of {history.length} entries
                </div>
              </div>
            )}
          </div>
        </>
      )}
    </div>
  )
}
