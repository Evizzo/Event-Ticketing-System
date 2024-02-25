import { useEffect, useState } from 'react';
import { retrieveAllUsers, editUser } from '../api/ApiService';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';

interface EditableUser {
  firstname: string;
  lastname: string;
  email: string;
  credits: string;
}
interface User {
  id: string;
  firstname: string;
  lastname: string;
  email: string;
  credits: string;
  role: string;
}
function AdminPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [editedValues, setEditedValues] = useState<Record<string, Partial<EditableUser>>>({});
  const authContext = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await retrieveAllUsers();
        setUsers(response.data);
        setLoading(false);
      } catch (error: any) {
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
  }, [authContext.isAuthenticated, authContext.role, navigate]);

  const handleEditUser = async (userId: string) => {
    try {
      const editedUser = editedValues[userId];
      if (!editedUser) return; // No changes made
      await editUser(userId, editedUser);
      const updatedUsers = users.map(user =>
        user.id === userId ? { ...user, ...editedUser } : user
      );
      setUsers(updatedUsers);
    } catch (error: any) {
      console.error('Error editing user:', error);
    }
  };

  const handleEditField = (userId: string, field: keyof EditableUser, value: string | number) => {
    setEditedValues({ ...editedValues, [userId]: { ...editedValues[userId], [field]: value } });
  };

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
                  <td>
                    <input
                      type="text"
                      value={editedValues[user.id]?.firstname || user.firstname}
                      onChange={(e) => handleEditField(user.id, 'firstname', e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      type="text"
                      value={editedValues[user.id]?.lastname || user.lastname}
                      onChange={(e) => handleEditField(user.id, 'lastname', e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      type="email"
                      value={editedValues[user.id]?.email || user.email}  
                      onChange={(e) => handleEditField(user.id, 'email', e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      type="number"
                      value={editedValues[user.id]?.credits || user.credits}  
                      onChange={(e) => handleEditField(user.id, 'credits', parseFloat(e.target.value))}
                    />
                  </td>
                  <td>
                    <button onClick={() => handleEditUser(user.id)}>Save</button>
                  </td>
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
