import { useEffect, useState } from 'react';
import { retrieveAllRedeemCodes, editRedeemCode, deleteRedeemCode, addRedeemCode } from '../api/ApiService';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';

interface EditableRedeemCode {
  name: string;
  discountPercentage: number;
  ownerEmail: string;
}

export interface RedeemCode {
  id: string;
  name: string;
  discountPercentage: number;
  ownerEmail: string;
}

function RedeemCodesAdminPage() {
  const [codes, setCodes] = useState<RedeemCode[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [editedValues, setEditedValues] = useState<Record<string, Partial<EditableRedeemCode>>>({});
  const [newCode, setNewCode] = useState<Partial<EditableRedeemCode>>({});
  const authContext = useAuth();
  const navigate = useNavigate();
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchCodes = async () => {
      try {
        const response = await retrieveAllRedeemCodes();
        setCodes(response.data);
        setLoading(false);
        setMessage("");
      } catch (error: any) {
        console.error('Error fetching redeem codes:', error);
        setLoading(false);
      }
    };

    fetchCodes();
  }, []);

  useEffect(() => {
    if (authContext.isAuthenticated && authContext.role !== 'ADMIN') {
      navigate('/');
    }
  }, [authContext.isAuthenticated, authContext.role, navigate]);

  const handleEditCode = async (codeId: string) => {
    try {
      const editedCode = editedValues[codeId];
      if (!editedCode) return;
      await editRedeemCode(codeId, editedCode);
      const updatedCodes = codes.map(code =>
        code.id === codeId ? { ...code, ...editedCode } : code
      );
      setCodes(updatedCodes);
      setMessage(`${codeId} updated successfully.`);
      setTimeout(() => {
        setMessage("");
      }, 5000);
    } catch (error: any) {
      setMessage(error.response.data.message);
      console.error('Error editing redeem code:', error);
    }
  };

  const handleEditField = (codeId: string, field: keyof EditableRedeemCode, value: string | number) => {
    setEditedValues({ ...editedValues, [codeId]: { ...editedValues[codeId], [field]: value } });
  };

  const handleDeleteCode = async (codeId: string) => {
    try {
      await deleteRedeemCode(codeId);
      const updatedCodes = codes.filter(code => code.id !== codeId);
      setCodes(updatedCodes);
      setMessage(`Redeem code ${codeId} deleted successfully.`);
      setTimeout(() => {
        setMessage("");
      }, 5000);
    } catch (error: any) {
      setMessage(error.response.data.message);
      console.error('Error deleting redeem code:', error);
    }
  };

  const handleAddCode = async () => {
    try {
      const response = await addRedeemCode(newCode);
      setCodes([...codes, response.data]);
      setNewCode({});
      setMessage(`Redeem code added successfully.`);
      setTimeout(() => {
        setMessage("");
      }, 5000);
    } catch (error: any) {
      setMessage(error.response.data.message);
      console.error('Error adding redeem code:', error);
    }
  };

  const handleNewCodeChange = (field: keyof EditableRedeemCode, value: string | number) => {
    setNewCode({ ...newCode, [field]: value });
  };

  return (
    <div>
      {message && <div className="alert alert-warning">{message}</div>}
      <div className="mb-3 row">
        <h2 className="col-sm-12">Add New Redeem Code</h2>
        <div className="col-sm-3">
          <label className="form-label">Name:</label>
          <input className="form-control" type="text" value={newCode.name || ''} onChange={(e) => handleNewCodeChange('name', e.target.value)} />
        </div>
        <div className="col-sm-3">
          <label className="form-label">Discount Percentage:</label>
          <input className="form-control" type="number" value={newCode.discountPercentage || ''} onChange={(e) => handleNewCodeChange('discountPercentage', parseFloat(e.target.value))} />
        </div>
        <div className="col-sm-3">
          <label className="form-label">Owner Email:</label>
          <input className="form-control" type="email" value={newCode.ownerEmail || ''} onChange={(e) => handleNewCodeChange('ownerEmail', e.target.value)} />
        </div>
        <div className="col-sm-3 d-flex align-items-end">
          <button className="btn btn-primary" onClick={handleAddCode}>Add Code</button>
        </div>
      </div>
      <h1 className="mb-4">Redeem Code List</h1>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="table-responsive">
          <table className="table table-striped">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Discount Percentage</th>
                <th>Owner Email</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {codes.map((code) => (
                <tr key={code.id}>
                  <td>{code.id}</td>
                  <td>
                    <input
                      type="text"
                      className="form-control"
                      value={editedValues[code.id]?.name || code.name}
                      onChange={(e) => handleEditField(code.id, 'name', e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      type="number"
                      className="form-control"
                      value={editedValues[code.id]?.discountPercentage || code.discountPercentage}
                      onChange={(e) => handleEditField(code.id, 'discountPercentage', parseFloat(e.target.value))}
                    />
                  </td>
                  <td>
                    <input
                      type="email"
                      className="form-control"
                      value={editedValues[code.id]?.ownerEmail || code.ownerEmail}
                      onChange={(e) => handleEditField(code.id, 'ownerEmail', e.target.value)}
                    />
                  </td>
                  <td>
                    <button className="btn btn-primary" onClick={() => handleEditCode(code.id)}>Save</button>
                    <button className="btn btn-danger" onClick={() => handleDeleteCode(code.id)}>Delete</button>
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

export default RedeemCodesAdminPage;
