import {Buffer} from "buffer";
import React, {useEffect, useState} from 'react';
import Sensor from "./Sensor";
import useWebSocket from 'react-use-websocket';

const consumerBaseUrl = "ws://localhost:8080/ws/v2/consumer/persistent";
const namespacePath = consumerBaseUrl + "/public/default";
const sensorIngestSubUrl = namespacePath + "/sensors_ingest/sen_ingest_sub";
const sensorMedianSubUrl = namespacePath + "/sensors_median/sen_median_sub";
const sensorMeanSubUrl = namespacePath + "/sensors_mean/sen_mean_sub";

function SensorOrchestrator() {
    const [sensorData, setSensorData] = useState({});
    const [medianData, setMedianData] = useState({});
    const [meanData, setMeanData] = useState({});

    const {sendJsonMessage: sendIngestMsg, lastJsonMessage: lastIngestMsg} = useWebSocket(sensorIngestSubUrl);
    const {sendJsonMessage: sengMedianMsg, lastJsonMessage: lastMedianMsg} = useWebSocket(sensorMedianSubUrl);
    const {sendJsonMessage: sengMeanMsg, lastJsonMessage: lastMeanMsg} = useWebSocket(sensorMeanSubUrl);

    useEffect(() => {
        if (lastIngestMsg !== null) {
            let message = JSON.parse(Buffer.from(lastIngestMsg.payload, 'base64').toString('utf8'));
            setSensorData(prevState => {
                if (!prevState[message.name]) {
                    prevState[message.name] = {values: [], times: []};
                }
                prevState[message.name].values.push(message.value);
                prevState[message.name].times.push(new Date(message.time));
                return {...prevState};
            })
            sendIngestMsg({"messageId": lastIngestMsg.messageId});
        }
    }, [lastIngestMsg, sendIngestMsg]);

    useEffect(() => {
        if (lastMedianMsg !== null) {
            let message = JSON.parse(Buffer.from(lastMedianMsg.payload, 'base64').toString('utf8'));
            console.log("M", message);
            setMedianData(prevState => {
                prevState[message.name] = message;
                return {...prevState};
            });
            sengMedianMsg({"messageId": lastMedianMsg.messageId});
        }
    }, [lastMedianMsg, sengMedianMsg]);

    useEffect(() => {
        if (lastMeanMsg !== null) {
            let message = JSON.parse(Buffer.from(lastMeanMsg.payload, 'base64').toString('utf8'));
            console.log("MM", message);
            setMeanData(prevState => {
                prevState[message.name] = message;
                return {...prevState};
            });
            sengMeanMsg({"messageId": lastMeanMsg.messageId});
        }
    }, [lastMeanMsg, sengMeanMsg]);

    function reviveDate(value) {
        const isoDateRegex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}Z$/
        return typeof value === 'string' && isoDateRegex.test(value)
            ? new Date(value)
            : value
    }

    return (
        <div id="charts">
            {Object.entries(sensorData).map(([category, {values, times}]) => (
                <div key={category}>
                    <Sensor name={category} values={values} times={times} mean={meanData[category] || {value:0}}
                            median={medianData[category] || {value:0}}/>
                </div>
            ))}
        </div>
    );
}

export default SensorOrchestrator;
