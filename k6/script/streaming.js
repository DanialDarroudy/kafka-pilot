import http from 'k6/http';
import { check } from 'k6';
import encoding from 'k6/encoding';

export const options = {
    scenarios: {
        streaming: {
            executor: 'constant-vus',
            vus: 100,
            gracefulStop: '0s',
        },
    },
};

function randomBytes(size) {
    let arr = new Uint8Array(size);
    for (let i = 0; i < size; i++) {
        arr[i] = Math.floor(Math.random() * 256);
    }
    return encoding.b64encode(arr.buffer);
}

export default function () {
    const body = JSON.stringify({
        key: `stream-${__VU}-${__ITER}`,
        payload: randomBytes(100),
    });

    const res = http.post(
        'http://host.docker.internal:8080/api/v1/messages',
        body,
        { headers: { 'Content-Type': 'application/json' } }
    );

    check(res, { '2xx': (r) => r.status >= 200 && r.status < 300 });
}