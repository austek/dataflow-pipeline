import http from "k6/http";

export default function () {
    let data = {n: `Sensor-${__VU}`, v: __ITER}
    http.post("http://localhost:8081/sensors", JSON.stringify(data),
        {headers: {'Content-Type': 'application/x-ndjson'}});
};