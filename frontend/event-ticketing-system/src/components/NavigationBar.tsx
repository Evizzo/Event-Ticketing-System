import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Container from 'react-bootstrap/Container';
import { Link } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';

function NavigationBar() {
    const authContext = useAuth();
    const isAuthenticated = authContext.isAuthenticated;

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
                        {isAuthenticated && <Nav.Link as={Link} to="/tickets">Tickets</Nav.Link>}
                        {isAuthenticated && <Nav.Link as={Link} to="/publish">Publish</Nav.Link>}
                        <Nav.Link as={Link} to="/search">Search</Nav.Link>
                        
                        <Nav.Link as={Link} to="/about">About</Nav.Link>
                        {!isAuthenticated && <Nav.Link as={Link} to="/login" style={{color: "cyan"}}>Login</Nav.Link>}
                        {!isAuthenticated && <Nav.Link as={Link} to="/register" style={{color: "cyan"}}>Register</Nav.Link>}
                        {isAuthenticated && <Nav.Link as={Link} to="/login" onClick={logout} style={{color: "red"}}>Logout</Nav.Link>}
                    </Nav>
                </Navbar.Collapse>
            </Container>
            {isAuthenticated && 
                <nav>
                    <div className="container-fluid">
                        <ul className="navbar-nav">
                        <li className="nav-item">
                            <span className="nav-link">
                            <strong>User:</strong> <Link to={`/current-user`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                        {authContext.email}
                                    </Link>
                            </span>
                        </li>
                        </ul>
                    </div>
                </nav>
            }
        </Navbar>
    );
}

export default NavigationBar;
