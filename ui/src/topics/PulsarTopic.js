import { Buffer } from 'buffer'
import React, { useContext, useEffect, useState} from 'react';
import useWebSocket, { ReadyState } from 'react-use-websocket';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';
import { TopicContext } from './Context';

function PulsarTopic({topic}) {
    const [columnDefs, setColumnDefs] = useState(topic.columnDefs);
    const {topicMessages, setTopicMessages} = useContext(TopicContext);

    const { sendJsonMessage, lastJsonMessage, readyState } = useWebSocket(topic.url);

    useEffect(() => {
        if (lastJsonMessage !== null) {
            let message = JSON.parse(Buffer.from(lastJsonMessage.payload, 'base64').toString('utf8'));
            setTopicMessages({...topicMessages, [topic.uiUrl]: [message, ...(topicMessages[topic.uiUrl] ?? [])]});
            sendJsonMessage({ "messageId": lastJsonMessage.messageId });
        }
    }, [lastJsonMessage, sendJsonMessage]);

    useEffect(() => {setColumnDefs(topic.columnDefs);}, [topic.columnDefs]);

    const connectionStatus = {
        [ReadyState.CONNECTING]: 'Connecting',
        [ReadyState.OPEN]: 'Open',
        [ReadyState.CLOSING]: 'Closing',
        [ReadyState.CLOSED]: 'Closed',
        [ReadyState.UNINSTANTIATED]: 'Uninstantiated',
    }[readyState];

    return (
        <div className="container">
            <span>{topic.title} Topic Connection: {connectionStatus}</span>
            <div className="ag-theme-alpine data-table">
                <AgGridReact
                    key={topic.uiUrl}
                    rowData={topicMessages[topic.uiUrl] ?? []}
                    columnDefs={columnDefs}>
                </AgGridReact>
            </div>
        </div>
    );
}

export default PulsarTopic;
