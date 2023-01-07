let time_interval = null;
(() => {
    if (time_interval) {
        clearInterval(time_interval);
        time_interval = undefined;
    }
    postMessage(1);
    time_interval = setInterval(() => {
        postMessage(1);
    }, 1000);
})()