import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Container from 'react-bootstrap/Container';
import { Link } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';
import { FaBell, FaTrash } from 'react-icons/fa';
import Dropdown from 'react-bootstrap/Dropdown';
import { deleteNotification, retrieveAllUserNotifications } from '../api/ApiService';
import { useEffect, useState } from 'react';

interface Notification {
    id: string;
    message: string; 
    title: string;
    createdAt: string;
}

function NavigationBar() {
    const authContext = useAuth();
    const isAuthenticated = authContext.isAuthenticated;
    const [notifications, setNotifications] = useState<Notification[]>([]);
    const [notificationClickCount, setNotificationClickCount] = useState(0);

    useEffect(() => {
        if (authContext.isAuthenticated) {
            retrieveNotifications();
        }
    }, [authContext.isAuthenticated, notificationClickCount]);

    const retrieveNotifications = async () => {
        try {
            const response = await retrieveAllUserNotifications();
            setNotifications(response.data);
        } catch (error) {
            console.error('Error fetching notifications:', error);
        }
    };

    const handleDeleteNotification = async (notificationId: string) => {
        try {
            await deleteNotification(notificationId);
            setNotificationClickCount(notificationClickCount + 1);
        } catch (error) {
            console.error('Error deleting notification:', error);
        }
    };
    function logout() {
        authContext.logout();
    }

    return (
        <Navbar bg="navbar navbar-dark bg-dark" expand="lg">
            <Container>
                <Navbar.Brand as={Link} to="/">Event Ticketing System</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ml-auto">
                        <Nav.Link as={Link} to="/">Home</Nav.Link>
                        <Nav.Link as={Link} to="/events">Events</Nav.Link>
                        <Nav.Link as={Link} to="/search">Search</Nav.Link>
                        {isAuthenticated && <Nav.Link as={Link} to="/tickets">Tickets</Nav.Link>}
                        {isAuthenticated && <Nav.Link as={Link} to="/publish">Publish</Nav.Link>}
                        {isAuthenticated && <Nav.Link as={Link} to="/published">Published</Nav.Link>}
                        
                        <Nav.Link as={Link} to="/about">About</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
            {!isAuthenticated &&
                <Nav className="ml-auto">
                    <Nav.Item><Nav.Link as={Link} to="/login" style={{color: "cyan"}}>Login</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/register" style={{color: "cyan"}}>Register</Nav.Link></Nav.Item>
                </Nav>
            }
            {isAuthenticated &&
                <Nav className="ml-auto">
                    <Nav.Item>
                    <Dropdown>
                        <Dropdown.Toggle variant="link" id="notification-dropdown">
                            <FaBell onClick={() => setNotificationClickCount(notificationClickCount + 1)} />
                        </Dropdown.Toggle>
                        <Dropdown.Menu style={{overflowY: 'auto', marginLeft: "-360%" }}>
                            {notifications.map((notification, index) => (
                                <Dropdown.Item key={index}>
                                    <div style={{overflowWrap: 'break-word' }}>
                                        <strong>{notification.title}</strong>
                                    </div>
                                    <div>
                                        {notification.message}
                                        <FaTrash
                                            style={{ color: 'red', marginLeft: '5px', cursor: 'pointer' }}
                                            onClick={() => handleDeleteNotification(notification.id)}
                                        />
                                    </div>
                                    <hr />
                                </Dropdown.Item>
                            ))}
                        </Dropdown.Menu>
                    </Dropdown>
                </Nav.Item>
                    <Nav.Item>
                        <Nav.Link as={Link} to={`/current-user`} style={{ textDecoration: 'none', color: 'white' }}>
                            <strong>User page</strong>
                        </Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link as={Link} to="/login" onClick={logout} style={{ color: "red" }}>
                            Logout
                        </Nav.Link>
                    </Nav.Item>
                </Nav>
            }
        </Navbar>
    );
}

export default NavigationBar;