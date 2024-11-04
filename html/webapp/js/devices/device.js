function detectDevice() {
    const userAgent = navigator.userAgent.toLowerCase();
    return /android|iphone|ipad|ipod/i.test(userAgent) ? "mobile" :
        /tablet/i.test(userAgent) ? "tablet" : "desktop";
}

const deviceType = detectDevice();

if (deviceType === "mobile" || deviceType === "tablet") {
    document.getElementById("embed-html").style.display = "none";
    document.getElementById("mobile-message").style.display = "block";
} else {
    const script = document.createElement("script");
    script.type = "text/javascript";
    script.src = "html/html.nocache.js";
    document.getElementsByTagName("head")[0].appendChild(script);
}
