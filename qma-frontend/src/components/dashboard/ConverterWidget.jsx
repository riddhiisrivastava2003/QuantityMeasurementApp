import React, { useState, useEffect, useCallback } from 'react'
import { ArrowLeftRight, Loader2, RefreshCw } from 'lucide-react'
import { quantityAPI, historyAPI } from '../../services/api'
import { useAuth } from '../../context/AuthContext'
import { UNIT_CATEGORIES, getCategoryForUnit } from '../../utils/units'
import { useDebounce } from '../../hooks/useDebounce'
import toast from 'react-hot-toast'

export default function ConverterWidget({ onConversionDone }) {
  const { isAuthenticated, user } = useAuth()

  const [category,   setCategory]  = useState('length')
  const [value,      setValue]     = useState('')
  const [fromUnit,   setFromUnit]  = useState('FEET')
  const [toUnit,     setToUnit]    = useState('METER')
  const [result,     setResult]    = useState(null)
  const [loading,    setLoading]   = useState(false)
  const [resultKey,  setResultKey] = useState(0)   // for re-trigger animation

  const debouncedValue = useDebounce(value, 500)

  const currentCategory = UNIT_CATEGORIES.find(c => c.id === category)

  // When category changes, reset units to first two of that category
  useEffect(() => {
    const cat = UNIT_CATEGORIES.find(c => c.id === category)
    if (cat && cat.units.length >= 2) {
      setFromUnit(cat.units[0].value)
      setToUnit(cat.units[1].value)
      setResult(null)
    }
  }, [category])

  // Auto-convert on debounced value / unit change
  useEffect(() => {
    if (debouncedValue && !isNaN(Number(debouncedValue)) && Number(debouncedValue) !== 0) {
      doConvert(Number(debouncedValue), fromUnit, toUnit)
    } else {
      setResult(null)
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [debouncedValue, fromUnit, toUnit])

  const doConvert = useCallback(async (val, from, to) => {
    if (!val || isNaN(val)) return
    setLoading(true)
    try {
      const data = await quantityAPI.convert({ value: val, fromUnit: from, toUnit: to })
      const res = typeof data === 'object' ? data.result : data
      setResult(res)
      setResultKey(k => k + 1)

      // Save to history if logged in
      if (isAuthenticated && user) {
        const cat = getCategoryForUnit(from)
        await historyAPI.save({
          username:        user.username,
          operation:       'CONVERT',
          fromUnit:        from,
          toUnit:          to,
          inputValue:      val,
          result:          res,
          measurementType: cat?.label?.toUpperCase() || 'UNKNOWN',
        }).catch(() => {})  // don't disrupt UX if history fails
        onConversionDone?.()
      }
    } catch (err) {
      const msg = err.response?.data?.error || 'Conversion failed'
      toast.error(msg)
      setResult(null)
    } finally {
      setLoading(false)
    }
  }, [isAuthenticated, user, onConversionDone])

  const handleSwap = () => {
    setFromUnit(toUnit)
    setToUnit(fromUnit)
    setResult(null)
    if (value) doConvert(Number(value), toUnit, fromUnit)
  }

  const handleReset = () => {
    setValue('')
    setResult(null)
  }

  const fromUnitLabel = currentCategory?.units.find(u => u.value === fromUnit)?.label || fromUnit
  const toUnitLabel   = currentCategory?.units.find(u => u.value === toUnit)?.label   || toUnit

  return (
    <div className="card p-6 space-y-5">
      {/* Category Tabs */}
      <div className="flex flex-wrap gap-2">
        {UNIT_CATEGORIES.map(cat => (
          <button
            key={cat.id}
            onClick={() => setCategory(cat.id)}
            className={`flex items-center gap-1.5 px-3.5 py-1.5 rounded-xl text-sm font-medium transition-all duration-200
              ${category === cat.id
                ? 'bg-brand-600 text-white shadow-glow-sm'
                : 'bg-surface-100 dark:bg-surface-800 text-gray-600 dark:text-gray-400 hover:bg-brand-50 dark:hover:bg-surface-700'
              }`}
          >
            <span>{cat.icon}</span>
            {cat.label}
          </button>
        ))}
      </div>

      {/* Value Input */}
      <div>
        <label className="label">Enter value</label>
        <div className="relative">
          <input
            type="number"
            value={value}
            onChange={e => setValue(e.target.value)}
            placeholder="e.g. 100"
            className="input-field pr-10 font-mono text-lg"
          />
          {value && (
            <button onClick={handleReset} className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors">
              <RefreshCw size={16} />
            </button>
          )}
        </div>
      </div>

      {/* Unit Selectors */}
      <div className="grid grid-cols-[1fr_auto_1fr] gap-2 items-end">
        <div>
          <label className="label">From</label>
          <select
            value={fromUnit}
            onChange={e => setFromUnit(e.target.value)}
            className="select-field"
          >
            {currentCategory?.units.map(u => (
              <option key={u.value} value={u.value}>{u.label}</option>
            ))}
          </select>
        </div>

        <button
          onClick={handleSwap}
          className="mb-0.5 w-10 h-10 rounded-xl bg-brand-50 dark:bg-surface-800 flex items-center justify-center
                     text-brand-600 hover:bg-brand-100 dark:hover:bg-surface-700 transition-all duration-200
                     hover:scale-110 active:scale-95"
          title="Swap units"
        >
          <ArrowLeftRight size={16} />
        </button>

        <div>
          <label className="label">To</label>
          <select
            value={toUnit}
            onChange={e => setToUnit(e.target.value)}
            className="select-field"
          >
            {currentCategory?.units.map(u => (
              <option key={u.value} value={u.value}>{u.label}</option>
            ))}
          </select>
        </div>
      </div>

      {/* Result */}
      <div className={`rounded-2xl p-5 transition-all duration-200
        ${result !== null
          ? 'bg-gradient-to-br from-brand-50 to-accent-50/50 dark:from-brand-900/30 dark:to-accent-900/20 border border-brand-100 dark:border-brand-800'
          : 'bg-surface-50 dark:bg-surface-800 border border-dashed border-gray-200 dark:border-surface-700'
        }`}
      >
        {loading ? (
          <div className="flex items-center justify-center gap-3 py-2 text-brand-600">
            <Loader2 size={20} className="animate-spin" />
            <span className="text-sm font-medium">Converting…</span>
          </div>
        ) : result !== null ? (
          <div key={resultKey} className="result-pop space-y-1">
            <p className="text-xs text-[var(--text-secondary)] font-medium uppercase tracking-wider">Result</p>
            <div className="flex items-baseline gap-2 flex-wrap">
              <span className="font-mono font-bold text-3xl text-[var(--text-primary)]">
                {typeof result === 'number' ? result.toLocaleString(undefined, { maximumFractionDigits: 6 }) : result}
              </span>
              <span className="text-brand-600 dark:text-brand-400 font-semibold">{toUnitLabel}</span>
            </div>
            <p className="text-sm text-[var(--text-secondary)]">
              {value} {fromUnitLabel} = {typeof result === 'number' ? result.toLocaleString(undefined, { maximumFractionDigits: 6 }) : result} {toUnitLabel}
            </p>
            {isAuthenticated && (
              <p className="text-xs text-accent-600 dark:text-accent-400 mt-1 flex items-center gap-1">
                <span>✓</span> Saved to your history
              </p>
            )}
          </div>
        ) : (
          <p className="text-center text-[var(--text-secondary)] text-sm py-2">
            Enter a value above to see the result
          </p>
        )}
      </div>
    </div>
  )
}
