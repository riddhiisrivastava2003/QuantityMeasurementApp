import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import App from './App.jsx'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <App />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3000,
          style: {
            fontFamily: 'Outfit, sans-serif',
            fontSize: '14px',
            fontWeight: '500',
            borderRadius: '12px',
            padding: '12px 16px',
          },
          success: {
            style: { background: '#ecfdf5', color: '#065f46', border: '1px solid #d1fae5' },
            iconTheme: { primary: '#059669', secondary: '#fff' },
          },
          error: {
            style: { background: '#fef2f2', color: '#7f1d1d', border: '1px solid #fecaca' },
            iconTheme: { primary: '#dc2626', secondary: '#fff' },
          },
        }}
      />
    </BrowserRouter>
  </React.StrictMode>
)
