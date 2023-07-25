import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './components/App';
import {BrowserRouter} from 'react-router-dom';
import {StyledEngineProvider} from '@mui/material/styles';

// styles
import './App.css';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <BrowserRouter basename={process.env.PUBLIC_URL}>
            <StyledEngineProvider injectFirst>
                <App/>
            </StyledEngineProvider>
        </BrowserRouter>
    </React.StrictMode>
);
