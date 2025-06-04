import React from 'react';
import '../App.css';

const LeadsDashboard = ({
    leads,
    loading,
    error,
    filters,
    handleFilterChange,
    getScoreColor,
    getCategoryColor,
    onDeleteLead // New prop for delete functionality
}) => {
  // Delete handler function
  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this lead?')) {
      try {
        const response = await fetch(`http://localhost:8080/api/leads/${id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json'
          }
        });
        
        if (response.ok) {
          onDeleteLead(id); // Update parent component's state
        } else {
          alert('Error deleting lead');
        }
      } catch (error) {
        console.error('Error:', error);
        alert('Error deleting lead');
      }
    }
  };

  return (
    <div className="dashboard-container">
      <h2 className="dashboard-title">Lead Dashboard</h2>

      {/* Filters Section */}
      <div className="filters-section">
        <div className="filter-group">
          <label htmlFor="budgetMin" className="filter-label">Min Budget ($):</label>
          <input
            type="number"
            id="budgetMin"
            name="budgetMin"
            value={filters.budgetMin}
            onChange={handleFilterChange}
            className="filter-input"
            placeholder="e.g., 100000"
          />
        </div>
        <div className="filter-group">
          <label htmlFor="budgetMax" className="filter-label">Max Budget ($):</label>
          <input
            type="number"
            id="budgetMax"
            name="budgetMax"
            value={filters.budgetMax}
            onChange={handleFilterChange}
            className="filter-input"
            placeholder="e.g., 500000"
          />
        </div>
        <div className="filter-group">
          <label htmlFor="location" className="filter-label">Location:</label>
          <input
            type="text"
            id="location"
            name="location"
            value={filters.location}
            onChange={handleFilterChange}
            className="filter-input"
            placeholder="e.g., Downtown"
          />
        </div>
        <div className="filter-group">
          <label htmlFor="sortBy" className="filter-label">Sort By Score:</label>
          <select
            id="sortBy"
            name="sortBy"
            value={filters.sortBy}
            onChange={handleFilterChange}
            className="filter-select"
          >
            <option value="">None</option>
            <option value="scoreDesc">Score (High to Low)</option>
            <option value="scoreAsc">Score (Low to High)</option>
          </select>
        </div>
      </div>

      {loading && <p className="status-message loading-message">Loading leads...</p>}
      {error && <p className="status-message error-message">{error}</p>}
      {!loading && !error && leads.length === 0 && (
        <p className="status-message no-leads-message">No leads found. Start by capturing a new lead!</p>
      )}

      {!loading && !error && leads.length > 0 && (
        <div className="table-wrapper">
          <table className="lead-table">
            <thead className="table-header">
              <tr>
                <th className="table-th rounded-tl">Name</th>
                <th className="table-th">Email</th>
                <th className="table-th">Phone</th>
                <th className="table-th">Budget</th>
                <th className="table-th">Location</th>
                <th className="table-th">Score</th>
                <th className="table-th">Category</th>
                <th className="table-th rounded-tr">Actions</th>
              </tr>
            </thead>
            <tbody className="table-body">
              {leads.map((lead, index) => (
                <tr key={lead.id} className={index % 2 === 0 ? 'table-row-even' : 'table-row-odd'}>
                  <td className="table-td">{lead.name}</td>
                  <td className="table-td">{lead.email}</td>
                  <td className="table-td">{lead.phone || 'N/A'}</td>
                  <td className="table-td">${lead.budget ? lead.budget.toLocaleString() : 'N/A'}</td>
                  <td className="table-td">{lead.preferredLocation || 'N/A'}</td>
                  <td className="table-td">
                    <span className={`score-badge ${getScoreColor(lead.score)}`}>
                      {lead.score || 'N/A'}
                    </span>
                  </td>
                  <td className="table-td">
                    <span className={`category-badge ${getCategoryColor(lead.category)}`}>
                      {lead.category || 'N/A'}
                    </span>
                  </td>
                  <td className="table-td">
                    <button
                      className="delete-btn"
                      onClick={() => handleDelete(lead.id)}
                      title="Delete Lead"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default LeadsDashboard; 