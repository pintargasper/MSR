function handleMouseDown(event) {
    event.preventDefault();
    event.stopPropagation();
    window.focus();
}

function handleMouseUp(event) {
    event.preventDefault();
    event.stopPropagation();
}

window.onkeydown = function (event) {
    const keysToPrevent = [" ", "PageUp", "PageDown", "End", "Home", "ArrowLeft", "ArrowUp", "ArrowRight", "ArrowDown"];
    if (keysToPrevent.indexOf(event.key) > -1) {
        event.preventDefault();
        return false;
    }
}

document.addEventListener("contextmenu", event => event.preventDefault());
document.getElementById("embed-html").addEventListener("mousedown", handleMouseDown, false);
document.getElementById("embed-html").addEventListener("mouseup", handleMouseUp, false);
