import { Routes, Route } from 'react-router-dom';
import Home from '../routes/Home';
import About from '../routes/About';
import Layout from './Layout';
import AboutWho from '../routes/AboutWho';
import OurValues from '../routes/OurValues';
import SensorOrchestrator from '../sensor/SensorOrchestrator';

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="/" element={<Home />} />
        <Route path="sensors" element={<SensorOrchestrator />} />
        <Route path="about" element={<About />} />
        <Route path="who-we-are" element={<AboutWho />} />
        <Route path="our-values" element={<OurValues />} />
        <Route path="*" element={<p>Not found!</p>} />
      </Route>
    </Routes>
  );
};

export default App;
