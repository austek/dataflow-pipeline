import Navbar from './Navbar';
import logo from '../logo.svg';
import { Link } from 'react-router-dom';

const Header = () => {
  return (

    <header>
      <div className="nav-area">
        <Link to="/" className="logo">
          <img src={logo} className="App-logo" alt="logo" />
        </Link>
        <Navbar />
      </div>
    </header>
  );
};

export default Header;
