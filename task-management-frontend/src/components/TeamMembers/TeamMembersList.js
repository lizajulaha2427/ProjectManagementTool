import React, { useEffect, useState } from 'react';

const API_BASE = "http://localhost:8080/members";

const TeamMemberList = () => {
  const [members, setMembers] = useState([]);
  const [form, setForm] = useState({ name: '', role: '' });
  const [editingId, setEditingId] = useState(null);

  useEffect(() => {
    fetchMembers();
  }, []);

  const fetchMembers = () => {
    fetch(API_BASE)
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          setMembers(data);
        } else {
          console.error('Unexpected response data:', data);
          setMembers([]);
        }
      })
      .catch(console.error);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const method = editingId ? "PUT" : "POST";
    const url = editingId ? `${API_BASE}/${editingId}` : API_BASE;

    try {
      const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      const text = await response.text(); // get message from backend
      alert(text); // show message

      if (!response.ok) throw new Error(text);

      fetchMembers();
      resetForm();
    } catch (err) {
      console.error(err);
    }
  };

  const handleDelete = (id) => {
    fetch(`${API_BASE}/${id}`, { method: 'DELETE' })
      .then(async res => {
        const msg = await res.text();
        alert(msg);
        fetchMembers();
      })
      .catch(console.error);
  };

  const handleEdit = (member) => {
    setEditingId(member.id);
    setForm({ name: member.name, role: member.role });
  };

  const resetForm = () => {
    setEditingId(null);
    setForm({ name: '', role: '' });
  };

  return (
    <div>
      <h2>Team Members</h2>
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
          placeholder="Role"
          value={form.role}
          onChange={e => setForm({ ...form, role: e.target.value })}
          required
        />
        <button type="submit">{editingId ? 'Update' : 'Add'} Member</button>
        {editingId && <button type="button" onClick={resetForm}>Cancel</button>}
      </form>
      <ul>
        {members.map(member => (
          <li key={member.id}>
            <strong>ID: {member.id}</strong> | <strong>{member.name}</strong> - {member.role}
            <button onClick={() => handleEdit(member)}>Edit</button>
            <button onClick={() => handleDelete(member.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TeamMemberList;
