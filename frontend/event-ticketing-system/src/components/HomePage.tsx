import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { Link } from 'react-router-dom';

function HomePage() {
    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <Card className="bg-light p-4 text-center">
                <Container>
                    <h1>Welcome to the Event Ticketing System</h1>
                    <p className="mb-4">
                        Buy tickets for your favorite events, concerts, and more. Find and book the best seats in town.
                    </p>
                    <Link to="/events">
                        <Button variant="primary">Explore Events</Button>
                    </Link>
                </Container>
            </Card>
        </div>
    );
}

export default HomePage;
