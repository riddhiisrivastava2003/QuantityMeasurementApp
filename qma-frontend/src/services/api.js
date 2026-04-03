import axios from 'axios'

const BASE_URL = '/api'  // proxied to localhost:8080 by Vite

// ─── Axios instance ────────────────────────────────────────────────────────
const api = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 10000,
})

// Attach JWT automatically from localStorage
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('qma_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// Handle 401 globally
api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      localStorage.removeItem('qma_token')
      // Let components handle redirect
    }
    return Promise.reject(err)
  }
)

// ─── Auth API ───────────────────────────────────────────────────────────────
export const authAPI = {
  login: async (username, password) => {
    const res = await api.post('/auth/login', { username, password })
    return res.data
  },

  register: async (username, password) => {
    const res = await api.post('/auth/register', { username, password })
    return res.data
  },

  logout: async (token) => {
    const res = await api.post('/auth/logout', {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    return res.data
  },

  getProfile: async (token) => {
    const res = await api.get('/auth/profile', {
      headers: { Authorization: `Bearer ${token}` }
    })
    return res.data
  },
}

// ─── Quantity / Conversion API ──────────────────────────────────────────────
export const quantityAPI = {
  // Convert units — public (no auth needed)
  convert: async ({ value, fromUnit, toUnit }) => {
    const res = await api.post('/quantity/convert', { value, fromUnit, toUnit })
    return res.data
  },

  // All operations (auth required)
  getAll: async () => {
    const res = await api.get('/quantity/all')
    return res.data
  },

  add: async (q1, q2) => {
    const res = await api.post('/quantity/add', { q1, q2 })
    return res.data
  },

  subtract: async (q1, q2) => {
    const res = await api.post('/quantity/subtract', { q1, q2 })
    return res.data
  },

  compare: async (q1, q2) => {
    const res = await api.post('/quantity/compare', { q1, q2 })
    return res.data
  },

  health: async () => {
    const res = await api.get('/quantity/test')
    return res.data
  },
}

// ─── History API ────────────────────────────────────────────────────────────
export const historyAPI = {
  // Save a history entry (called after a logged-in conversion)
  save: async (data) => {
    const res = await api.post('/history/save', data)
    return res.data
  },

  // Get current user's history
  getMyHistory: async () => {
    const res = await api.get('/history/my')
    return res.data
  },

  // Clear current user's history
  clearHistory: async () => {
    const res = await api.delete('/history/clear')
    return res.data
  },
}

export default api
