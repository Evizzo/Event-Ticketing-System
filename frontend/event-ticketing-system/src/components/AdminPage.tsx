import { useEffect, useState } from 'react';
import { retrieveAllUsers } from '../api/ApiService';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';

interface User {
  id: string;
  firstname: string;
  lastname: string;
  email: string;
  credits: number;
  role: string;
}

function AdminPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const authContext = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await retrieveAllUsers();
        setUsers(response.data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching users:', error);
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  useEffect(() => {
    if (authContext.isAuthenticated && authContext.role !== 'ADMIN') {
      navigate('/');
    }
  }, [authContext.isAuthenticated, navigate]);

  return (
    <div>
      <h1 className="mb-4">User List</h1>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="table-responsive">
          <table className="table table-striped">
            <thead>
              <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Credits</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>{user.firstname}</td>
                  <td>{user.lastname}</td>
                  <td>{user.email}</td>
                  <td>{user.credits}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default AdminPage;
