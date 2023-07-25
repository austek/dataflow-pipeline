import * as React from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import { topics } from '../topicsList';
import PulsarTopic from "../topics/PulsarTopic";

import { TopicContext } from '../topics/Context';

function TabPanel(props) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`simple-tabpanel-${index}`}
            aria-labelledby={`simple-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box sx={{ p: 3 }}>
                    <Typography variant={'div'}>{children}</Typography>
                </Box>
            )}
        </div>
    );
}

function a11yProps(index) {
    return {
        id: `simple-tab-${index}`,
        'aria-controls': `simple-tabpanel-${index}`,
    };
}

export default function Topics() {
    const [value, setValue] = React.useState(0);
    const [topicMessages, setTopicMessages] = React.useState({});

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    function generateItemId() {
        let min = Math.ceil(1);
        let max = Math.floor(10);
        let id = String(Math.floor(Math.random() * (max - min + 1) + min)).padStart(2, '0');
        return '00000000-0000-0000-0000-0000000000' + id;
    }

    function handleGenerateNewOrder() {
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'userId': '00000000-0000-0000-0000-000000000001',
                'walletId': '00000000-0000-0000-0000-000000000001',
                'items': [
                    {
                        'itemId': generateItemId(),
                        'quantity': 1
                    }
                ]
            })
        };
        fetch('http://localhost:7070/showcase/v1/orders', requestOptions);
    }


    return (
        <Box sx={{ width: '100%' }}>
            <button onClick={handleGenerateNewOrder}>
                Generate New Order
            </button>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs // disable the tab indicator because it doesn't work well with wrapped container
                    TabIndicatorProps={{ sx: { display: 'none' } }}
                    sx={{
                        '& .MuiTabs-flexContainer': {
                            flexWrap: 'wrap',
                        },
                    }}
                    value={value}
                    onChange={handleChange}
                    textColor="secondary"
                    indicatorColor="secondary"
                    aria-label="secondary tabs example"
                    centered
                >
                    {topics.map((topic, index) => {
                        return (
                            <Tab key={index} label={<span style={{ color: 'white' }}>{topic.title}</span>} {...a11yProps(index)} />
                        );
                    })}
                </Tabs>
            </Box>
            {topics.map((topic, index) => {
                return (
                    <TabPanel key={index} value={value} index={index}>
                        <TopicContext.Provider value={{ topicMessages, setTopicMessages}}>
                            <PulsarTopic key={topic.uiUrl} topic={topic}/>
                        </TopicContext.Provider>
                    </TabPanel>
                );
            })}
        </Box>
    );
}
