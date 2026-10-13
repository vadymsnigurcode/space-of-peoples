import { useEffect, useState } from 'react'
import './App.css'

async function fetchWorkers(signal) {
  const response = await fetch('/api/jdbc/workers', { signal })
  if (!response.ok) {
    throw new Error('Worker request failed')
  }
  return response.json()
}

function getInitials(worker) {
  return [worker.firstName, worker.lastName]
    .filter(Boolean)
    .map((name) => name[0])
    .join('')
    .slice(0, 2)
    .toUpperCase()
}

function App() {
  const [workers, setWorkers] = useState([])
  const [query, setQuery] = useState('')
  const [loadState, setLoadState] = useState('loading')

  useEffect(() => {
    const controller = new AbortController()
    fetchWorkers(controller.signal)
      .then((data) => {
        setWorkers(data)
        setLoadState('ready')
      })
      .catch((error) => {
        if (error.name !== 'AbortError') {
          setLoadState('error')
        }
      })

    return () => controller.abort()
  }, [])

  async function reloadWorkers() {
    setLoadState('loading')
    try {
      setWorkers(await fetchWorkers())
      setLoadState('ready')
    } catch {
      setLoadState('error')
    }
  }

  const normalizedQuery = query.trim().toLowerCase()
  const filteredWorkers = workers.filter((worker) =>
    [worker.firstName, worker.lastName, worker.email, worker.id]
      .filter(Boolean)
      .some((value) => String(value).toLowerCase().includes(normalizedQuery)),
  )

  return (
    <div className="app-shell">
      <header className="topbar">
        <a className="brand" href="/" aria-label="Space of Peoples home">
          <span className="brand-mark" aria-hidden="true">S</span>
          <span className="brand-name">
            Space of Peoples
            <span>PEOPLE OPERATIONS</span>
          </span>
        </a>
        <div className="system-tag">
          <span className="system-dot" />
          JDBC DIRECTORY
        </div>
      </header>

      <main className="directory-page">
        <div className="breadcrumb">
          <span>PEOPLE</span>
          <span className="breadcrumb-divider">/</span>
          <span className="breadcrumb-current">DIRECTORY</span>
        </div>

        <section className="page-heading" aria-labelledby="page-title">
          <div>
            <p className="eyebrow">TEAM RECORDS</p>
            <h1 id="page-title">Workers</h1>
          </div>
          <div className="total-block" aria-label={`${workers.length} workers`}>
            <span>ON FILE</span>
            <strong>{String(workers.length).padStart(2, '0')}</strong>
          </div>
        </section>

        <section className="directory-panel" aria-label="Worker directory">
          <div className="directory-toolbar">
            <label className="search-field">
              <span className="sr-only">Search workers</span>
              <input
                type="search"
                placeholder="Search workers"
                value={query}
                onChange={(event) => setQuery(event.target.value)}
              />
            </label>
            <div className="toolbar-actions">
              <span className="result-count">
                {filteredWorkers.length} <span>/ {workers.length}</span>
              </span>
              <button
                className="refresh-button"
                type="button"
                onClick={reloadWorkers}
                disabled={loadState === 'loading'}
              >
                Refresh
              </button>
            </div>
          </div>

          {loadState === 'loading' && workers.length === 0 ? (
            <div className="state-panel" role="status">Loading workers…</div>
          ) : loadState === 'error' ? (
            <div className="state-panel error-panel" role="alert">
              <strong>Worker records are unavailable.</strong>
              <span>Check the application and database, then try again.</span>
              <button type="button" onClick={reloadWorkers}>Try again</button>
            </div>
          ) : workers.length === 0 ? (
            <div className="state-panel">No workers on file.</div>
          ) : filteredWorkers.length === 0 ? (
            <div className="state-panel">No workers match “{query}”.</div>
          ) : (
            <div className="table-scroll">
              <table>
                <thead>
                  <tr>
                    <th scope="col">ID</th>
                    <th scope="col">WORKER</th>
                    <th scope="col">EMAIL</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredWorkers.map((worker, index) => (
                    <tr key={worker.id}>
                      <td className="worker-id">{String(worker.id).padStart(4, '0')}</td>
                      <td>
                        <div className="worker-name-cell">
                          <span className={`worker-avatar avatar-tone-${index % 4}`} aria-hidden="true">
                            {getInitials(worker) || 'W'}
                          </span>
                          <span className="worker-name">
                            {[worker.firstName, worker.lastName].filter(Boolean).join(' ') || 'Unnamed worker'}
                          </span>
                        </div>
                      </td>
                      <td className="worker-email">{worker.email || '—'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          <footer className="panel-footer">
            <span>SPACE OF PEOPLES</span>
            <span>DIRECTORY · JDBC</span>
          </footer>
        </section>
      </main>
    </div>
  )
}

export default App
