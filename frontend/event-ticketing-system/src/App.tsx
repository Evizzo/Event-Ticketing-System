import HomePage from "./components/HomePage";
import AvaliableEvents from "./components/AvaliableEvents"
import {BrowserRouter, Routes, Route, Navigate} from "react-router-dom"
import NavigationBar from "./components/NavigationBar";
import UsersTickets from "./components/UsersTickets";
import About from "./components/About"
import Publish from './components/Publish'
import Search from "./components/Search";
import EventPage from "./components/EventPage";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import AuthProvider from "./api/AuthContex";
import { ReactNode } from "react";
import { useAuth } from "./api/AuthContex";
import NotFound from "./components/NotFound";
import UserPage from "./components/UserPage";
import UsersPublishedEvents from "./components/UsersPublishedEvents";

function App(){
  function AuthenticatedRoute({ children }: { children: ReactNode }) {
    const authContext = useAuth()
    
    if (authContext.isAuthenticated){
      console.log(authContext)
      return children
    }
    else
      return <Navigate to="/login" />
}
  return(
    <div className="EtsApp">
      <AuthProvider>
        <BrowserRouter>
          <NavigationBar />
          <Routes>
            <Route path='/' element={
              <HomePage/>
            }/>
            <Route path='/events' element={
              <AvaliableEvents/>
            }/>
            <Route path="/events/:eventId" element={
              <EventPage />
            } />
            <Route path="/current-user" element={
              <AuthenticatedRoute>
                <UserPage />
              </AuthenticatedRoute>
            } />
            <Route path="/published" element={
              <AuthenticatedRoute>
                <UsersPublishedEvents />
              </AuthenticatedRoute>
            } />
            <Route path="/tickets" element={
              <AuthenticatedRoute>
                <UsersTickets/>
              </AuthenticatedRoute>
            }/>
            <Route path="/publish" element={
              <AuthenticatedRoute>
                <Publish/>
              </AuthenticatedRoute>
            }/>
            <Route path="/search" element={
                <Search/>
            }/>

            <Route path="/about" element={<About/>}/>
            <Route path="/register" element={<RegisterPage/>}/>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="*" element={<NotFound />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </div>

  )
}

export default App;