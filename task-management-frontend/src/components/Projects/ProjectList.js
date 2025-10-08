import React, { useEffect, useState } from 'react';

const API_BASE = "http://localhost:8080/projects";

const ProjectList = () => {
  const [projects, setProjects] = useState([]);
  const [form, setForm] = useState({ name: '', description: '', deadline: '' });
  const [editingId, setEditingId] = useState(null);

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = () => {
    fetch(API_BASE)
      .then(res => res.json())
      .then(data => setProjects(data))
      .catch(console.error);
  };

  const handleSubmit = e => {
    e.preventDefault();
    const method = editingId ? "PUT" : "POST";
    const url = editingId ? `${API_BASE}/${editingId}` : API_BASE;

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    })
      .then(async res => {
        const text = await res.text();
        if (!res.ok) {
          alert(text);
          throw new Error(text);
        }
        alert(text);
        fetchProjects();
        resetForm();
      })
      .catch(err => console.error("Error:", err));
  };

  const handleDelete = id => {
    fetch(`${API_BASE}/${id}`, { method: 'DELETE' })
      .then(res => res.text())
      .then(msg => {
        alert(msg);
        fetchProjects();
      })
      .catch(console.error);
  };

  const handleEdit = project => {
    setEditingId(project.id);
    setForm({ 
      name: project.name, 
      description: project.description, 
      deadline: project.deadline || '' 
    });
  };

  const resetForm = () => {
    setEditingId(null);
    setForm({ name: '', description: '', deadline: '' });
  };

  const today = new Date().toISOString().split("T")[0];

  return (
    <div>
      <h2>Projects</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Name"
          value={form.name}
          onChange={e => setForm({ ...form, name: e.target.value })}
          required
        />
        <input
          type="text"
          placeholder="Description"
          value={form.description}
          onChange={e => setForm({ ...form, description: e.target.value })}
          required
        />
        <input
          type="date"
          value={form.deadline}
          min={today}   // ✅ Prevent selecting past dates
          onChange={e => setForm({ ...form, deadline: e.target.value })}
          required
        />
        <button type="submit">{editingId ? "Update" : "Add"} Project</button>
        {editingId && <button type="button" onClick={resetForm}>Cancel</button>}
      </form>

      <ul>
        {projects.map(p => (
          <li key={p.id}>
            <strong>ID: {p.id}</strong> | <strong>{p.name}</strong> - {p.description}
            {p.deadline && <> (Deadline: {p.deadline})</>}
            <button onClick={() => handleEdit(p)}>Edit</button>
            <button onClick={() => handleDelete(p.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ProjectList;
