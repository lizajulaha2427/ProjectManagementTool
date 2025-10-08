import React, { useEffect, useState } from 'react';

const API_BASE = "http://localhost:8080/tasks";

const TaskList = () => {
  const [tasks, setTasks] = useState([]);
  const [form, setForm] = useState({ name: '', status: '', projectId: '' });
  const [editingId, setEditingId] = useState(null);

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = () => {
    fetch(API_BASE)
      .then(res => res.json())
      .then(data => {
        const normalized = data.map(task => ({
          ...task,
          projectId: task.project_id
        }));
        setTasks(normalized);
      })
      .catch(console.error);
  };

  const handleSubmit = e => {
    e.preventDefault();
    const method = editingId ? "PUT" : "POST";
    const url = editingId ? `${API_BASE}/${editingId}` : API_BASE;

    const payload = {
      name: form.name,
      status: form.status,
      project_id: parseInt(form.projectId, 10)
    };

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
      .then(async res => {
        const text = await res.text();
        if (!res.ok) {
          alert(text);
          throw new Error(text);
        }
        alert(text);
        fetchTasks();
        resetForm();
      })
      .catch(err => console.error("Error:", err));
  };

  const handleDelete = id => {
    fetch(`${API_BASE}/${id}`, { method: 'DELETE' })
      .then(async res => {
        const msg = await res.text();
        alert(msg);
        fetchTasks();
      })
      .catch(console.error);
  };

  const handleEdit = task => {
    setEditingId(task.id);
    setForm({ 
      name: task.name || '', 
      status: task.status || '', 
      projectId: task.projectId ? task.projectId.toString() : '' 
    });
  };

  const resetForm = () => {
    setEditingId(null);
    setForm({ name: '', status: '', projectId: '' });
  };

  return (
    <div>
      <h2>Tasks</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Task Name"
          value={form.name}
          onChange={e => setForm({ ...form, name: e.target.value })}
          required
        />
        <input
          type="text"
          placeholder="Status"
          value={form.status}
          onChange={e => setForm({ ...form, status: e.target.value })}
          required
        />
        <input
          type="number"
          placeholder="Project ID"
          value={form.projectId}
          onChange={e => setForm({ ...form, projectId: e.target.value })}
          required
        />
        <button type="submit">{editingId ? "Update" : "Add"} Task</button>
        {editingId && <button type="button" onClick={resetForm}>Cancel</button>}
      </form>

      <ul>
        {tasks.map(task => (
          <li key={task.id}>
            <strong>ID: {task.id}</strong> | <strong>{task.name}</strong> | Status: {task.status} | Project ID: {task.projectId}
            <button onClick={() => handleEdit(task)}>Edit</button>
            <button onClick={() => handleDelete(task.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TaskList;
