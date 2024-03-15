import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { RestartPasswordRequest, executeResetPasswordWithToken } from '../api/ApiService';

const RestartPassword = () => {
  const [password, setPassword] = useState('');
  const { token } = useParams<{ token?: string }>();
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!token) {
        alert('Token is missing.');
      return;
    }

    const restartPasswordRequest: RestartPasswordRequest = {
      token: token,
      newPassword: password,
    };

    try {
        await executeResetPasswordWithToken(restartPasswordRequest);
        alert("Password changed successfuly !");
        navigate("/login")
    } catch (error: any) {
        setMessage(error.response.data.message)
    }
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card mt-5">
            <div className="card-body">
              <h2 className="card-title text-center mb-4">Restart Password</h2>
              <form onSubmit={handleSubmit}>
                <div className="form-group text-center">
                  <label htmlFor="password" className="mb-2">New Password:</label>
                  <input
                    type="password"
                    className="form-control"
                    id="password"
                    value={password}
                    onChange={handleChange}
                    required
                  />
                </div>
                <br></br>
                <div className="text-center">
                  <button type="submit" className="btn btn-primary">Submit</button>
                </div>
              </form>
              {message && <div className="alert alert-info mt-3">{message}</div>}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RestartPassword;
