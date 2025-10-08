import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import ProjectList from './components/Projects/ProjectList';
import TaskList from './components/Tasks/TaskList';
import TeamMembersList from './components/TeamMembers/TeamMembersList';
import './App.css';
function App() {
  return (
    <Router>
      <nav>
        <Link to="/projects">Projects</Link> |{' '}
        <Link to="/tasks">Tasks</Link> |{' '}
        <Link to="/team-members">Team Members</Link>
      </nav>
      <div className="section">
        <Routes>
          <Route path="/" element={<ProjectList />} />
          <Route path="/projects" element={<ProjectList />} />
          <Route path="/tasks" element={<TaskList />} />
          <Route path="/team-members" element={<TeamMembersList />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;