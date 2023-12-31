import * as React from 'react';
import {LineChart} from '@mui/x-charts/LineChart';
import {useEffect, useState} from "react";

export default function Sensor(props) {
    const [state, setState] = useState({
        name: props.name,
        values: props.values,
        times: props.times,
        median: props.median.value || 0,
        mean: props.mean.value || 0,
    });
    useEffect(() => {
        setState(prevState => ({
            ...prevState,
            values: props.values,
            times: props.times,
            median: props.median.value || 0,
            mean: props.mean.value || 0,
        }));
    }, [props.values, props.times,
        props.median,
        props.mean]);

    const timeFormatter = (date) => date.getHours().toString() + ":" + date.getMinutes().toString();


    return (
        <div id="chart">
            <h2>{props.name}</h2>
            <span>Mean: {state.mean}°C</span><br/>
            <span>Median: {state.median}°C</span>
            <LineChart
                xAxis={[{scaleType: 'time', data: state.times, valueFormatter: timeFormatter}]}
                series={[{data: state.values, label: '°C', area: false}]}
                width={1500}
                height={300}
            />
        </div>
    );
}
