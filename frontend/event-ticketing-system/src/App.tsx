import HomePage from "./components/HomePage";
import AvaliableEvents from "./components/AvaliableEvents"
import {Route, Routes} from "react-router-dom"
import NavigationBar from "./components/NavigationBar";
import UsersEvents from "./components/UsersEvents";
import About from "./components/About"
import Publish from './components/Publish'
import Search from "./components/Search";
import EventPage from "./components/EventPage";

function App(){

  return(
    <>
      <NavigationBar />
      <Routes>
        <Route path='/' element={<HomePage/>}/>
        <Route path='/events' element={<AvaliableEvents/>}/>
        <Route path="/events/:eventId" element={<EventPage />} />
        <Route path="/manage" element={<UsersEvents/>}/>
        <Route path="/publish" element={<Publish/>}/>
        <Route path="/search" element={<Search/>}/>
        <Route path="/about" element={<About/>}/>
      </Routes>
    </>
  )
}

export default App;