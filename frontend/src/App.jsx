import { useEffect, useState } from 'react';
import './index.css';
import './App.css';

function App() {
  const [todos, setTodos] = useState([]);
  
  // State für das Erstellen/Bearbeiten
  const [taskdescription, setTaskdescription] = useState("");
  const [dueDate, setDueDate] = useState("");
  const [priority, setPriority] = useState("MEDIUM");
  
  // Edit State
  const [editingId, setEditingId] = useState(null);
  
  // Filter States
  const [search, setSearch] = useState("");
  const [filterPriority, setFilterPriority] = useState("");
  const [filterCompleted, setFilterCompleted] = useState("");

  const fetchTodos = () => {
    // API Parameter zusammenbauen
    const query = new URLSearchParams();
    if (search) query.append("search", search);
    if (filterPriority) query.append("priority", filterPriority);
    if (filterCompleted !== "") query.append("completed", filterCompleted);
    
    fetch(`http://localhost:8080/tasks?${query.toString()}`)
      .then(res => res.json())
      .then(data => setTodos(data))
      .catch(console.error);
  };

  useEffect(() => {
    fetchTodos();
  }, [search, filterPriority, filterCompleted]); // Lädt automatisch bei Filter-Änderung nach

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!taskdescription.trim()) return;

    const payload = { 
      taskdescription, 
      dueDate, 
      priority 
    };

    if (editingId) {
      // Bearbeiten (PUT)
      fetch(`http://localhost:8080/tasks/${editingId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      }).then(() => {
        setEditingId(null);
        resetForm();
        fetchTodos();
      }).catch(console.error);
    } else {
      // Erstellen (POST)
      fetch("http://localhost:8080/tasks", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      }).then(() => {
        resetForm();
        fetchTodos();
      }).catch(console.error);
    }
  };

  const handleDelete = (id) => {
    fetch(`http://localhost:8080/tasks/${id}`, { method: "DELETE" })
      .then(() => fetchTodos())
      .catch(console.error);
  };

  const handleToggleComplete = (todo) => {
    fetch(`http://localhost:8080/tasks/${todo.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ...todo, completed: !todo.completed })
    }).then(() => fetchTodos()).catch(console.error);
  };

  const handleEdit = (todo) => {
    setEditingId(todo.id);
    setTaskdescription(todo.taskdescription || "");
    setDueDate(todo.dueDate || "");
    setPriority(todo.priority || "MEDIUM");
  };

  const resetForm = () => {
    setEditingId(null);
    setTaskdescription("");
    setDueDate("");
    setPriority("MEDIUM");
  };

  const isOverdue = (dateString) => {
    if (!dateString) return false;
    return new Date(dateString) < new Date(new Date().setHours(0,0,0,0));
  };

  return (
    <div className="app-container">
      <header className="header">
        <h1>🚀 Super ToDo-App</h1>
      </header>

      <main className="main-content">
        {/* Formular-Bereich */}
        <section className="form-section card">
          <h2>{editingId ? "Todo bearbeiten" : "Neues Todo anlegen"}</h2>
          <form onSubmit={handleSubmit} className="todo-form">
            <div className="form-group">
              <label>Beschreibung:</label>
              <input type="text" value={taskdescription} onChange={e => setTaskdescription(e.target.value)} required />
            </div>
            
            <div className="form-row">
              <div className="form-group">
                <label>Fälligkeit:</label>
                <input type="date" value={dueDate} onChange={e => setDueDate(e.target.value)} />
              </div>
              
              <div className="form-group">
                <label>Priorität:</label>
                <select value={priority} onChange={e => setPriority(e.target.value)}>
                  <option value="HIGH">🔥 Hoch</option>
                  <option value="MEDIUM">⚡ Mittel</option>
                  <option value="LOW">🐢 Niedrig</option>
                </select>
              </div>
            </div>
            
            <div className="button-group">
              <button type="submit" className="btn btn-primary">
                {editingId ? "Speichern" : "Hinzufügen"}
              </button>
              {editingId && (
                <button type="button" className="btn btn-secondary" onClick={resetForm}>Abbrechen</button>
              )}
            </div>
          </form>
        </section>

        {/* Filter-Bereich */}
        <section className="filter-section card">
          <div className="filter-grid">
            <input type="text" placeholder="🔍 Suchen..." value={search} onChange={e => setSearch(e.target.value)} />
            <select value={filterPriority} onChange={e => setFilterPriority(e.target.value)}>
              <option value="">Alle Prioritäten</option>
              <option value="HIGH">Hoch</option>
              <option value="MEDIUM">Mittel</option>
              <option value="LOW">Niedrig</option>
            </select>
            <select value={filterCompleted} onChange={e => setFilterCompleted(e.target.value)}>
              <option value="">Alle Status</option>
              <option value="false">Offen</option>
              <option value="true">Erledigt</option>
            </select>
          </div>
        </section>

        {/* Listen-Bereich */}
        <section className="todo-list">
          {todos.length === 0 ? (
            <p className="empty-state">Keine Todos gefunden! 🎉</p>
          ) : (
            todos.map((todo) => (
              <div key={todo.id} className={`todo-card ${todo.completed ? 'completed' : ''} ${!todo.completed && isOverdue(todo.dueDate) ? 'overdue' : ''}`}>
                <div className="todo-header">
                  <span className={`badge priority-${todo.priority.toLowerCase()}`}>
                    {todo.priority}
                  </span>
                  <span className="date-info">
                    {todo.dueDate && <span>Fällig: <b>{new Date(todo.dueDate).toLocaleDateString()}</b></span>}
                  </span>
                </div>
                
                <div className="todo-body">
                  <h3>{todo.taskdescription}</h3>
                </div>

                <div className="todo-footer">
                  <button onClick={() => handleToggleComplete(todo)} className="btn btn-icon">
                    {todo.completed ? '❌ Auf Offen setzen' : '✔️ Erledigen'}
                  </button>
                  <div className="action-buttons">
                    <button onClick={() => handleEdit(todo)} className="btn btn-edit">✏️ Edit</button>
                    <button onClick={() => handleDelete(todo.id)} className="btn btn-danger">🗑️ Delete</button>
                  </div>
                </div>
              </div>
            ))
          )}
        </section>
      </main>
    </div>
  );
}

export default App;
