import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Container from 'react-bootstrap/Container';

function NavigationBar() {
    return (
        <Navbar bg="navbar navbar-dark bg-dark" expand="lg">
            <Container>
                <Navbar.Brand href="/">Event Ticketing System</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ml-auto">
                        <Nav.Link href="/">Home</Nav.Link>
                        <Nav.Link href="/events">Events</Nav.Link>
                        <Nav.Link href="/manage">Tickets</Nav.Link>
                        <Nav.Link href="/publish">Publish</Nav.Link>
                        <Nav.Link href="/about">About</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavigationBar;
