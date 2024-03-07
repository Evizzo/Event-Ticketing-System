import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getUserWithPublishedEvents, User, Event } from '../api/ApiService';

const UserProfilePage: React.FC = () => {
    const { email } = useParams<{ email?: string }>();
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        const fetchUserData = async () => {
            if (email) {
                try {
                    const response = await getUserWithPublishedEvents(email);
                    setUser(response.data);
                    console.log(email)
                } catch (error) {
                    console.error('Error fetching user data:', error);
                }
            }
        };

        fetchUserData();
    }, [email]);

    return (
        <div className="container">
            {user && (
                <div className="user-profile">
                    <h1 className="profile-header">User Profile</h1>
                    <div className="profile-info">
                        <p><strong>Name:</strong> {user.firstname} {user.lastname}</p>
                        <p><strong>Email:</strong> {user.email}</p>
                    </div>
                    <div className="published-events">
                        <h2 className="events-header">Published Events</h2>
                        <table className="table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Date</th>
                                    <th>Location</th>
                                </tr>
                            </thead>
                            <tbody>
                                {user.publishedEvents.map((event: Event) => (
                                    <tr key={event.id}>
                                        <td><Link to={`/events/${event.id}`}>{event.name}</Link></td>
                                        <td>{event.date}</td>
                                        <td>{event.location}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserProfilePage;
